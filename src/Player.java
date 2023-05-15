import java.util.ArrayList;

public class Player {
    int score = 0;
    int id;

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
        System.out.print("Player" + id + ": ");
        for (int i = 0; i < Cards.size(); i++) {
            System.out.println(Cards.get(i));
        }
    }
}