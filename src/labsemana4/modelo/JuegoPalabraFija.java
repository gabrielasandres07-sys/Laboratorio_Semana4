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
public class JuegoPalabraFija extends JuegoBase {

    private static final long serialVersionUID = 1L;

    private String palabraConfigurada;

    public JuegoPalabraFija(String palabra) throws PalabraInvalidaException {
        palabraConfigurada = palabra;
        establecerPalabraSecreta(palabra);
    }

   
    public void cambiarPalabra(String palabra) {
        palabraConfigurada = palabra;
    }

    @Override
    protected String elegirPalabra() {
        return palabraConfigurada;
    }

    @Override
    public Modalidad getModalidad() {
        return Modalidad.PALABRA_FIJA;
    }
}
