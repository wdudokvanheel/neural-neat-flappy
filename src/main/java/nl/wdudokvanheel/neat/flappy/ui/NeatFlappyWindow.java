package nl.wdudokvanheel.neat.flappy.ui;

import nl.wdudokvanheel.neat.flappy.gamelogic.Bird;
import nl.wdudokvanheel.neat.flappy.gamelogic.FlappyGame;
import nl.wdudokvanheel.neat.flappy.neat.NeatBird;
import nl.wdudokvanheel.neat.flappy.ui.component.BirdDetails;
import nl.wdudokvanheel.neat.flappy.ui.component.ButtonPanel;
import nl.wdudokvanheel.neat.flappy.ui.component.GamePanel;
import nl.wdudokvanheel.neat.flappy.ui.component.InfoPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NeatFlappyWindow extends JFrame implements KeyListener {
    public static String[] BIRD_COLOR_NAMES = {"blue", "red", "greena", "purple", "bluel", "yellow", "teal", "lila", "pink", "greenb"};
    public static String[] BIRD_COLORS = {"#134ccf", "#cf0f4c", "#11cf20", "#6c0dcf", "#8badf6", "#f68bad", "#14cfb8", "#bf8bf6", "#cf0e98", "#98cf10"};

    private Logger logger = LoggerFactory.getLogger(NeatFlappyWindow.class);

    public static HashMap<String, BufferedImage> images = new HashMap<>();

    public int speed = 1;
    public boolean paused = true;
    public boolean noGfx = false;
    public int highscore = 0;

    private GamePanel gamePanel;
    private InfoPanel infoPanel;
    private ButtonPanel buttonPanel;

    public NeatFlappyWindow() throws HeadlessException {
        super("Neat Flappy Bird");
        loadImages();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        gamePanel = new GamePanel(this);

        Container container = getContentPane();
        buttonPanel = new ButtonPanel(this);
        infoPanel = new InfoPanel();

        container.setLayout(new BorderLayout());
        container.add(gamePanel, BorderLayout.CENTER);
        container.add(infoPanel, BorderLayout.EAST);
        container.add(buttonPanel, BorderLayout.SOUTH);

        setSize(FlappyGame.GAME_WIDTH + BirdDetails.WIDTH, FlappyGame.GAME_HEIGHT);
        setMinimumSize(new Dimension(FlappyGame.GAME_WIDTH + BirdDetails.WIDTH, FlappyGame.GAME_HEIGHT));

        container.setVisible(true);
        buttonPanel.revalidate();
        buttonPanel.repaint();
        this.revalidate();
        this.repaint();

        pack();
        setVisible(true);

        setLocation(400, 0);
    }

    public void playGame(FlappyGame game) {
        gamePanel.setGame(game);
        List<NeatBird> birds = game.birds.stream().map(bird -> (NeatBird) bird).toList();
        infoPanel.setBirds(birds);

        long lastUpdate = System.currentTimeMillis();

        while (game.isRunning()) {
            if (!paused) {
                game.update();
                updateHighScore(game);
            }

            if (!noGfx) {
                this.repaint();
                try {
                    // Limit FPS to 60
                    Thread.sleep(Math.max(0, (1000 / (60 * speed) - (System.currentTimeMillis() - lastUpdate))));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lastUpdate = System.currentTimeMillis();
            }
        }
    }

    public void updateHighScore(FlappyGame game) {
        for (Bird bird : game.birds) {
            highscore = Math.max(bird.score, highscore);
        }
    }

    public int getAliveBirds(FlappyGame game) {
        int count = 0;
        for (Bird bird : game.birds) {
            if (bird.alive) count++;
        }

        return count;
    }

    private void loadImages() {
        String filenames[] = {"obstacle-body", "obstacle-cap-top", "obstacle-cap-bottom"};

        try {
            for (String filename : filenames) {
                NeatFlappyWindow.images.put(filename, ImageIO.read((this.getClass().getClassLoader().getResource("images/" + filename + ".png"))));
            }

            for (String color : BIRD_COLOR_NAMES) {
                logger.debug("Loading {}", color);
                NeatFlappyWindow.images.put("bird-" + color + "-0", ImageIO.read((this.getClass().getClassLoader().getResource("images/bird-" + color + "-0.png"))));
                NeatFlappyWindow.images.put("bird-" + color + "-1", ImageIO.read((this.getClass().getClassLoader().getResource("images/bird-" + color + "-1.png"))));

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            paused = !paused;
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
            speed = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            speed = 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_3) {
            speed = 4;
        }
        if (e.getKeyCode() == KeyEvent.VK_4) {
            speed = 8;
        }
        if (e.getKeyCode() == KeyEvent.VK_5) {
            speed = 16;
        }
        if (e.getKeyCode() == KeyEvent.VK_N) {
            noGfx = !noGfx;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

