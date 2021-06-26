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
import sys

# Import 
from src.MainManager import MainManager

# ===================================================================
# Functions
# ===================================================================

# Run shell test

def callShellFile(oponent,duels, matches):
    subprocess.run(shlex.split(f'./../AI_Test/AiTest.sh {oponent} {duels} {matches}'))

def runDuelsAndFitness(population, duels, matches, oponent,manager):

    for indx, member in enumerate(population):
        manager.fsm_m.wtriteJSONFile(member[2]) # charge data in json to test
        callShellFile(oponent,duels,matches)       # run duels
        manager.res_m.updateData()   # update values of duels
        fit = manager.op.fitnessFunctionTotal(manager.res_m.getData()) # get fitness

        population[indx][3] = fit

        # Update database
        str_m = manager.res_m.toString(member[2])
        manager.db.setNewMemberInAllPop(member[0],member[1],str_m, member[3])
    
    manager.db.saveChanges()

    return population



# ===================================================================
# Genetic Function
# ===================================================================
def genetic_funciton(gen,manager):
    print("======================== Genetic Function ========================")

    cross_rate, mut_rate, alpha = [.75,.05,.05]

    # Select population
    pop = manager.db.getPop()

    # Select Parents
    parents =  manager.op.selectPopulationToMatting(pop)
    parents = manager.op.shuffleList(parents) #  Suffle parents

    # Matting
    childs_data = manager.op.matting(parents,cross_rate, mut_rate, alpha)
    
    childs = []
    newGen = gen + 1
    for indx,child in enumerate(childs_data):
        childs.append([newGen, indx+1, list(child),None])

    # Execute duels and calculate fitness of the population
    childs = runDuelsAndFitness(childs, 1, 100, "RANDOMV1", manager)

    # Update pop
    for indx, p in enumerate(parents):
        manager.db.updateMemberInPop(p[0],p[1],childs[indx])

    # Update bests in bd
    best = manager.db.getBestFitnessInPop()
    manager.db.setNewBest(best[0], best[1], best[3])

    manager.db.saveChanges() # save changes in to database

# ===================================================================
# Main
# ===================================================================
def main():
    # callShellFile()

    # Set up variables
    
    script_dir = os.path.dirname(__file__)
    
    # Get JSON results file path 
    path = './../MagarenaCode_1_96/resources/magic/ai/FSMPlaysResults.json'
    file_path_results = os.path.join(script_dir,  path)

    # Get JSON ai file path 
    path_FSM = './../MagarenaCode_1_96/resources/magic/ai/FSMData.json'
    file_path_FSM = os.path.join(script_dir,  path_FSM)

    manager = MainManager(file_path_results,file_path_FSM)

    # Numero de generacions crear
    print("Set number of generacions: ")
    n = input()
    # Genetic Algorithm call
    for i in range(n):
        lastGen = manager.db.getLastGen()
        genetic_funciton(lastGen, manager)

    # Plot functions
    manager.data_plot.getAllFitnessPlots()

    # BD close
    manager.db.close()

        


# ===================================================================
# Call
# ===================================================================
main()
