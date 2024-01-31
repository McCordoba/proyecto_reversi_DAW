package ReversiFinal;

import java.util.Random;
import java.util.Scanner;

public class JuegoReversi {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Selección del modo de juego
        ModoJuego modoJuego = seleccionarModoJuego(scanner);

        // Ejecutar el juego según el modo seleccionado
        switch (modoJuego) {
            case JvJ:
                System.out.println("Introduzca su nombre");
                Jugador jugador1 = new Jugador(scanner.nextLine(), '\u25CF');
                System.out.println("Introduzca su nombre");
                Jugador jugador2 = new Jugador(scanner.nextLine(), '\u25CB');

                // Sorteo para determinar qué jugador usará las fichas 'O'
                Random random = new Random();
                boolean jugador1UsaO = random.nextBoolean();

                if (jugador1UsaO) {
                    System.out.println(jugador1.getNombre() + " usará las fichas '\u25CF'");
                    System.out.println(jugador2.getNombre() + " usará las fichas '\u25CB'");
                } else {
                    System.out.println(jugador1.getNombre() + " usará las fichas '\u25CB'");
                    System.out.println(jugador2.getNombre() + " usará las fichas '\u25CF'");
                }

                JuegoJvJ juegoJvJ = new JuegoJvJ();
                juegoJvJ.crearTablero();
                juegoJvJ.juego();
                break;
            case JvM:
                JuegoJvM juegoJvM = new JuegoJvM();
                juegoJvM.crearTablero();
                juegoJvM.juego();
                break;
            case MvM:
                JuegoMvM juegoMvM = new JuegoMvM();
                juegoMvM.juego();
                break;
            default:
                System.out.println("Modo de juego no válido. Saliendo del programa.");
        }

        scanner.close();
    }

    public static ModoJuego seleccionarModoJuego(Scanner scanner) {
        System.out.println("Selecciona el modo de juego:");
        System.out.println("1. Jugador vs Jugador");
        System.out.println("2. Jugador vs Máquina");
        System.out.println("3. Máquina vs Máquina");

        while (true) {
            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        return ModoJuego.JvJ;
                    case 2:
                        return ModoJuego.JvM;
                    case 3:
                        return ModoJuego.MvM;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debes ingresar un número. Inténtalo de nuevo.");
            }
        }
    }
}


