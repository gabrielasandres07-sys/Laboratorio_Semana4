package labsemana4.gui;

import labsemana4.modelo.EtapaAhorcado;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

public class PanelDibujo extends JPanel {

    private static final int ANCHO_DISENO = 320;
    private static final int ALTO_DISENO = 360;
    private static final int RETARDO_ANIMACION = 30;
    private static final float PASO_ANIMACION = 0.08f;

    private static final Color COLOR_MADERA = new Color(122, 74, 32);
    private static final Color COLOR_CUERDA = new Color(90, 90, 90);
    private static final Color COLOR_CUERPO = new Color(35, 35, 35);
    private static final Color COLOR_DERROTA = new Color(170, 30, 30);

    private final Timer temporizador;
    private EtapaAhorcado etapaActual;
    private EtapaAhorcado etapaObjetivo;
    private float progreso;
    private boolean animando;
    private boolean derrota;
    private Runnable alTerminarAnimacion;

    public PanelDibujo() {
        this.etapaActual = EtapaAhorcado.VACIO;
        this.etapaObjetivo = EtapaAhorcado.VACIO;
        this.progreso = 1f;
        this.animando = false;
        setOpaque(true);
        setBackground(new Color(248, 246, 240));
        setPreferredSize(new Dimension(ANCHO_DISENO, ALTO_DISENO));
        setMinimumSize(new Dimension(220, 260));

        this.temporizador = new Timer(RETARDO_ANIMACION, e -> avanzarAnimacion());
    }

    public boolean setEtapa(EtapaAhorcado nuevaEtapa, Runnable alTerminar) {
        EtapaAhorcado destino = (nuevaEtapa == null) ? EtapaAhorcado.VACIO : nuevaEtapa;
        this.alTerminarAnimacion = alTerminar;
        temporizador.stop();

        if (destino.ordinal() <= etapaActual.ordinal()) {
            etapaActual = destino;
            etapaObjetivo = destino;
            progreso = 1f;
            animando = false;
            repaint();
            return false;
        }

        etapaObjetivo = destino;
        progreso = 0f;
        animando = true;
        temporizador.start();
        repaint();
        return true;
    }

    public void setDerrota(boolean derrota) {
        this.derrota = derrota;
        repaint();
    }

    private void avanzarAnimacion() {
        progreso += PASO_ANIMACION;
        if (progreso >= 1f) {
            progreso = 1f;
            etapaActual = etapaObjetivo;
            animando = false;
            temporizador.stop();
            repaint();
            Runnable aviso = alTerminarAnimacion;
            alTerminarAnimacion = null;
            if (aviso != null) {
                aviso.run();
            }
        } else {
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            double escala = Math.min(getWidth() / (double) ANCHO_DISENO,
                    getHeight() / (double) ALTO_DISENO);
            double desplazamientoX = (getWidth() - ANCHO_DISENO * escala) / 2.0;
            double desplazamientoY = (getHeight() - ALTO_DISENO * escala) / 2.0;
            g2.translate(desplazamientoX, desplazamientoY);
            g2.scale(escala, escala);

            dibujarHorca(g2);
            dibujarCuerpo(g2);
        } finally {
            g2.dispose();
        }
    }

    private void dibujarHorca(Graphics2D g2) {
        g2.setColor(COLOR_MADERA);
        g2.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(55, 330, 265, 330));
        g2.draw(new Line2D.Double(100, 330, 100, 35));
        g2.draw(new Line2D.Double(100, 35, 230, 35));
        g2.draw(new Line2D.Double(100, 75, 135, 35));

        g2.setColor(COLOR_CUERDA);
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Double(230, 35, 230, 70));
    }

    private void dibujarCuerpo(Graphics2D g2) {
        g2.setColor(derrota ? COLOR_DERROTA : COLOR_CUERPO);
        g2.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        EtapaAhorcado[] etapas = EtapaAhorcado.values();

        for (int i = 1; i <= etapaActual.ordinal(); i++) {
            dibujarParte(g2, etapas[i], 1f);
        }

        if (animando && etapaActual.ordinal() + 1 < etapas.length) {
            dibujarParte(g2, etapas[etapaActual.ordinal() + 1], progreso);
        }
    }

    private void dibujarParte(Graphics2D g2, EtapaAhorcado etapa, float avance) {
        switch (etapa) {
            case CABEZA -> dibujarCabeza(g2, avance);
            case TRONCO -> dibujarLinea(g2, 230, 120, 230, 205, avance);
            case BRAZO_IZQ -> dibujarLinea(g2, 230, 145, 185, 185, avance);
            case BRAZO_DER -> dibujarLinea(g2, 230, 145, 275, 185, avance);
            case PIERNA_IZQ -> dibujarLinea(g2, 230, 205, 190, 270, avance);
            case PIERNA_DER -> dibujarLinea(g2, 230, 205, 270, 270, avance);
            default -> {
            }
        }
    }

    private void dibujarCabeza(Graphics2D g2, float avance) {
        double radio = 25;
        double diametro = radio * 2;
        Arc2D arco = new Arc2D.Double(230 - radio, 95 - radio, diametro, diametro,
                90, -360 * Math.max(0f, Math.min(1f, avance)), Arc2D.OPEN);
        g2.draw(arco);

        if (avance > 0.95f) {
            g2.fillOval(220, 89, 5, 5);
            g2.fillOval(235, 89, 5, 5);
            g2.draw(new Arc2D.Double(219, 99, 22, 12, 200, 140, Arc2D.OPEN));
        }
    }

    private void dibujarLinea(Graphics2D g2, double x1, double y1,
                              double x2, double y2, float avance) {
        double factor = Math.max(0f, Math.min(1f, avance));
        double xFin = x1 + (x2 - x1) * factor;
        double yFin = y1 + (y2 - y1) * factor;
        g2.draw(new Line2D.Double(x1, y1, xFin, yFin));
    }

    @Override
    public void removeNotify() {
        temporizador.stop();
        super.removeNotify();
    }
}
