import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TetrisGUI extends Application {
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private static final int TILE_SIZE = 30;
    private static final int EMPTY = 0;
    private static final int FILLED = 1;
    private Tetromino currentTetromino;
    private int[][] board;
    private GridPane gridPane;

    public TetrisGUI() {
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        currentTetromino = generateRandomTetromino();
        // 盤面を初期化する処理を追加する場合はここに記述
    }

    private Tetromino generateRandomTetromino() {
        Random random = new Random();
        TetrominoType[] tetrominoTypes = TetrominoType.values();
        TetrominoType randomType = tetrominoTypes[random.nextInt(tetrominoTypes.length)];
        int[][] shape = randomType.getShape();
        int startX = (BOARD_WIDTH - shape[0].length) / 2;
        int startY = 0;
        Color color = randomType.getColor();
        return new Tetromino(shape, startX, startY, color);
    }    
    
    @Override
    public void start(Stage primaryStage) {
        gridPane = new GridPane();
        
        drawBoard();

        Scene scene = new Scene(gridPane);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.LEFT) {
                    moveTetrominoLeft();
                } else if (keyCode == KeyCode.RIGHT) {
                    moveTetrominoRight();
                } else if (keyCode == KeyCode.DOWN) {
                    moveTetrominoDown();
                }
            }
        });

        primaryStage.setTitle("Tetris");
        primaryStage.setScene(scene);
        primaryStage.show();

        // ゲームのループを開始
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            moveDown();
            drawBoard();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // テトロミノの生成
        spawnNextTetromino();
    }

    private void moveDown() {
        if (canMoveTetromino(currentTetromino, currentTetromino.getX(), currentTetromino.getY() + 1)) {
            currentTetromino.moveDown();
        } else {
            mergeTetrominoWithBoard();
            clearLines();
            currentTetromino = generateRandomTetromino();
        }
    }

    private void clearLines() {
        List<Integer> fullLines = new ArrayList<>();

        for (int row = 0; row < BOARD_HEIGHT; row++) {
            boolean lineIsFull = true;
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col] != FILLED) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                fullLines.add(row);
            }
        }

        for (Integer row : fullLines) {
            // ラインをクリア（上の行を一つ下にシフト）
            for (int i = row; i > 0; i--) {
                System.arraycopy(board[i - 1], 0, board[i], 0, BOARD_WIDTH);
            }
            // ラインの一番上を空にする
            Arrays.fill(board[0], EMPTY);
        }
    }

    private void mergeTetrominoWithBoard() {
        int[][] shape = currentTetromino.getShape();
        int tetrominoHeight = shape.length;
        int tetrominoWidth = shape[0].length;
        int tetrominoX = currentTetromino.getX();
        int tetrominoY = currentTetromino.getY();

        for (int row = 0; row < tetrominoHeight; row++) {
            for (int col = 0; col < tetrominoWidth; col++) {
                if (shape[row][col] == FILLED) {
                    int boardX = tetrominoX + col;
                    int boardY = tetrominoY + row;
                    if (boardY >= 0 && boardY < BOARD_HEIGHT) {
                        board[boardY][boardX] = FILLED;
                    }
                }
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
                    tile.setFill(Color.BLACK);
                } else {
                    tile.setFill(Color.WHITE);
                }
                gridPane.add(tile, col, row);
            }
        }

        // 現在のテトリスミノを盤面に描画する
        int[][] shape = currentTetromino.getShape();
        int startX = currentTetromino.getX();
        int startY = currentTetromino.getY();
        Color tetrominoColor = currentTetromino.getColor();
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == FILLED) {
                    Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                    tile.setStroke(Color.BLACK);
                    tile.setFill(tetrominoColor);
                    gridPane.add(tile, startX + col, startY + row);
                }
            }
        }
    }

    private void moveTetrominoDown() {
        if (canMoveTetromino(currentTetromino, currentTetromino.getX(), currentTetromino.getY() + 1)) {
            currentTetromino.moveDown();
            drawBoard();
        }
    }

    private void moveTetrominoRight() {
        if (canMoveTetromino(currentTetromino, currentTetromino.getX() + 1, currentTetromino.getY())) {
            currentTetromino.moveRight();
            drawBoard();
        }
    }

    private void moveTetrominoLeft() {
        if (canMoveTetromino(currentTetromino, currentTetromino.getX() - 1, currentTetromino.getY())) {
            currentTetromino.moveLeft();
            drawBoard();
        }
    }

    private boolean canMoveTetromino(Tetromino tetromino, int newX, int newY) {
        int[][] shape = tetromino.getShape();
        int tetrominoHeight = shape.length;
        int tetrominoWidth = shape[0].length;

        // 移動後の位置が盤面の範囲外であれば移動不可
        if (newX < 0 || newX + tetrominoWidth > BOARD_WIDTH || newY < 0 || newY + tetrominoHeight > BOARD_HEIGHT) {
            return false;
        }

        // 移動後の位置に既に他のテトリスブロックが存在する場合は移動不可
        for (int row = 0; row < tetrominoHeight; row++) {
            for (int col = 0; col < tetrominoWidth; col++) {
                if (shape[row][col] == FILLED) {
                    int boardX = newX + col;
                    int boardY = newY + row;
                    if (boardY >= 0 && board[boardY][boardX] == FILLED) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void spawnNextTetromino() {
        currentTetromino = generateRandomTetromino();
        if (!canMoveTetromino(currentTetromino, currentTetromino.getX(), currentTetromino.getY())) {
            // テトリミノを配置できない状態（ゲームオーバー）
            // ゲームオーバー処理を行い、ゲームをリセットするなどの処理を行うことができます
            // 以下はゲームをリセットする例です
            resetGame();
        }
    }

    private void resetGame() {
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        currentTetromino = generateRandomTetromino();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
