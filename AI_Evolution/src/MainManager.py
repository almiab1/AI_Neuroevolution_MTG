# ===================================================================
#   File name: MainManager.py
#   Author: Alejandro Mira Abad
#   Date created: 16/06/2021
#   Python Version: 3.8.7
# ===================================================================

# ===================================================================
# Imports
# ===================================================================
from src.DataManager import DataManager
from src.Operators import Operators
from src.DBManager import DBManager

# ===================================================================
# Class Opertors
# ===================================================================
class MainManager():

    def __init__(self,file_path_results, file_path_FSM, db_location=None):

        self.res_m = DataManager(file_path_results)
        self.fsm_m = DataManager(file_path_FSM)
        self.op = Operators()
        self.db = DBManager(db_location)