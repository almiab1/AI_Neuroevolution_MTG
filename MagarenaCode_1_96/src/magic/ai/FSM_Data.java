package magic.ai;

import java.io.InputStream;
import org.json.*;

public class FSM_Data {
    
    private JSONObject json;
    private String pathJSON = "./FSM_Data.json";
    
    FSM_Data(){
        String resourceName = this.pathJSON;
        InputStream is = FSM_Data.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        System.out.println("JSON Obtained--> " + object.toString());
        
        this.json = object;
    }
    
    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------
    // Getter JSON object
    public JSONObject getJSON() {
        return this.json;
    }

    public String getPathJSON() {
        return this.pathJSON;
    }

    // ----------------------------------------------------------------
    // Setter
    // ----------------------------------------------------------------

    // Setter name
    public void setJSON(JSONObject newJson) {
      this.json = newJson;
    }
    
    public void setPathJSON(String newPath) {
      this.pathJSON = newPath;
    }

    // ----------------------------------------------------------------
    // Choice's Methods
    // ----------------------------------------------------------------

    /* Exploration all json
        System.out.println("---------------------- JSON Exploration ----------------------");
        
        for (Object key : json.keySet()) {
            //based on you key types
            String keyStr = (String)key;
            Object keyvalue = json.get(keyStr);

            //Print key and value
            System.out.println("key: "+ keyStr + " value: " + keyvalue);
        }
        
        System.out.println("-------------------- JSON Exploration ENDS -------------------");
    */

    public void getLandChoice(int diferenceLifes){
        // get lands state
        JSONObject landsObj = new JSONObject(json.get("PhaseLowerLand").toString());

        // selector
        if(diferenceLifes == 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("1").toString());

        } else if(diferenceLifes > 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("2").toString());

        } else if(diferenceLifes < 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("3").toString());
        } else{

        }
    }

    public void getLowerCreaturesChoice(int diferenceLifes){
        // get lands state
        JSONObject landsObj = new JSONObject(json.get("PhaseLowerCreatures").toString());

        // selector
        if(diferenceLifes == 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("1").toString());

        } else if(0 < diferenceLifes && diferenceLifes < 3){
            JSONObject opt1Lands = new JSONObject(landsObj.get("3").toString());

        } else if(diferenceLifes > 3){
            JSONObject opt1Lands = new JSONObject(landsObj.get("4").toString());

        } else if(diferenceLifes < 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("2").toString());

        } else{

        }
    }

    public void getAtackChoice(int diferenceLifes){
        // get lands state
        JSONObject landsObj = new JSONObject(json.get("PhaseAtack").toString());

        // selector
        if(diferenceLifes == 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("1").toString());

        } else if(0 < diferenceLifes && diferenceLifes < 3){
            JSONObject opt1Lands = new JSONObject(landsObj.get("2").toString());

        } else if(diferenceLifes > 3){
            JSONObject opt1Lands = new JSONObject(landsObj.get("4").toString());

        } else if(diferenceLifes < 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("3").toString());

        } else{

        }
    }

    public void getDefendChoice(int diferenceLifes){
        // get lands state
        JSONObject landsObj = new JSONObject(json.get("PhaseDefend").toString());

        // selector
        if(diferenceLifes == 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("1").toString());

        } else if(0 > diferenceLifes && diferenceLifes > -3){
            JSONObject opt1Lands = new JSONObject(landsObj.get("3").toString());

        } else if(diferenceLifes < -3){
            JSONObject opt1Lands = new JSONObject(landsObj.get("4").toString());

        } else if(diferenceLifes > 0){
            JSONObject opt1Lands = new JSONObject(landsObj.get("2").toString());

        } else{

        }
    }

}
