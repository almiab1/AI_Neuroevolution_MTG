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
from src.DataRepresent import DataRepresent

# ===================================================================
# Class Manager
# ===================================================================
class MainManager():

    def __init__(self,file_path_results, file_path_FSM, file_path_FSM_secondary, db_location=None):

        self.res_m = DataManager(file_path_results)
        self.fsm_m = DataManager(file_path_FSM)
        self.fsm_m_s = DataManager(file_path_FSM_secondary)
        self.op = Operators()
        self.db = DBManager(db_location)
        self.data_plot = DataRepresent(self.db.connection)