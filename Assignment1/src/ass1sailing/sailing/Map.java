package ass1sailing.sailing;

import java.awt.Dimension;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ass1sailing.sailing.objects.Island;
import ass1sailing.sailing.objects.Merchant;
import ass1sailing.sailing.objects.Pirate;

import ass1sailing.org.json.JSONArray;
import ass1sailing.org.json.JSONTokener;
import ass1sailing.org.json.JSONObject;


/**
 * COMMENT: Comment Map 
 *
 * @author malcolmr
 */
public class Map {

    private double[] mySize;
    private Pirate myPlayer;
    private List<Island> myIslands;
    private List<Merchant> myMerchants;
    
    public Map(double width, double height) {
        mySize = new double[2];
        mySize[0] = width;
        mySize[1] = height;
        myIslands = new ArrayList<Island>();
        myMerchants = new ArrayList<Merchant>();
        myPlayer = null;
    }

    public double[] size() {
        return mySize;
    }
    
    public void setSize(double width, double height) {
        mySize[0] = width;
        mySize[1] = height;
    }
    
    public Pirate player() {
        return myPlayer;
    }
    
    public List<Island> islands() {
        return myIslands;
    }

    public void addIsland(Island island) {
        myIslands.add(island);
    }
    
    public List<Merchant> merchants() {
        return myMerchants;
    }

    public void addMerchant(Merchant merchant) {
        myMerchants.add(merchant);
    }
    
    static public Map read(InputStream in) {
        
        JSONTokener jtk = new JSONTokener(in);
        JSONObject jsonMap = new JSONObject(jtk);

        double width = jsonMap.getDouble("width");
        double height = jsonMap.getDouble("height");
        Map map = new Map(width, height);

        JSONObject jsonPlayer = jsonMap.getJSONObject("player");
        map.myPlayer = Pirate.fromJSON(jsonPlayer);

        JSONArray jsonIslands = jsonMap.getJSONArray("islands");
        for (int i = 0; i < jsonIslands.length(); i++) {
            Island island = Island.fromJSON(jsonIslands.getJSONObject(i));
            map.addIsland(island);
        }
        
        JSONArray jsonMerchants = jsonMap.getJSONArray("merchants");
        for (int i = 0; i < jsonMerchants.length(); i++) {
            Merchant merchant = Merchant.fromJSON(jsonMerchants.getJSONObject(i));
            map.addMerchant(merchant);
        }
        
        return map;
    }

}
