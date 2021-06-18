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
import json
import os

# ===================================================================
# Functions
# ===================================================================

# Run shell test

def callShellFile(oponent,duels, matches):
    print("====================== Execute Test ======================")
    subprocess.run(shlex.split(f'./../AI_Test/AiTest.sh {oponent} {duels} {matches}'))

# JSON Managment
def readJSONFile(rutaJSONFile):
    data = None

    with open(rutaJSONFile, 'r') as file:
        data = json.load(file)

    return data

def wtriteJSONFile(rutaJSONFile, jsonObject):
    with open(rutaJSONFile, 'w') as file:
        json.dump(jsonObject, file, indent=4)

# ===================================================================
# Evolution Operations
# ===================================================================
def crossoverOperation():
    print("====================== Execute Crossover Operation ======================")

def mutationOperation():
    print("====================== Execute Mutation Operation ======================")

# ===================================================================
# Main
# ===================================================================
def main():
    # callShellFile()

    # Get JSON file path
    script_dir = os.path.dirname(__file__)
    file_path = os.path.join(script_dir,  './../MagarenaCode_1_96/resources/magic/ai/FSMPlaysResults.json')


    #Calls
    # try:
    callShellFile("FSM", 1, 3) # Run Duels
    # finally:
    jsonObject = readJSONFile(file_path) # Read results of the duels
    print("JSON Object --> {}".format(jsonObject))


# ===================================================================
# Call
# ===================================================================
main()
