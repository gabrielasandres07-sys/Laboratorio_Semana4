
package labsemana4.modelo;


import excepciones.PalabraDuplicadaException;
import excepciones.PalabraInvalidaException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class AdministradorPalabras implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> palabras;
    private transient Random random; 

    public AdministradorPalabras() {
        palabras = new ArrayList<>();
        random = new Random();
        palabras.add("ROJO");
        palabras.add("AZUL");
        palabras.add("VERDE");
        palabras.add("AMARILLO");
        palabras.add("NARANJA");
        palabras.add("MORADO");
        palabras.add("ROSADO");
        palabras.add("BLANCO");
        palabras.add("NEGRO");
        palabras.add("CELESTE");
        palabras.add("TURQUESA");
        palabras.add("CASTAÑO");
        palabras.add("MONTAÑA");
    }

    // metodo que valida y agrega una palabra 
    public void agregar(String palabra) throws PalabraInvalidaException, PalabraDuplicadaException {
        String normalizada = UtilTexto.normalizar(palabra);
        validar(normalizada);
        if (existe(normalizada, 0)) {
            throw new PalabraDuplicadaException("La palabra " + normalizada + "  ya existe en la lista.");
        }
        palabras.add(normalizada);
    }

    // metodo que Revisa que la palabra 
    private void validar(String palabra) throws PalabraInvalidaException {
        String mensaje = "La palabra debe tener entre 3 y 15 letras, sin espacios ni números ";
        if (palabra.length() < 3 || palabra.length() > 15) {
            throw new PalabraInvalidaException(mensaje);
        }
        for (int i = 0; i < palabra.length(); i++) {
            if (UtilTexto.esLetra(palabra.charAt(i)) == false) {
                throw new PalabraInvalidaException(mensaje);
            }
        }
    }

    // metodo que  busca la palabra en la lista 
    private boolean existe(String palabra, int indice) {
        if (indice >= palabras.size()) {
            return false; 
        }
        if (palabras.get(indice).equals(palabra)) {
            return true; 
        }
        return existe(palabra, indice + 1); 
    }

    // Devuelve una palabra 
    public String obtenerAleatoria() {
        if (random == null) {
            random = new Random();
        }
        int posicion = random.nextInt(palabras.size());
        return palabras.get(posicion);
    }

    public int cantidad() {
        return palabras.size();
    }

    
    public List<String> getPalabras() {
        return Collections.unmodifiableList(new ArrayList<>(palabras));
    }
}