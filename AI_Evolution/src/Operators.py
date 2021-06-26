# ===================================================================
#   File name: Opertators.py
#   Author: Alejandro Mira Abad
#   Date created: 16/06/2021
#   Python Version: 3.8.7
# ===================================================================

# ===================================================================
# Imports
# ===================================================================
import numpy as np
import random

# ===================================================================
# Class Opertors
# ===================================================================
class Operators():

    # =================================================================
    # Fitness Methods
    # =================================================================

    def fitnessFunction(self, diferenceLifes, turnsPlayed):
        matchFitnes = diferenceLifes / turnsPlayed  # fitnes operation
        return matchFitnes
    
    def fitnessFunctionTotal(self, dataSet):
        # init variables
        fitness = None
        totalCost_sum = 0
        numMatches = 0

        for duel in dataSet:
            numMatches += len(dataSet[duel]) # sum the number of matches
            for match in dataSet[duel]:
                totalCost_sum += self.fitnessFunction(match["DiferenceLifes"],match["TurnsPlayed"]) # add match fitnes
        
        fitness = totalCost_sum / numMatches

        return fitness
    
    # =================================================================
    # Utilities
    # =================================================================

    def normalizeValuesAI(self, aiData):
        dataSet = None
        sum_probs = 0

        for index_e, element in enumerate(aiData):
            for phase, value in element.items():
                for index_s, states in enumerate(value):
                    sum_probs = 0

                    for keyAction, prob in states["Opts"].items():
                        sum_probs += prob
                    
                    for keyAction, prob in states["Opts"].items():
                        aiData[index_e][phase][index_s]["Opts"][keyAction] = np.round(prob / sum_probs, 2)
                    

        dataSet = aiData.copy()

        return dataSet
    
    def shuffleList(self, list):
        random.shuffle(list)
        return list

    # =================================================================
    # Neuroevolution operations
    # =================================================================

    def crossoverOperation(self, p1, p2, cross_rate):
        # print("=================== Execute Crossover Operation ================")

        # children are copies of parents by default
        ch1, ch2 = p1.copy(), p2.copy()
        # check for recombination
        if np.random.rand() < cross_rate:
            # select crossover point that is not on the end of the string
            crossIndex = random.randint(1,len(ch1)-1)
            # perform crossover
            ch1[crossIndex] = p2[crossIndex]
            ch2[crossIndex] = p1[crossIndex]
        return [ch1, ch2]

    def mutationOperation(self, parent, mut_rate, alpha):
        # print("\n=================== Execute Mutation Operation =================")
        child = parent.copy()
        # Mutar dif vidas

        for index_e, element in enumerate(child):
            for phase, value in element.items():
                for index_s, states in enumerate(value):
                    # Lifes Mutation
                    if np.random.rand() < mut_rate:
                        newRange = states["Lifes"] + alpha  if np.random.rand() < mut_rate else states["Lifes"] - alpha
                        child[index_e][phase][index_s]["Lifes"] = newRange

                    # Ops Mutation
                    for keyAction, prob in states["Opts"].items(): 
                        if np.random.rand() < mut_rate:
                            newProb = prob
                            if random.choice([True, False]):
                                newProb = prob + alpha
                            else:
                                newProb = prob - alpha
                            
                            child[index_e][phase][index_s]["Opts"][keyAction] = newProb

        child = self.normalizeValuesAI(child)

        return child
    
    def matting(self,pop,cross_rate, mut_rate, alpha):
        # Get json of the pop
        popData = []
        
        for indx in range(len(pop)):
            popData.append(pop[indx][2])

        popData = np.array(popData)

        # Create empty childs array
        childs = []

        n = int((len(popData)/2))
        # Operations
        for indx in range(n):           
        
            p1 = popData[indx]
            p2 = popData[indx+n]

            # Crossover operation    
            new_child_1, new_child_2 = self.crossoverOperation(p1, p2,cross_rate) # Crossover operation

            # Mutation operation    
            new_child_1 = self.mutationOperation(new_child_1,mut_rate,alpha) # Mutation operation
            new_child_2 = self.mutationOperation(new_child_2,mut_rate,alpha) # Mutation operation

            childs.append(new_child_1)
            childs.append(new_child_2)

        return childs

    def selectPopulationToMatting(self,population):

        # Computes the totallity of the population fitness
        max = sum([memb[3] for memb in population])
        
        # Computes for each chromosome the probability 
        mem_probs = [memb[3]/max for memb in population]

        mem_probs = np.array(mem_probs)
        population_indexs = np.arange(1,len(population)+1)
        
        n_parents = int(len(population)/2)

        matting_pop_id = np.random.choice(a=population_indexs,size=n_parents,replace=False, p=mem_probs)

        selected_pop = []
        for i in population:
            if i[1] in matting_pop_id:
                selected_pop.append(list(i))

        return selected_pop
