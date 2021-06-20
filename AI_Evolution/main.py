# ===================================================================
#   File name: main.py
#   Author: Alejandro Mira Abad
#   Date created: 16/06/2021
#   Python Version: 3.8.7
# ===================================================================


# ===================================================================
# Imports
# ===================================================================
# import numpy as np
import subprocess
import shlex
import os

# Import 
from src.DataManager import DataManager
from src.Operators import Operators

# ===================================================================
# Functions
# ===================================================================

# Run shell test

def callShellFile(oponent,duels, matches):
    subprocess.run(shlex.split(f'./../AI_Test/AiTest.sh {oponent} {duels} {matches}'))


# ===================================================================
# Main
# ===================================================================
def main():
    # callShellFile()

    # Set up variables
    # Get JSON file path
    script_dir = os.path.dirname(__file__)
    
    path = './../MagarenaCode_1_96/resources/magic/ai/FSMPlaysResults.json'
    file_path = os.path.join(script_dir,  path)

    path_FSM = './../MagarenaCode_1_96/resources/magic/ai/FSMData.json'
    file_path_FSM = os.path.join(script_dir,  path_FSM)


    #Calls
    try:
        # callShellFile("RANDOMV1", 3, 3) # Run Duels
        print("\n")
    finally:
        print("\n======================== Python Test ========================")
        dat_manager = DataManager(file_path)
        fsm = DataManager(file_path_FSM)
        op = Operators()
        
        # =================================================================
        # Test calls
        # =================================================================
        op.fitnessFunctionTotal(dat_manager.getData()) # fitness function call

        j1 = [{"PhaseLowerLand":[{"Lifes":1,"Opts":{"N":1,"B":1}}]},{"PhaseLowerCreatures":[{"Lifes":1,"Opts":{"N":1,"B":1}}]},{"PhaseAtack":[{"Lifes":1,"Opts":{"N":1,"B":1}}]},{"PhaseDefend":[{"Lifes":1,"Opts":{"N":1,"B":1}}]}]
        j2 = [{"PhaseLowerLand":[{"Lifes":2,"Opts":{"N":2,"B":2}}]},{"PhaseLowerCreatures":[{"Lifes":2,"Opts":{"N":2,"B":2}}]},{"PhaseAtack":[{"Lifes":2,"Opts":{"N":2,"B":2}}]},{"PhaseDefend":[{"Lifes":2,"Opts":{"N":2,"B":2}}]}]
        
        c1,c2 = op.crossoverOperation(fsm.parseToNpArray(j1),dat_manager.parseToNpArray(j2), 0.9) # crossover funtion call

        cmn = op.mutationOperation(fsm.parseToNpArray(fsm.getData()), 0.05, 0.1) # mutation function call
        
        print("""
        -- C1 --
        
{}

        -- C2 --
        
{}

        -- CN --

{}
        """.format(c1,c2,cmn))

# ===================================================================
# Call
# ===================================================================
main()
