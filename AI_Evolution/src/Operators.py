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

    def costFunction(self):
        print("====================== Execute Cost Function ===========================")

    # =================================================================
    # Neuroevolution operations
    # =================================================================

    def crossoverOperation():
        print("====================== Execute Crossover Operation =====================")

    def mutationOperation():
        print("====================== Execute Mutation Operation ======================")