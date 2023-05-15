import java.util.*;

public class Deck {

        // Array of 52 cards
        ArrayList<String> deckCard = new ArrayList<String>(Arrays.asList(
                        "cA", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "cJ", "cQ", "cK",
                        "dA", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "d10", "dJ", "dQ", "dK",
                        "hA", "h2", "h3", "h4", "h5", "h6", "h7", "h8", "h9", "h10", "hJ", "hQ", "hK",
                        "sA", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "sJ", "sQ", "sK"));

        void displayCards() {
                System.out.print("Deck: [");
                for (int i = 0; i < deckCard.size(); i++) {
                        System.out.print(deckCard.get(i));
                        if (i < deckCard.size() - 1) {
                                System.out.print(", ");
                        }
                }
                System.out.println("]");
        }

        void removeCard(String card) {
                deckCard.remove(card);
        }

        void shuffleDeck() {
                Collections.shuffle(deckCard);
        }

        String getFirstCard() {
                return deckCard.get(0);
        }
}
