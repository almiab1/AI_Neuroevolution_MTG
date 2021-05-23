package magic.ai;

import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.choice.MagicChoice;
import java.util.*;
        
public class FSM extends MagicAI {

    // ----------------------------------------------------------------------------
    // Set up
    // ----------------------------------------------------------------------------

    private static final long SEC_TO_NANO=1000000000L;

    private final boolean CHEAT;

    FSM(final boolean cheat) {
        CHEAT = cheat;
    }

    private void log(final String message) {
        MagicGameLog.log(message);
    }

    // ----------------------------------------------------------------------------
    // Selection methods for the phases
    // ----------------------------------------------------------------------------
    
    private void evaluateCards(List<Object[]> choiceResultsList){
        log("------------- Choices search -------------");
        for(Object[] choice:choiceResultsList){
            
            log("Choice:"+'\n' +
                "   Choice list size is {"+choiceResultsList.size()+"}"+'\n' +
                "   Choice class is "+choice[0].getClass()+'\n' +
                "   Choice string: "+choice[0].toString()+'\n' +
                "--------------------------");
        }
        log("------------- Choices search ends -------------");
    }
    
    private List<MagicCard> getLandsOfMyHand(final MagicPlayer scorePlayer){
        List<MagicCard> hand = scorePlayer.getHand();
        
        List<MagicCard> lands = new ArrayList<MagicCard>();
        
        for (MagicCard card:hand){
            if(card.isLand()){ 
                lands.add(card);
            }
        }
        
       log("Lands finded ["+ lands.size() + "] = " + lands);
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
        
        log("Creatures finded ["+ creatures.size() + "] = " + creatures);
        
        return creatures;
    }

    private MagicCard getStrongestCreature(List<MagicCard> creatures){
        
        MagicCard strongestCreature = null;
        
        // Length of the list is 1
        if (creatures.size() == 1) {
            strongestCreature = creatures.get(0);
        } else if( creatures.size() > 1) { // Length of the list is more than one
            strongestCreature = creatures.get(0);
           
            // Find the strongest creature
            for (int i = 0; i < creatures.size(); i++) {
                if(strongestCreature.getCardDefinition().getCardPower() < creatures.get(i).getCardDefinition().getCardPower()){ 
                    strongestCreature = creatures.get(i);
                }  else if(strongestCreature.getCardDefinition().getCardPower() == creatures.get(i).getCardDefinition().getCardPower() &&
                        strongestCreature.getCardDefinition().getCardToughness() < creatures.get(i).getCardDefinition().getCardToughness()){ 
                    strongestCreature = creatures.get(i);
                }
            }
        }
        log("Strogest Creature = " + strongestCreature);
        return strongestCreature;
    }
    
    private MagicCard getWeakestCreature(List<MagicCard> creatures){
        
        MagicCard weakestCreature = null;
        
        // Length of the list is 1
        if (creatures.size() == 1) {
            weakestCreature = creatures.get(0);
        } else if( creatures.size() > 1) {  // Length of the list is more than one
            weakestCreature = creatures.get(0);
           
            // Find the weakest creature
            for (int i = 0; i < creatures.size(); i++) {
                if(weakestCreature.getCardDefinition().getCardPower() > creatures.get(i).getCardDefinition().getCardPower()){ 
                    weakestCreature = creatures.get(i);
                }  else if(weakestCreature.getCardDefinition().getCardPower() == creatures.get(i).getCardDefinition().getCardPower() &&
                        weakestCreature.getCardDefinition().getCardToughness() > creatures.get(i).getCardDefinition().getCardToughness()){ 
                    weakestCreature = creatures.get(i);
                }
            }
        }
        log("Weakest Creature = " + weakestCreature);
        return weakestCreature;
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
        
        evaluateCards(choiceResultsList);
                
        List<MagicCard> lands = getLandsOfMyHand(scorePlayer);
        List<MagicCard> creatures = getCreatures(scorePlayer);
        
        MagicCard strongestCreature = getStrongestCreature(creatures);
        MagicCard weakestCreature = getWeakestCreature(creatures);
        
        // No choices
        final int size=choiceResultsList.size();
        if (size==0) {
            throw new RuntimeException("No choice results");
        }

        // Single choice
        if (size==1) {
            return sourceGame.map(choiceResultsList.get(0));
        }
        
        // Random choice
        int randomIndex = (int)(Math.random() * ((choiceResultsList.size())));
        Object[] choice = choiceResultsList.get(randomIndex);
        // Logging.
        final long timeTaken = System.currentTimeMillis() - startTime;
        log("FSM" +
            " cheat=" + CHEAT +
            " index=" + scorePlayer.getIndex() +
            " life=" + scorePlayer.getLife() +
            " phase=" + sourceGame.getPhase().getType() +
            " step=" + sourceGame.getStep() +
            " slice=" + (0/1000000) +
            " time=" + timeTaken+
            " Choice selected = "+choice[0].toString());
        return sourceGame.map(choice);
    }
}