package nl.wdudokvanheel.neat.flappy.gamelogic;

public class Bird {
    public boolean alive = true;
    //Vertical acceleration
    public double acceleration = 0;
    //Vertical position
    public double position = 0;
    //Distance traveled
    public int distance = 0;
    //Score
    public int score = 0;
    public int jumpWait = 0;

    public void jump(double power) {
        if(jumpWait > 0)
            return;

        acceleration = 12 * power;
        jumpWait = 15;
    }

    public void jump(double power, double maxPower) {
        if(jumpWait > 0)
            return;

        acceleration = power * maxPower;
        jumpWait = 15;
    }

    public void update(Obstacle nextObstacle) {
        if(jumpWait > 0)
            jumpWait--;

        acceleration -= FlappyGame.GRAVITY;
        position -= acceleration;
        distance++;
    }
}
