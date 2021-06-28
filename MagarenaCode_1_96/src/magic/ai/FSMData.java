package magic.ai;

import java.io.InputStream;
import java.util.*;
import org.json.*;

/*  ----------------------------------------------------------------
        DATA STRUCTURE
    ----------------------------------------------------------------

    (jsonObj) array [3]
        0 {1}
            PhaseLowerLand [3]
                0 {2}
                    (int) Lifes
                    (jsonObj) Opts    
       

*/

public class FSMData {
    
    private JSONArray json;
    private String pathJSON = "FSMData.json"; // path json
    private String pathJSONSecond = "FSMDataSecondary.json"; // path json secondary

    
    // Contructor
    FSMData(boolean isMain){
        String resourceName = null;
        if (isMain) {
            resourceName = this.pathJSON;
        } else {
            resourceName = this.pathJSONSecond;
        }
        
        InputStream is = FSMData.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONArray array = new JSONArray(tokener);
        // System.out.println("JSON Obtained--> " + object.toString());
        
        this.json = array;
    }
    
    /* ----------------------------------------------------------------
        Getters
       ---------------------------------------------------------------- */

    // Getter JSON object
    public JSONArray getJSON() {
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
    public void setJSON(JSONArray newJson) {
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
        JSONArray landsObj = this.json.getJSONObject(0).getJSONArray("PhaseLowerLand"); // get lands state
        JSONObject optLands = null;
        String selection = null;
        
        // selector
        if(diferenceLifes > landsObj.getJSONObject(0).getInt("Lifes")){
            optLands = landsObj.getJSONObject(0).getJSONObject("Opts");
        } else if(diferenceLifes == landsObj.getJSONObject(1).getInt("Lifes")){
            optLands = landsObj.getJSONObject(1).getJSONObject("Opts");
        } else if(diferenceLifes < landsObj.getJSONObject(2).getInt("Lifes")){
            optLands = landsObj.getJSONObject(2).getJSONObject("Opts");
        }

        selection = weightBasedSelector(optLands);

        return selection;
    }

    public String getLowerCreaturesChoice(int diferenceLifes){
        // init
        JSONArray creaturesObj = json.getJSONObject(1).getJSONArray("PhaseLowerCreatures"); // get lands state
        JSONObject optCreatures = null;
        String selection = null;

        // selector
        if(diferenceLifes > creaturesObj.getJSONObject(0).getInt("Lifes")){
            optCreatures = creaturesObj.getJSONObject(0).getJSONObject("Opts");

        } else if(diferenceLifes > creaturesObj.getJSONObject(1).getInt("Lifes")){            
            optCreatures = creaturesObj.getJSONObject(1).getJSONObject("Opts");

        } else if(diferenceLifes == creaturesObj.getJSONObject(2).getInt("Lifes")){
            optCreatures = creaturesObj.getJSONObject(2).getJSONObject("Opts");

        } else if(diferenceLifes < creaturesObj.getJSONObject(3).getInt("Lifes")){
            optCreatures = creaturesObj.getJSONObject(3).getJSONObject("Opts");
        }
        
        if(optCreatures != null){
            selection = weightBasedSelector(optCreatures);
        }

        return selection;
    }
    
    public String getAtackChoice(int diferenceLifes){
        // init
        JSONArray creaturesObj = json.getJSONObject(2).getJSONArray("PhaseAtack"); // get lands state
        JSONObject optCreatures = null;
        String selection = null;

        // selector
        if(diferenceLifes > creaturesObj.getJSONObject(0).getInt("Lifes")){
            optCreatures = creaturesObj.getJSONObject(0).getJSONObject("Opts");
        } else if(diferenceLifes > creaturesObj.getJSONObject(1).getInt("Lifes")){
            optCreatures = creaturesObj.getJSONObject(1).getJSONObject("Opts");
        } else if(diferenceLifes == creaturesObj.getJSONObject(2).getInt("Lifes")){
            optCreatures = creaturesObj.getJSONObject(2).getJSONObject("Opts");
        } else if(diferenceLifes < creaturesObj.getJSONObject(3).getInt("Lifes")){
            optCreatures = creaturesObj.getJSONObject(3).getJSONObject("Opts");
        }
        selection = weightBasedSelector(optCreatures);

        return selection;
    }

    public String getDefendChoice(int diferenceLifes){
        // init
        JSONArray deffObj = json.getJSONObject(3).getJSONArray("PhaseDefend"); // get lands state
        String selection = null;
        JSONObject optDeff = null;

        // selector
        if(diferenceLifes > deffObj.getJSONObject(0).getInt("Lifes")){
            optDeff = deffObj.getJSONObject(0).getJSONObject("Opts");
        } else if(diferenceLifes == deffObj.getJSONObject(1).getInt("Lifes")){
            optDeff = deffObj.getJSONObject(1).getJSONObject("Opts");
        } else if(diferenceLifes <= deffObj.getJSONObject(2).getInt("Lifes")){
            optDeff = deffObj.getJSONObject(2).getJSONObject("Opts");
        } else if(diferenceLifes < deffObj.getJSONObject(3).getInt("Lifes")){
            optDeff = deffObj.getJSONObject(3).getJSONObject("Opts");
        }

        selection = weightBasedSelector(optDeff);

        return selection;
    }
}