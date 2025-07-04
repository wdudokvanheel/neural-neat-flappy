package nl.wdudokvanheel.neat.flappy.ui.component;

import nl.wdudokvanheel.neat.flappy.ui.FlappyApplication;
import nl.wdudokvanheel.neural.core.Network;
import nl.wdudokvanheel.neural.core.neuron.Connection;
import nl.wdudokvanheel.neural.core.neuron.InputNeuron;
import nl.wdudokvanheel.neural.core.neuron.Neuron;
import nl.wdudokvanheel.neural.core.neuron.OutputNeuron;
import nl.wdudokvanheel.neat.flappy.neat.NeatBird;
import nl.wdudokvanheel.neat.flappy.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BirdDetails extends JPanel {
    private Logger logger = LoggerFactory.getLogger(BirdDetails.class);

    public static int WIDTH = 500;
    public static int HEIGHT = 100;
    public int index;

    private Color activeBackground = Color.decode("#eaeaea");
    private Color inActiveBackground = Color.decode("#c1c1c1");
    private Color colorPositiveConnection = Color.decode("#11cf20");
    private Color colorNegativeConnection = Color.decode("#cf0f4c");
    private Color separator = Color.decode("#1d1d1d");

    private NeatBird bird;

    private int networkPos = 325;

    private int jumpCooldownPos = 125;
    private int jumpCoolDownHeight = 75;
    private int jumpCoolDownWidth = 25;

    private int inputPos = 200;
    private int inputWidth = 75;

    private Network network;
    private int neuronSize = 20;
    private int neuronSpacing = 40;
    private int layers = 0;
    private int maxNeuronsPerLayer = 0;
    private Color myColor;

    public BirdDetails(int index) {
        this.index = index;
        this.myColor = Color.decode(FlappyApplication.BIRD_COLORS[index]);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getBackgroundColor());
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        if (index != 0) {
            g2.setColor(separator);
            g.drawLine(0, 0, WIDTH, 0);
        }

        if (bird == null)
            return;
        if (!bird.alive) {
            g2.setComposite(AlphaComposite.SrcOver.derive(0.3f));
        }

        BufferedImage image;
        if (bird.jumpWait > 8)
            image = FlappyApplication.images.get("bird-" + FlappyApplication.BIRD_COLOR_NAMES[index] + "-0");
        else
            image = FlappyApplication.images.get("bird-" + FlappyApplication.BIRD_COLOR_NAMES[index] + "-1");
        g2.drawImage(image, 8, Math.round((HEIGHT - image.getHeight() * 3) / 2), image.getWidth() * 3, image.getHeight() * 3, null);
        g2.setColor(separator);
        g2.drawString("Species " + bird.getSpecies().id, 8, image.getHeight() * 4 + 5);
        g2.drawRect(jumpCooldownPos, (HEIGHT - jumpCoolDownHeight) / 2, jumpCoolDownWidth, jumpCoolDownHeight);
        g2.drawRect(jumpCooldownPos + 1, (HEIGHT - jumpCoolDownHeight) / 2 + 1, jumpCoolDownWidth - 2, jumpCoolDownHeight - 2);
        g2.drawRect(jumpCooldownPos + 2, (HEIGHT - jumpCoolDownHeight) / 2 + 2, jumpCoolDownWidth - 4, jumpCoolDownHeight - 4);

        g.setColor(myColor);
        int height = (int) ((jumpCoolDownHeight - 6) * (1 - bird.jumpWait / 15.0));
        g2.fillRect(jumpCooldownPos + 3, ((HEIGHT - jumpCoolDownHeight) / 2 + 3) + (jumpCoolDownHeight - 6 - height), jumpCoolDownWidth - 6, height);
        drawConnections(g2, network);
        drawNeurons(g2, network);

        int inputHeight = 10;

        g.setColor(separator);
        int val = Math.max(0, Math.min(inputWidth, (int) Math.round(bird.inputValues[0] * inputWidth)));

        g.drawLine(inputPos, inputHeight + 20, inputPos + inputWidth, inputHeight + 20);
        g.drawLine(inputPos, inputHeight + 12, inputPos, inputHeight + 28);
        g.drawLine(inputPos + inputWidth, inputHeight + 12, inputPos + inputWidth, inputHeight + 28);

        g.drawLine(inputPos + val, inputHeight + 15, inputPos + val, inputHeight + 25);

        val = Math.max(0, Math.min(inputWidth, (int) Math.round((bird.inputValues[1] + 1) / 2 * inputWidth)));
        inputHeight += 40;
        g.drawLine(inputPos, inputHeight + 20, inputPos + inputWidth, inputHeight + 20);
        g.drawLine(inputPos, inputHeight + 12, inputPos, inputHeight + 28);
        g.drawLine(inputPos + inputWidth, inputHeight + 12, inputPos + inputWidth, inputHeight + 28);

        g.drawLine(inputPos + val, inputHeight + 15, inputPos + val, inputHeight + 25);

    }

    private void drawNeurons(Graphics2D g, Network network) {
        for (Neuron neuron : network.getAllNeurons()) {
            Vector2i position = getNeuronPosition(neuron);

            g.setStroke(new BasicStroke(2));
            g.setColor(separator);
            g.drawOval(position.x, position.y, neuronSize, neuronSize);
            g.setColor(getNeuronColor(neuron));
            g.fillOval(position.x, position.y, neuronSize, neuronSize);
//            drawCenter(g, "#" + neuron.getId(), position.x + neuronSize / 2, position.y + neuronSize / 2 - 15);
        }
    }

    private Vector2i getNeuronPosition(Neuron neuron) {
        int margin = neuronSpacing;
        int x = networkPos;
        int y = (HEIGHT - maxNeuronsPerLayer * neuronSize) / 2 - neuronSize / 2;

        int index = getIndex(network, neuron);
        if (neuron instanceof InputNeuron) {
            int neuronsInLayer = network.inputNeurons.size();

            if (neuronsInLayer < maxNeuronsPerLayer) {
                int diff = maxNeuronsPerLayer - neuronsInLayer;
                y += diff * margin / 2;
            }

            y += index * margin;
        } else if (neuron instanceof OutputNeuron) {
            int neuronsInLayer = network.outputNeurons.size();

            if (neuronsInLayer < maxNeuronsPerLayer) {
                int diff = maxNeuronsPerLayer - neuronsInLayer;
                y += diff * margin / 2;
            }
            x += margin + ((layers) * margin);
            y += index * margin;
        } else {
            int neuronsInLayer = getNeuronsPerLayer(neuron.layer);

            if (neuronsInLayer < maxNeuronsPerLayer) {
                int diff = maxNeuronsPerLayer - neuronsInLayer;
                y += diff * margin / 2;
            }

            x += margin + ((neuron.layer - 1) * margin);
            y += index * margin;
        }

        return new Vector2i(x, y);
    }

    private void drawConnections(Graphics2D g, Network network) {
        for (Neuron neuron : network.getAllNeurons()) {
            if (neuron instanceof InputNeuron)
                continue;

            for (Connection input : neuron.inputs) {
                Vector2i source = getNeuronPosition(input.source).add(neuronSize / 2);
                Vector2i target = getNeuronPosition(input.target).add(neuronSize / 2);

                Color color;
                if (input.weight > 0) {
                    color = colorPositiveConnection;
                } else {
                    color = colorNegativeConnection;
                }

                double distance = Math.sqrt(Math.pow(target.x - source.x, 2) + Math.pow(target.y - source.y, 2));

                double unitX = (target.x - source.x) / distance;
                double unitY = (target.y - source.y) / distance;

                double x1 = source.x + neuronSize / 2 * unitX;
                double y1 = source.y + neuronSize / 2 * unitY;
                double x2 = target.x - neuronSize / 2 * unitX;
                double y2 = target.y - neuronSize / 2 * unitY;

                g.setStroke(new BasicStroke((float) ((Math.max(0.3, Math.abs(input.weight))))));
                g.setColor(color);
                g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            }
        }
    }

    private int getIndex(Network network, Neuron neuron) {
        if (neuron instanceof InputNeuron) {
            return network.inputNeurons.indexOf(neuron);
        }
        if (neuron instanceof OutputNeuron) {
            return network.outputNeurons.indexOf(neuron);
        }

        int index = 0;
        for (Neuron hiddenNeuron : network.hiddenNeurons) {
            if (hiddenNeuron == neuron)
                return index;
            if (hiddenNeuron.layer == neuron.layer)
                index++;
        }

        return index;
    }

    private Color getNeuronColor(Neuron neuron) {
        int alpha = 255;
        if (neuron instanceof InputNeuron) {
//            double val = bird.inputValues[network.inputNeurons.indexOf(neuron)];
//            alpha = (int) Math.round(Math.abs(val * 255));
//            if (val > 0)
//                return new Color(17, 207, 32, Math.min(255, alpha));
//            else {
//                return new Color(207, 15, 76, alpha);
//            }
            return Color.decode("#134ccf");
        }
        if (neuron instanceof OutputNeuron) {
            double val = bird.outputValue;
            if (val < 0.5) {
                return new Color(207, 15, 76);
            } else {
                return new Color(17, 207, 32, Math.min(255, alpha));
            }
        }
        return Color.decode("#134ccf");
    }

    public void setBird(NeatBird bird) {
        this.bird = bird;
        this.network = new Network(bird.getGenome());
        this.layers = network.getLayers();
        this.maxNeuronsPerLayer = Math.max(network.inputNeurons.size(), network.outputNeurons.size());
        for (int i = 0; i < layers; i++) {
            int neurons = getNeuronsPerLayer(i);
            if (neurons > maxNeuronsPerLayer)
                maxNeuronsPerLayer = neurons;
        }
    }

    private int getNeuronsPerLayer(int layer) {
        int total = 0;
        for (Neuron hiddenNeuron : network.hiddenNeurons) {
            if (hiddenNeuron.layer == layer) {
                total++;
            }
        }
        return total;
    }

    private Color getBackgroundColor() {
        if (bird == null || !bird.alive) {
            return inActiveBackground;
        }

        return activeBackground;
    }
}

