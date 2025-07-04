package nl.wdudokvanheel.neat.flappy;

import nl.wdudokvanheel.neat.flappy.gamelogic.Bird;
import nl.wdudokvanheel.neat.flappy.gamelogic.FlappyGame;
import nl.wdudokvanheel.neat.flappy.neat.BirdFactory;
import nl.wdudokvanheel.neat.flappy.neat.NeatBird;
import nl.wdudokvanheel.neat.flappy.ui.NeatFlappyWindow;
import nl.wdudokvanheel.neural.neat.NeatEvolution;
import nl.wdudokvanheel.neural.neat.model.*;
import nl.wdudokvanheel.neural.neat.service.InnovationService;
import nl.wdudokvanheel.neural.util.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NeatFlappy {
    private static final int MAX_GENERATIONS = 30000;

    private final Logger logger = LoggerFactory.getLogger(NeatFlappy.class);

    private NeatContext context;
    private NeatFlappyWindow ui;

    public static void main(String[] args) {
        new NeatFlappy().start();
    }

    private void start() {
        // Setup UI
        ui = new NeatFlappyWindow();

        // Create NEAT context
        context = NeatEvolution.createContext(new BirdFactory());

        // Neat configuration
        context.configuration.populationSize = 10;
        context.configuration.targetSpecies = 4;
        context.configuration.speciesThreshold = 1;
        context.configuration.minimumSpeciesSizeForChampionCopy = 1;
        context.configuration.setInitialLinks = true;
        context.configuration.copyChampionsAllSpecies = false;
        context.configuration.mutateAddConnectionProbability = 0.05;
        context.configuration.mutateToggleConnectionProbability = 0.3;
        context.configuration.mutateAddNeuronProbability = 0.01;
        context.configuration.multipleMutationsPerGenome = true;

        // Create blueprint creature with a simple genome
        Genome blueprintGenome = createInitialGenome(context.innovationService);
        Creature blueprintCreature = context.creatureFactory.createNewCreature(blueprintGenome);

        // Setup initial population with the blueprint creature
        NeatEvolution.generateInitialPopulation(context, blueprintCreature);

        // Main loop
        for (int i = 0; i < MAX_GENERATIONS; i++) {
            // Get all the birds from the NEAT context
            List<NeatBird> birds = getBirds();

            // Run the game with the birds
            scoreBirds(birds);

            // Get the fittest creature from the context
            Creature fittestCreature = context.getFittestCreature();

            if (fittestCreature != null) {
                logger.debug("Champion score: {}", fittestCreature.getFitness());
                ui.setTitle("Neat Flappy Bird :: Generation " + context.generation + " :: " + context.creatures.size() + " creatures :: " + context.species.size() + "/" + context.configuration.targetSpecies + " Species (" + Print.format(context.configuration.speciesThreshold) + ") :: Fitness " + Print.format(fittestCreature.getFitness()));
            }

            // Create the next generation based on their scores
            NeatEvolution.nextGeneration(context);
            logger.debug("Generation {} finished - {} creatures - {} species", context.generation, context.creatures.size(), context.species.size());
        }

        Print.pop(context.creatures);
    }

    private Genome createInitialGenome(InnovationService innovation) {
        Genome genome = new Genome();

        NeuronGene inputBias = new NeuronGene(NeuronGeneType.INPUT, innovation.getInputNodeInnovationId(0));
        NeuronGene input1 = new NeuronGene(NeuronGeneType.INPUT, innovation.getInputNodeInnovationId(1));
        NeuronGene input2 = new NeuronGene(NeuronGeneType.INPUT, innovation.getInputNodeInnovationId(2));
        NeuronGene output = new NeuronGene(NeuronGeneType.OUTPUT, innovation.getOutputNodeInnovationId(0));

        ConnectionGene connectionInput0 = new ConnectionGene(innovation.getConnectionInnovationId(input1, output), input1.getInnovationId(), output.getInnovationId());
        ConnectionGene connectionInput1 = new ConnectionGene(innovation.getConnectionInnovationId(input2, output), input2.getInnovationId(), output.getInnovationId());
        genome.addConnections(connectionInput0, connectionInput1);

        genome.addNeurons(inputBias, input1, input2, output);
        return genome;
    }

    private List<NeatBird> getBirds() {
        List<NeatBird> birds = new ArrayList<>();
        for (Creature creature : context.creatures) {
            if (creature instanceof NeatBird) {
                birds.add((NeatBird) creature);
            }
        }
        return birds;
    }

    private void scoreBirds(List<NeatBird> birds) {
        FlappyGame game = new FlappyGame();
        List<Bird> plain = birds.stream().map(bird -> (Bird) bird).collect(Collectors.toList());
        game.addBirds(plain);
        ui.playGame(game);
    }
}
