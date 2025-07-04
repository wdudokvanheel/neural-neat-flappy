package nl.wdudokvanheel.neat.flappy.gamelogic;

public class Obstacle {
    public int x;
    public int width;
    public int top;
    public int bottom;

    public Obstacle(int x, int width, int top, int bottom) {
        this.x = x;
        this.width = width;
        this.top = top;
        this.bottom = bottom;
    }
}
