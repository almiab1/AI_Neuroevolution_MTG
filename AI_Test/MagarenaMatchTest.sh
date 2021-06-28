#!/bin/bash

# Magarena Test

# ---------------------------------------------------------------------------
# Set up
# ---------------------------------------------------------------------------
magarena_dir='./../MagarenaCode_1_96/release/' # Relative Path
# magarena_dir='/mnt/d/0-Varios/Universidad/TFG/Framework/AI_TFG/MagarenaCode_1_96/release/' # Path wls
# magarena_dir='/media/pica/DATA/0-Varios/Universidad/TFG/Framework/AI_TFG/MagarenaCode_1_96/release' # Path win10
# magarena_dir='/home/tfg/TFG/AI_TFG/MagarenaCode_1_96/release' # Path cluster
cd $magarena_dir
clear
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Test Configurations
# ---------------------------------------------------------------------------
# Get AI names
oponent=$1

games=$3
duels=$2
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
# FSM Secon  -->     FSMS |
# Decks
# To FSM --> Red-FSM
# ---------------------------------------------------------------------------
testAI(){
    java -splash: -Xms512M -Xmx1024M -jar Magarena.jar --headless  \
        --ai1 FSM --str1 5 --deck1 Red-FSM \
        --ai2 $oponent --str2 5 --deck2 Red-FSM \
        --life $lifes --games $games --duels $duels --threads $threads
}
# ---------------------------------------------------------------------------

# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------
testAI