import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class GameFiles {
    public static ArrayList<String> fileNames = new ArrayList<String>();
    public static String savePath = System.getProperty("user.dir") + "\\SaveFiles\\";
    public static String workingPath;

    public static void createFolder(String folderName) {
        String folderPath = savePath + folderName;
        File folder = new File(folderPath);
        folder.mkdir();
        workingPath = folderPath + "\\";
    }

    public static void main(String[] args) {
        createFolder("test");

        int id = 0;
        int score = 0;
        ArrayList<String> cards = new ArrayList<String>();
        cards.add("h5");
        cards.add("h6");
        String currentPlayingCard = "h4";

        JSONObject playerDetails = new JSONObject();
        playerDetails.put("id", id);
        playerDetails.put("score", score);
        playerDetails.put("cards", cards);
        playerDetails.put("currentPlayingCard", currentPlayingCard);

        JSONObject playerDetails2 = new JSONObject();
        playerDetails2.put("id", id);
        playerDetails2.put("score", score);
        playerDetails2.put("cards", cards);
        playerDetails2.put("currentPlayingCard", currentPlayingCard);

        JSONArray list = new JSONArray();
        list.add(playerDetails);
        list.add(playerDetails2);

        try {
            File playerFile = new File(workingPath + "player.json");
            playerFile.createNewFile();
            FileWriter file = new FileWriter(playerFile);
            file.write(list.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
