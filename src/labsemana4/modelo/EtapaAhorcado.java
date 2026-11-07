/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labsemana4.modelo;

/**
 *
 * @author USER
 */
public enum EtapaAhorcado {
    VACIO,
    CABEZA,
    TRONCO,
    BRAZO_IZQ,
    BRAZO_DER,
    PIERNA_IZQ,
    PIERNA_DER;
    
    public static EtapaAhorcado dePorErrores(int errores) {
        int indice = Math.max(0, Math.min(errores, values().length - 1));
        return values()[indice];
    }
    
}
