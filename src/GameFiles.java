import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GameFiles {
    public static ArrayList<String> fileNames = new ArrayList<String>();
    final public static String rootPath = System.getProperty("user.dir") + "\\SaveFiles\\";
    public static String workingPath;
    public static String folderPath;
    public static String filePath;
    public static String folderName;
    public static File fileNamesfile = new File(rootPath + "fileNames.txt");
    public static File playerfile;
    public static File deckCenterfile;
    public static File gameInfofile;
    public static Player[] Players = Game.Players;
    public static Scanner scanner = new Scanner(System.in); // Create a Scanner object

    public static class player {

        public static void save() {
            int id;
            int score;
            ArrayList<String> cards;

            JSONArray playerList = new JSONArray();

            for (int i = 0; i < 4; i++) {
                id = Players[i].getId();
                score = Players[i].getScore();
                cards = Players[i].getCards();

                JSONObject playerDetails = new JSONObject();
                playerDetails.put("id", id);
                playerDetails.put("score", score);
                playerDetails.put("cards", cards);

                playerList.add(playerDetails);
            }

            saveJSON(playerfile, playerList);
        }

        public static void load() {
            JSONArray playerList = new JSONArray();
            int score;
            List<String> cards;

            playerList = loadJSON(playerfile);

            for (int i = 0; i < 4; i++) {
                JSONObject playerDetail = (JSONObject) playerList.get(i);
                score = (int) (long) playerDetail.get("score");
                String cardString = (String) playerDetail.get("cards").toString();
                cards = stringtoList(cardString);

                Players[i].setScore(score);
                Players[i].setCards(cards);
            }

        }
    }

    public static class deckCenter {

        public static void save() {
            ArrayList<String> cards = new ArrayList<String>();

            JSONArray list = new JSONArray();

            for (int i = 0; i < 2; i++) {
                switch (i) {
                    case 0:
                        cards = Game.deck.cards;
                        break;
                    case 1:
                        cards = Game.center.cards;
                        break;
                }

                JSONObject details = new JSONObject();
                details.put("cards", cards);

                list.add(details);
            }

            saveJSON(deckCenterfile, list);
        }

        public static void load() {
            JSONArray list = new JSONArray();

            list = loadJSON(deckCenterfile);

            for (int i = 0; i < 2; i++) {
                JSONObject details = (JSONObject) list.get(i);
                List<String> cards;

                String cardString = (String) details.get("cards").toString();
                cards = stringtoList(cardString);

                if (i == 0) {
                    Game.deck.setCards(cards);
                } else {
                    Game.center.setCards(cards);
                }
            }
        }
    }

    public static class gameInfo {
        public static int round;
        public static int turn;
        public static int trick;
        public static int firstTurns;
        public static String leadCard;
        public static HashMap<Integer, Integer> sameSuitCards = new HashMap<Integer, Integer>();
        public static HashMap<String, Integer> playedCards = new HashMap<String, Integer>();

        public static void save() {
            round = Game.round;
            turn = Game.turn;
            trick = Game.trick;
            firstTurns = Game.turns[0];
            leadCard = Game.leadCard;
            sameSuitCards = Game.sameSuitCards;
            String sameSuitCardsString = sameSuitCards.entrySet().toString();
            playedCards = Game.playedCards;

            JSONArray gameInfo = new JSONArray();
            JSONObject gameInfoDetails = new JSONObject();

            gameInfoDetails.put("round", round);
            gameInfoDetails.put("turn", turn);
            gameInfoDetails.put("trick", trick);
            gameInfoDetails.put("firstTurns", firstTurns);
            gameInfoDetails.put("leadCard", leadCard);
            gameInfoDetails.put("sameSuitCards", sameSuitCardsString);
            gameInfoDetails.put("playedCards", playedCards);

            gameInfo.add(gameInfoDetails);

            saveJSON(gameInfofile, gameInfo);
        }

        public static void load() {
            JSONArray gameInfo = new JSONArray();
            gameInfo = loadJSON(gameInfofile);
            JSONObject gameInfoDetails = (JSONObject) gameInfo.get(0);

            round = (int) (long) gameInfoDetails.get("round");
            turn = (int) (long) gameInfoDetails.get("turn");
            trick = (int) (long) gameInfoDetails.get("trick");
            firstTurns = (int) (long) gameInfoDetails.get("firstTurns");
            leadCard = (String) gameInfoDetails.get("leadCard").toString();
            String sameSuitCardsString = (String) gameInfoDetails.get("sameSuitCards").toString();
            sameSuitCards = new HashMap<Integer, Integer>();
            sameSuitCardsString = sameSuitCardsString.substring(1, sameSuitCardsString.length() - 1);

            if (sameSuitCardsString.length() != 0) {
                String[] keyValuePairs = sameSuitCardsString.split(",");

                for (String pair : keyValuePairs) {
                    String[] entry = pair.split("=");
                    int key = Integer.parseInt(entry[0].trim());
                    int value = Integer.parseInt(entry[1].trim());
                    sameSuitCards.put(key, value);
                }
            }

            playedCards = (HashMap<String, Integer>) gameInfoDetails.get("playedCards");

            Game.round = round;
            Game.turn = turn;
            Game.trick = trick;
            Game.turns[0] = firstTurns;
            Game.updateTurns();
            Game.leadCard = leadCard;
            Game.sameSuitCards = sameSuitCards;
            Game.playedCards = playedCards;
        }
    }

    public static void saveJSON(File file, JSONArray list) {
        try {
            file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(list.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static JSONArray loadJSON(File file) {
        JSONParser jsonParser = new JSONParser();
        JSONArray list = new JSONArray();

        try (FileReader reader = new FileReader(file)) {
            Object obj = jsonParser.parse(reader);

            list = (JSONArray) obj;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<String> stringtoList(String string) {
        List<String> arrList;

        string = string.replaceAll("^\\[|]$", "");
        arrList = Arrays.asList(string.split(","));

        if (arrList.get(0).length() > 2) {
            for (int i = 0; i < arrList.size(); i++) {
                String card = arrList.get(i);
                card = card.substring(1, card.length() - 1);
                arrList.set(i, card);
            }
        }

        return arrList;
    }

    public static void createFolder() {
        File folder = new File(folderPath);
        folder.mkdir();

        if (!fileNames.contains(folderName)) {
            try {
                FileWriter myWriter = new FileWriter(fileNamesfile, true);
                myWriter.write(folderName + ",");
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            fileNames.add(folderName);
        }

    }

    public static void updatePath(String folderName) {
        folderPath = rootPath + folderName;
        workingPath = folderPath + "\\";

        filePath = workingPath + "player.json";
        playerfile = new File(filePath);

        filePath = workingPath + "deckCenter.json";
        deckCenterfile = new File(filePath);

        filePath = workingPath + "gameInfo.json";
        gameInfofile = new File(filePath);
    }

    public static void updateFilesNames() {
        try {
            Scanner reader = new Scanner(fileNamesfile);
            String names = reader.nextLine();

            String[] namesArr = names.split(",");

            for (String name : namesArr) {
                fileNames.add(name);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void save() {
        Boolean valid = false;
        Boolean folderExist;
        updateFilesNames();

        while (!valid) {
            System.out.println("Enter name to save");
            String input = scanner.nextLine();

            folderExist = fileNames.contains(input);
            if (folderExist) {
                System.out.println("File already exist. Do you want to overwrite? (y/n)");
                String option = scanner.nextLine();

                if (option.equals("y")) {
                    folderName = input;
                    valid = true;
                }
            } else {
                folderName = input;
                valid = true;
            }
        }

        updatePath(folderName);
        createFolder();
        player.save();
        deckCenter.save();
        gameInfo.save();
        System.out.println("Game saved successfully.");
    }

    public static void load() {
        Boolean valid = false;
        updateFilesNames();

        System.out.print("Saved Games: ");
        System.out.println(fileNames);

        while (!valid) {
            System.out.println("Enter the game you want to load: ");
            String input = scanner.nextLine();

            if (fileNames.contains(input)) {
                folderName = input;
                valid = true;
            } else {
                System.out.println("Game does not exist.");
            }
        }

        updatePath(folderName);
        player.load();
        deckCenter.load();
        gameInfo.load();
    }

    public static void main(String[] args) {
        Game.startNewGame();
        load();
        Game.mainDisp();
    }
}
