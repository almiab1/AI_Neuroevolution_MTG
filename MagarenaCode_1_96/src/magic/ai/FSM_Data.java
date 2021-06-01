package magic.ai;

import java.io.InputStream;
import java.util.*;
import org.json.*;

public class FSM_Data {
    
    private JSONObject json;
    private String pathJSON = "/JSON/FSM_Data.json"; // compilation path
    // private String pathJSON = "FSM_Data.json"; // test path
    
    // Contructor
    FSM_Data(){
        String resourceName = this.pathJSON;
        // InputStream is = FSM_Data.class.getResourceAsStream(resourceName);
        InputStream is = FSM_Data.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        // System.out.println("JSON Obtained--> " + object.toString());
        
        this.json = object;
    }
    
    /* ----------------------------------------------------------------
        Getters
       ---------------------------------------------------------------- */

    // Getter JSON object
    public JSONObject getJSON() {
        return this.json;
    }
    // Getter JSON path
    public String getPathJSON() {
        return this.pathJSON;
    }

    /* ----------------------------------------------------------------
        Setters
       ---------------------------------------------------------------- */

    // Setter name
    public void setJSON(JSONObject newJson) {
      this.json = newJson;
    }
    // Setter JSONPath
    public void setPathJSON(String newPath) {
      this.pathJSON = newPath;
    }

    /* ----------------------------------------------------------------
        Weight selection method - Design
        ----------------------------------------------------------------
        
        json object -->
        weightBasedSelector()
        --> string "code action choice"
       ---------------------------------------------------------------- */
    private String weightBasedSelector(JSONObject jsonObj){

        // init 
        String actionChoice = null;

        // Random selection
        Random rand = new Random();
        Double randomWeight = rand.nextDouble();
        Double acum_weight = 0.0;
        
        // Selector
        for (Object key : jsonObj.keySet()) {
            
            // get info obj
            String keyStr = key.toString();
            Double keyvalue = jsonObj.getDouble(keyStr);
            
            acum_weight += keyvalue; // sum weight
            
            if(randomWeight <= acum_weight){
                actionChoice = keyStr;
                return actionChoice;
            }

        }
        
        return actionChoice;
    }

    /* ----------------------------------------------------------------
        Choice's Methods
        ----------------------------------------------------------------

        --- Get to JSON XXX choices - Design ---

        (int) diferences lifes -->
        getXXXChoice()
        --> (string) action code

        --- Exploration all json ---

        for (Object key : json.keySet()) {
            //based on key types
            String keyStr = (String)key;
            Object value = json.get(keyStr);
        }
       ----------------------------------------------------------------
    */

    public String getLandChoice(int diferenceLifes){
        
        // init
        JSONObject landsObj = json.getJSONObject("PhaseLowerLand"); // get lands state
        JSONObject optLands = null;
        String selection = null;
        
        // selector
        if(diferenceLifes == 0){
            optLands = landsObj.getJSONObject("1");
        } else if(diferenceLifes > 0){
            optLands = landsObj.getJSONObject("2");
        } else if(diferenceLifes < 0){
            optLands = landsObj.getJSONObject("3");
        }

        selection = weightBasedSelector(optLands);

        return selection;
    }

    public String getLowerCreaturesChoice(int diferenceLifes){
        // init
        JSONObject creaturesObj = json.getJSONObject("PhaseLowerCreatures"); // get lands state
        JSONObject optCreatures = null;
        String selection = null;

        // selector
        if(diferenceLifes == 0){
            optCreatures = creaturesObj.getJSONObject("1");

        } else if(0 < diferenceLifes && diferenceLifes < 3){            
            optCreatures = creaturesObj.getJSONObject("3");

        } else if(diferenceLifes > 3){
            optCreatures = creaturesObj.getJSONObject("4");

        } else if(diferenceLifes < 0){
            optCreatures = creaturesObj.getJSONObject("2");
        }
        
        if(optCreatures != null){
            selection = weightBasedSelector(optCreatures);
        }

        return selection;
    }
    
    public String getAtackChoice(int diferenceLifes){
        // init
        JSONObject creaturesObj = json.getJSONObject("PhaseAtack"); // get lands state
        JSONObject optCreatures = null;
        String selection = null;

        // selector
        if(diferenceLifes == 0){
            optCreatures = creaturesObj.getJSONObject("1");
        } else if(0 < diferenceLifes && diferenceLifes < 3){
            optCreatures = creaturesObj.getJSONObject("2");
        } else if(diferenceLifes > 3){
            optCreatures = creaturesObj.getJSONObject("4");
        } else if(diferenceLifes < 0){
            optCreatures = creaturesObj.getJSONObject("3");
        }
        selection = weightBasedSelector(optCreatures);

        return selection;
    }

    public String getDefendChoice(int diferenceLifes){
        // init
        JSONObject deffObj = json.getJSONObject("PhaseDefend"); // get lands state
        String selection = null;
        JSONObject optDeff = null;

        // selector
        if(diferenceLifes == 0){
            optDeff = deffObj.getJSONObject("1");
        } else if(0 < diferenceLifes && diferenceLifes < 3){
            optDeff = deffObj.getJSONObject("3");
        } else if(diferenceLifes > 3){
            optDeff = deffObj.getJSONObject("4");
        } else if(diferenceLifes < 0){
            optDeff = deffObj.getJSONObject("2");
        }

        selection = weightBasedSelector(optDeff);

        return selection;
    }
}