
package labsemana4.gui;
 import excepciones.PalabraDuplicadaException;
import excepciones.PalabraInvalidaException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import labsemana4.modelo.AdministradorPalabras;

    public class PanelAdministracion extends JPanel{
    private AdministradorPalabras administrador;
    private JTextField txtPalabra;
    private JButton btnAgregar;
    private JButton btnVolver;
    private JLabel lblMensaje;
    private JLabel lblCantidad;
    private DefaultListModel<String> modeloLista;
    
     public PanelAdministracion(AdministradorPalabras administrador) {
        this.administrador = administrador;
        setLayout(new BorderLayout(10, 10));

        // Parte de arriba
        JPanel panelArriba = new JPanel(new FlowLayout());
        panelArriba.add(new JLabel("Nueva palabra:"));
        txtPalabra = new JTextField(15);
        panelArriba.add(txtPalabra);
        btnAgregar = new JButton("Agregar");
        panelArriba.add(btnAgregar);
        add(panelArriba, BorderLayout.NORTH);

        // Centro
        modeloLista = new DefaultListModel<>();
        JList<String> lista = new JList<>(modeloLista);
        add(new JScrollPane(lista), BorderLayout.CENTER);

        // Abajo: 
        JPanel panelAbajo = new JPanel(new BorderLayout());
        lblMensaje = new JLabel("Escriba una palabra y presione Agregar.");
        lblCantidad = new JLabel();
        btnVolver = new JButton("Volver al menú");
        panelAbajo.add(lblMensaje, BorderLayout.NORTH);
        panelAbajo.add(lblCantidad, BorderLayout.CENTER);
        panelAbajo.add(btnVolver, BorderLayout.EAST);
        add(panelAbajo, BorderLayout.SOUTH);

       
        btnAgregar.addActionListener(e -> agregarPalabra());
        txtPalabra.addActionListener(e -> agregarPalabra());

        actualizarLista();
    }

    // metodo  agregar la palabra 
    private void agregarPalabra() {
        try {
            administrador.agregar(txtPalabra.getText());
            lblMensaje.setForeground(new Color(0, 128, 0));
            lblMensaje.setText("Palabra agregada correctamente ");
            txtPalabra.setText("");
            actualizarLista();
        } catch (PalabraInvalidaException ex) {
            lblMensaje.setForeground(Color.RED);
            lblMensaje.setText(ex.getMessage());
        } catch (PalabraDuplicadaException ex) {
            lblMensaje.setForeground(Color.RED);
            lblMensaje.setText(ex.getMessage());
        }
        txtPalabra.requestFocus();
    }

    
    private void actualizarLista() {
        modeloLista.clear();
        for (String palabra : administrador.getPalabras()) {
            modeloLista.addElement(palabra);
        }
        lblCantidad.setText("Total de palabras: " + administrador.cantidad());
    }

   
    public JButton getBtnVolver() {
        return btnVolver;
    }
}

