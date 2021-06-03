package magic.ai;

import java.util.*;
import magic.model.MagicCard;
import magic.model.MagicPlayer;
import magic.model.choice.MagicDeclareAttackersResult;
import magic.model.choice.MagicDeclareBlockersResult;
import magic.model.choice.MagicPlayChoiceResult;
import magic.model.phase.MagicPhaseType;

public class FSMSelector {

    private List<MagicCard> landsHand;
    private List<MagicCard> creaturesHand;
    private FSMData fsmData;
    
    // Contructor
    FSMSelector(FSMData data){
        this.fsmData = data;
    }
    
    /* ----------------------------------------------------------------
        Setters
       ----------------------------------------------------------------
    
       ----------------------------------------------------------------
        Basic set methods - Design
       ----------------------------------------------------------------
        
        (MagicPlayer) scorePlayer -->
        setX()
        --> (List of MagicCards) listX
    ------------------------------------------------------------------- */

    public void setLandsOfMyHand(final MagicPlayer scorePlayer){
        List<MagicCard> hand = scorePlayer.getHand();
        
        List<MagicCard> lands = new ArrayList<MagicCard>();
        
        for (MagicCard card:hand){
            if(card.isLand()){ 
                lands.add(card);
            }
        }
        
        this.landsHand = lands;
    }
    
    public void setCreatures(final MagicPlayer scorePlayer){
        List<MagicCard> hand = scorePlayer.getHand();
        
        List<MagicCard> creatures = new ArrayList<MagicCard>();
        
        for (MagicCard card:hand){
            if(card.isCreature()){ 
                creatures.add(card);
            }
        }
        
        this.creaturesHand = creatures;
    }
    
    /* ----------------------------------------------------------------
        Is methods
       ---------------------------------------------------------------- 
       
       ----------------------------------------------------------------
        Basic is methods - Design
       ----------------------------------------------------------------
        
        (String) cardName -->
        isX()
        --> (boolean) isX
    ------------------------------------------------------------------- */
    
    private boolean isLandCardsHand(String cardName){

        boolean isLand = false;

        for(MagicCard card:this.landsHand){
            if(cardName == card.getName()){
                isLand = true;
            }
        }

        return isLand;
    }

    private boolean isCreatureCardsHand(String cardName){

        boolean isCreature = false;

        for(MagicCard card:this.creaturesHand){
            if(cardName == card.getName()){
                isCreature = true;
            }
        }

        return isCreature;
    }
    
    private boolean isLandsInChoices(List<Object[]> choices){
        for(Object[] choice:choices){
            if(isLandCardsHand(choice[0].toString())){ 
                return true;
            }
        }

        return false;
    }

    // ----------------------------------------------------------------------------
    // Selection methods for the phases
    // ----------------------------------------------------------------------------
    
    // debuging method
    
    public void logChoices(List<Object[]> choiceResultsList){
        System.out.println("------------- Choices search -------------");
        System.out.println("Choice list size is {"+choiceResultsList.size()+"}");
        for(Object[] choice:choiceResultsList){
            System.out.println("Choice:"+'\n' +
                "   Choice class is "+choice[0].getClass()+
                "   Choice string: "+choice[0].toString());
        }
        System.out.println("----------- Choices search ends -----------"+'\n');
    }
    
    
    /* ----------------------------------------------------------------
        States methods
       ----------------------------------------------------------------
    
        (List of Object[]) choices -->
        getXXX()
        --> (Object[]) cardSelected
    ------------------------------------------------------------------- */  

    // Creatures atack and lower methods
    private Object[] getStrongestCreature(List<Object[]> choices){
        
        // Init
        List<MagicCard> creaturesHand = this.creaturesHand;
        Object[] strongestCreatureChoice = null;
        MagicCard strongestCreature = null;
        List<MagicCard> creaturesChoicesList = new ArrayList<MagicCard>();
        
        // Get card of the choices list
        for(Object[] choice:choices){
            if(choice[0] != MagicPlayChoiceResult.PASS || choice[0] != MagicPlayChoiceResult.SKIP || choice[0] != null){
                for(MagicCard creature: creaturesHand){
                    if(isCreatureCardsHand(choice[0].toString())){
                        creaturesChoicesList.add(creature);
                    }
                }
            }
        }
            
        // Selection the strogest creature of the choices list
        if(creaturesChoicesList.size() == 1){ 
            strongestCreature = creaturesChoicesList.get(0);
        } else if(creaturesChoicesList.size() > 1){
            strongestCreature = creaturesChoicesList.get(0);
        
            // Find the strongest creature
            for (MagicCard creature:creaturesChoicesList) {
                if(strongestCreature.getCardDefinition().getCardPower() < creature.getCardDefinition().getCardPower()){ 
                    strongestCreature = creature;
                }  else if(strongestCreature.getCardDefinition().getCardPower() == creature.getCardDefinition().getCardPower() &&
                        strongestCreature.getCardDefinition().getCardToughness() < creature.getCardDefinition().getCardToughness()){ 
                    strongestCreature = creature;
                }
            }
        }
        
        // Get choice
        if(strongestCreature != null) {
            for(Object[] choice:choices){
                if(choice[0].toString() == strongestCreature.getName()){
                    strongestCreatureChoice = choice;
                }
            }
        }

        return strongestCreatureChoice;
    }
    
    private Object[] getWeakestCreature(List<Object[]> choices){
        
        // Init
        List<MagicCard> creaturesHand = this.creaturesHand;
        Object[] weakestCreatureChoice = null;
        MagicCard weakestCreature = null;
        List<MagicCard> creaturesChoicesList = new ArrayList<MagicCard>();
        
        
        // Get card of the choices list
        for(Object[] choice:choices){
            if(choice[0] != MagicPlayChoiceResult.PASS || choice[0] != MagicPlayChoiceResult.SKIP || choice[0] != null){
                for(MagicCard creature: creaturesHand){
                    if(isCreatureCardsHand(choice[0].toString())){
                        creaturesChoicesList.add(creature);
                    }
                }
            }
        }
            
        // Selection the strogest creature of the choices list
        if(creaturesChoicesList.size() == 1){ 
            weakestCreature = creaturesChoicesList.get(0);
        } else if(creaturesChoicesList.size() > 1){
            weakestCreature = creaturesChoicesList.get(0);
        
            // Find the strongest creature
            for (MagicCard creature:creaturesChoicesList) {
                if(weakestCreature.getCardDefinition().getCardPower() > creature.getCardDefinition().getCardPower()){ 
                    weakestCreature = creature;
                }  else if(weakestCreature.getCardDefinition().getCardPower() == creature.getCardDefinition().getCardPower() &&
                        weakestCreature.getCardDefinition().getCardToughness() > creature.getCardDefinition().getCardToughness()){ 
                    weakestCreature = creature;
                }
            }
        }
        
        // Get choice
        if(weakestCreature != null) {
            for(Object[] choice:choices){
                if(choice[0].toString() == weakestCreature.getName()){
                    weakestCreatureChoice = choice;
                }
            }
        }
        
        return weakestCreatureChoice;
    }

    // Lower methods
    private Object[] getPassChoice(List<Object[]> choices){
        // Init
        Object[] passChoice = null;
                
        // Get card of the choices list
        for(Object[] choice:choices){
            if(choice[0] == MagicPlayChoiceResult.PASS){
                passChoice = choice;
            }
        }
        return passChoice;
    }

    // Atacks methods
    private Object[] getAtackWhithAll(List<Object[]> choices){
        // Init
        Object[] selectedChoice = null;
        
        // Search
        MagicDeclareAttackersResult initChoice = (MagicDeclareAttackersResult) choices.get(0)[0];
        int bigSizeChoice = initChoice.getSize();
        
        // Get card of the choices list
        for(Object[] choice:choices){
            MagicDeclareAttackersResult selection = (MagicDeclareAttackersResult) choice[0];
            if(bigSizeChoice < selection.getSize()){
                selectedChoice = choice;
            }
        }
        
        return selectedChoice;
    }

    private Object[] getNoAtackChoice(List<Object[]> choices){
        // Init
        Object[] selectedChoice = null;
        
        // Get choice of the choices list
        for(Object[] choice:choices){
            MagicDeclareAttackersResult selection = (MagicDeclareAttackersResult) choice[0];
            if(selection.getSize() == 0){
                selectedChoice = choice;
            }
        }
        
        return selectedChoice;
    }

    // Lands methods
    private Object[] getLandChoice(List<Object[]> choices){
        // Init
        Object[] landChoice = null;
        List<Object[]> landChoiceList = new ArrayList<Object[]>();
                
        // Get card of the choices list
        for(Object[] choice:choices){
            boolean isLand = isLandCardsHand(choice[0].toString());

            if(choice[0] != MagicPlayChoiceResult.PASS && isLand){
                landChoiceList.add(choice);
            }
        }

        // Get random land choice
        if(landChoiceList.size() > 0){
            int randomIndex = (int)(Math.random() * ((landChoiceList.size())));
            landChoice = landChoiceList.get(randomIndex);
        }

        return landChoice;
    }

    // Block choices
    private Object[] getNoDefChoice(List<Object[]> choices){
        // Init
        Object[] noDefChoice = null;
                
        // Get card of the choices list
        for(Object[] choice:choices){
            MagicDeclareBlockersResult selectedChoice = (MagicDeclareBlockersResult) choice[0];
            
            if(selectedChoice.getPosition() == 0){
                noDefChoice = choice;
            }
        }
        return noDefChoice;
    }
    
    private Object[] getBestBlockChoice(List<Object[]> choices){
        // Init
        Object[] bestChoice = choices.get(0);
        MagicDeclareBlockersResult selectedChoice = (MagicDeclareBlockersResult) choices.get(0)[0];
        int bestScore = selectedChoice.getScore();
        
        // Get card of the choices list
        for(Object[] choice:choices){
            MagicDeclareBlockersResult i_choice = (MagicDeclareBlockersResult) choice[0];            
            
            if(bestScore < i_choice.getScore() && i_choice.getPosition() != 0){
                bestScore = i_choice.getScore();
                bestChoice = choice;
            }
        }
        return bestChoice;
    }
    
    private Object[] getWeakestBlockChoice(List<Object[]> choices){
        // Init
        Object[] weakestChoice = choices.get(0);
        MagicDeclareBlockersResult selectedChoice = (MagicDeclareBlockersResult) choices.get(0)[0];
        int weakestScore = selectedChoice.getScore();
        
        // Get card of the choices list
        for(Object[] choice:choices){
            MagicDeclareBlockersResult i_choice = (MagicDeclareBlockersResult) choice[0];            
            
            if(weakestScore > i_choice.getScore() && i_choice.getPosition() != 0){
                weakestScore = i_choice.getScore();
                weakestChoice = choice;
            }
        }
        return weakestChoice;
    }

    private Object[] getBigDefendChoice(List<Object[]> choices){
        // Init
        Object[] bigDefendChoice = choices.get(0);
        MagicDeclareBlockersResult selectedChoice = (MagicDeclareBlockersResult) choices.get(0)[0];
        int sizeCreaturesCombat = selectedChoice.sizeCreaturesCombat();
        
        // Get card of the choices list
        for(Object[] choice:choices){
            MagicDeclareBlockersResult i_choice = (MagicDeclareBlockersResult) choice[0];            
            
            if(sizeCreaturesCombat < i_choice.sizeCreaturesCombat()){
                sizeCreaturesCombat = i_choice.sizeCreaturesCombat();
                bigDefendChoice = choice;
            }
        }
        return bigDefendChoice;
    }
    
    // ----------------------------------------------------------------------------
    // Select choice
    // ----------------------------------------------------------------------------
    
    // Evaluation methods
    private Object[] evaluateDefendAction(String optionSelected, List<Object[]> choices){
        Object[] evaluatedActionSelected = null;
        
        switch(optionSelected){
            case "ND":
                evaluatedActionSelected = getNoDefChoice(choices);
                break;
            case "DD":
                evaluatedActionSelected = getWeakestBlockChoice(choices);
                break;
            case "DF":
                evaluatedActionSelected = getBestBlockChoice(choices);
                break;
            case "DT":
                evaluatedActionSelected = getBigDefendChoice(choices);
                break;
            default:
                break;
        }
        
        return evaluatedActionSelected;
    }

    private Object[] evaluateAtackAction(String optionSelected, List<Object[]> choices){
        Object[] evaluatedActionSelected = null;

        switch(optionSelected){
            case "NA":
                evaluatedActionSelected = getNoAtackChoice(choices);
                break;
            case "AD":
                evaluatedActionSelected = getWeakestCreature(choices);
                break;
            case "AF":
                evaluatedActionSelected = getStrongestCreature(choices);
                break;
            case "AT":
                evaluatedActionSelected = getAtackWhithAll(choices);
                break;
            default:
                break;
        }

        return evaluatedActionSelected;
    }

    private Object[] evaluateLowerCreaturesAction(String optionSelected, List<Object[]> choices){
        Object[] evaluatedActionSelected = null;

        switch(optionSelected){
            case "NBC":
                evaluatedActionSelected = getPassChoice(choices);
                break;
            case "BD":
                evaluatedActionSelected = getWeakestCreature(choices);
                break;
            case "BF":
                evaluatedActionSelected = getStrongestCreature(choices);
                break;
            case "BTC":
                evaluatedActionSelected = getStrongestCreature(choices);
                break;
            default:
                break;
        }
        
        return evaluatedActionSelected;
    }

    private Object[] evaluateLandAction(String optionSelected, List<Object[]> choices){
        Object[] evaluatedActionSelected = null;

        switch(optionSelected){
            case "BT":
                evaluatedActionSelected = getLandChoice(choices);
                break;
            case "NBT":
                evaluatedActionSelected = getPassChoice(choices);
                break;
            default:
                break;
        }
        
        return evaluatedActionSelected;
    }

   /* ----------------------------------------------------------------
        Selection method - Design
      ----------------------------------------------------------------
        
        (int) diferenceLifes, (MagicPhaseType) phase, (List of Object[]) choices -->
        selectChoiceFSM()
        --> (Object[]) choice
    ------------------------------------------------------------------- */
    public Object[] selectChoiceFSM(int diferenceLifes, MagicPhaseType phase, List<Object[]> choices){
        // init
        String optionSelected = null;
        Object[] choiceSelected = null;
        
        
        switch(phase){
            // First Main phase
            case FirstMain:

                if(isLandsInChoices(choices)){
                    optionSelected = this.fsmData.getLandChoice(diferenceLifes);
                    choiceSelected = evaluateLandAction(optionSelected, choices);
                } else {
                    optionSelected = this.fsmData.getLowerCreaturesChoice(diferenceLifes);
                    choiceSelected = evaluateLowerCreaturesAction(optionSelected, choices);
                }

                if(choiceSelected != null) {
                    System.out.println("[FSM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                }                
                break;
                
            // Second Main phase
            case SecondMain:

                if(isLandsInChoices(choices)){
                    optionSelected = this.fsmData.getLandChoice(diferenceLifes);
                    choiceSelected = evaluateLandAction(optionSelected, choices);
                } else {
                    optionSelected = this.fsmData.getLowerCreaturesChoice(diferenceLifes);
                    choiceSelected = evaluateLowerCreaturesAction(optionSelected, choices);
                }
                
                if(choiceSelected != null) {
                    System.out.println("[FSM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                }
                break;

            // Declare Blockers phase
            case DeclareBlockers:
                optionSelected = this.fsmData.getDefendChoice(diferenceLifes);
                choiceSelected = evaluateDefendAction(optionSelected, choices);
                
                if(choiceSelected != null) {
                    System.out.println("[FSM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                }
                break;

            // Atack phase
            case DeclareAttackers:
                optionSelected = this.fsmData.getAtackChoice(diferenceLifes);
                choiceSelected = evaluateAtackAction(optionSelected, choices);
                
                if(choiceSelected != null) {
                    System.out.println("[FSM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                }
                break;

            // Random choice
            default:
                int randomIndex = (int)(Math.random() * ((choices.size())));
                choiceSelected  = choices.get(randomIndex);
                
                System.out.println("[RANDOM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                break;
        }

        System.out.println("");

        return choiceSelected;
    }
}