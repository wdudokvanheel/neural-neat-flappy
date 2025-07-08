package nl.wdudokvanheel.neat.flappy;

import nl.wdudokvanheel.neat.flappy.gamelogic.Bird;
import nl.wdudokvanheel.neat.flappy.gamelogic.FlappyGame;
import nl.wdudokvanheel.neat.flappy.neat.BirdFactory;
import nl.wdudokvanheel.neat.flappy.neat.NeatBird;
import nl.wdudokvanheel.neat.flappy.ui.NeatFlappyWindow;
import nl.wdudokvanheel.neural.neat.NeatContext;
import nl.wdudokvanheel.neural.neat.NeatEvolution;
import nl.wdudokvanheel.neural.neat.genome.Genome;
import nl.wdudokvanheel.neural.neat.genome.InputNeuronGene;
import nl.wdudokvanheel.neural.neat.genome.OutputNeuronGene;
import nl.wdudokvanheel.neural.neat.service.GenomeBuilder;
import nl.wdudokvanheel.neural.neat.service.InnovationService;
import nl.wdudokvanheel.neural.util.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class NeatFlappy {
    private static final int MAX_GENERATIONS = 30000;

    private final Logger logger = LoggerFactory.getLogger(NeatFlappy.class);

    private NeatContext<NeatBird> context;
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
        context.configuration.targetSpecies = 3;
        context.configuration.speciesThreshold = 1;
        context.configuration.minimumSpeciesSizeForChampionCopy = 1;
        context.configuration.setInitialLinks = true;
        context.configuration.copyChampionsAllSpecies = false;
        context.configuration.mutateAddConnectionProbability = 0.05;
        context.configuration.mutateToggleConnectionProbability = 0.3;
        context.configuration.mutateAddNeuronProbability = 0.2;
        context.configuration.multipleMutationsPerGenome = true;

        // Create blueprint creature with a simple genome
        Genome blueprintGenome = createInitialGenome(context.innovationService);
        NeatBird blueprintCreature = context.creatureFactory.createNewCreature(blueprintGenome);

        // Setup initial population with the blueprint creature
        NeatEvolution.generateInitialPopulation(context, blueprintCreature);

        // Main loop
        for (int i = 0; i < MAX_GENERATIONS; i++) {
            // Get all the birds from the NEAT context
            List<NeatBird> birds = context.creatures;

            // Run the game with the birds
            scoreBirds(birds);

            // Get the fittest creature from the context
            NeatBird fittestCreature = context.getFittestCreature();

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
        GenomeBuilder builder = new GenomeBuilder(innovation);
        InputNeuronGene[] inputs = builder.addInputNeurons(3);
        OutputNeuronGene output = builder.addOutputNeuron(0);

        builder.addConnection(inputs[1], output);
        builder.addConnection(inputs[2], output);

        return builder.getGenome();
    }

    private void scoreBirds(List<NeatBird> birds) {
        FlappyGame game = new FlappyGame();

        // Convert NeatBirds to the expected Bird class for the game
        List<Bird> plain = birds
                .stream()
                .map(bird -> (Bird) bird)
                .collect(Collectors.toList());

        game.addBirds(plain);
        ui.playGame(game);
    }
}
