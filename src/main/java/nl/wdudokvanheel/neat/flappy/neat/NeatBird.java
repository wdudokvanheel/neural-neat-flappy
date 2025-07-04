package nl.wdudokvanheel.neat.flappy.neat;

import nl.wdudokvanheel.neat.flappy.gamelogic.Bird;
import nl.wdudokvanheel.neat.flappy.gamelogic.FlappyGame;
import nl.wdudokvanheel.neat.flappy.gamelogic.Obstacle;
import nl.wdudokvanheel.neural.core.Network;
import nl.wdudokvanheel.neural.neat.model.Creature;
import nl.wdudokvanheel.neural.neat.model.Genome;
import nl.wdudokvanheel.neural.neat.model.Species;

public class NeatBird extends Bird implements Creature{
	private static int COUNTER = 0;
	public int id;
	private Genome genome;
	private Species species;
	private Network network;
	public double[] inputValues = new double[2];
	public double outputValue = 0;

	public NeatBird(Genome genome){
		id = ++COUNTER;
		this.genome = genome;
		this.network = new Network(genome);
	}

	public void update(Obstacle nextObstacle){
		//Get input values for neural network (horizontal & vertical distance to next obstacle/gap)

		//Distance to next obstacle
		double horizontalDistance = (nextObstacle.x + nextObstacle.width) - FlappyGame.xPosition + FlappyGame.BIRD_SIZE / 2;
		horizontalDistance = horizontalDistance / FlappyGame.MAX_OBSTACLE_DISTANCE;
		inputValues[0] = horizontalDistance;

		//Vertical distance to center of gap
		double verticalDistance = nextObstacle.top + ((nextObstacle.bottom - nextObstacle.top) / 2.0);
		verticalDistance = verticalDistance - position;
		verticalDistance = verticalDistance / FlappyGame.GAME_HEIGHT;
		inputValues[1] = verticalDistance;

		//Run network and get output
		network.resetNeuronValues();
		network.setInput(1, horizontalDistance, verticalDistance);
		outputValue = network.getOutput();

		//See if it wants to jump
		if(outputValue > 0.5){
            jump((outputValue - 0.5) * 2, 18);
		}

		//Update physics of bird
		super.update(nextObstacle);
	}

	@Override
	public Genome getGenome(){
		return genome;
	}

	@Override
	public double getFitness(){
		return distance - (network.hiddenNeurons.size() * 100) - (genome.getActiveConnections().size() * 33);
	}

	@Override
	public void setFitness(double fitness){
	}

	@Override
	public Species getSpecies(){
		return species;
	}

	@Override
	public void setSpecies(Species species){
		this.species = species;
	}
}
