import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Tetris extends Application {
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private static final int TILE_SIZE = 30;
    private static final int EMPTY = 0;
    private static final int FILLED = 1;

    private int[][] board;
    private Tetromino currentTetromino;
    private GridPane gridPane;
    private boolean isGameOver;

    public Tetris() {
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        isGameOver = false;
    }

    @Override
    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        drawBoard();

        BorderPane root = new BorderPane();
        root.setCenter(gridPane);

        Scene scene = new Scene(root, BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (isGameOver) {
                    return;
                }

                if (event.getCode() == KeyCode.LEFT) {
                    moveLeft();
                } else if (event.getCode() == KeyCode.RIGHT) {
                    moveRight();
                } else if (event.getCode() == KeyCode.DOWN) {
                    moveDown();
                } else if (event.getCode() == KeyCode.UP) {
                    rotate();
                }
            }
        });

        primaryStage.setTitle("Tetris");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();

        startGameLoop();
    }

    private void startGameLoop() {
        Thread gameLoop = new Thread(() -> {
            while (!isGameOver) {
                try {
                    Thread.sleep(500); // テトリミノの自動落下の速度
                    Platform.runLater(() -> {
                        if (!isGameOver) {
                            moveDown();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameLoop.setDaemon(true);
        gameLoop.start();
    }

    private void moveLeft() {
        if (canMove(currentTetromino.getX() - 1, currentTetromino.getY(), currentTetromino.getShape())) {
            currentTetromino.moveLeft();
            updateBoard();
            drawBoard();
        }
    }

    private void moveRight() {
        if (canMove(currentTetromino.getX() + 1, currentTetromino.getY(), currentTetromino.getShape())) {
            currentTetromino.moveRight();
            updateBoard();
            drawBoard();
        }
    }

    private void moveDown() {
        if (canMove(currentTetromino.getX(), currentTetromino.getY() + 1, currentTetromino.getShape())) {
            currentTetromino.moveDown();
            updateBoard();
            drawBoard();
        } else {
            placeTetromino();
            clearLines();
            if (!isGameOver) {
                spawnNewTetromino();
            }
        }
    }

    private void rotate() {
        int[][] rotatedShape = currentTetromino.rotate();
        if (canMove(currentTetromino.getX(), currentTetromino.getY(), rotatedShape)) {
            currentTetromino.setShape(rotatedShape);
            updateBoard();
            drawBoard();
        }
    }

    private boolean canMove(int x, int y, int[][] shape) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != EMPTY) {
                    int newX = x + col;
                    int newY = y + row;
                    if (newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT || board[newY][newX] == FILLED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeTetromino() {
        int[][] shape = currentTetromino.getShape();
        int x = currentTetromino.getX();
        int y = currentTetromino.getY();

        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != EMPTY) {
                    int boardX = x + col;
                    int boardY = y + row;
                    board[boardY][boardX] = FILLED;
                }
            }
        }
    }

    private void clearLines() {
        List<Integer> fullLines = new ArrayList<>();

        for (int row = 0; row < BOARD_HEIGHT; row++) {
            boolean lineFilled = true;
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col] == EMPTY) {
                    lineFilled = false;
                    break;
                }
            }
            if (lineFilled) {
                fullLines.add(row);
            }
        }

        if (!fullLines.isEmpty()) {
            for (int row : fullLines) {
                for (int col = 0; col < BOARD_WIDTH; col++) {
                    board[row][col] = EMPTY;
                }
            }

            for (int i = fullLines.get(0) - 1; i >= 0; i--) {
                System.arraycopy(board[i], 0, board[i + fullLines.size()], 0, BOARD_WIDTH);
            }
        }
    }

    private void spawnNewTetromino() {
        Random random = new Random();
        TetrominoType[] tetrominoTypes = TetrominoType.values();
        TetrominoType type = tetrominoTypes[random.nextInt(tetrominoTypes.length)];

        int[][] shape = type.getShape();
        int x = (BOARD_WIDTH - shape[0].length) / 2;
        int y = 0;

        currentTetromino = new Tetromino(shape, x, y, Color.BLACK);
        if (!canMove(x, y, shape)) {
            isGameOver = true;
            System.out.println("Game Over");
        }
    }

    private void updateBoard() {
        clearBoard();
        placeTetromino();
    }

    private void clearBoard() {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = EMPTY;
            }
        }
    }

    private void drawBoard() {
        gridPane.getChildren().clear();
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setStroke(Color.BLACK);
                if (board[row][col] == FILLED) {
                    tile.setFill(Color.BLUE);
                } else {
                    tile.setFill(Color.WHITE);
                }
                gridPane.add(tile, col, row);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

