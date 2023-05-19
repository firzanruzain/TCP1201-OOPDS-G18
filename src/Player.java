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
}