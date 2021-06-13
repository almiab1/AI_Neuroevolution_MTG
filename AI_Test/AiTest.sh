#!/bin/bash

# Magarena Test

# ---------------------------------------------------------------------------
# Set up
# ---------------------------------------------------------------------------
magarena_dir='/mnt/d/0-Varios/Universidad/TFG/Framework/AI_TFG/MagarenaCode_1_96/release/'
# magarena_dir='/media/pica/DATA/0-Varios/Universidad/TFG/Framework/AI_TFG/MagarenaCode_1_96/release'
cd $magarena_dir
clear
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Test Configurations
# ---------------------------------------------------------------------------
games=3
duels=10
threads=4
lifes=20
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
# Decks
# To FSM --> Red-FSM
# ---------------------------------------------------------------------------
testAI(){
    java -splash: -Xms512M -Xmx1024M -jar Magarena.jar --headless  \
        --ai1 FSM --str1 5 --deck1 Red-FSM \
        --ai2 RANDOMV1 --str2 5 --deck2 Red-FSM \
        --life $lifes --games $games --duels $duels --threads $threads
}
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------
testAI