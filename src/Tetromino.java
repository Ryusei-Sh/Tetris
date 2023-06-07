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
    
    public Color getColor() {
        return color;
    }    

    public int[][] getShape() {
        return shape;
    }

    public void setShape(int[][] shape) {
        this.shape = shape;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public void moveDown() {
        y++;
    }

    public int[][] rotate() {
        // テトリミノの回転ロジックを実装
        // ...
        return shape;
    }
}
