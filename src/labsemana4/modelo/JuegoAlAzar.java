/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labsemana4.modelo;

/**
 *
 * @author USER
 */
import excepciones.PalabraInvalidaException;

public class JuegoAlAzar extends JuegoBase {

    private static final long serialVersionUID = 1L;

    private AdministradorPalabras administrador;

    public JuegoAlAzar(AdministradorPalabras administrador) throws PalabraInvalidaException {
        this.administrador = administrador;
        reiniciar();
    }

    @Override
    protected String elegirPalabra() {
        return administrador.obtenerAleatoria();
    }

    @Override
    public Modalidad getModalidad() {
        return Modalidad.AL_AZAR;
    }
}
