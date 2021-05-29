package magic.ai;

// Magarena imports
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicPhaseType;
import magic.model.choice.MagicDeclareAttackersResult;
import magic.model.choice.MagicDeclareBlockersResult;
import magic.model.choice.MagicPlayChoiceResult;
import magic.ai.FSM_Data;

// Java imports
import java.io.IOException;
import java.util.*;
        
public class FSM extends MagicAI {

    // ----------------------------------------------------------------------------
    // Set up
    // ----------------------------------------------------------------------------

    private static final long SEC_TO_NANO=1000000000L;

    private final boolean CHEAT;
    
    private FSM_Data fsm_data;

    private List<MagicCard> creaturesHand; 
    private List<MagicCard> landsHand; 


    FSM(final boolean cheat) {
        CHEAT = cheat;
        this.fsm_data = new FSM_Data();
    }

    private void log(final String message) {
        MagicGameLog.log(message);
    }

    // ----------------------------------------------------------------------------
    // Selection methods for the phases
    // ----------------------------------------------------------------------------
    
    private void evaluateCards(List<Object[]> choiceResultsList){
        System.out.println("------------- Choices search -------------");
        System.out.println("Choice list size is {"+choiceResultsList.size()+"}");
        for(Object[] choice:choiceResultsList){
            System.out.println("Choice:"+'\n' +
                "   Choice class is "+choice[0].getClass()+
                "   Choice string: "+choice[0].toString());
        }
        System.out.println("----------- Choices search ends -----------");
    }
    
    // Lands methods
    private List<MagicCard> getLandsOfMyHand(final MagicPlayer scorePlayer){
        List<MagicCard> hand = scorePlayer.getHand();
        
        List<MagicCard> lands = new ArrayList<MagicCard>();
        
        for (MagicCard card:hand){
            if(card.isLand()){ 
                lands.add(card);
            }
        }
        return lands;
    }
    
    // Creatures atack and lower methods
    private List<MagicCard> getCreatures(final MagicPlayer scorePlayer){
        List<MagicCard> hand = scorePlayer.getHand();
        
        List<MagicCard> creatures = new ArrayList<MagicCard>();
        
        for (MagicCard card:hand){
            if(card.isCreature()){ 
                creatures.add(card);
            }
        }
        
        return creatures;
    }

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
                    if(choice[0].toString() == creature.getName()){
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
                    if(choice[0].toString() == creature.getName()){
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

    private Object[] getAtackWhithAll(List<Object[]> choices){
        // Init
        Object[] selectedChoice = null;
        
        for(Object[] choice:choices){
            MagicDeclareAttackersResult c = (MagicDeclareAttackersResult) choice[0];
            System.out.println("Size of the array: " + c.getSize());
        }
       
        /*
        int bigSizeChoice = choices.get(0)[0].toString.toArray().length;
        
        // Get card of the choices list
        for(Object[] choice:choices){
            if(bigSizeChoice < choice[0].length){
                selectedChoice = choice;
            }
        }
        */
        return selectedChoice;
    }

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

    private Object[] getLandChoice(List<Object[]> choices){
        // Init
        Object[] landChoice = null;
        List<Object[]> landChoiceList = new ArrayList<Object[]>();
                
        // Get card of the choices list
        for(Object[] choice:choices){
            if(choice[0] != MagicPlayChoiceResult.PASS){
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
    private Object[] getNoAtackChoice(List<Object[]> choices){
        // Init
        Object[] noAtackChoice = null;
                
        // Get card of the choices list
        for(Object[] choice:choices){
            MagicDeclareBlockersResult selectedChoice = (MagicDeclareBlockersResult) choice[0];
            
            if(selectedChoice.getPosition() == 0){
                noAtackChoice = choice;
            }
        }
        return noAtackChoice;
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
                evaluatedActionSelected = getNoAtackChoice(choices);
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
                evaluatedActionSelected = getPassChoice(choices);
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

   // Selection method
    private Object[] selectChoiceFSM(int diferenceLifes, MagicPhaseType phase, List<Object[]> choices){
        // init
        String optionSelected = null;
        Object[] choiceSelected = null;
        
        
        switch(phase){
            // First Main phase
            case FirstMain:
                optionSelected = this.fsm_data.getLandChoice(diferenceLifes);
                
                if(optionSelected == null){
                    optionSelected = this.fsm_data.getLowerCreaturesChoice(diferenceLifes);
                    choiceSelected = evaluateLowerCreaturesAction(optionSelected, choices);
                } else {
                    choiceSelected = evaluateLandAction(optionSelected, choices);
                }

                System.out.println("[FSM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                break;
                
            // Second Main phase
            case SecondMain:
                optionSelected = this.fsm_data.getLandChoice(diferenceLifes);

                if(optionSelected == null){
                    optionSelected = this.fsm_data.getLowerCreaturesChoice(diferenceLifes);
                    choiceSelected = evaluateLowerCreaturesAction(optionSelected, choices);
                } else {
                    choiceSelected = evaluateLandAction(optionSelected, choices);
                }

                System.out.println("[FSM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                break;

            // Declare Blockers phase
            case DeclareBlockers:
                optionSelected = this.fsm_data.getDefendChoice(diferenceLifes);
                choiceSelected = evaluateDefendAction(optionSelected, choices);
                System.out.println("[FSM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                break;

            // Atack phase
            case DeclareAttackers:
                optionSelected = this.fsm_data.getAtackChoice(diferenceLifes);
                choiceSelected = evaluateAtackAction(optionSelected, choices);
                break;

            // Random choice
            default:
                int randomIndex = (int)(Math.random() * ((choices.size())));
                choiceSelected  = choices.get(randomIndex);
                System.out.println("[RANDOM - "+phase+"] Option selected: " + optionSelected + ", Choice selected: " + choiceSelected[0].toString());
                break;
        }

        return choiceSelected;
    }
    
    // ----------------------------------------------------------------------------
    // JSON
    // ----------------------------------------------------------------------------
    
    private void testJSON() throws IOException{
        this.fsm_data.getLandChoice(0);
    }

    // ----------------------------------------------------------------------------
    // findNextEventChoiceResults
    // ----------------------------------------------------------------------------
    @Override
    public Object[] findNextEventChoiceResults(final MagicGame sourceGame,final MagicPlayer scorePlayer) {

        // Check game
        final long startTime = System.currentTimeMillis();

        final MagicGame choiceGame=new MagicGame(sourceGame,scorePlayer);
        if (!CHEAT) {
            choiceGame.hideHiddenCards();
        }
        final MagicEvent event=choiceGame.getNextEvent();
        final List<Object[]> choiceResultsList=event.getArtificialChoiceResults(choiceGame);
        
        // Update lists
        this.creaturesHand = getCreatures(scorePlayer);
        this.landsHand = getLandsOfMyHand(scorePlayer);

        
        // No choices
        final int size=choiceResultsList.size();
        if (size==0) {
            throw new RuntimeException("No choice results");
        }

        // Single choice
        if (size==1) {
            return sourceGame.map(choiceResultsList.get(0));
        }
        
        // ----------------------------------------------------------
        // More than one choice
        // ----------------------------------------------------------

        // Get info about the choices
        evaluateCards(choiceResultsList);

        // Selection choice
        Object[] choiceSelected = null;

        // FSM 
        MagicPhaseType phase = sourceGame.getPhase().getType();
        int diferenceLifes =  sourceGame.getPlayer(1).getLife() - sourceGame.getPlayer(0).getLife();
        System.out.println("Diference Lifes = "+diferenceLifes);
        Object[] choiceSelectedFSM = selectChoiceFSM(diferenceLifes,phase,choiceResultsList);

        choiceSelected = choiceSelectedFSM;

        /*
        if(choiceSelectedFSM != null) {
            choiceSelected = choiceSelectedFSM;

            System.out.println("-------------------- FSM SLECTION --------------------");
            System.out.println("Choice selected FSM: " + choiceSelected[0].toString() + " and the phase is " + phase.toString() + '\n');
        } else {
            // Random choice
            int randomIndex = (int)(Math.random() * ((choiceResultsList.size())));
            choiceSelected  = choiceResultsList.get(randomIndex);
            
            System.out.println("-------------------- RAND SLECTION --------------------");
            System.out.println("Choice selected FSM: " + choiceSelected[0].toString() + " and the phase is " + phase.toString() + '\n');
        }
        */
        
        // Logging.
        final long timeTaken = System.currentTimeMillis() - startTime;
        log("--------------------------FSM--------------------------" + '\n'+
            " cheat=" + CHEAT +
            " index=" + scorePlayer.getIndex() +
            " life=" + scorePlayer.getLife() +
            " phase=" + sourceGame.getPhase().getType() +
            " step=" + sourceGame.getStep() +
            " slice=" + (0/1000000) +
            " time=" + timeTaken+
            " Choice selected = "+choiceSelected[0].toString()+'\n'+
            "-------------------------------------------------------");
        System.out.println("--------------------------FSM--------------------------" + '\n'+
            " cheat=" + CHEAT +
            " index=" + scorePlayer.getIndex() +
            " life=" + scorePlayer.getLife() +
            " phase=" + sourceGame.getPhase().getType() +
            " step=" + sourceGame.getStep() +
            " slice=" + (0/1000000) +
            " time=" + timeTaken+
            " Choice selected = "+choiceSelected[0].toString()+'\n'+
            "-------------------------------------------------------"+ '\n');
        return sourceGame.map(choiceSelected);
    }
}