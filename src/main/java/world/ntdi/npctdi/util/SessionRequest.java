package world.ntdi.npctdi.util;

import net.minecraft.util.Tuple;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class SessionRequest {

    public static String getValue(JSONArray array) {
        for (Object property : array) {
            JSONObject property_obj = (JSONObject) property;
            return (String) property_obj.get("value");
        }
        return null;
    }

    public static String getSig(JSONArray array) {
        for (Object property : array) {
            JSONObject property_obj = (JSONObject) property;
            return (String) property_obj.get("signature");
        }
        return null;
    }

    public static JSONArray getJsonArray(String uuid){
        try {
            String url = "https://sessionserver.mojang.com/session/minecraft/profile/%UUID%?unsigned=false".replace("%UUID%", uuid);

            HttpURLConnection http = minecraftSessionGet(url);

            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject) parse.parse(sb.toString());

            return (JSONArray) data_obj.get("properties");
        } catch (Exception e) {
            return null;
        }

    }

    public static String ignToUUID(String ign) {
        try {
            String url = "https://api.mojang.com/users/profiles/minecraft/%IGN%".replace("%IGN%", ign);

            HttpURLConnection http = minecraftSessionGet(url);

            BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            JSONParser parse = new JSONParser();
            JSONObject data_obj = (JSONObject) parse.parse(sb.toString());

            return (String) data_obj.get("id");
        } catch (Exception e) {
            return null;
        }
    }

    public static HttpURLConnection minecraftSessionGet(String urlUUID) throws IOException, ParseException {
        URL url = new URL(urlUUID);
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("GET");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        return http;
    }
}
