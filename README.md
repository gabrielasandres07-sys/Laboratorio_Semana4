Mario Doubleday-22611133
Josue Ponce-22611140
Gabriela Nohemy-22541357

---

# Laboratorio Semana 4: Juego del Ahorcado

Aplicación de escritorio desarrollada en Java con Swing que implementa el juego del ahorcado. El proyecto corresponde al Laboratorio de la Semana 4 y aplica programación orientada a objetos, herencia, interfaces, clases abstractas, enumeraciones, recursividad, excepciones personalizadas, serialización, interfaz gráfica, animación y sonido.

## Requisitos

- Java 21 o superior.
- NetBeans (el proyecto es un proyecto Java Ant).
- No requiere librerías externas ni conexión a Internet.
- No utiliza archivos de imagen, audio ni serialización binaria en disco.

## Cómo ejecutar

### En NetBeans

1. Abrir NetBeans.
2. Ir a `File` → `Open Project…`.
3. Seleccionar la carpeta del proyecto, la que contiene `build.xml` y `nbproject`.
4. Presionar `Run Project` (`F6`).

La clase principal está configurada en `nbproject/project.properties` como `main.class=labsemana4.Main`.

### Desde la línea de comandos

```bat
ant clean jar
java -jar dist/LabSemana4.jar
```

## Funcionalidades

- Menú principal con tres opciones: palabra fija, palabra al azar y administración de palabras.
- Partida con palabra secreta elegida por el jugador.
- Partida con palabra aleatoria tomada del administrador.
- Entrada de letras por teclado y teclado en pantalla de la A a la Z, incluida la Ñ.
- Contador de intentos restantes, letras acertadas y letras erradas.
- Dibujo del ahorcado con `Graphics2D`, animado mediante `javax.swing.Timer`.
- Mensajes de victoria y derrota, con opción de jugar de nuevo o volver al menú.
- Efectos de sonido generados por código con `javax.sound.sampled`, con opción de silenciar.
- Administración de palabras: agregar palabras nuevas y ver la lista existente.
- Cambio de palabra secreta durante una partida de palabra fija.

## Reglas de validación

### Palabras

Una palabra se considera válida cuando cumple todas estas condiciones:

- Tiene entre 3 y 15 letras.
- Solo contiene letras de la A a la Z, incluida la Ñ, con o sin tilde.
- No contiene espacios, números, signos de puntuación ni símbolos.
- No se repite dentro del administrador de palabras, comparando sin distinguir mayúsculas, minúsculas ni tildes.

Las palabras se normalizan antes de guardarse o compararse: se convierten a mayúsculas, se eliminan las tildes y se conserva la Ñ. Por eso `árbol` y `ARBOL` se consideran la misma palabra y la segunda se rechaza como duplicada.

### Letras

Una letra se considera válida cuando cumple todas estas condiciones:

- Es exactamente un carácter.
- Es una letra de la A a la Z o la Ñ.
- No fue ingresada antes en la misma partida, ni como acierto ni como error.

Si la letra es inválida se lanza `LetraInvalidaException`; si ya fue usada, `LetraRepetidaException`; si la partida terminó, `PartidaTerminadaException`.

### Partida

- Cada partida comienza con 6 intentos.
- Un acierto revela todas las apariciones de la letra y no descuenta intentos.
- Un error descuenta un intento y agrega una parte al dibujo.
- La partida se gana al revelar todas las letras de la palabra.
- La partida se pierde al llegar a 0 intentos.
- Una partida terminada no admite más letras hasta reiniciarla.

## Diagrama de clases

```text
excepciones
├── AhorcadoException
├── LetraInvalidaException
├── LetraRepetidaException
├── PalabraDuplicadaException
├── PalabraInvalidaException
└── PartidaTerminadaException

labsemana4.modelo
├── JuegoAhorcado              (interfaz)
├── JuegoBase                  (abstracta, implementa JuegoAhorcado)
│   ├── JuegoPalabraFija
│   └── JuegoAlAzar
├── AdministradorPalabras
├── UtilTexto
├── EtapaAhorcado              (enum)
├── EstadoPartida              (enum)
├── Modalidad                  (enum)
└── ResultadoLetra             (enum)

labsemana4.gui
├── PanelMenu
├── PanelJuego
├── PanelDibujo
├── PanelAdministracion
└── VentanaPrincipal

labsemana4.gui.sonido
├── EfectoSonido               (enum)
└── GestorSonido

labsemana4
└── Main
```

Relaciones principales:

- `JuegoBase` implementa la interfaz `JuegoAhorcado` y concentra la lógica común.
- `JuegoPalabraFija` y `JuegoAlAzar` heredan de `JuegoBase`.
- `JuegoAlAzar` usa composición con `AdministradorPalabras` para obtener la palabra.
- `VentanaPrincipal` contiene un `CardLayout` con `PanelMenu`, `PanelJuego` y `PanelAdministracion`.
- `PanelJuego` compone un `PanelDibujo` y un `GestorSonido`.
- `GestorSonido` genera audio a partir de los datos de `EfectoSonido`.
- Todas las excepciones heredan de `AhorcadoException`.

## Recursividad

El proyecto usa recursividad en tres puntos:

- `JuegoBase.revelarLetra`: recorre las posiciones de la palabra para revelar cada aparición de la letra acertada.
- `JuegoBase.haGanado`: recorre las posiciones de la palabra para comprobar si todas están reveladas.
- `AdministradorPalabras.existe`: recorre la lista de palabras para detectar duplicados.

## Uso de enumeraciones

- `EtapaAhorcado`: representa las etapas del dibujo (`VACIO`, `CABEZA`, `TRONCO`, `BRAZO_IZQ`, `BRAZO_DER`, `PIERNA_IZQ`, `PIERNA_DER`).
- `EstadoPartida`: representa el estado de la partida (`EN_CURSO`, `GANADA`, `PERDIDA`).
- `Modalidad`: distingue la partida con palabra fija de la partida con palabra al azar.
- `ResultadoLetra`: indica si una letra fue acierto o error.
- `EfectoSonido`: define la frecuencia y duración de cada efecto de audio.

## Excepciones personalizadas

Todas las excepciones del proyecto heredan de `AhorcadoException`, que a su vez hereda de `Exception`. Cada una representa un error concreto del dominio:

- `PalabraInvalidaException`: la palabra no cumple las reglas.
- `PalabraDuplicadaException`: la palabra ya existe en el administrador.
- `LetraInvalidaException`: la entrada no es una letra válida.
- `LetraRepetidaException`: la letra ya se usó en la partida.
- `PartidaTerminadaException`: se intentó jugar una partida finalizada.

La interfaz gráfica captura estas excepciones y las muestra al usuario sin cerrar la aplicación.

## Justificación de `Serializable`

Las clases `JuegoBase`, `AdministradorPalabras` y las excepciones implementan `Serializable` porque el enunciado exige que el estado del juego pueda serializarse. Esto permite guardar y recuperar el estado de una partida o del administrador de palabras mediante flujos de objetos, sin perder la información acumulada.

Se declara `serialVersionUID` en cada clase serializable para mantener la compatibilidad entre versiones de la clase al deserializar. En `AdministradorPalabras`, el atributo `Random` se marca como `transient` porque no forma parte del estado lógico que interesa conservar y no es necesario serializarlo; al deserializar, el generador se reconstruye.

## Animación y sonido

- La animación del dibujo se implementa con `javax.swing.Timer`, que avanza el trazo de cada parte del cuerpo de forma progresiva y repinta el panel.
- El sonido se genera en tiempo de ejecución con `javax.sound.sampled`: `GestorSonido` construye las ondas, las reproduce en un hilo aparte y omite el audio cuando no hay una línea disponible o el usuario lo silencia.

## Estructura del proyecto

```text
Laboratorio_Semana4/
├── src/
│   ├── excepciones/
│   ├── labsemana4/
│   │   ├── Main.java
│   │   ├── modelo/
│   │   └── gui/
│   │       └── sonido/
├── build.xml
├── manifest.mf
├── nbproject/
└── README.md
```

