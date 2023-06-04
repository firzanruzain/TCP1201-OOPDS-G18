import java.util.*;

public class Player {
    int score = 0;
    int id;
    String currentPlayingCard;
    ArrayList<String> Cards = new ArrayList<String>();

    Player(int id) {
        this.id = id;
    }

    void addScore(int x) {
        score = score + x;
    }

    void addCard(String Card) {
        Cards.add(Card);
    }

    void removeCard(String Card) {
        Cards.remove(Card);
    }

    void displayScore() {
        System.out.print("Player" + id + " = " + score);
    }

    void displayCards() {
        System.out.print("Player" + id + ": [");
        for (int i = 0; i < Cards.size(); i++) {
            System.out.print(Cards.get(i));
            if (i < Cards.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    Boolean haveCard(String card) {
        return Cards.contains(card);
    }

    void setPlayingCard(String Card) {
        currentPlayingCard = Card;
    }

    String getPlayingCard() {
        return currentPlayingCard;
    }

    int getId() {
        return id;
    }

    int cardAmount() {
        int card_amount = Cards.size();
        return card_amount;
    }

    void cardScore() {
        this.score = 0;
        for (int i = 0; i < Cards.size(); i++) {
            if (Cards.get(i).equals("dA") || Cards.get(i).equals("cA") || Cards.get(i).equals("hA") || Cards.get(i).equals("sA")) {
                score = score + 1;
            } else if (Cards.get(i).equals("d2") || Cards.get(i).equals("c2") || Cards.get(i).equals("h2") || Cards.get(i).equals("s2")) {
                score = score + 2;
            } else if (Cards.get(i).equals("d3") || Cards.get(i).equals("c3") || Cards.get(i).equals("h3") || Cards.get(i).equals("s3")) {
                score = score + 3;
            } else if (Cards.get(i).equals("d4") || Cards.get(i).equals("c4") || Cards.get(i).equals("h4") || Cards.get(i).equals("s4")) {
                score = score + 4;
            } else if (Cards.get(i).equals("d5") || Cards.get(i).equals("c5") || Cards.get(i).equals("h5") || Cards.get(i).equals("s5")) {
                score = score + 5;
            } else if (Cards.get(i).equals("d6") || Cards.get(i).equals("c6") || Cards.get(i).equals("h6") || Cards.get(i).equals("s6")) {
                score = score + 6;
            } else if (Cards.get(i).equals("d7") || Cards.get(i).equals("c7") || Cards.get(i).equals("h7") || Cards.get(i).equals("s7")) {
                score = score + 7;
            } else if (Cards.get(i).equals("d8") || Cards.get(i).equals("c8") || Cards.get(i).equals("h8") || Cards.get(i).equals("s8")) {
                score = score + 8;
            } else if (Cards.get(i).equals("d9") || Cards.get(i).equals("c9") || Cards.get(i).equals("h9") || Cards.get(i).equals("s9")) {
                score = score + 9;
            } else if (Cards.get(i).equals("d10") || Cards.get(i).equals("c10") || Cards.get(i).equals("h10") || Cards.get(i).equals("s10")) {
                score = score + 10;
            } else if (Cards.get(i).equals("dJ") || Cards.get(i).equals("cJ") || Cards.get(i).equals("hJ") || Cards.get(i).equals("sJ")) {
                score = score + 10;
            } else if (Cards.get(i).equals("dQ") || Cards.get(i).equals("cQ") || Cards.get(i).equals("hQ") || Cards.get(i).equals("sQ")) {
                score = score + 10;
            } else if (Cards.get(i).equals("dK") || Cards.get(i).equals("cK") || Cards.get(i).equals("hK") || Cards.get(i).equals("sK")) {
                score = score + 10;
            }
        }
    }
}
