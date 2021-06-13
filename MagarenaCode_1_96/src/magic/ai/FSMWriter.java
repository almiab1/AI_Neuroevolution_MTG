package magic.ai;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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
        // System.out.println("JSON Obtained--> " + object.toString());
        
        this.json = object;
    }

    /* ----------------------------------------------------------------
        Manage JSON
       ---------------------------------------------------------------- */
    private void updateJSON(){
        String resourceName = this.nameJSON;
        InputStream is = FSMWriter.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        
        this.json = object;
    }

    public void saveChangesInFile(){
        try {
            Files.write(Paths.get(this.routeJSON), this.json.toString().getBytes(Charset.defaultCharset()));
            updateJSON();
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
        int lastDuelKeyInt = getLastDuelKey() + 1;
        String lastDuelKey = String.valueOf(lastDuelKeyInt);
        JSONArray emptyArray = new JSONArray();
        
        this.json.put(lastDuelKey,emptyArray);
    }
    
    
    public void writeResultsMatches(int diferenceLifes){
    
        String duelKey = String.valueOf(getLastDuelKey());
        
        boolean isWin = false;
        if(diferenceLifes >= 0) isWin = true;
        
        JSONObject matchJson = new JSONObject();
        matchJson.put("Win", isWin);
        matchJson.put("DiferenceLifes", diferenceLifes);

        this.json.getJSONArray(duelKey).put(matchJson);
    }
}