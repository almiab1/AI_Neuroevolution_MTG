package magic.ai;

import java.util.List;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.event.MagicEvent;
        
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

    private void evaluateHand(final MagicGame sourceGame,final MagicPlayer scorePlayer) {
        int numLands = 0;
        int numCreatures = 0;
        
        List<MagicCard> hand = scorePlayer.getHand();
        
        for (MagicCard card:hand){
            if(card.isLand()){ 
                numLands += 1;
            } else if (card.isCreature()){
                numCreatures += 1;
            }
        }
        
        log("Num creatures = " + numCreatures + " Num lands = "+numLands);
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
        
        evaluateHand(sourceGame,scorePlayer);
        
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
            " Energy = " + scorePlayer.getEnergy() +
            " Hand size = " + scorePlayer.getHandSize() +
            " Hand = " + scorePlayer.getHand() +
            " Mana activations = " + scorePlayer.getManaActivations(choiceGame)
                );
        
        return sourceGame.map(choiceResultsList.get(randomIndex));
    }
}