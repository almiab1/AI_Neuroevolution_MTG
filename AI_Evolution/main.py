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

    # Select population
    pop = manager.db.getMembersGen(gen)
    # Execute duels and calculate fitness of the population
    runDuelsAndFitness(pop, 3, 10, "RANDOMV1", manager)
    # Select parents (best of the tested population)
    best = manager.db.getBestOfGen(gen,n_parents)
    print("""
    Genetic Function

    Best
    
    {}
    """.format(best))
    # Matting

    # Update population with the childs
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
        genetic_funciton(1,2, manager)
        # =================================================================
        # Test calls
        # =================================================================

        # op.fitnessFunctionTotal(dat_manager.getData()) # fitness function call

#         j1 = [{"PhaseLowerLand":[{"Lifes":1,"Opts":{"N":1,"B":1}}]},{"PhaseLowerCreatures":[{"Lifes":1,"Opts":{"N":1,"B":1}}]},{"PhaseAtack":[{"Lifes":1,"Opts":{"N":1,"B":1}}]},{"PhaseDefend":[{"Lifes":1,"Opts":{"N":1,"B":1}}]}]
#         j2 = [{"PhaseLowerLand":[{"Lifes":2,"Opts":{"N":2,"B":2}}]},{"PhaseLowerCreatures":[{"Lifes":2,"Opts":{"N":2,"B":2}}]},{"PhaseAtack":[{"Lifes":2,"Opts":{"N":2,"B":2}}]},{"PhaseDefend":[{"Lifes":2,"Opts":{"N":2,"B":2}}]}]
        
#         c1,c2 = op.crossoverOperation(fsm.parseToNpArray(j1),dat_manager.parseToNpArray(j2), 0.9) # crossover funtion call

#         cmn = op.mutationOperation(fsm.parseToNpArray(fsm.getData()), 0.05, 0.1) # mutation function call
        
#         print("""
#         -- C1 --
        
# {}

#         -- C2 --
        
# {}

#         -- CN --

# {}
#         """.format(c1,c2,cmn))

        # pop = dat_manager.generatePopulation(9) # Generate random pop
        # callShellFile("RANDOMV1", 3, 10) # Run Duels
        # dat_manager.updateData()         # update values of duels
        # strObj = dat_manager.toString(fsm.getData())         # parse to string
        # fit = op.fitnessFunctionTotal(dat_manager.getData()) # fitness function call

        # db.setNewMember(1,1,strObj,fit)

        # for indx,e in enumerate(pop):
            
            
        #     fsm.wtriteJSONFile(e)

        #     callShellFile("RANDOMV1", 3, 10) # Run Duels
        #     dat_manager.updateData()         # update values of duels

        #     fit = op.fitnessFunctionTotal(dat_manager.getData()) # fitness function call

        #     strObj = dat_manager.toString(e)
        #     db.setNewMember(1,indx+2,strObj,fit)
    
        #     # db.getMembersGen(db.getLastGen())

        # db.getBestOfGen(1,2)
            
        manager.db.saveChanges()
        manager.db.close()

        


# ===================================================================
# Call
# ===================================================================
main()
