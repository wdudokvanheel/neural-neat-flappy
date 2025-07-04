package nl.wdudokvanheel.neat.flappy;

/**
 * @Author Wesley Dudok van Heel
 */
public class Vector2i {
    public int x;
    public int y;

    public Vector2i() {
        x = 0;
        y = 0;
    }

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2i add(int value) {
        return new Vector2i(x + value, y + value);
    }

    public Vector2i subtract(int x, int y) {
        return new Vector2i(this.x - x, this.y - y);
    }

    @Override
    public String toString() {
        return "Vector2i{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
