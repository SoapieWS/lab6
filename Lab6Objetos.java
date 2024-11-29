/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab6objetos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author eliza
 */
public class Lab6Objetos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        static Scanner scanner = new Scanner(System.in);
    static Map<String, Player> players = new HashMap<>(); // Almacena usuarios registrados
    static Player loggedInPlayer = null; // Jugador actualmente conectado

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== MENÚ INICIO ===");
            System.out.println("1. INICIAR SESIÓN");
            System.out.println("2. REGISTRO");
            System.out.println("3. SALIR");
            System.out.print("Elige una opción: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (option) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    System.out.println("Saliendo del programa...");
                    return;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }

    static void login() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        if (players.containsKey(username) && players.get(username).getPassword().equals(password)) {
            loggedInPlayer = players.get(username);
            System.out.println("Inicio de sesión exitoso. ¡Bienvenido, " + loggedInPlayer.getName() + "!");
            mainMenu();
        } else {
            System.out.println("Usuario o contraseña incorrectos. Intenta de nuevo.");
        }
    }

    static void register() {
        System.out.print("Nombre del jugador: ");
        String name = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();

        if (players.containsKey(username)) {
            System.out.println("El username ya está en uso. Intenta de nuevo.");
            return;
        }

        System.out.print("Contraseña (5 caracteres): ");
        String password = scanner.nextLine();

        if (password.length() != 5) {
            System.out.println("La contraseña debe tener exactamente 5 caracteres.");
            return;
        }

        players.put(username, new Player(name, username, password));
        System.out.println("Registro exitoso. Ahora puedes iniciar sesión.");
    }

    static void mainMenu() {
        while (loggedInPlayer != null) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. JUGAR X-0");
            System.out.println("2. RANKING");
            System.out.println("3. CERRAR SESIÓN");
            System.out.print("Elige una opción: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer

            switch (option) {
                case 1:
                    playGame();
                    break;
                case 2:
                    showRanking();
                    break;
                case 3:
                    System.out.println("Sesión cerrada. Regresando al menú de inicio...");
                    loggedInPlayer = null;
                    return;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }

    static void playGame() {
        System.out.print("Username del Player 2: ");
        String opponentUsername = scanner.nextLine();

        if (opponentUsername.equalsIgnoreCase("EXIT")) {
            System.out.println("Regresando al menú principal...");
            return;
        }

        if (!players.containsKey(opponentUsername)) {
            System.out.println("El usuario no está registrado. Intenta de nuevo.");
            return;
        }

        Player player2 = players.get(opponentUsername);

        System.out.println("Iniciando partida entre " + loggedInPlayer.getName() + " (X) y " + player2.getName() + " (O).");
        TicTacToeGame game = new TicTacToeGame(loggedInPlayer, player2);
        game.start();
    }

    static void showRanking() {
        System.out.println("\n=== RANKING ===");
        players.values().stream()
                .sorted(Comparator.comparingInt(Player::getPoints).reversed())
                .forEach(player -> System.out.println(player.getName() + " - Puntos: " + player.getPoints()));
    }
}

class Player {
    private String name;
    private String username;
    private String password;
    private int points;

    public Player(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.points = 0;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}

class TicTacToeGame {
    private char[][] board = new char[3][3];
    private Player player1;
    private Player player2;

    public TicTacToeGame(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        for (int i = 0; i < 3; i++) {
            Arrays.fill(board[i], ' ');
        }
    }

    public void start() {
        Player currentPlayer = player1;
        char currentMark = 'X';
        int moves = 0;

        while (true) {
            printBoard();
            System.out.println(currentPlayer.getName() + " (" + currentMark + "), es tu turno.");
            System.out.print("Ingresa fila (0-2): ");
            int row = scanner.nextInt();
            System.out.print("Ingresa columna (0-2): ");
            int col = scanner.nextInt();

            if (row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ') {
                System.out.println("Movimiento inválido. Intenta de nuevo.");
                continue;
            }

            board[row][col] = currentMark;
            moves++;

            if (checkWin(currentMark)) {
                printBoard();
                System.out.println("¡" + currentPlayer.getName() + " (" + currentMark + ") ha ganado!");
                currentPlayer.addPoints(1);
                break;
            }

            if (moves == 9) {
                printBoard();
                System.out.println("¡Empate!");
                break;
            }

            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            currentMark = (currentMark == 'X') ? 'O' : 'X';
        }
    }

    private void printBoard() {
        System.out.println("  0 1 2");
        for (int i = 0; i < 3; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j]);
                if (j < 2) System.out.print("|");
            }
            System.out.println();
            if (i < 2) System.out.println("  -----");
        }
    }

    private boolean checkWin(char mark) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) return true;
            if (board[0][i] == mark && board[1][i] == mark && board[2][i] == mark) return true;
        }
        return (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) ||
               (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark);
    }
    
}
