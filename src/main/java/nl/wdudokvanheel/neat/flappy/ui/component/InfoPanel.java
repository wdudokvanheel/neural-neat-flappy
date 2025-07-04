package nl.wdudokvanheel.neat.flappy.ui.component;

import nl.wdudokvanheel.neat.flappy.neat.NeatBird;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InfoPanel extends JPanel {
    public BirdDetails[] children = new BirdDetails[10];

    public InfoPanel() {
        setPreferredSize(new Dimension(BirdDetails.WIDTH, BirdDetails.HEIGHT * children.length));

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        for (int i = 0; i < children.length; i++) {
            BirdDetails details = new BirdDetails(i);
            children[i] = details;
            this.add(details);
            details.revalidate();
            details.repaint();
        }

        revalidate();
        repaint();
    }

    public void setBirds(List<NeatBird> birds) {
        for (int i = 0; i < Math.min(10, birds.size()); i++) {
            children[i].setBird(birds.get(i));
        }
    }
}
