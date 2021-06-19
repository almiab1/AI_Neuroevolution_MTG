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

class DataManager():
    
    def __init__(self, path):

        # First read json file
        with open(path, 'r') as file:
            jsonObject = json.load(file)
    
        # Set values
        self.pathFile = path
        self.data = jsonObject

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

    def setData(self, newPath):
        self.pathFile = newPath
    
    
    # =================================================================
    # JSON Managment Methods
    # =================================================================
    def readJSONFile(self):
        with open(self.pathFile, 'r') as file:
            self.data = json.load(file)

    def wtriteJSONFile(self, jsonObject):
        with open(self.pathFile, 'w') as file:
            json.dump(jsonObject, file, indent=4)