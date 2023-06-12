import javafx.scene.paint.Color;

public class Tetromino {
    private int[][] shape;
    private int x;
    private int y;
    private Color color;

    public Tetromino(int[][] shape, int x, int y, Color color) {
        this.shape = shape;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }

    public int[][] getShape() {
        return shape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }

    public void moveDown() {
        y++;
    }

    public void moveRight() {
        x++;
    }

    public void moveLeft() {
        x--;
    }

    public int[][] rotate() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotatedShape = new int[cols][rows];
    
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rotatedShape[col][rows - row - 1] = shape[row][col];
            }
        }
    
        return rotatedShape;
    }    
}
