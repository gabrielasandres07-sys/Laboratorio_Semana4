package labsemana4.gui.sonido;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.SwingUtilities;
import java.io.ByteArrayOutputStream;

public class GestorSonido {

    private static final float FRECUENCIA_MUESTREO = 44100f;
    private static final int BITS = 16;
    private static final int CANALES = 1;
    private static final double AMPLITUD = 0.30;
    private static final int SILENCIO_ENTRE_NOTAS_MS = 20;

    private final AudioFormat formato;
    private final DataLine.Info info;

    private volatile boolean silenciado;
    private volatile boolean disponible;
    private volatile Runnable avisoSinAudio;

    public GestorSonido() {
        this.formato = new AudioFormat(FRECUENCIA_MUESTREO, BITS, CANALES, true, false);
        this.info = new DataLine.Info(SourceDataLine.class, formato);
        this.disponible = AudioSystem.isLineSupported(info);
    }

    public boolean isDisponible() {
        return disponible;
    }

    public boolean isSilenciado() {
        return silenciado;
    }

    public void setSilenciado(boolean silenciado) {
        this.silenciado = silenciado;
    }

    public void setAvisoSinAudio(Runnable aviso) {
        this.avisoSinAudio = aviso;
    }

    public void reproducir(EfectoSonido efecto) {
        if (efecto == null || silenciado || !disponible) {
            return;
        }
        Thread hilo = new Thread(() -> reproducirInterno(efecto), "sonido-ahorcado");
        hilo.setDaemon(true);
        hilo.start();
    }

    private void reproducirInterno(EfectoSonido efecto) {
        byte[] audio = generar(efecto);
        try (SourceDataLine linea = AudioSystem.getSourceDataLine(formato)) {
            linea.open(formato, audio.length);
            linea.start();
            linea.write(audio, 0, audio.length);
            linea.drain();
            linea.stop();
        } catch (LineUnavailableException ex) {
            disponible = false;
            Runnable aviso = avisoSinAudio;
            if (aviso != null) {
                SwingUtilities.invokeLater(aviso);
            }
        }
    }

    private byte[] generar(EfectoSonido efecto) {
        int[] frecuencias = efecto.getFrecuencias();
        int[] duraciones = efecto.getDuraciones();
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        for (int i = 0; i < frecuencias.length; i++) {
            int muestras = (int) (FRECUENCIA_MUESTREO * duraciones[i] / 1000.0);
            for (int j = 0; j < muestras; j++) {
                short valor;
                if (frecuencias[i] <= 0) {
                    valor = 0;
                } else {
                    double t = (double) j / FRECUENCIA_MUESTREO;
                    double onda = Math.sin(2.0 * Math.PI * frecuencias[i] * t);
                    double envolvente = envolvente(j, muestras);
                    valor = (short) (onda * Short.MAX_VALUE * AMPLITUD * envolvente);
                }
                salida.write(valor & 0xFF);
                salida.write((valor >> 8) & 0xFF);
            }
            if (i < frecuencias.length - 1) {
                agregarSilencio(salida, SILENCIO_ENTRE_NOTAS_MS);
            }
        }
        return salida.toByteArray();
    }

    private void agregarSilencio(ByteArrayOutputStream salida, int milisegundos) {
        int muestras = (int) (FRECUENCIA_MUESTREO * milisegundos / 1000.0);
        for (int i = 0; i < muestras; i++) {
            salida.write(0);
            salida.write(0);
        }
    }

    private double envolvente(int muestra, int totalMuestras) {
        int rampa = (int) (FRECUENCIA_MUESTREO * 0.01);
        if (rampa <= 0) {
            return 1.0;
        }
        double ataque = Math.min(1.0, (double) muestra / rampa);
        double liberacion = Math.min(1.0, (double) (totalMuestras - muestra) / rampa);
        return Math.min(ataque, liberacion);
    }
}
