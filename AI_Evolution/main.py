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
    subprocess.run(shlex.split(f'./../AI_Test/MagarenaMaches.sh {oponent} {duels} {matches}'))

def runDuelsAndFitness(population, duels, matches, oponent,manager):

    for indx, member in enumerate(population):

        # print("\nMatch FSM ---> Gen {} and Id {}\n".format(member[0], member[1]))

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
# Generate init random pop
# ===================================================================
def genInitPop(numPop,manager):
    #  Generate data pop
    pop_d = manager.fsm_m.generatePopulation(numPop)
    pop = []
    for indx,mem in enumerate(pop_d):
        pop.append([1, indx+1, list(mem),None])
    # Execute duels and calculate fitness of the population
    pop_t = runDuelsAndFitness(pop, 1, 10, "MCTS", manager)
    # Update pop
    manager.db.updatePopTable(pop_t)
    # Update bests in bd
    best_pop = manager.db.getBestFitnessInPop()
    best_his = manager.db.getBestFitnessHistory()
    manager.db.setNewBestInPop(best_pop[0], best_pop[1], best_pop[3]) 
    manager.db.setNewBestInHistory(best_his[0], best_his[1], best_his[3])
    # Save changes in bd
    manager.db.saveChanges()



# ===================================================================
# Genetic Function
# ===================================================================
def genetic_funciton(gen,manager):
    # print("======================== Genetic Function ========================")

    cross_rate, mut_rate, alpha = [.75,.1,.1]

    # Select population
    pop = manager.db.getPop()

    # Select Parents
    n_parents = int(len(pop)/2)
    parents =  manager.op.selectPopulation(pop,n_parents)
    parents = manager.op.shuffleList(parents) #  Suffle parents

    # Matting
    childs_data = manager.op.matting(parents,cross_rate, mut_rate, alpha)
    
    childs = []
    newGen = gen + 1
    for indx,child in enumerate(childs_data):
        childs.append([newGen, indx+1, list(child),None])

    # Execute duels and calculate fitness of the population
    childs = runDuelsAndFitness(childs, 1, 10, "MCTS", manager)

    # Seleccion poblacion + hijos --> seleccion por ruletas
    popAndChilds = pop + childs
    selectedPop  =  manager.op.selectPopulation(popAndChilds,200)

    # Update pop
    manager.db.updatePopTable(selectedPop)

    # Update bests in bd
    best_pop = manager.db.getBestFitnessInPop()
    best_his = manager.db.getBestFitnessHistory()
    manager.db.setNewBestInPop(best_pop[0], best_pop[1], best_pop[3]) 
    manager.db.setNewBestInHistory(best_his[0], best_his[1], best_his[3]) 

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

    path_FSM_secondary = './../MagarenaCode_1_96/resources/magic/ai/FSMDataSecondary.json'
    file_path_FSM = os.path.join(script_dir,  path_FSM)

    # Build manager
    manager = MainManager(file_path_results,file_path_FSM,path_FSM_secondary)

    # Put best fsm in that moment into secondary fsm
    # best = manager.db.getBestFitnessHistory()
    # manager.fsm_m_s.wtriteJSONFile(best[2]) # charge data in json to test

    # Numero de generacions crear
    # n = int(input("Set number of generacions: "))
    n = 100
    # Generate init pop
    genInitPop(200,manager)

    # Generate initial chart
    lastGen = manager.db.getLastGen()
    manager.data_plot.updateBestPopFile()
    manager.data_plot.generatePlotPop(str(lastGen))

    # Genetic Algorithm call
    for i in range(n):
        lastGen = manager.db.getLastGen()
        genetic_funciton(lastGen, manager)
        # Create plots
        manager.data_plot.updateBestPopFile()
        manager.data_plot.generatePlotPop(str(lastGen+1))
        
        if i is n/2:
            manager.data_plot.getAllFitnessPlots()



    # Plot functions
    manager.data_plot.getAllFitnessPlots()

    # BD close
    manager.db.close()

        


# ===================================================================
# Call
# ===================================================================
main()
