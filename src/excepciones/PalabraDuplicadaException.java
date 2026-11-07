
package excepciones;


public class PalabraDuplicadaException extends AhorcadoException {
     private static final long serialVersionUID = 1L;

    public PalabraDuplicadaException(String mensaje) {
        super(mensaje);
    }

}
