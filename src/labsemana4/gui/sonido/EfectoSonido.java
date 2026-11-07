package labsemana4.gui.sonido;

public enum EfectoSonido {

    ACIERTO(new int[]{880}, new int[]{120}),
    ERROR(new int[]{220}, new int[]{250}),
    ALERTA(new int[]{440, 0, 440}, new int[]{80, 40, 80}),
    VICTORIA(new int[]{523, 659, 784}, new int[]{140, 140, 260}),
    DERROTA(new int[]{392, 330, 262}, new int[]{180, 180, 320});

    private final int[] frecuencias;
    private final int[] duraciones;

    EfectoSonido(int[] frecuencias, int[] duraciones) {
        this.frecuencias = frecuencias;
        this.duraciones = duraciones;
    }

    public int[] getFrecuencias() {
        return frecuencias.clone();
    }

    public int[] getDuraciones() {
        return duraciones.clone();
    }
}
