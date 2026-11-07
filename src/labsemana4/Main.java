package labsemana4;

import labsemana4.gui.VentanaPrincipal;
import labsemana4.modelo.AdministradorPalabras;

import javax.swing.SwingUtilities;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdministradorPalabras administrador = new AdministradorPalabras();
            VentanaPrincipal ventana = new VentanaPrincipal(administrador);
            ventana.setVisible(true);
        });
    }
}
