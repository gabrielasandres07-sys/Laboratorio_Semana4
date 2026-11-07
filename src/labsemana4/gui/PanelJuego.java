package labsemana4.gui;

import excepciones.LetraInvalidaException;
import excepciones.LetraRepetidaException;
import excepciones.PalabraInvalidaException;
import excepciones.PartidaTerminadaException;
import labsemana4.gui.sonido.EfectoSonido;
import labsemana4.gui.sonido.GestorSonido;
import labsemana4.modelo.EstadoPartida;
import labsemana4.modelo.JuegoAhorcado;
import labsemana4.modelo.JuegoPalabraFija;
import labsemana4.modelo.Modalidad;
import labsemana4.modelo.ResultadoLetra;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PanelJuego extends JPanel {

    private static final String ALFABETO = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
    private static final Color COLOR_EXITO = new Color(0, 120, 0);
    private static final Color COLOR_ERROR = new Color(170, 0, 0);
    private static final Color FONDO_ACIERTO = new Color(198, 239, 206);
    private static final Color FONDO_ERROR = new Color(255, 205, 210);

    private final JuegoAhorcado juego;
    private final VentanaPrincipal ventana;
    private final GestorSonido sonido;

    private final JLabel lblModalidad = new JLabel();
    private final JLabel lblPalabra = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblIntentos = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblAcertadas = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblErradas = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblMensaje = new JLabel("", SwingConstants.CENTER);
    private final JLabel lblAudio = new JLabel(" ");
    private final JLabel lblFinal = new JLabel("", SwingConstants.CENTER);

    private final JTextField txtLetra = new JTextField(3);
    private final JButton btnProbar = new JButton("Probar");
    private final JButton btnCambiarPalabra = new JButton("Cambiar palabra");
    private final JButton btnSonido = new JButton();
    private final JButton btnJugarDeNuevo = new JButton("Jugar de nuevo");
    private final JButton btnVolverMenuFinal = new JButton("Volver al menú");
    private final JPanel panelFinal = new JPanel(new BorderLayout(8, 8));
    private final PanelDibujo panelDibujo = new PanelDibujo();
    private final Map<Character, JButton> teclas = new LinkedHashMap<>();

    public PanelJuego(JuegoAhorcado juego, VentanaPrincipal ventana, GestorSonido sonido) {
        this.juego = juego;
        this.ventana = ventana;
        this.sonido = sonido;

        setLayout(new BorderLayout(12, 12));
        setBorder(new EmptyBorder(14, 14, 14, 14));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearInferior(), BorderLayout.SOUTH);

        configurarEventos();
        sonido.setAvisoSinAudio(() ->
                lblAudio.setText("Audio no disponible: el juego continúa sin sonido."));
        if (!sonido.isDisponible()) {
            lblAudio.setText("Audio no disponible: el juego continúa sin sonido.");
        }

        actualizarVista();
        mostrarMensaje("Adivina la palabra ingresando una letra a la vez.", false);
        txtLetra.requestFocusInWindow();
    }

    private JPanel crearCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout(10, 0));
        lblModalidad.setText("El Ahorcado · " + juego.getModalidad().getTexto());
        lblModalidad.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actualizarTextoBotonSonido();
        btnSonido.addActionListener(e -> alternarSonido());
        JButton btnMenu = new JButton("Menú");
        btnMenu.addActionListener(e -> ventana.mostrarMenu());
        acciones.add(btnSonido);
        acciones.add(btnMenu);

        cabecera.add(lblModalidad, BorderLayout.WEST);
        cabecera.add(acciones, BorderLayout.EAST);
        return cabecera;
    }

    private JPanel crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(18, 0));
        centro.add(panelDibujo, BorderLayout.WEST);
        centro.add(crearPanelInformacion(), BorderLayout.CENTER);
        return centro;
    }

    private JPanel crearPanelInformacion() {
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(2, 6, 2, 6));

        lblPalabra.setFont(new Font("Monospaced", Font.BOLD, 34));
        lblPalabra.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblIntentos.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblIntentos.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblAcertadas.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblAcertadas.setForeground(COLOR_EXITO);
        lblAcertadas.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblErradas.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblErradas.setForeground(COLOR_ERROR);
        lblErradas.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);

        info.add(Box.createVerticalStrut(4));
        info.add(lblPalabra);
        info.add(Box.createVerticalStrut(12));
        info.add(lblIntentos);
        info.add(Box.createVerticalStrut(6));
        info.add(lblAcertadas);
        info.add(Box.createVerticalStrut(2));
        info.add(lblErradas);
        info.add(Box.createVerticalStrut(10));
        info.add(lblMensaje);
        info.add(Box.createVerticalStrut(10));
        info.add(crearTeclado());
        info.add(Box.createVerticalStrut(10));
        info.add(crearFilaEntrada());
        info.add(Box.createVerticalGlue());
        return info;
    }

    private JPanel crearTeclado() {
        JPanel teclado = new JPanel(new GridLayout(0, 9, 4, 4));
        teclado.setAlignmentX(Component.CENTER_ALIGNMENT);
        teclado.setMaximumSize(new Dimension(520, 140));

        for (char letra : ALFABETO.toCharArray()) {
            JButton boton = new JButton(String.valueOf(letra));
            boton.setFont(new Font("SansSerif", Font.BOLD, 13));
            boton.setMargin(new Insets(2, 2, 2, 2));
            boton.setFocusable(false);
            boton.setOpaque(true);
            boton.addActionListener(e -> procesarEntrada(String.valueOf(letra)));
            teclas.put(letra, boton);
            teclado.add(boton);
        }
        return teclado;
    }

    private JPanel crearFilaEntrada() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel etiqueta = new JLabel("Letra:");
        txtLetra.setFont(new Font("Monospaced", Font.BOLD, 18));
        txtLetra.setHorizontalAlignment(JTextField.CENTER);
        txtLetra.setPreferredSize(new Dimension(52, 30));

        btnProbar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnProbar.addActionListener(e -> procesarEntrada(txtLetra.getText()));

        fila.add(etiqueta);
        fila.add(txtLetra);
        fila.add(btnProbar);

        if (juego.getModalidad() == Modalidad.PALABRA_FIJA) {
            btnCambiarPalabra.addActionListener(e -> cambiarPalabra());
            fila.add(btnCambiarPalabra);
        }
        return fila;
    }

    private JPanel crearInferior() {
        JPanel inferior = new JPanel();
        inferior.setLayout(new BoxLayout(inferior, BoxLayout.Y_AXIS));

        lblAudio.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblAudio.setForeground(new Color(120, 90, 0));
        lblAudio.setAlignmentX(Component.CENTER_ALIGNMENT);
        inferior.add(lblAudio);

        panelFinal.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                new EmptyBorder(8, 10, 8, 10)));
        panelFinal.setVisible(false);

        lblFinal.setFont(new Font("SansSerif", Font.BOLD, 15));
        panelFinal.add(lblFinal, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnJugarDeNuevo.addActionListener(e -> reiniciarPartida());
        btnVolverMenuFinal.addActionListener(e -> ventana.mostrarMenu());
        botones.add(btnJugarDeNuevo);
        botones.add(btnVolverMenuFinal);
        panelFinal.add(botones, BorderLayout.SOUTH);

        inferior.add(panelFinal);
        return inferior;
    }

    private void configurarEventos() {
        txtLetra.addActionListener(e -> procesarEntrada(txtLetra.getText()));
    }

    private void procesarEntrada(String texto) {
        if (juego.getEstado() != EstadoPartida.EN_CURSO) {
            mostrarMensaje("La partida ya terminó. Presione «Jugar de nuevo».", true);
            return;
        }

        try {
            ResultadoLetra resultado = juego.ingresarLetra(texto);
            txtLetra.setText("");
            actualizarVista();

            if (resultado == ResultadoLetra.ACIERTO) {
                mostrarMensaje("¡Acertaste! La letra está en la palabra.", false);
            } else {
                mostrarMensaje("Esa letra no está. Intentos restantes: "
                        + juego.getIntentosRestantes() + ".", true);
            }

            reproducirSegunEstado(resultado);

            if (juego.getEstado() != EstadoPartida.EN_CURSO) {
                finalizarPartida();
            } else {
                txtLetra.requestFocusInWindow();
            }
        } catch (LetraInvalidaException ex) {
            manejarError(ex.getMessage());
        } catch (LetraRepetidaException ex) {
            manejarError(ex.getMessage());
        } catch (PartidaTerminadaException ex) {
            manejarError(ex.getMessage());
            finalizarPartida();
        }
    }

    private void manejarError(String mensaje) {
        sonido.reproducir(EfectoSonido.ALERTA);
        mostrarMensaje(mensaje, true);
        txtLetra.selectAll();
        txtLetra.requestFocusInWindow();
    }

    private void reproducirSegunEstado(ResultadoLetra resultado) {
        switch (juego.getEstado()) {
            case GANADA -> sonido.reproducir(EfectoSonido.VICTORIA);
            case PERDIDA -> sonido.reproducir(EfectoSonido.DERROTA);
            case EN_CURSO -> sonido.reproducir(resultado == ResultadoLetra.ACIERTO
                    ? EfectoSonido.ACIERTO
                    : EfectoSonido.ERROR);
            default -> {
            }
        }
    }

    private void actualizarVista() {
        lblPalabra.setText(formatearPalabra(juego.getPalabraMostrada()));
        lblIntentos.setText("Intentos: " + juego.getIntentosRestantes()
                + " / " + juego.getMaxIntentos());
        lblAcertadas.setText("Acertadas: " + formatearLista(juego.getLetrasAcertadas()));
        lblErradas.setText("Erradas: " + formatearLista(juego.getLetrasErradas()));

        actualizarTeclado();

        boolean animando = panelDibujo.setEtapa(juego.getEtapa(), () -> {
            if (juego.getEstado() == EstadoPartida.EN_CURSO) {
                setEntradaHabilitada(true);
                txtLetra.requestFocusInWindow();
            }
        });
        if (animando) {
            setEntradaHabilitada(false);
        }

        panelDibujo.setDerrota(juego.getEstado() == EstadoPartida.PERDIDA);
    }

    private void actualizarTeclado() {
        Color fondoNormal = new Color(238, 238, 238);
        for (Map.Entry<Character, JButton> entrada : teclas.entrySet()) {
            char letra = entrada.getKey();
            JButton boton = entrada.getValue();
            if (juego.getLetrasAcertadas().contains(letra)) {
                boton.setBackground(FONDO_ACIERTO);
                boton.setForeground(COLOR_EXITO);
            } else if (juego.getLetrasErradas().contains(letra)) {
                boton.setBackground(FONDO_ERROR);
                boton.setForeground(COLOR_ERROR);
            } else {
                boton.setBackground(fondoNormal);
                boton.setForeground(Color.BLACK);
            }
        }
    }

    private void finalizarPartida() {
        setEntradaHabilitada(false);
        EstadoPartida estado = juego.getEstado();

        if (estado == EstadoPartida.GANADA) {
            lblFinal.setText("¡Ganaste! Completaste la palabra.");
            lblFinal.setForeground(COLOR_EXITO);
            lblPalabra.setText(formatearPalabra(juego.getPalabraSecreta()));
            panelDibujo.setDerrota(false);
        } else if (estado == EstadoPartida.PERDIDA) {
            lblFinal.setText("Perdiste. Se agotaron los " + juego.getMaxIntentos()
                    + " intentos. La palabra era " + juego.getPalabraSecreta() + ".");
            lblFinal.setForeground(COLOR_ERROR);
            panelDibujo.setDerrota(true);
        }

        panelFinal.setVisible(true);
        revalidate();
        repaint();
    }

    private void reiniciarPartida() {
        try {
            juego.reiniciar();
            panelFinal.setVisible(false);
            panelDibujo.setDerrota(false);
            setEntradaHabilitada(true);
            txtLetra.setText("");
            mostrarMensaje("Nueva partida. Adivina la palabra letra por letra.", false);
            actualizarVista();
            txtLetra.requestFocusInWindow();
            revalidate();
            repaint();
        } catch (PalabraInvalidaException ex) {
            sonido.reproducir(EfectoSonido.ALERTA);
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "No se pudo reiniciar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cambiarPalabra() {
        if (!(juego instanceof JuegoPalabraFija fija)) {
            return;
        }

        JPasswordField campo = new JPasswordField(15);
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel("Nueva palabra secreta (3 a 15 letras):"), BorderLayout.NORTH);
        panel.add(campo, BorderLayout.CENTER);

        int opcion = JOptionPane.showConfirmDialog(this, panel, "Cambiar palabra",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        fija.cambiarPalabra(new String(campo.getPassword()));
        reiniciarPartida();
    }

    private void setEntradaHabilitada(boolean habilitada) {
        txtLetra.setEnabled(habilitada);
        btnProbar.setEnabled(habilitada);
        for (JButton boton : teclas.values()) {
            boton.setEnabled(habilitada);
        }
    }

    private void alternarSonido() {
        sonido.setSilenciado(!sonido.isSilenciado());
        actualizarTextoBotonSonido();
    }

    private void actualizarTextoBotonSonido() {
        btnSonido.setText(sonido.isSilenciado() ? "Sonido: OFF" : "Sonido: ON");
    }

    private void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setForeground(esError ? COLOR_ERROR : COLOR_EXITO);
        lblMensaje.setText(mensaje);
    }

    private String formatearPalabra(String palabra) {
        StringBuilder sb = new StringBuilder();
        for (char c : palabra.toCharArray()) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private String formatearLista(List<Character> letras) {
        if (letras.isEmpty()) {
            return "—";
        }
        StringBuilder sb = new StringBuilder();
        for (Character c : letras) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
