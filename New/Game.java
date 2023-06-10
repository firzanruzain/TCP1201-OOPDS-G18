package New;

public class Game {
    public static Deck deck = new Deck();
    public static Center center = new Center();

    public static void main(String[] args) {
        deck.resetDeck();
        deck.displayCards();
        // center.addCard("S4");
        center.displayCards();
    }
}
