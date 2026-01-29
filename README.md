# NEUROEVOLUTIONARY AI FOR MAGARENA

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Python 3.8](https://img.shields.io/badge/python-3.8-blue.svg)](https://www.python.org/downloads/release/python-380/)
[![Java 8](https://img.shields.io/badge/java-8-orange.svg)](https://www.java.com/)

An intelligent AI system that learns to play Magic: The Gathering using neuroevolutionary methods, developed as a final thesis project for the [Degree in Interactive Technologies](https://www.upv.es/titulaciones/GTI/index-en.html) at the [Polytechnic University of Valencia](https://www.upv.es/).

---

## 📋 Table of Contents

- [About](#about)
- [Features](#features)
- [Architecture](#architecture)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Training Results](#training-results)
- [How It Works](#how-it-works)
- [Troubleshooting](#troubleshooting)
- [License](#license)
- [Author](#author)
- [Acknowledgments](#acknowledgments)
- [Contact & Contributing](#contact--contributing)

---

## 📖 About

This project implements a neuroevolutionary AI capable of learning and adapting to play [Magarena](https://github.com/magarena/magarena), an open-source Magic: The Gathering (MTG) game engine, intelligently and competitively. The AI uses a **Finite State Machine (FSM)** with parameters evolved through **genetic algorithms** to make strategic gameplay decisions.

### 🎯 Project Goals

- Apply neuroevolutionary methods to a complex, variant game environment
- Create a self-developed framework for genetic algorithm training
- Evaluate the complexity and effectiveness of the learning task
- Develop an AI that can compete intelligently against established MTG AI opponents

---

## ✨ Features

- **🧬 Genetic Algorithm Implementation**: Evolves AI strategies through selection, crossover, and mutation
- **🎮 FSM-Based Decision Making**: Uses Finite State Machines with evolved parameters for gameplay decisions
- **⚡ Multi-threaded Execution**: Runs 8 parallel matches for efficient training
- **📊 Comprehensive Tracking**: SQLite database tracks all generations, populations, and fitness scores
- **📈 Visualization Tools**: Auto-generates fitness plots and performance graphs using matplotlib and plotly
- **🔄 Adaptive Learning**: AI adapts based on life difference, game phase, and action probabilities
- **🎲 Multiple Opponents**: Trains against various AI opponents including MCTS (Monte Carlo Tree Search)

---

## 🏗️ Architecture

The project consists of four main components that work together:

```
┌─────────────────────────────────────────────────────────────────┐
│                     Training Pipeline                           │
└─────────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        ▼                     ▼                     ▼
┌──────────────────┐  ┌──────────────┐   ┌─────────────────┐
│  AI_Evolution    │  │   AI_Test    │   │ MagarenaCode    │
│  (Python)        │  │   (Bash)     │   │ (Java)          │
│                  │  │              │   │                 │
│ • Genetic Algo   │──┤ Shell Script ├──►│ • FSM Player    │
│ • Population Mgmt│  │ Orchestrator │   │ • Game Engine   │
│ • Fitness Eval   │◄─┤              │◄──┤ • Match Results │
│ • Data Analysis  │  └──────────────┘   └─────────────────┘
└──────────────────┘                              │
        │                                         │
        ▼                                         ▼
┌──────────────────┐                   ┌─────────────────┐
│  Training DB     │                   │  FSM JSON Data  │
│  (SQLite)        │                   │  (Parameters)   │
└──────────────────┘                   └─────────────────┘
```

### Workflow

1. **Initialize**: Generate random population of 200 FSM parameter sets
2. **Simulate**: Each AI plays matches against opponents through Magarena engine
3. **Evaluate**: Calculate fitness based on performance (life difference / turns played)
4. **Evolve**: Apply genetic operators (selection, crossover 75%, mutation 10%)
5. **Iterate**: Repeat for 100+ generations
6. **Visualize**: Generate plots and export data for analysis

---

## 💻 Requirements

### System Requirements
- **Java Runtime Environment 8** or higher
- **Python 3.8** or higher
- **Pip** (Python package installer)
- **Pipenv** (Python virtual environment manager)

### Operating System
- Linux (tested)
- macOS (should work)
- Windows (may require modifications to shell scripts)

---

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/almiab1/AI_Neuroevolution_MTG.git
cd AI_Neuroevolution_MTG
```

### 2. Install Python Dependencies

Navigate to the AI_Evolution folder and set up the Python environment:

```bash
cd AI_Evolution
pipenv install
```

This will install all required Python packages:
- `numpy` - Numerical computations
- `pandas` - Data manipulation
- `matplotlib` - Plotting
- `seaborn` - Statistical visualization
- `plotly` - Interactive plots
- `kaleido` - Static image export

### 3. Verify Java Installation

Ensure Java 8 is installed:

```bash
java -version
```

You should see output indicating Java version 1.8 or higher.

---

## 🎮 Usage

### Running Training

To start a training session:

```bash
cd AI_Evolution
pipenv shell
python3 main.py
```

### Training Parameters

You can modify training parameters in `main.py`:

- **Number of generations**: Line 148, default is `n = 100`
- **Population size**: Default is `200`, hardcoded in `genInitPop(200, manager)` on line 150 and in selection logic on line 105
- **Genetic operators**: Line 82 in the `genetic_function`
  - `cross_rate = 0.75` - Crossover rate (75%)
  - `mut_rate = 0.1` - Mutation rate (10%)
  - `alpha = 0.1` - Alpha parameter for genetic operations

### Training Process

The training will:
1. Generate an initial random population of 200 FSM parameter sets
2. Run each AI against MCTS opponent for 10 matches
3. Calculate fitness scores
4. Evolve the population through genetic operations
5. Generate fitness plots after each generation
6. Save all data to SQLite database and CSV files
7. Repeat for the specified number of generations

### Monitoring Progress

- **Console Output**: Shows generation progress and fitness scores
- **Database**: `population.db` in the working directory tracks all data
- **Plots**: Generated in `plots/` directory after each generation
- **CSV Files**: Exported data in `dataset_*.csv` files

---

## 📁 Project Structure

```
AI_Neuroevolution_MTG/
│
├── AI_Evolution/                  # Main Python framework
│   ├── src/
│   │   ├── MainManager.py         # Orchestrates all managers
│   │   ├── Operators.py           # Genetic algorithm operators
│   │   ├── DBManager.py           # Database management
│   │   ├── DataManager.py         # JSON/FSM data handling
│   │   └── DataRepresent.py       # Visualization & plotting
│   ├── main.py                    # Entry point for training
│   ├── Pipfile                    # Python dependencies
│   └── Pipfile.lock               # Locked dependency versions
│
├── AI_Test/
│   └── MagarenaMatches.sh         # Shell script to run matches
│
├── MagarenaCode_1_96/             # Modified Magarena game engine (Magarena v1.96)
│                                   # Custom FSM AI player implementation integrated into the engine
│   ├── src/magic/ai/              # FSM AI implementation (Java)
│   │   ├── FSM.java               # Main FSM player
│   │   ├── FSMData.java           # FSM parameter loader
│   │   ├── FSMSelector.java       # Decision selection logic
│   │   └── FSMWriter.java         # Match result writer
│   ├── resources/magic/ai/        # FSM data files (JSON)
│   └── ...                        # Game engine code
│
├── Trainings/                     # Training results storage
│   ├── trainingResults_1/         # First training run
│   ├── trainingResults_2/         # Second training run
│   └── trainingResults_3/         # Third training run
│       ├── population.db          # SQLite database
│       ├── dataset_*.csv          # Exported CSV data
│       └── plots/                 # Generated visualizations
│
├── README.md                      # This file
└── LICENSE                        # GPL v3.0 license
```

---

## 📊 Training Results

Training results are stored in the `Trainings/` directory. Each training session creates:

- **population.db**: SQLite database containing:
  - Population table (current generation)
  - AllPopulation table (complete history)
  - BestFitnessPop table (best per generation)
  - BestFitnessHistory table (best overall)

- **CSV Exports**:
  - `dataset_pop.csv` - Current population data
  - `dataset_all_pop.csv` - All generations data
  - `dataset_best_pop.csv` - Best per generation
  - `dataset_best_history.csv` - Historical best

- **Visualization**: Plots directory contains fitness graphs for each generation

---

## 🔬 How It Works

### Finite State Machine (FSM)

The AI uses an FSM with multiple states corresponding to different game situations. Each state contains action probabilities that determine the AI's decisions:

- **Life-based states**: Different strategies based on life difference vs opponent
- **Phase-based decisions**: Adapts behavior for different game phases
- **Action probabilities**: Normalized probabilities for each possible action

### Genetic Algorithm

1. **Initialization**: Generate random FSM parameter sets
2. **Fitness Evaluation**: `fitness = life_difference / turns_played`
3. **Selection**: Roulette wheel selection based on fitness
4. **Crossover**: 75% chance to combine two parent FSMs
5. **Mutation**: 10% chance to randomly modify parameters
6. **Replacement**: Select best 200 from parents + children

### Training Strategy

The AI trains by playing matches against established opponents (MCTS) and evolving strategies that maximize the life difference while minimizing turns played, encouraging both winning and efficiency.

---

## 🐛 Troubleshooting

### Common Issues

**Issue**: `pipenv: command not found`
```bash
pip install --user pipenv
```

**Issue**: Java not found
```bash
# Ubuntu/Debian
sudo apt-get install openjdk-8-jre

# macOS
brew install openjdk@8
```

**Issue**: Permission denied on shell scripts
```bash
chmod +x AI_Test/MagarenaMatches.sh
```

---

## 📜 License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Alejandro Mira Abad**

- Final Thesis Project
- Degree in Interactive Technologies
- Polytechnic University of Valencia (UPV)

---

## 🙏 Acknowledgments

- [Magarena](https://github.com/magarena/magarena) - Open-source MTG game engine
- Polytechnic University of Valencia - Academic support
- The MTG and AI research communities

---

## 📧 Contact & Contributing

For questions, suggestions, or contributions, please open an issue on the [GitHub repository](https://github.com/almiab1/AI_Neuroevolution_MTG.git).

---

**Note**: This is an academic research project. The AI is designed for educational purposes and exploring neuroevolutionary methods in complex game environments.
