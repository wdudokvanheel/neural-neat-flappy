package nl.wdudokvanheel.neat.flappy.gamelogic;

import java.util.ArrayList;
import java.util.List;

public class FlappyGame {
    public static final double GRAVITY = 9.8 / 10;

    public static int GAME_WIDTH = 1200;
    public static int GAME_HEIGHT = 1200;
    public static int BIRD_SIZE = 57;
    public static int xPosition = 48;
    public static int MAX_OBSTACLE_DISTANCE = 650;
    public static int OBSTACLE_WIDTH = 60;
    public static int MAX_PASS_DISTANCE = 300;
    public static int MIN_PASS_DISTANCE = 50;

    public List<Bird> birds = new ArrayList<>();
    public List<Obstacle> obstacles = new ArrayList<>();
    public int score = 0;

    public FlappyGame() {
        for (int i = 1; i < 4; i++) {
            obstacles.add(generateObstacle(i * MAX_OBSTACLE_DISTANCE));
        }
    }

    public void addBird(Bird bird) {
        bird.position = GAME_HEIGHT / 2;
        birds.add(bird);
    }

    public void addBirds(List<Bird> birds) {
        for (Bird bird : birds) {
            bird.position = GAME_HEIGHT / 2;
            this.birds.add(bird);
        }
    }

    public void update() {
        for (Obstacle obstacle : new ArrayList<>(obstacles)) {
            obstacle.x -= 2;
            if (obstacle.x + obstacle.width <= 0) {
                obstacles.remove(obstacle);
                obstacles.add(generateObstacle(obstacles.get(obstacles.size() - 1).x + MAX_OBSTACLE_DISTANCE));
                birds.forEach(bird -> bird.score++);
                score++;
            }
        }

        for (Bird bird : birds) {
            if (!bird.alive)
                continue;

            bird.update(getNextObstacle());

			if ((isInsideObstacleX() && isInsideObstacleY(bird)) || bird.position + BIRD_SIZE / 2 - 50 > GAME_HEIGHT || bird.position - BIRD_SIZE + 50 < 0) {
                double x1 = (getNextObstacle().x + getNextObstacle().width - xPosition + BIRD_SIZE / 2);
                double y1 = (getNextObstacle().bottom - getNextObstacle().top) / 2;
                double x2 = xPosition;
                double y2 = bird.position;
                double dist = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
                bird.distance = (int) Math.max(0, bird.distance + Math.max(0, FlappyGame.MAX_OBSTACLE_DISTANCE - dist));
                bird.alive = false;
            }
        }
    }

    public boolean isRunning() {
        for (Bird bird : birds) {
            if (bird.alive)
                return true;
        }
        return false;
    }

    public boolean isInsideObstacleY(Bird bird) {
        Obstacle obs = getNextObstacle();
        return bird.position - BIRD_SIZE / 2 < obs.top || bird.position + BIRD_SIZE / 2 > obs.bottom;
    }

    public boolean isInsideObstacleX() {
        Obstacle obs = getNextObstacle();
        return obs.x < xPosition + BIRD_SIZE / 2 && obs.x + obs.width > xPosition - BIRD_SIZE / 2;
    }

    private Obstacle generateObstacle(int x) {
        int height = MAX_PASS_DISTANCE;
        double shrink = Math.max(0, score) * 15;
        height -= (int) shrink;
        height = Math.max(MIN_PASS_DISTANCE, height);
        int start = (int) (Math.random() * (GAME_HEIGHT - 20 - height) + 10);
        return new Obstacle(x, OBSTACLE_WIDTH, start, start + height);
    }

    public Obstacle getNextObstacle() {
        return obstacles.getFirst();
    }
}

