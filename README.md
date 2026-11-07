# Juego del Ahorcado — Laboratorio Programación II

Este juego de ahorcado lo hicimos en Java con interfaz gráfica Swing. Tiene dos modalidades: **palabra fija**, donde un jugador escribe una palabra para que otro la adivine, y **palabra al azar**, donde el juego escoge una palabra de forma random de la lista. También tiene administración de palabras y sonidos generados por código.

## Integrantes

| Integrante | Parte del trabajo |
|---|---|
| Gabriela Nohemy Sandres | Núcleo del juego: `JuegoAhorcado`, `JuegoBase`, `JuegoPalabraFija`, `JuegoAlAzar` y los enums del modelo |
| Josué David Ponce | Palabras, excepciones y administración: `UtilTexto`, `AdministradorPalabras`, excepciones, `PanelAdministracion`, `PanelMenu` y README |
| Mario René Doubleday | Pantalla de juego, dibujo y sonido: `Main`, `VentanaPrincipal`, `PanelJuego`, `PanelDibujo`, `EfectoSonido`, `GestorSonido` |

## Cómo ejecutar

1. Abrir el proyecto en NetBeans (Java 17 o superior).
2. Ejecutar la clase principal `labsemana4.Main`.
3. Todo el juego se maneja desde la ventana; no se usa la consola.

## Lista de palabras

Para la lista inicial del modo al azar escogimos colores, porque fue lo primero que se nos vino a la cabeza y hay una gran variedad de colores para elegir. Agregamos CASTAÑO, que es un color con Ñ, y MONTAÑA, para probar que el programa funciona con la letra Ñ.

## Paquetes

| Paquete | Contenido |
|---|---|
| `excepciones` | `AhorcadoException` y sus 5 excepciones hijas |
| `labsemana4.modelo` | Interfaz, clase abstracta, variantes, administrador, `UtilTexto` y enums. No importa nada de Swing, AWT ni sonido |
| `labsemana4.gui` | Ventana principal y paneles de la interfaz |
| `labsemana4.gui.sonido` | `EfectoSonido` y `GestorSonido` |

## Diagrama de clases

```
PAQUETE excepciones
                       Exception
                           ▲
                           │ extends
                   AhorcadoException
                           ▲
       ┌───────────┬───────┴───────┬───────────────┬────────────────┐
       │           │               │               │                │
 LetraInvalida  LetraRepetida  PartidaTerminada  PalabraInvalida  PalabraDuplicada
  Exception      Exception       Exception        Exception        Exception


PAQUETE labsemana4.modelo

                  «interface» JuegoAhorcado
                             ▲
                             │ implements
                  «abstract» JuegoBase  ─ ─ ─ ─ ─ ─ ─►  UtilTexto «final»
                    ▲              ▲                       ▲
            extends │              │ extends               ┆ usa
                    │              │                       ┆
         JuegoPalabraFija      JuegoAlAzar ◇─────────► AdministradorPalabras

  Enums: EstadoPartida, ResultadoLetra, Modalidad, EtapaAhorcado


PAQUETE labsemana4.gui

  Main ──crea──► VentanaPrincipal (JFrame + CardLayout)
                     │
                     ├── PanelMenu ──────────► crea JuegoPalabraFija / JuegoAlAzar
                     ├── PanelAdministracion ► usa AdministradorPalabras
                     └── PanelJuego ─────────► usa JuegoAhorcado (polimorfismo)
                            └── PanelDibujo ─► dibuja EtapaAhorcado con Graphics2D


PAQUETE labsemana4.gui.sonido

  GestorSonido ──reproduce──► EfectoSonido «enum»
       ▲
       ┆ usan (un solo gestor compartido)
  PanelJuego, PanelMenu, PanelAdministracion
```

**Leyenda:** `▲ extends / implements` = herencia · `◇──►` = contiene (composición) · `─ ─►` / `┆` = usa (dependencia).

## Reglas de validación

- **Mayúsculas y minúsculas:** no se distinguen. Todo se normaliza a mayúsculas.
- **Tildes:** se ignoran. `Á` equivale a `A`, `É` a `E`, y la diéresis también (`Ü` equivale a `U`).
- **Ñ:** es una letra distinta de la N y se acepta como válida.
- **Alfabeto válido:** A–Z y Ñ.
- **Letra ingresada:** debe ser un único carácter alfabético. Una letra repetida no resta intentos.
- **Palabras:** entre 3 y 15 letras, sin espacios, números ni símbolos. No se permiten palabras duplicadas (se comparan ya normalizadas, por lo que `ÁRBOL` y `arbol` son la misma).

La normalización está en un solo lugar, `UtilTexto.normalizar()`: pasa el texto a mayúsculas, separa las tildes con `java.text.Normalizer`, vuelve a juntar la Ñ y elimina las tildes sueltas.

## Excepciones

Todas heredan de `AhorcadoException` (que hereda de `Exception`). Se lanzan en el modelo y se capturan en la GUI para mostrar el mensaje sin detener el programa.

| Excepción | Cuándo se lanza |
|---|---|
| `LetraInvalidaException` | La entrada está vacía, tiene más de un carácter o no es una letra |
| `LetraRepetidaException` | La letra ya se había ingresado (guarda la letra y ofrece `getLetra()`) |
| `PartidaTerminadaException` | Se intenta jugar con la partida ya ganada o perdida |
| `PalabraInvalidaException` | La palabra está vacía o no cumple las reglas |
| `PalabraDuplicadaException` | La palabra ya existe en la lista del administrador |

Quedarse sin intentos no es una excepción: es el estado `PERDIDA` de la partida.

Cuando la GUI captura una excepción de uso (letra inválida o repetida, palabra inválida o duplicada) también suena el efecto `ALERTA`. `PanelMenu` y `PanelAdministracion` reciben el mismo `GestorSonido` que `PanelJuego` con `setGestorSonido(...)`, así el botón de silenciar afecta a todas las pantallas.

## Métodos recursivos

| Método | Clase | Caso base |
|---|---|---|
| `revelarLetra(char letra, int posicion)` | `JuegoBase` | Se llegó al final de la palabra |
| `haGanado(int posicion)` | `JuegoBase` | Se recorrió toda la palabra (true) o se encontró una diferencia (false) |
| `existe(String palabra, int indice)` | `AdministradorPalabras` | Se llegó al final de la lista (false) o se encontró la palabra (true) |

## Justificación del uso de Serializable

Lo utilizamos porque `Serializable` es una **interfaz marcadora**, es decir, que no tiene ningún método: solo marca la clase para indicarle a Java que sus objetos se pueden convertir en bytes y luego reconstruirlos.

En el proyecto que hicimos no se escribe ningún archivo, pero las clases del modelo declaran `implements Serializable` porque representan el estado de una partida. Así ya está listo para poder guardar o enviar una partida en el futuro sin cambiar ni afectar el diseño.

Cada clase declara un `serialVersionUID`, que es el número de versión de la clase y permite verificar que un objeto guardado corresponde a la misma versión. `AdministradorPalabras` también es serializable porque `JuegoAlAzar` lo contiene. Los enums y las excepciones ya son serializables.

### Por qué el campo Random es transient

El campo `Random` de `AdministradorPalabras` se declaró `transient` para que no se serialice, porque no guarda información importante y se puede volver a crear en cualquier momento. Cuando se deserializa quedaría en `null`, y `obtenerAleatoria()` lo vuelve a crear si hace falta. Por eso lo declaramos transient.
