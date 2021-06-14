package magic.ai;

// Magarena imports
import magic.model.MagicGame;
import magic.model.MagicGameLog;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.phase.MagicPhaseType;
import magic.ai.FSMData;

// Java imports
import java.util.*;
        
public class FSM extends MagicAI {

    // ----------------------------------------------------------------------------
    // Set up
    // ----------------------------------------------------------------------------

    private static final long SEC_TO_NANO=1000000000L;

    private final boolean CHEAT;
    
    private FSMData fsmData;
    private FSMSelector fsmSelector;


    FSM(final boolean cheat) {
        CHEAT = cheat;
        this.fsmData = new FSMData();
        this.fsmSelector = new FSMSelector(this.fsmData);
    }

    private void log(final String message) {
        MagicGameLog.log(message);
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
        
        // Update support lists
        this.fsmSelector.setCreaturesPlayer(scorePlayer);
        this.fsmSelector.setLandsPlayer(scorePlayer);
        
        // No choices
        final int size=choiceResultsList.size();
        if (size==0) {
            throw new RuntimeException("No choice results");
        }

        // Single choice
        if (size==1) {
            return sourceGame.map(choiceResultsList.get(0));
        }
        
        // Log choices
        // this.fsmSelector.logChoices(choiceResultsList);
        
        // ----------------------------------------------------------
        // More than one choice
        // ----------------------------------------------------------
        
        Object[] choiceSelected = null; // Init Selection choice
        // FSM 
        MagicPhaseType phase = sourceGame.getPhase().getType(); // get actual phase
        int diferenceLifes =  sourceGame.getDiferenceLifes(); // calculate diference lifes
        Object[] choiceSelectedFSM = this.fsmSelector.selectChoiceFSM(diferenceLifes,phase,choiceResultsList); // select choice
        
        choiceSelected = choiceSelectedFSM; // Set selected choice by FSM
        
        // Logging.
        // final long timeTaken = System.currentTimeMillis() - startTime;
        /*
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
            " life=" + scorePlayer.getLife() +
            " Diference Lifes  = " +diferenceLifes+
            " Choice selected = "+choiceSelected[0].toString()+'\n'+
            "-------------------------------------------------------"+ '\n');
        */
        return sourceGame.map(choiceSelected);
    }
}