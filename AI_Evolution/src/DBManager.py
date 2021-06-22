# ===================================================================
#   File name: main.py
#   Author: Alejandro Mira Abad
#   Date created: 21/06/2021
#   Python Version: 3.8.7
# ===================================================================

# ===================================================================
# Imports
# ===================================================================
import sqlite3

from src.DataManager import DataManager

# ===================================================================
# Class DB manager
# ===================================================================

class DBManager():

    __db_path = "./resources/population.db"

    def __init__(self,db_location=None):
        """Initialize db class variables"""
        try:
            if db_location is not None:
                self.connection = sqlite3.connect(db_location)
            else:
                self.connection = sqlite3.connect(self.__db_path)
            
            self.cur = self.connection.cursor()
            
            # print("Connection done!")
        except sqlite3.Error as error:
            print("Error while connecting to sqlite", error)
        
        self.data_m = DataManager()
    
    def __del__(self):
        self.connection.close()

    def close(self):
        """close sqlite3 connection"""
        self.connection.close()

    def execute(self, new_data):
        """execute a row of data to current cursor"""
        self.cur.execute(new_data)
    
    def saveChanges(self):
        """commit changes to database"""
        self.connection.commit()

    # =================================================================
    # Get sentences
    # =================================================================
    
    def getLastGen(self):
        lastGenKey = None
        lastGenKey = self.cur.execute('SELECT Generation FROM Population WHERE Generation=(SELECT MAX(Generation) FROM Population)').fetchone()
        return lastGenKey[0]
    
    def getMembersGen(self, genKey):
        members = self.cur.execute('SELECT * FROM Population WHERE Generation = ?',(genKey,)).fetchall()

        members = self.parseJSONMember(members)

        return members
    
    def getBestOfGen(self, genKey, n):
        bests = self.cur.execute('SELECT * FROM Population WHERE Generation = ? ORDER BY Fitness DESC LIMIT ?',(genKey,n)).fetchall()

        bests = self.parseJSONMember(bests)

        return bests


    # =================================================================
    # Set sentences
    # =================================================================
    def setNewMember(self,gen, id, data, fitness):
        self.cur.execute('INSERT INTO Population (Generation,IdMember,AI,Fitness) VALUES (?,?,?,?)',(gen, id, data, fitness))
    
    # =================================================================
    # Update sentences
    # =================================================================

    def updateFitness(self, gen, id, fitness):

        members = self.cur.execute('UPDATE Population SET Fitness = ? WHERE Generation = ? and IdMember = ?',(fitness,gen,id)).fetchone()

        
        for indx, member in enumerate(members):
            members[indx] = self.data_m.toJSON(member[2]) # Parse string json to json object

        return members
    
    # =================================================================
    # utilities
    # =================================================================

    def parseJSONMember(self, members):
        for indx, member in enumerate(members):
            members[indx] = list(member)
            members[indx][2] = self.data_m.toJSON(member[2]) # Parse string json to json object
        
        return members