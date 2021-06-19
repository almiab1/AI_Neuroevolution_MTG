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
import json

# ===================================================================
# Functions
# ===================================================================


class Operators():

    def __init__(self, dataset):
        self.dataset = dataset

    def __str__(self):
        return """ Operators class
    Dataset : {}
    """.format(self.dataset)

    # =================================================================
    # Getters and setters
    # =================================================================
    def getDataset(self):
        return self.dataset

    def setDataset(self, newDataset):
        self.dataset = newDataset

    # =================================================================
    # Selection Best
    # =================================================================

    def fitnessFunction(self, diferenceLifes, turnsPlayed):
        matchFitnes = diferenceLifes / turnsPlayed  # fitnes operation
        return matchFitnes
    
    def fitnessFunctionTotal(self):
        # init variables
        fitnes = None
        totalCost_sum = 0
        numMatches = 0
        print("=================== Execute Cost Function ===================")
        for duel in self.dataset:
            numMatches += len(self.dataset[duel]) # sum the number of matches
            for match in self.dataset[duel]:
                totalCost_sum += self.fitnessFunction(match["DiferenceLifes"],match["TurnsPlayed"]) # add match fitnes
        
        fitnes = totalCost_sum / numMatches
        print("Total cost --> {}".format(fitnes))

    # =================================================================
    # Neuroevolution operations
    # =================================================================

    def crossoverOperation():
        print("====================== Execute Crossover Operation =====================")

    def mutationOperation():
        print("====================== Execute Mutation Operation ======================")
