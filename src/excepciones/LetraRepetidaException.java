
package excepciones;


public class LetraRepetidaException extends AhorcadoException {
  private static final long serialVersionUID = 1L;
    private char letra;

    public LetraRepetidaException(String mensaje, char letra) {
        super(mensaje);
        this.letra = letra;
    }

    public char getLetra() {
        return letra;
    }

}
