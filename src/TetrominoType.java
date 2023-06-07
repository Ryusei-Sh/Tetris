import javafx.scene.paint.Color;


public enum TetrominoType {
    I(new int[][]{{1, 1, 1, 1}}, Color.CYAN),
    J(new int[][]{{1, 1, 1}, {0, 0, 1}}, Color.BLUE),
    L(new int[][]{{1, 1, 1}, {1, 0, 0}}, Color.ORANGE),
    O(new int[][]{{1, 1}, {1, 1}}, Color.YELLOW),
    S(new int[][]{{0, 1, 1}, {1, 1, 0}}, Color.GREEN),
    T(new int[][]{{1, 1, 1}, {0, 1, 0}}, Color.MAGENTA),
    Z(new int[][]{{1, 1, 0}, {0, 1, 1}}, Color.RED);

    private final int[][] shape;
    private final Color color;

    TetrominoType(int[][] shape, Color color) {
        this.shape = shape;
        this.color = color;
    }

    public int[][] getShape() {
        return shape;
    }

    public Color getColor() {
        return color;
    }
}

