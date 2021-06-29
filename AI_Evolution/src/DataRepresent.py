# ===================================================================
#   File name: main.py
#   Author: Alejandro Mira Abad
#   Date created: 26/06/2021
#   Python Version: 3.8.7
# ===================================================================

# ===================================================================
# Imports
# ===================================================================
import seaborn as sns
import pandas as pd
import matplotlib.pyplot as plt
from scipy import stats
import numpy as np
# Ploty imports
import plotly.express as px
import plotly.graph_objects as go

from src.DBManager import tables_types as db_names
# ===================================================================
# Class Opertors
# ===================================================================

class DataRepresent():

    def __init__(self, conn):
        self.conn = conn

        self.db_pop = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM Population", conn)
        self.db_pop.to_csv('resources/dataset_pop.csv', index=False)
        
        self.db_all_pop = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM AllPopulation", conn)
        self.db_all_pop.to_csv('resources/dataset_all_pop.csv', index=False)

        self.db_bests_pop = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM BestFitnessInPop", conn)
        self.db_bests_pop.to_csv('resources/dataset_best_pop.csv', index=False)

        self.db_bests_his = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM BestFitnessInHistory", conn)
        self.db_bests_his.to_csv('resources/dataset_best_history.csv', index=False)
    
    def __str__(self):
        return"""
    Data Represent Class properties:

Data Set Pop:
{}
Data Set All Pop:
{}
Data Set Best Pop:
{}
        """.format(self.db_pop,self.db_all_pop,self.db_bests)
    
    # ===============================================================
    # Update methods
    # ===============================================================
    def updatePopFile(self):
        self.db_pop = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM Population", self.conn)
        self.db_pop.to_csv('resources/dataset_pop.csv', index=False)
    
    def updateAllPopFile(self):
        self.db_all_pop = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM AllPopulation", self.conn)
        self.db_all_pop.to_csv('resources/dataset_all_pop.csv', index=False)

    def updateBestPopFile(self):
        self.db_bests_pop = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM BestFitnessInPop", self.conn)
        self.db_bests_pop.to_csv('resources/dataset_best_pop.csv', index=False)
    
    def updateBestHisFile(self):
        self.db_bests_his = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM BestFitnessInHistory", self.conn)
        self.db_bests_his.to_csv('resources/dataset_best_history.csv', index=False)
    
    def updateAllCsv(self):
        self.updatePopFile()
        self.updateAllPopFile()
        self.updateBestPopFile()
        self.updateBestHisFile()

    # ===============================================================
    # Plots Functions
    # ===============================================================
    def generatePlotBestsPop(self):
        fig = px.line(self.db_bests_pop, y="Fitness")
        fig.update_layout(title="Evolition Best Members in Population")
        fig.write_image('resources/plots/bests_pop.jpg')
    
    def generatePlotBestsHis(self):
        fig = px.line(self.db_bests_his, y="Fitness")
        fig.update_layout(title="Evolition Best Members in Population Histoty")
        fig.write_image('resources/plots/bests_history.jpg')

    def generatePlotPop(self, code_file):
        self.updatePopFile()
        fig = px.box(self.db_pop, x="Fitness",labels=True, notched=True)
        fig.update_layout(title="Box and Whiskers - Population")
        fig.write_image('resources/plots/popImg/pop_'+code_file+'.jpg')

    def generatePlotAllPop(self):
        fig = px.box(self.db_all_pop, x="Gen", y="Fitness",notched=True)
        fig.update_layout(title="Box and Whiskers - Population History")
        fig.write_image('resources/plots/all_pop.jpg')
    
    def getAllFitnessPlots(self):
        self.updateAllCsv()
        # self.generatePlotPop(str(1))
        self.generatePlotAllPop()
        self.generatePlotBestsPop()
        self.generatePlotBestsHis()