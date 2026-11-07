
package labsemana4.gui;

import excepciones.PalabraInvalidaException;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import labsemana4.modelo.AdministradorPalabras;
import labsemana4.modelo.JuegoAhorcado;
import labsemana4.modelo.JuegoAlAzar;
import labsemana4.modelo.JuegoPalabraFija;
import labsemana4.modelo.Modalidad;


public class PanelMenu extends JPanel {

    private AdministradorPalabras administrador;
    private JPasswordField txtPalabraSecreta;
    private JButton btnPalabraFija;
    private JButton btnAlAzar;
    private JButton btnAdministrar;
    private JButton btnSalir;
    private JLabel lblMensaje;

    public PanelMenu(AdministradorPalabras administrador) {
        this.administrador = administrador;
        setLayout(new GridLayout(0, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        add(new JLabel("JUEGO DEL AHORCADO", SwingConstants.CENTER));

        add(new JLabel("Palabra secreta (solo para palabra fija):"));
        txtPalabraSecreta = new JPasswordField();
        add(txtPalabraSecreta);

        btnPalabraFija = new JButton(Modalidad.PALABRA_FIJA.getTexto());
        btnAlAzar = new JButton(Modalidad.AL_AZAR.getTexto());
        btnAdministrar = new JButton("Administrar palabras");
        btnSalir = new JButton("Salir");
        add(btnPalabraFija);
        add(btnAlAzar);
        add(btnAdministrar);
        add(btnSalir);

        lblMensaje = new JLabel(" ", SwingConstants.CENTER);
        add(lblMensaje);

        btnSalir.addActionListener(e -> System.exit(0));
    }

   
    public JuegoAhorcado crearJuegoPalabraFija() {
        String palabra = new String(txtPalabraSecreta.getPassword());
        try {
            JuegoAhorcado juego = new JuegoPalabraFija(palabra);
            txtPalabraSecreta.setText("");
            lblMensaje.setText(" ");
            return juego;
        } catch (PalabraInvalidaException ex) {
            lblMensaje.setForeground(Color.RED);
            lblMensaje.setText(ex.getMessage());
            return null;
        }
    }

   
    public JuegoAhorcado crearJuegoAlAzar() {
        try {
            JuegoAhorcado juego = new JuegoAlAzar(administrador);
            lblMensaje.setText(" ");
            return juego;
        } catch (PalabraInvalidaException ex) {
            lblMensaje.setForeground(Color.RED);
            lblMensaje.setText(ex.getMessage());
            return null;
        }
    }

    public JButton getBtnPalabraFija() {
        return btnPalabraFija;
    }

    public JButton getBtnAlAzar() {
        return btnAlAzar;
    }

    public JButton getBtnAdministrar() {
        return btnAdministrar;
    }
}