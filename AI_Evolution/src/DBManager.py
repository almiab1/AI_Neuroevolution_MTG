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
class tables_types:
    allPop = 'AllPopulation'
    pop = 'Population'
    best = 'BestFitness'

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
        lastGenKey = self.cur.execute('SELECT Gen FROM AllPopulation WHERE Gen=(SELECT MAX(Gen) FROM AllPopulation)').fetchone()
        return lastGenKey[0]
    
    def getMembersGenAllPop(self, genKey):
        members = self.cur.execute('SELECT * FROM AllPopulation WHERE Gen = ?',(genKey,)).fetchall()

        members = self.parseJSONMembers(members)

        return members
    
    def getPop(self):
        members = self.cur.execute('SELECT * FROM Population').fetchall()

        members = self.parseJSONMembers(members)

        return members
    
    def getBestFitness(self,table, genKey, n):
        bests = self.cur.execute('SELECT * FROM ? WHERE Gen = ? ORDER BY Fitness DESC LIMIT ?',(table, genKey, n)).fetchall()

        bests = self.parseJSONMembers(bests)

        return bests

    def getBestFitnessInPop(self):
        bests = self.cur.execute('SELECT * FROM Population WHERE Fitness=(SELECT MAX(Fitness) FROM Population)').fetchone()

        bests = self.parseJSONMember(bests)

        return bests

    def getBestFitnessHistory(self):
        best = self.cur.execute('SELECT * FROM AllPopulation WHERE Fitness=(SELECT MAX(Fitness) FROM AllPopulation)').fetchone()

        best = self.parseJSONMember(best)

        return best

    # =================================================================
    # Set sentences
    # =================================================================
    def setNewMemberInPop(self,gen, id, data, fitness):
        self.cur.execute('INSERT INTO Population (Gen,IdMember,AI,Fitness) VALUES (?,?,?,?)',(gen, id, data, fitness))

    def setNewMemberInAllPop(self,gen, id, data, fitness):
        self.cur.execute('INSERT INTO AllPopulation (Gen,IdMember,AI,Fitness) VALUES (?,?,?,?)',(gen, id, data, fitness))
    
    def setNewBestInPop(self,gen, id, fitness):
        self.cur.execute('INSERT INTO BestFitnessInPop (Gen,IdMember,Fitness) VALUES (?,?,?)',(gen, id, fitness))
    
    def setNewBestInHistory(self,gen, id, fitness):
        self.cur.execute('INSERT INTO BestFitnessInHistory (Gen,IdMember,Fitness) VALUES (?,?,?)',(gen, id, fitness))
    
    # =================================================================
    # Update sentences
    # =================================================================

    def updateMemberInPop(self,gen,id, member):
        str_data = self.data_m.toString(member[2])
        new_gen,new_id,fitness = [member[0],member[1],member[3]]
        self.cur.execute('UPDATE Population SET Gen = ?, IdMember = ?, Fitness = ?, AI = ? WHERE Gen = ? and IdMember = ?',(new_gen,new_id,fitness,str_data,gen,id))

    # =================================================================
    # utilities
    # =================================================================

    def parseJSONMembers(self, members):
        for indx, member in enumerate(members):
            members[indx] = list(member)
            members[indx][2] = self.data_m.toJSON(member[2]) # Parse string json to json object
        
        return members

    def parseJSONMember(self, member):
        member = list(member)
        member[2] = self.data_m.toJSON(member[2]) # Parse string json to json object
        
        return member