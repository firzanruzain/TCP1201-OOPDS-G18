package New;

public class Center {
    CardSet centerCards = new CardSet();

    void displayCards() {
        System.out.print("Center : [");
        centerCards.displayCards();
    }

    void addCard(String card) {
        centerCards.addCard(card);
    }
}
