package app.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by 23878410v on 28/10/16.
 */
public class Card {
    private String name;
    private JSONArray names;
    private String manaCost;
    private int cmc;
    private JSONArray colors;
    private String type;
    private JSONArray subtypes;
    private String rarity;
    private String set;
    private String text;
    private String artist;
    private String number;
    private int power;
    private int toughness;
    private String layout;
    private float multiverseid;
    private String imageUrl;
    private JSONArray rulings;
    private JSONArray foreignNames;
    private JSONArray printings;
    private String originalText;
    private String originalType;
    private String id;
    private String flavor;

    public Card(JSONObject jsonObject) throws JSONException, NoSuchFieldException, IllegalAccessException {
        for (Field a: Card.class.getDeclaredFields()) {
            if(jsonObject.has(a.getName())){
                if(a.getType().equals(String.class)){
                    a.set(this, jsonObject.getString(a.getName()));
                }else if(a.getType().equals(Integer.class)){
                    a.set(this, Integer.parseInt(jsonObject.getString(a.getName())));
                }else if(a.getType().equals(JSONArray.class)){
                    a.set(this, jsonObject.getJSONArray(a.getName()));
                }else if(a.getType().equals(Float.class)){
                    a.set(this, Float.parseFloat(jsonObject.getString(a.getName())));
                }
            }
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArray getNames() {
        return names;
    }

    public void setNames(JSONArray names) {
        this.names = names;
    }

    public String getManaCost() {
        return manaCost;
    }

    public void setManaCost(String manaCost) {
        this.manaCost = manaCost;
    }

    public int getCmc() {
        return cmc;
    }

    public void setCmc(int cmc) {
        this.cmc = cmc;
    }

    public JSONArray getColors() {
        return colors;
    }

    public void setColors(JSONArray colors) {
        this.colors = colors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONArray getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(JSONArray subtypes) {
        this.subtypes = subtypes;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getToughness() {
        return toughness;
    }

    public void setToughness(int toughness) {
        this.toughness = toughness;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public float getMultiverseid() {
        return multiverseid;
    }

    public void setMultiverseid(float multiverseid) {
        this.multiverseid = multiverseid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public JSONArray getRulings() {
        return rulings;
    }

    public void setRulings(JSONArray rulings) {
        this.rulings = rulings;
    }

    public JSONArray getForeignNames() {
        return foreignNames;
    }

    public void setForeignNames(JSONArray foreignNames) {
        this.foreignNames = foreignNames;
    }

    public JSONArray getPrintings() {
        return printings;
    }

    public void setPrintings(JSONArray printings) {
        this.printings = printings;
    }

    public String getOriginalText() {
        return originalText;
    }

    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    public String getOriginalType() {
        return originalType;
    }

    public void setOriginalType(String originalType) {
        this.originalType = originalType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }
}
