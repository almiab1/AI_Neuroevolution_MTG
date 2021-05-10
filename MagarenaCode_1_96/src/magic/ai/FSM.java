package magic.ai;

import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
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
    
    private List<MagicCard> getLandsOfMyHand(final MagicPlayer scorePlayer){
        List<MagicCard> hand = scorePlayer.getHand();
        
        List<MagicCard> lands = new ArrayList<MagicCard>();
        
        for (MagicCard card:hand){
            if(card.isLand()){ 
                lands.add(card);
            }
        }
        
//        log("Lands finded ["+ lands.size() + "] = " + lands);
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
        
        if (creatures.size() == 1) {
            strongestCreature = creatures.get(0);
        } else if( creatures.size() > 1) {
            strongestCreature = creatures.get(0);
           
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
                
//        getLandsOfMyHand(scorePlayer);
        List<MagicCard> creatures = getCreatures(scorePlayer);
        
        MagicCard strongestCreature = getStrongestCreature(creatures);
        
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

        // Logging.
        final long timeTaken = System.currentTimeMillis() - startTime;
        log("RandomV1" +
            " cheat=" + CHEAT +
            " index=" + scorePlayer.getIndex() +
            " life=" + scorePlayer.getLife() +
            " phase=" + sourceGame.getPhase().getType() +
            " step=" + sourceGame.getStep() +
            " slice=" + (0/1000000) +
            " time=" + timeTaken + 
            " Event = " + choiceResultsList.get(0)
                );
        
        return sourceGame.map(choiceResultsList.get(randomIndex));
    }
}