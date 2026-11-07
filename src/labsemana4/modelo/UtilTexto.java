package labsemana4.modelo;

import java.text.Normalizer;

public final class UtilTexto {
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

    public static boolean esPalabraValida(String palabra) {
        if (palabra.length() < 3 || palabra.length() > 15) {
            return false;
        }
        for (int i = 0; i < palabra.length(); i++) {
            if (!esLetra(palabra.charAt(i))) {
                return false;
            }
        }
        return true;
    }
   }