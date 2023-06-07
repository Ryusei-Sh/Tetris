public enum TetrominoType {
    I(new int[][]{{1, 1, 1, 1}}),
    J(new int[][]{{1, 1, 1}, {0, 0, 1}}),
    L(new int[][]{{1, 1, 1}, {1, 0, 0}}),
    O(new int[][]{{1, 1}, {1, 1}}),
    S(new int[][]{{0, 1, 1}, {1, 1, 0}}),
    T(new int[][]{{1, 1, 1}, {0, 1, 0}}),
    Z(new int[][]{{1, 1, 0}, {0, 1, 1}});

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
