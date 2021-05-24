package magic.ai;

import java.io.FileReader;
import java.io.InputStream;
import org.json.*;

public class FSM_Data {
    
    public JSONObject getJSON(String path) {
        String resourceName = "./FSM_Data.json";
        InputStream is = FSM_Data.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new NullPointerException("Cannot find resource file " + resourceName);
        }

        JSONTokener tokener = new JSONTokener(is);
        JSONObject object = new JSONObject(tokener);
        System.out.println("JSON --> " + object.toString());

        return object;
    }
}
