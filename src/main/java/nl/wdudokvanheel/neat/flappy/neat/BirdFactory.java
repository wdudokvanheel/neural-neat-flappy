package nl.wdudokvanheel.neat.flappy.neat;

import nl.wdudokvanheel.neural.neat.CreatureFactory;
import nl.wdudokvanheel.neural.neat.genome.Genome;

public class BirdFactory implements CreatureFactory<NeatBird> {
    @Override
    public NeatBird createNewCreature(Genome genome) {
        return new NeatBird(genome);
    }
}
