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
from src.MainManager import MainManager

# ===================================================================
# Functions
# ===================================================================

# Run shell test

def callShellFile(oponent,duels, matches):
    subprocess.run(shlex.split(f'./../AI_Test/AiTest.sh {oponent} {duels} {matches}'))

def runDuelsAndFitness(population, duels, matches, oponent,manager):

    for member in population:
        manager.fsm_m.wtriteJSONFile(member[2]) # charge data in json to test
        callShellFile(oponent,duels,matches)       # run duels
        manager.res_m.updateData()   # update values of duels
        fit = manager.op.fitnessFunctionTotal(manager.res_m.getData()) # get fitness
        manager.db.updateFitness(member[0],member[1], fit)
    
    manager.db.saveChanges()





# ===================================================================
# Genetic Function
# ===================================================================
def genetic_funciton(gen,n_parents,manager):
    print("======================== Genetic Function ========================")

    cross_rate, mut_rate, alpha = [.75,.05,0.05]

    # Select population
    pop = manager.db.getMembersGen(gen)

    # Execute duels and calculate fitness of the population
    runDuelsAndFitness(pop, 2, 25, "RANDOMV1", manager)

    # Select parents (best of the tested population)
    best = manager.db.getBestOfGen(gen,n_parents)

    # Matting
    childs = manager.op.matting(best,cross_rate, mut_rate, alpha)

    # Update population with the childs
    newGen = manager.db.getLastGen() + 1

    for indx,child in enumerate(childs):
        str_child = manager.res_m.toString(child.tolist())
        manager.db.setNewMember(newGen, indx+1, str_child,None)
# ===================================================================
# Main
# ===================================================================
def main():
    # callShellFile()

    # Set up variables
    # Get JSON file path
    script_dir = os.path.dirname(__file__)
    
    path = './../MagarenaCode_1_96/resources/magic/ai/FSMPlaysResults.json'
    file_path_results = os.path.join(script_dir,  path)

    path_FSM = './../MagarenaCode_1_96/resources/magic/ai/FSMData.json'
    file_path_FSM = os.path.join(script_dir,  path_FSM)


    #Calls
    try:
        # callShellFile("RANDOMV1", 3, 3) # Run Duels
        print("\n")
    finally:
        print("======================== Python Test ========================")
        manager = MainManager(file_path_results,file_path_FSM)


        # =================================================================
        # Genetic Algorithm calls
        # =================================================================
        lastGen = manager.db.getLastGen()
        genetic_funciton(lastGen,3, manager)
        # =================================================================
        # Test calls
        # =================================================================
            
        manager.db.saveChanges()
        manager.db.close()

        


# ===================================================================
# Call
# ===================================================================
main()
