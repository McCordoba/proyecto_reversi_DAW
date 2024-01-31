package ReversiFinal;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class JuegoMvM {
    private char[][] tablero;
    private char jugadorActivo;

    public JuegoMvM() {
        tablero = new char[8][8];
        inciciarTablero();
        jugadorActivo = 'O';
    }

    public void inciciarTablero() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tablero[i][j] = '-';
            }
        }

        tablero[3][3] = 'O';
        tablero[4][4] = 'O';
        tablero[3][4] = 'X';
        tablero[4][3] = 'X';
    }

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

    public void juego() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            if (noQuedanMovimientosPosibles()) {
                System.out.println("No quedan movimientos posibles. El juego ha terminado.");
                System.out.println("Puntos Jugador 1 (X): " + calcularPuntuacion('X'));
                System.out.println("Puntos Jugador 2 (O): " + calcularPuntuacion('O'));
                determinarGanador(); // Llamar al método para determinar el ganador
                break; // Salir del bucle while y terminar el juego
            }

            // Obtener el ArrayList con el movimiento random
            ArrayList<Integer> movimiento = obtenerMovimientosPosibles();

            System.out.println("Jugador 1: " + jugadorActivo);

            if (!movimiento.isEmpty()) {
                int fila = movimiento.get(0);
                int columna = movimiento.get(1);

                tablero[fila][columna] = jugadorActivo;

                voltearFichas(fila, columna);

                if (jugadorActivo == 'X') {
                    jugadorActivo = 'O';
                } else {
                    jugadorActivo = 'X';
                }
            } else {
                System.out.println("Movimiento no válido. Se pasa el turno al siguiente jugador.");
                jugadorActivo = cambiarJugador();
                continue; // Saltar a la siguiente iteración del bucle
            }

            crearTablero();

            // Esperar hasta que se presione la tecla Enter
            System.out.println("Presiona Enter para continuar...");
            scanner.nextLine();
        }

        scanner.close();
    }


    public boolean movimientoValido(int fila, int columna, char jugadorActivo) {
        if (fila < 0 || fila > 7 || columna < 0 || columna > 7 || tablero[fila][columna] != '-') {
            return false;
        }

        char jugadorOponente = (jugadorActivo == 'X') ? 'O' : 'X';

        for (int xDir = -1; xDir <= 1; xDir++) {
            for (int yDir = -1; yDir <= 1; yDir++) {
                // No podemos capturar en la misma dirección que el movimiento
                if (xDir == 0 && yDir == 0) {
                    continue;
                }

                int x = fila + xDir;
                int y = columna + yDir;
                boolean encontradoOponente = false;

                while (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    if (tablero[x][y] == '-') {
                        break;
                    } else if (tablero[x][y] == jugadorOponente) {
                        encontradoOponente = true;
                        x += xDir;
                        y += yDir;
                    } else if (tablero[x][y] == jugadorActivo && encontradoOponente) {
                        return true;
                    } else {
                        break;
                    }
                }
            }
        }
        return false;
    }

    public void voltearFichas(int fila, int columna) {
        int[] xDirecciones = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] yDirecciones = {0, 1, 1, 1, 0, -1, -1, -1};

        for (int i = 0; i < 8; i++) {
            int f = fila + xDirecciones[i];
            int c = columna + yDirecciones[i];
            boolean sePuedeVoltear = false;

            while (f >= 0 && f < 8 && c >= 0 && c < 8 && tablero[f][c] == cambiarJugador()) {
                f += xDirecciones[i];
                c += yDirecciones[i];
                sePuedeVoltear = true;
            }

            if (sePuedeVoltear && f >= 0 && f < 8 && c >= 0 && c < 8 && tablero[f][c] == jugadorActivo) {
                f = fila + xDirecciones[i];
                c = columna + yDirecciones[i];

                while (tablero[f][c] == cambiarJugador()) {
                    tablero[f][c] = jugadorActivo;
                    f += xDirecciones[i];
                    c += yDirecciones[i];
                }
            }
        }
    }

    public ArrayList<Integer> obtenerMovimientosPosibles() {
        ArrayList<Integer> filas = new ArrayList<>();
        ArrayList<Integer> columnas = new ArrayList<>();
        ArrayList<Integer> movimiento = new ArrayList<>();

        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                if (movimientoValido(fila, columna, jugadorActivo)) {
                    filas.add(fila);
                    columnas.add(columna);
                }
            }
        }

        if (!filas.isEmpty() && !columnas.isEmpty()) {
            Random random = new Random();
            int indice = random.nextInt(filas.size());
            movimiento.add(filas.get(indice));
            movimiento.add(columnas.get(indice));
        } else {
            movimiento.add(-1); // Valor de fila inválido
            movimiento.add(-1); // Valor de columna inválido
        }

        return movimiento;
    }

    public boolean noQuedanMovimientosPosibles() {
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                if (movimientoValido(fila, columna, jugadorActivo)) {
                    return false; // Se encontró un movimiento válido, el juego aún no ha terminado
                }
            }
        }
        return true; // No se encontraron movimientos válidos, el juego ha terminado
    }

    public int calcularPuntuacion(char jugador) {
        int puntuacion = 0;
        for (int fila = 0; fila < 8; fila++) {
            for (int columna = 0; columna < 8; columna++) {
                if (tablero[fila][columna] == jugador) {
                    puntuacion++;
                }
            }
        }
        return puntuacion;
    }

    public void determinarGanador() {
        int puntuacionJugador1 = calcularPuntuacion('X');
        int puntuacionJugador2 = calcularPuntuacion('O');

        if (puntuacionJugador1 > puntuacionJugador2) {
            System.out.println("¡El Jugador 1 (X) es el ganador!");
        } else if (puntuacionJugador1 < puntuacionJugador2) {
            System.out.println("¡El Jugador 2 (O) es el ganador!");
        } else {
            System.out.println("¡Empate! No hay ganador.");
        }
    }

    public char cambiarJugador() {
        if (jugadorActivo == 'X') {
            return 'O';
        } else {
            return 'X';
        }
    }
}

