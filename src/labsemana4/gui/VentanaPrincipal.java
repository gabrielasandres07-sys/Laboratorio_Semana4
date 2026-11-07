package labsemana4.gui;

import labsemana4.gui.sonido.GestorSonido;
import labsemana4.modelo.AdministradorPalabras;
import labsemana4.modelo.JuegoAhorcado;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;

public class VentanaPrincipal extends JFrame {

    private static final String CARD_MENU = "menu";
    private static final String CARD_JUEGO = "juego";
    private static final String CARD_ADMIN = "admin";

    private final AdministradorPalabras administrador;
    private final GestorSonido sonido;
    private final CardLayout cardLayout;
    private final JPanel contenedor;
    private final PanelMenu panelMenu;

    private PanelJuego panelJuego;
    private PanelAdministracion panelAdministracion;

    public VentanaPrincipal(AdministradorPalabras administrador) {
        super("Juego del Ahorcado");
        this.administrador = administrador;
        this.sonido = new GestorSonido();
        this.cardLayout = new CardLayout();
        this.contenedor = new JPanel(cardLayout);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(820, 620));
        setSize(900, 680);
        setLocationRelativeTo(null);

        panelMenu = new PanelMenu(administrador);
        contenedor.add(panelMenu, CARD_MENU);
        add(contenedor);

        panelMenu.getBtnPalabraFija().addActionListener(e -> iniciarPalabraFija());
        panelMenu.getBtnAlAzar().addActionListener(e -> iniciarAlAzar());
        panelMenu.getBtnAdministrar().addActionListener(e -> mostrarAdministracion());

        cardLayout.show(contenedor, CARD_MENU);
    }

    private void iniciarPalabraFija() {
        JuegoAhorcado juego = panelMenu.crearJuegoPalabraFija();
        if (juego != null) {
            mostrarJuego(juego);
        }
    }

    private void iniciarAlAzar() {
        if (administrador.cantidad() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No hay palabras cargadas en el administrador.",
                    "Sin palabras", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JuegoAhorcado juego = panelMenu.crearJuegoAlAzar();
        if (juego != null) {
            mostrarJuego(juego);
        }
    }

    private void mostrarJuego(JuegoAhorcado juego) {
        if (panelJuego != null) {
            contenedor.remove(panelJuego);
        }
        panelJuego = new PanelJuego(juego, this, sonido);
        contenedor.add(panelJuego, CARD_JUEGO);
        cardLayout.show(contenedor, CARD_JUEGO);
        panelJuego.revalidate();
        panelJuego.repaint();
    }

    public void mostrarMenu() {
        cardLayout.show(contenedor, CARD_MENU);
    }

    public void mostrarAdministracion() {
        if (panelAdministracion != null) {
            contenedor.remove(panelAdministracion);
        }
        panelAdministracion = new PanelAdministracion(administrador);
        panelAdministracion.getBtnVolver().addActionListener(e -> mostrarMenu());
        contenedor.add(panelAdministracion, CARD_ADMIN);
        cardLayout.show(contenedor, CARD_ADMIN);
    }
}
