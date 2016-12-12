package app.api;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by 23878410v on 28/10/16.
 */
public class ApiController {

    private static final String BASE_URL = "https://api.magicthegathering.io/v1/cards";

    public static ArrayList<Card> GetAllCards(int page, int pageSize){
        try {
            URIBuilder builturi = new URIBuilder(BASE_URL);
            builturi.addParameter("page", ""+page);
            builturi.addParameter("pageSize", ""+pageSize);
            JSONObject json = doCall(builturi.toString());
            if(json != null){
                return parseCards(json);
            }
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Card> GetAllCards(int page, int pageSize, String colors, String rarities){
        try {
            URIBuilder builturi = new URIBuilder(BASE_URL);
            builturi.addParameter("page", ""+page);
            builturi.addParameter("pageSize", ""+pageSize);
            builturi.addParameter("colors", colors);
            builturi.addParameter("rarity", rarities);
            System.out.println(builturi.toString());
            JSONObject json = doCall(builturi.toString());
            if(json != null){
                return parseCards(json);
            }
        }catch(URISyntaxException e){
            e.printStackTrace();
        }
        return null;
    }


    private static JSONObject doCall(String url) {
        try {
            String JsonResponse = HttpUtils.get(url);
            JSONObject json = new JSONObject(JsonResponse);
            if(json.has("status")){
                System.out.println(json.getString("status") + " " + json.getString("message"));
                //throw new ApiControllerException(json.getString("status"),json.getString("message"));
            }
            return json;
        //}catch (ApiControllerException e){
        //    Log.e("ApiControllerException", e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<Card> parseCards(JSONObject json){
        ArrayList<Card> test = new ArrayList<>();
        try {
            JSONArray cards = json.getJSONArray("cards");
            for (int i = 0; i <  cards.length(); i++) {
                test.add(new Card(cards.getJSONObject(i)));
            }
            return test;
        } catch (JSONException e) {
            //Log.e("InitialData::getCards", e.toString());
        } catch (NoSuchFieldException e) {
            //Log.e("InitialData::getCards", e.toString());
        } catch (IllegalAccessException e) {
            //Log.e("InitialData::getCards", e.toString());
        }
        return null;
    }
}
