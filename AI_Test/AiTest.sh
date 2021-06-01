#!/bin/bash

# Magarena Test

# ---------------------------------------------------------------------------
# Set up
# ---------------------------------------------------------------------------
# magarena_dir='/mnt/d/0-Varios/Universidad/TFG/Framework/AI_TFG/MagarenaCode_1_96/release/'
magarena_dir='/media/pica/DATA/0-Varios/Universidad/TFG/Framework/AI_TFG/MagarenaCode_1_96/release'
cd $magarena_dir
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Test Configurations
# ---------------------------------------------------------------------------
games=1
duels=1
threads=4
lifes=1
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Test
# ---------------------------------------------------------------------------
# Types of AIs:
# Minimax    -->     MMAB | MMABC
# Montecarlo -->     MCTS | MCTS
# Vegas      -->    VEGAS | VEGASC
# MTDF       -->     MTDF | MTDFC
# Random AI  --> RANDOMV1 | RANDOMV1C
# FSM        -->      FSM | FSMC
# ---------------------------------------------------------------------------
testAI(){
    java -splash: -Xms512M -Xmx1024M -jar Magarena.jar --headless  \
        --ai1 MCTS --str1 8 --deck1 @ \
        --ai2 RANDOMV1 --str2 8 --deck2 @ \
        --life $lifes --games $games --duels $duels --threads $threads
}
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------
testAI

