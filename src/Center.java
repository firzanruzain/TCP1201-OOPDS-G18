import java.util.*;

public class Center {
    ArrayList<String> Cards = new ArrayList<String>();

    void addCard(String Card) {
        Cards.add(Card);
    }

    void removeCard(String Card) {
        Cards.remove(Card);
    }

    void displayCards() {
        System.out.print("Center: [");
        for (int i = 0; i < Cards.size(); i++) {
            System.out.print(Cards.get(i));
            if (i < Cards.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}