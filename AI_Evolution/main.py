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

# ===================================================================
# Functions
# ===================================================================

# Run shell test

def callShellFile(oponent,duels, matches):
    print("====================== Execute Test ======================")
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
    dataManager = DataManager(file_path)
    # try:
    # callShellFile("FSM", 1, 3) # Run Duels
    # finally:
    dataManager.readJSONFile(file_path) # Read results of the duels

    print(dataManager)


# ===================================================================
# Call
# ===================================================================
main()
