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
from src.DataManager import DataManager
from src.Operators import Operators

# ===================================================================
# Functions
# ===================================================================

# Run shell test

def callShellFile(oponent,duels, matches):
    subprocess.run(shlex.split(f'./../AI_Test/AiTest.sh {oponent} {duels} {matches}'))


# ===================================================================
# Main
# ===================================================================
def main():
    # callShellFile()

    # Set up variables
    # Get JSON file path
    script_dir = os.path.dirname(__file__)
    path = './../MagarenaCode_1_96/resources/magic/ai/FSMPlaysResults.json'
    file_path = os.path.join(script_dir,  path)


    #Calls
    try:
        # callShellFile("RANDOMV1", 3, 3) # Run Duels
        print("\n")
    finally:
        print("\n======================== Python Test ========================")
        dataManager = DataManager(file_path)
        operators = Operators(dataManager.getData())

        operators.fitnessFunctionTotal()

        # c1,c2 = operators.crossoverOperation(json1,json2, 0.9)

# ===================================================================
# Call
# ===================================================================
main()
