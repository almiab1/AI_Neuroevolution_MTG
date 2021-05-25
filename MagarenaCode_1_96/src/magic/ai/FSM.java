package magic.ai;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.ai.FSM_Data;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
        
public class FSM extends MagicAI {

    // ----------------------------------------------------------------------------
    // Set up
    // ----------------------------------------------------------------------------

    private static final long SEC_TO_NANO=1000000000L;

    private final boolean CHEAT;
    
    private FSM_Data fsm_data;

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
        log("------------- Choices search -------------");
        log("Choice list size is {"+choiceResultsList.size()+"}");
        for(Object[] choice:choiceResultsList){
            log("Choice:"+'\n' +
                "   Choice class is "+choice[0].getClass()+
                "   Choice string: "+choice[0].toString());
        }
        log("----------- Choices search ends -----------");
    }
    
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

    private MagicCard getStrongestCreature(List<MagicCard> creaturesHand, List<Object[]> choices){
        
        // Init
        MagicCard strongestCreature = null;
        List<MagicCard> creaturesChoicesList = new ArrayList<MagicCard>();
        
        // Get card of the choices list
        for(Object[] choice:choices){
            if(choice[0].toString() != "pass" || choice[0].toString() != "skip"){
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
        } else if(creaturesChoicesList.size() > 0){
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

        return strongestCreature;
    }
    
    private MagicCard getWeakestCreature(List<MagicCard> creaturesHand, List<Object[]> choices){
        
        // Init
        MagicCard weakestCreature = null;
        List<MagicCard> creaturesChoicesList = new ArrayList<MagicCard>();
        
        
        // Get card of the choices list
        for(Object[] choice:choices){
            if(choice[0].toString() != "pass" || choice[0].toString() != "skip"){
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
        
        return weakestCreature;
    }

    private Object[] getPassChoice(List<Object[]> choices){
        // Init
        Object[] passChoice = null;
                
        // Get card of the choices list
        for(Object[] choice:choices){
            if(choice[0].toString() == "pass"){
                passChoice = choice;
            }
        }
        return passChoice;
    }
    
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
        List<MagicCard> lands = getLandsOfMyHand(scorePlayer);
        List<MagicCard> creatures = getCreatures(scorePlayer);
        MagicCard strongestCreature = getStrongestCreature(creatures, choiceResultsList);
        MagicCard weakestCreature = getWeakestCreature(creatures, choiceResultsList);
        Object[] passChoice = getPassChoice(choiceResultsList);
        
        try {
            testJSON();
        } catch (IOException ex) {
            Logger.getLogger(FSM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // String phase = sourceGame.getPhase().getType().toString();
        
        // if(phase == "FirstMain" || phase == "SecondMain" || phase == "DeclareBlockers" || phase == "DeclareAttackers"){
        //     log("------------- Lands and Creatures -------------"+'\n'+
        //         "   Lands => "+lands+'\n'+
        //         "   Creatures => "+creatures+'\n'+
        //         "       Wakest => "+weakestCreature+'\n'+
        //         "       Stongest => "+strongestCreature+'\n'+
        //         "----------- Lands and Creatures Ends ----------");
        // }

        // Random choice
        int randomIndex = (int)(Math.random() * ((choiceResultsList.size())));
        Object[] choiceSelected = choiceResultsList.get(randomIndex);
        
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
        return sourceGame.map(choiceSelected);
    }
}