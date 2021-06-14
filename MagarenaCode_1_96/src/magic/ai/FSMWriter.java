package magic.ai;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

public class FSMWriter {
    
    private JSONObject json;
    private String nameJSON = "FSMPlaysResults.json"; // path json
    private String routeJSON = "../resources/magic/ai/"+this.nameJSON;
    
    // Contructor
    public FSMWriter(){
        String resourceName = this.nameJSON;
        InputStream is = FSMWriter.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        System.out.println("JSON Obtained--> " + object.toString());
        
        this.json = object;
    }

    /* ----------------------------------------------------------------
        Manage JSON
       ---------------------------------------------------------------- */
    public void updateJSON(){
        String resourceName = this.nameJSON;
        InputStream is = FSMWriter.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        // System.out.println("JSON state update --------------" + '\n' + "JSON state --> " +  this.json.toString());
        this.json = object;
        // System.out.println("JSON state updated +++++++++++++" + '\n' + "JSON state --> " +  this.json.toString());
    }

    public void saveChangesInFile(){
        try {
            Files.write(Paths.get(this.routeJSON), this.json.toString().getBytes(Charset.defaultCharset()));
        } catch (IOException ex) {
            Logger.getLogger(FSMWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        return this.nameJSON;
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
      this.nameJSON = newPath;
    }

    /* ----------------------------------------------------------------
        Write in JSON
       ---------------------------------------------------------------- */
    private int getLastDuelKey(){
        int last = 0;
        for (Object key : json.keySet()) {
            //based on key types
            String keyStr = (String) key;

            int intKey = Integer.valueOf(keyStr);
            if (intKey > last) last = intKey;            
        }
        
        return last;
    }

    public void writeDuel(){
        int lastDuelKeyInt = getLastDuelKey() + 1; // get the new duel key
        String lastDuelKey = String.valueOf(lastDuelKeyInt); // convert to String
        JSONArray emptyArray = new JSONArray(); // init matches json array
        
        this.json.put(lastDuelKey,emptyArray); // add duel to general json
    }
    
    
    public void writeResultsMatches(int diferenceLifes){
    
        String duelKey = String.valueOf(getLastDuelKey()); // get our duel
        
        // Determine if AI won
        boolean isWin = false;
        if(diferenceLifes >= 0) isWin = true;
        
        JSONObject matchJson = new JSONObject(); // create json match
        matchJson.put("Win", isWin); // introduce data to json
        matchJson.put("DiferenceLifes", diferenceLifes);

        this.json.getJSONArray(duelKey).put(matchJson); // add match to duel json array
    }
}