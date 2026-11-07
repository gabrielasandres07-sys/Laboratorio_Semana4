
package labsemana4.modelo;

import java.text.Normalizer;

public class UtilTexto {
  private static final char Enie = '\u00D1'; 
  
   private UtilTexto() {
       
    }
   // metodo normalizar palabras , Pasa el texto a mayúsculas, sin tildes 
   public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String resultado = texto.trim().toUpperCase();
        resultado = Normalizer.normalize(resultado, Normalizer.Form.NFD);
       
        resultado = resultado.replace("N\u0303", String.valueOf(Enie));
        resultado = resultado.replaceAll("\\p{M}", "");
        return resultado;
    }
    // metodo que sirve para verificar el caracter
    public static boolean esLetra(char c) {
        if (c >= 'A' && c <= 'Z') {
            return true;
        }
        if (c == Enie) {
            return true;
        }
        return false;
    }
   }

