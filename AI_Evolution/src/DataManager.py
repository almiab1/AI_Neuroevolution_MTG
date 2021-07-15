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
import numpy as np
from src.Operators import Operators

# ===================================================================
# Functions
# ===================================================================


class DataManager():

    def __init__(self, path = None):

        if path is not None:
            # First read json file
            with open(path, 'r') as file:
                jsonObject = json.load(file)

            # Set values
            self.pathFile = path
            self.data = jsonObject
        
        self.op = Operators()

    def __str__(self):
        return """Data Manager class
    JSON Object : {}
    Path File : {}
    """.format(self.data, self.pathFile)

    # =================================================================
    # Getters and setters
    # =================================================================
    def getData(self):
        return self.data

    def getPath(self):
        return self.pathFile

    def setData(self, newData):
        self.dataset = newData

    def setPath(self, newPath):
        self.pathFile = newPath

    # =================================================================
    # JSON Managment Methods
    # =================================================================

    def updateData(self):
        with open(self.pathFile, 'r') as file:
            self.data = json.load(file)

    def wtriteJSONFile(self, jsonObject):
        with open(self.pathFile, 'w') as file:
            json.dump(jsonObject, file, indent=4)

    def parseToNpArray(self, data):
        array = np.array(data)
        return array

    def toString(self, data):
        data_str = json.dumps(data)
        return data_str

    def toJSON(self, data_str):
        data_obj = json.loads(data_str)
        return data_obj

    # =================================================================
    # Generating Population Methods
    # =================================================================

    def generatePopulation(self, size):

        new_pop = []

        for i in range(size):
            new_ai = [
                {"PhaseLowerLand": [
                    {"Lifes": 0,"Opts": {"BT": np.random.rand(),"NBT": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"BT": np.random.rand(),"NBT": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"BT": np.random.rand(),"NBT": np.random.rand()}}
                ]},
                {"PhaseLowerCreatures":[
                    {"Lifes": 3,"Opts": {"NBC": np.random.rand(),"BD": np.random.rand(),"BF": np.random.rand(),"BTC": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"NBC": np.random.rand(),"BD": np.random.rand(),"BF": np.random.rand(),"BTC": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"NBC": np.random.rand(),"BD": np.random.rand(),"BF": np.random.rand(),"BTC": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"NBC": np.random.rand(),"BD": np.random.rand(),"BF": np.random.rand(),"BTC": np.random.rand()}}
                ]},
                {"PhaseAtack": [
                    {"Lifes": 3,"Opts": {"NA": np.random.rand(),"AD": np.random.rand(),"AF": np.random.rand(),"AT": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"NA": np.random.rand(),"AD": np.random.rand(),"AF": np.random.rand(),"AT": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"NA": np.random.rand(),"AD": np.random.rand(),"AF": np.random.rand(),"AT": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"NA": np.random.rand(),"AD": np.random.rand(),"AF": np.random.rand(),"AT": np.random.rand()}}
                ]},
                {"PhaseDefend": [
                    {"Lifes": 0,"Opts": {"ND": np.random.rand(),"DD": np.random.rand(),"DF": np.random.rand(),"DT": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"ND": np.random.rand(),"DD": np.random.rand(),"DF": np.random.rand(),"DT": np.random.rand()}},
                    {"Lifes": 3,"Opts": {"ND": np.random.rand(),"DD": np.random.rand(),"DF": np.random.rand(),"DT": np.random.rand()}},
                    {"Lifes": 0,"Opts": {"ND": np.random.rand(),"DD": np.random.rand(),"DF": np.random.rand(),"DT": np.random.rand()}},
                ]}
            ]

            new_ai = self.op.normalizeValuesAI(new_ai)
            new_pop.append(new_ai)

        return new_pop
