
package excepciones;


public class LetraInvalidaException extends AhorcadoException {
    private static final long serialVersionUID = 1L;

    public LetraInvalidaException(String mensaje) {
        super(mensaje);
    }

}
