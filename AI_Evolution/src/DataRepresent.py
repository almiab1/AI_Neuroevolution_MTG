# ===================================================================
#   File name: main.py
#   Author: Alejandro Mira Abad
#   Date created: 26/06/2021
#   Python Version: 3.8.7
# ===================================================================

# ===================================================================
# Imports
# ===================================================================
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt

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

        self.db_bests = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM BestFitness", conn)
        self.db_bests.to_csv('resources/dataset_best.csv', index=False)
    
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

    def updateBestFile(self):
        self.db_bests = pd.read_sql_query("SELECT Gen,IdMember,Fitness FROM BestFitness", self.conn)
        self.db_bests.to_csv('resources/dataset_best.csv', index=False)

    # ===============================================================
    # Plots Functions
    # ===============================================================
    def generatePlotBests(self, x_param, y_param):
        plt.figure()
        fig = self.db_bests.plot(x=x_param, y=y_param).get_figure()
        fig.savefig('resources/plots/bests.jpg')

    def generatePlotPop(self, x_param, y_param):
        plt.figure()
        fig = self.db_pop.plot(x=x_param, y=y_param).get_figure()
        fig.savefig('resources/plots/pop.jpg')

    def generatePlotAllPop(self, x_param, y_param):
        plt.figure()
        fig = self.db_all_pop.plot(x=x_param, y=y_param).get_figure()
        fig.savefig('resources/plots/all_pop.jpg')
    
    def getAllFitnessPlots(self):
        self.generatePlotPop("IdMember","Fitness")
        self.generatePlotAllPop("IdMember","Fitness")
        self.generatePlotBests("IdMember","Fitness")