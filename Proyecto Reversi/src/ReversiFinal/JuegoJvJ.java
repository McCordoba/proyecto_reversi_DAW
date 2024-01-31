package ReversiFinal;

import java.util.ArrayList;
import java.util.Scanner;

// MAQUINA JUGADOR
public class JuegoJvJ {
    private char[][] tablero; // Representa el estado actual del tablero
    private char jugadorActivo; // Indica el jugador que tiene el turno

    // Constructor de la clase
    public JuegoJvJ() {
        tablero = new char[8][8]; // Crea una matriz de 8x8 para representar el tablero
        inciciarTablero(); // Inicializa el tablero con las fichas iniciales
        jugadorActivo = 'O'; // Establece el jugador activo como 'O'
    }

    // Inicializa el tablero con las fichas iniciales
    public void inciciarTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tablero[i][j] = '-'; // Coloca el carácter '-' en todas las celdas del tablero
            }
        }

        // Coloca las fichas iniciales en las posiciones centrales
        tablero[3][3] = 'O';
        tablero[4][4] = 'O';
        tablero[3][4] = 'X';
        tablero[4][3] = 'X';
    }

    // Imprime el tablero actual en la consola
    public void crearTablero() {
        System.out.println("  1 2 3 4 5 6 7 8"); // Imprime los números de columna

        for (int i = 0; i < 8; i++) {
            System.out.print((i + 1) + " "); // Imprime el número de fila

            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] == 'X') {
                    System.out.print("\u25CF "); // Ficha redonda blanca
                } else if (tablero[i][j] == 'O') {
                    System.out.print("\u25CB "); // Ficha redonda negra
                } else {
                    System.out.print(tablero[i][j] + " "); // Imprime el contenido de cada celda del tablero
                }
            }

            System.out.println(); // Imprime una nueva línea después de cada fila
        }
    }

    // Inicia el juego y controla el flujo del mismo
    public void juego() {
        Scanner scanner = new Scanner(System.in); // Crea un objeto Scanner para leer la entrada del usuario

        while (true) {
            // Comprueba si no quedan movimientos posibles
            if (noQuedanMovimientosPosibles()) {
                System.out.println("No quedan movimientos posibles. El juego ha terminado.");
                System.out.println("Puntos Jugador 1 (X): " + calcularPuntuacion('X'));
                System.out.println("Puntos Jugador 2 (O): " + calcularPuntuacion('O'));
                determinarGanador(); // Llama al método para determinar el ganador
                break; // Sale del bucle while y termina el juego
            }

            System.out.println("Jugador 1: " + jugadorActivo);
            System.out.println("Introduce número de fila (1-8): ");
            int fila = obtenerEntrada(scanner, 1, 8) - 1; // Obtiene la fila ingresada por el jugador y resta 1 para obtener el índice correspondiente
            System.out.println("Introduce número de columna (1-8): ");
            int columna = obtenerEntrada(scanner, 1, 8) - 1; // Obtiene la columna ingresada por el jugador y resta 1 para obtener el índice correspondiente

            if (movimientoValido(fila, columna, jugadorActivo)) {
                tablero[fila][columna] = jugadorActivo; // Coloca la ficha del jugador en la posición especificada

                voltearFichas(fila, columna); // Voltea las fichas del oponente según las reglas del juego

                // Cambia el jugador activo para el próximo turno
                if (jugadorActivo == 'X') {
                    jugadorActivo = 'O';
                } else {
                    jugadorActivo = 'X';
                }
            } else {
                System.out.println("Movimiento no válido. Inténtalo de nuevo.");
            }
            crearTablero(); // Imprime el tablero actualizado
        }
    }

    // Obtiene una entrada válida del usuario dentro de un rango específico
    public int obtenerEntrada(Scanner scanner, int min, int max) {
        while (true) {
            try {
                String entrada = scanner.nextLine(); // Lee la entrada del usuario como una cadena
                int valor = Integer.parseInt(entrada); // Convierte la cadena a un valor entero

                if (valor >= min && valor <= max) {
                    return valor; // Devuelve el valor si está dentro del rango especificado
                } else {
                    System.out.println("El número debe estar entre " + min + " y " + max + ". Inténtalo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debes ingresar un número. Inténtalo de nuevo.");
            }
        }
    }

    // Comprueba si un movimiento es válido para un jugador dado en una posición dada
    public boolean movimientoValido(int fila, int columna, char jugadorActivo) {
        if (fila < 0 || fila >= 8 || columna < 0 || columna >= 8 || tablero[fila][columna] != '-') {
            return false; // El movimiento es inválido si la posición está fuera del tablero o no está vacía
        }

        char jugadorOponente = (jugadorActivo == 'X') ? 'O' : 'X'; // Determina el jugador oponente

        for (int xDir = -1; xDir <= 1; xDir++) {
            for (int yDir = -1; yDir <= 1; yDir++) {
                // No se puede capturar en la misma dirección que el movimiento
                if (xDir == 0 && yDir == 0) {
                    continue;
                }
                int x = fila + xDir;
                int y = columna + yDir;
                boolean encontradoOponente = false;

                // Busca al jugador oponente en la dirección especificada
                while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    if (tablero[x][y] == '-') {
                        break; // Si encuentra una celda vacía, el movimiento no es válido
                    } else if (tablero[x][y] == jugadorOponente) {
                        encontradoOponente = true;
                        x += xDir;
                        y += yDir;
                    } else if (tablero[x][y] == jugadorActivo && encontradoOponente) {
                        return true; // Si encuentra una ficha del jugador activo después de encontrar al menos una ficha del oponente, el movimiento es válido
                    } else {
                        break; // Si encuentra una ficha del jugador activo sin haber encontrado previamente una ficha del oponente, el movimiento no es válido
                    }
                }
            }
        }
        return false; // Si no se encuentra ninguna captura posible en ninguna dirección, el movimiento es inválido
    }

    // Voltea las fichas del oponente según las reglas del juego
    public void voltearFichas(int fila, int columna) {
        char jugadorOponente = (jugadorActivo == 'X') ? 'O' : 'X'; // Determina el jugador oponente

        for (int xDir = -1; xDir <= 1; xDir++) {
            for (int yDir = -1; yDir <= 1; yDir++) {
                // No se puede capturar en la misma dirección que el movimiento
                if (xDir == 0 && yDir == 0) {
                    continue;
                }

                int x = fila + xDir;
                int y = columna + yDir;
                boolean encontradoOponente = false;
                ArrayList<int[]> fichasVolteadas = new ArrayList<>();

                // Busca al jugador oponente en la dirección especificada
                while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    if (tablero[x][y] == '-') {
                        break; // Si encuentra una celda vacía, no hay fichas para voltear en esta dirección
                    } else if (tablero[x][y] == jugadorOponente) {
                        encontradoOponente = true;
                        fichasVolteadas.add(new int[]{x, y}); // Guarda las coordenadas de la ficha del oponente
                        x += xDir;
                        y += yDir;
                    } else if (tablero[x][y] == jugadorActivo && encontradoOponente) {
                        // Si encuentra una ficha del jugador activo después de encontrar al menos una ficha del oponente, voltea las fichas del oponente
                        for (int[] coordenadas : fichasVolteadas) {
                            tablero[coordenadas[0]][coordenadas[1]] = jugadorActivo;
                        }

                        break; // Sale del bucle while, ya que ya no es necesario buscar más fichas para voltear
                    } else {
                        break; // Si encuentra una ficha del jugador activo sin haber encontrado previamente una ficha del oponente, no hay fichas para voltear en esta dirección
                    }
                }
            }
        }
    }

    // Comprueba si no quedan movimientos posibles para ningún jugador
    public boolean noQuedanMovimientosPosibles() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] == '-') {
                    // Comprueba si hay al menos un movimiento válido para algún jugador en la posición actual
                    if (movimientoValido(i, j, 'X') || movimientoValido(i, j, 'O')) {
                        return false; // Si encuentra un movimiento válido, devuelve false (quedan movimientos posibles)
                    }
                }
            }
        }

        return true; // Si no encuentra ningún movimiento válido, devuelve true (no quedan movimientos posibles)
    }

    // Calcula la puntuación de un jugador dado contando sus fichas en el tablero
    public int calcularPuntuacion(char jugador) {
        int puntuacion = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] == jugador) {
                    puntuacion++;
                }
            }
        }

        return puntuacion;
    }

    // Determina el ganador del juego y lo muestra en la consola
    public void determinarGanador() {
        int puntuacionJugador1 = calcularPuntuacion('X');
        int puntuacionJugador2 = calcularPuntuacion('O');

        if (puntuacionJugador1 > puntuacionJugador2) {
            System.out.println("¡El jugador 1 (X) gana el juego!");
        } else if (puntuacionJugador2 > puntuacionJugador1) {
            System.out.println("¡El jugador 2 (O) gana el juego!");
        } else {
            System.out.println("¡El juego termina en empate!");
        }
    }
}