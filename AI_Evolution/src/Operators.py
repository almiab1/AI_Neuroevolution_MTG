# ===================================================================
#   File name: main.py
#   Author: Alejandro Mira Abad
#   Date created: 16/06/2021
#   Python Version: 3.8.7
# ===================================================================

# ===================================================================
# Imports
# ===================================================================
import numpy as np
import random

# ===================================================================
# Functions
# ===================================================================


class Operators():

    # =================================================================
    # Fitness Methods
    # =================================================================

    def fitnessFunction(self, diferenceLifes, turnsPlayed):
        matchFitnes = diferenceLifes / turnsPlayed  # fitnes operation
        return matchFitnes
    
    def fitnessFunctionTotal(self, dataSet):
        # init variables
        fitness = None
        totalCost_sum = 0
        numMatches = 0
        print("=================== Execute Cost Function ===================")
        for duel in dataSet:
            numMatches += len(dataSet[duel]) # sum the number of matches
            for match in dataSet[duel]:
                totalCost_sum += self.fitnessFunction(match["DiferenceLifes"],match["TurnsPlayed"]) # add match fitnes
        
        fitness = totalCost_sum / numMatches
        print("Fitness --> {}".format(fitness))

        return fitness
    
    # =================================================================
    # Utilities
    # =================================================================

    def normalizeValuesAI(self, aiData):
        dataSet = None
        sum_probs = 0

        for index_e, element in enumerate(aiData):
            for phase, value in element.items():
                for index_s, states in enumerate(value):
                    sum_probs = 0
                    for keyAction, prob in states["Opts"].items():
                        sum_probs += prob
                    
                    for keyAction, prob in states["Opts"].items():
                        aiData[index_e][phase][index_s]["Opts"][keyAction] = np.round(prob / sum_probs, 2)
                    

        dataSet = aiData.copy()

        return dataSet

    # =================================================================
    # Neuroevolution operations
    # =================================================================

    def crossoverOperation(self, p1, p2, cross_rate):
        print("=================== Execute Crossover Operation ================")

        # children are copies of parents by default
        ch1, ch2 = p1.copy(), p2.copy()
        # check for recombination
        if np.random.rand() < cross_rate:
            # select crossover point that is not on the end of the string
            crossIndex = random.randint(1, len(p1)-2)
            print("Cross index: {}".format(crossIndex))

            # perform crossover
            ch1 = np.concatenate((p1[:crossIndex],p2[crossIndex:]))
            ch2 = np.concatenate((p2[:crossIndex],p1[crossIndex:]))
        return [ch1, ch2]

    def mutationOperation(self, parent, mut_rate, alpha):
        print("\n=================== Execute Mutation Operation =================")
        child = parent.copy()

        for index_e, element in enumerate(child):
            for phase, value in element.items():
                for index_s, states in enumerate(value):                   
                    for keyAction, prob in states["Opts"].items():
                        if np.random.rand() < mut_rate: 
                            child[index_e][phase][index_s]["Opts"][keyAction] = prob * alpha

        child = self.normalizeValuesAI(child)

        return child
