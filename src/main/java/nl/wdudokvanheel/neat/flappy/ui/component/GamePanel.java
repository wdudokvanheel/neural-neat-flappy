package nl.wdudokvanheel.neat.flappy.ui.component;

import nl.wdudokvanheel.neat.flappy.gamelogic.Bird;
import nl.wdudokvanheel.neat.flappy.gamelogic.FlappyGame;
import nl.wdudokvanheel.neat.flappy.gamelogic.Obstacle;
import nl.wdudokvanheel.neat.flappy.ui.NeatFlappyWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel {
    private final Color COLOR_BG = Color.decode("#d8fcf8");
    private final Color COLOR_OBSTACLE = Color.decode("#493C2B");
    private final Color[] COLOR_BIRD = {
            Color.decode("#360414"),
            Color.decode("#cf0f4c"),
            Color.decode("#A46422"),
            Color.decode("#31A2F2"),
            Color.decode("#EB8931"),
            Color.decode("#F7E26B"),
            Color.decode("#2F484E"),
            Color.decode("#44891A"),
            Color.decode("#A3CE27"),
            Color.decode("#1B2632"),
    };

    private FlappyGame game;
    NeatFlappyWindow ui;

    public GamePanel(NeatFlappyWindow ui) {
        this.ui = ui;
        setSize(FlappyGame.GAME_WIDTH, FlappyGame.GAME_HEIGHT);
        setPreferredSize(new Dimension(FlappyGame.GAME_WIDTH, FlappyGame.GAME_HEIGHT));
    }

    public void setGame(FlappyGame game) {
        this.game = game;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(hints);

        g.setColor(COLOR_BG);
        g.fillRect(0, 0, FlappyGame.GAME_WIDTH, FlappyGame.GAME_HEIGHT);

        if (game == null) {
            return;
        }

        Obstacle nextObstacle = game.getNextObstacle();
        double x = (nextObstacle.x + (nextObstacle.width));
        double y = nextObstacle.top + ((nextObstacle.bottom - nextObstacle.top) / 2.0);
        g.setColor(COLOR_BIRD[0]);
        g.fillRect((int) (x - 5), (int) (y - 5), 12, 12);
        g.setColor(COLOR_BIRD[1]);

        g.fillRect((int) (x - 5) + 3, (int) (y - 5) + 3, 6, 6);

        BufferedImage body = NeatFlappyWindow.images.get("obstacle-body");
        BufferedImage capTop = NeatFlappyWindow.images.get("obstacle-cap-top");
        BufferedImage capBottom = NeatFlappyWindow.images.get("obstacle-cap-bottom");

        for (Obstacle obstacle : game.obstacles) {
            g.drawImage(body, (int) Math.round(obstacle.x + (obstacle.width - (body.getWidth() * 3)) / 2), 0, body.getWidth() * 3, obstacle.top, null);
            g.drawImage(capTop, (int) Math.round(obstacle.x + (obstacle.width - (capTop.getWidth() * 3)) / 2), obstacle.top - (capTop.getHeight() * 3), capTop.getWidth() * 3, capTop.getHeight() * 3, null);

            g.drawImage(body, (int) Math.round(obstacle.x + (obstacle.width - (body.getWidth() * 3)) / 2), obstacle.bottom, body.getWidth() * 3, game.GAME_HEIGHT - obstacle.bottom, null);
            g.drawImage(capBottom, (int) Math.round(obstacle.x + (obstacle.width - (capBottom.getWidth() * 3)) / 2), obstacle.bottom, capBottom.getWidth() * 3, capBottom.getHeight() * 3, null);
        }

        BufferedImage image;
        for (int i = 0; i < Math.min(10, game.birds.size()); i++) {
            Bird bird = game.birds.get(i);

            if (!bird.alive)
                continue;
            g.setColor(COLOR_BIRD[i % COLOR_BIRD.length]);

            if (bird.jumpWait > 8)
                image = NeatFlappyWindow.images.get("bird-" + NeatFlappyWindow.BIRD_COLOR_NAMES[i % 10] + "-0");
            else
                image = NeatFlappyWindow.images.get("bird-" + NeatFlappyWindow.BIRD_COLOR_NAMES[i % 10] + "-1");

            g.drawImage(image, (int) Math.round(FlappyGame.xPosition - image.getWidth() * 1.5), (int) Math.round(bird.position - image.getHeight() * 1.5), image.getWidth() * 3, image.getHeight() * 3, null);

        }

        g.setColor(COLOR_BIRD[1]);
        g.drawString("Score: " + game.score, FlappyGame.GAME_WIDTH - 100, 16);
        g.drawString("Alive: " + ui.getAliveBirds(game) + " / " + game.birds.size(), FlappyGame.GAME_WIDTH - 100, 32);
        g.drawString("Highscore: " + ui.highscore, FlappyGame.GAME_WIDTH - 100, 48);
    }
}

