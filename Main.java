package tictactoe;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

enum GameState {
    PROGRESS,
    X_WON,
    O_WON,
    DRAW,
    IMPOSSIBLE
}

public class Main {

    private static final Map<GameState, String> messages = new HashMap<>() {
        {
           put(GameState.PROGRESS, "Game not finished");
           put(GameState.X_WON, "X wins");
           put(GameState.O_WON, "O wins");
           put(GameState.DRAW, "Draw");
           put(GameState.IMPOSSIBLE, "Impossible");
        }
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        char[][] gameState = new char[][] {
            { ' ', ' ', ' ' },
            { ' ', ' ', ' ' },
            { ' ', ' ', ' ' }
        };

        char currentPlayer = 'X';

        displayGameState(gameState);

        while (true) {
            System.out.print("Enter coordinates: ");
            String x = scanner.next();
            String y = scanner.next();

            if (!Character.isDigit(x.charAt(0)) || !Character.isDigit(y.charAt(0))) {
                System.out.println("You should enter numbers!");
            } else {
                int xCoordinate = Integer.parseInt(x);
                int yCoordinate = Integer.parseInt(y);

                if (xCoordinate < 1 || xCoordinate > 3 || yCoordinate < 1 || yCoordinate > 3) {
                    System.out.println("Coordinates should be from 1 to 3!");
                } else {
                    char current = gameState[xCoordinate - 1][yCoordinate - 1];

                    if (current == 'X' || current == 'O') {
                        System.out.println("This cell is occupied! Choose another one!");
                    } else {
                        gameState[xCoordinate - 1][yCoordinate - 1] = currentPlayer;

                        displayGameState(gameState);

                        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';

                        GameState state = analyzeGameState(gameState);
                        boolean isFinished = state == GameState.O_WON || state == GameState.X_WON || state == GameState.DRAW;

                        if (isFinished) {
                            System.out.println(messages.get(state));
                            break;
                        }
                    }
                }
            }
        }
    }

    private static void displayGameState(char[][] gameState) {
        System.out.println("---------");

        for (char[] row: gameState) {
            System.out.print("| ");

            for (char symbol: row) {
                System.out.print(Character.toString(symbol).concat(" "));
            }
            System.out.print("| ");
            System.out.println();
        }

        System.out.println("---------");
    }

    private static GameState analyzeGameState(char[][] gameState) {
        boolean isValid = isValidGameState(gameState);
        boolean oWinner = isWinner(gameState, 'O');
        boolean xWinner = isWinner(gameState, 'X');

        boolean emptyCellsPresent = hasEmptyCells(gameState);

        if (isValid && (oWinner ^ xWinner)) {
            return oWinner ? GameState.O_WON : GameState.X_WON;
        } else if (isValid && emptyCellsPresent && !xWinner && !oWinner) {
            return GameState.PROGRESS;
        } else if (isValid && !xWinner && !oWinner) {
            return GameState.DRAW;
        } else {
            return GameState.IMPOSSIBLE;
        }
    }

    private static boolean isValidGameState(char[][] gameState) {
        int numOfSymX = 0;
        int numOfSymO = 0;

        for (char[] row: gameState) {
            for (char symbol: row) {
                if (symbol == 'O') {
                    numOfSymO++;
                }

                if (symbol == 'X') {
                    numOfSymX++;
                }
            }
        }

        return Math.abs(numOfSymO - numOfSymX) < 2;
    }

    private static boolean isWinner(char[][] gameState, char side) {
        boolean hTop = gameState[0][0] == side && gameState[0][1] == side && gameState[0][2] == side;
        boolean hMiddle = gameState[1][0] == side && gameState[1][1] == side && gameState[1][2] == side;
        boolean hBottom = gameState[2][0] == side && gameState[2][1] == side && gameState[2][2] == side;

        boolean vLeft = gameState[0][0] == side && gameState[1][0] == side && gameState[2][0] == side;
        boolean vMiddle = gameState[0][1] == side && gameState[1][1] == side && gameState[2][1] == side;
        boolean vRight = gameState[0][2] == side && gameState[1][2] == side && gameState[2][2] == side;

        boolean diagonalRight = gameState[0][0] == side && gameState[1][1] == side && gameState[2][2] == side;
        boolean diagonalLeft = gameState[0][2] == side && gameState[1][1] == side && gameState[2][0] == side;

        return hTop || hMiddle || hBottom || vLeft || vMiddle || vRight || diagonalRight || diagonalLeft;
    }

    private static boolean hasEmptyCells(char[][] gameState) {
        int emptyCellCount = 0;

        for (char[] row: gameState) {
            for (char symbol: row) {
                if (symbol == ' ' || symbol == '_') {
                    emptyCellCount++;
                }
            }
        }

        return emptyCellCount > 0;
    }
}
