# NEAT Flappy Bird Simulation

This project is an example integration of the NEAT (NeuroEvolution of Augmenting Topologies) library in a Flappy
Bird–style game. It demonstrates how to set up a population of AI-controlled birds whose neural networks evolve over
generations to master the game mechanics. 

**Please note** that with a tiny population of only 10 birds, it can take a while
for them to find the solution, in a more realistic environment you'd crank the population up way higher.

## Features

* **Real-time visualization** of multiple birds playing Flappy Bird concurrently
* **Interactive controls** to pause/resume, adjust simulation speed, and toggle rendering
* **Detailed per-bird diagnostics**: network structure, input/output values, jump cooldown, and species information
* **Modular architecture** separating game logic, UI components, and NEAT integration

## Getting Started

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/neat-flappy.git
   cd neat-flappy
   ```

2. **Build the project**:

   ```bash
   mvn clean package
   ```

   This creates an executable JAR in the `target/` directory.

3. **Run the simulation**:

   ```bash
   java -jar target/neat-flappy-1.0.jar
   ```

   The main window will appear, showing the game panel and per-bird details in real time. The simulation starts out in a
   paused state, click the resume button in the lower left to start the simulation.

### Controls

* **P**: Pause/Resume simulation
* **1/2/3/4/5**: Set simulation speed to 1×, 2×, 4×, 8×, or 16×, respectively
* **N**: Toggle graphics rendering (for faster evolution)
* **Speed / Pause / No Screen buttons**: Clickable UI equivalents

## Configuration

All NEAT parameters are initialized in `NeatFlappy.java` (method `start`). After modifying, rebuild to apply new
settings.

## Project Structure

```
src/main/java
├── nl.wdudokvanheel.neat.flappy
│   ├── NeatFlappy.java            # Entry point, NEAT setup and main loop
│   ├── gamelogic                  # Independent core game mechanics (Bird, FlappyGame, Obstacle)
│   └── neat                       # NEAT integration (BirdFactory, NeatBird)
└── nl.wdudokvanheel.neat.flappy.ui
    ├── NeatFlappyWindow.java      # Main Swing window, image loading, key listeners
    └── component                  # Various Swing components (GamePanel, InfoPanel, BirdDetails, ButtonPanel)
```

Images used for birds and obstacles are created by me and stored under `src/main/resources/images/`