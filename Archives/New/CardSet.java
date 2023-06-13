import java.util.ArrayList;

public class CardSet {
    protected ArrayList<String> cards = new ArrayList<String>();
    protected String cardSetName;

    public CardSet() {
    }

    public CardSet(String name) {
        this.cardSetName = name;
    }

    public void displayCards() {
        System.out.print(String.format("%-8s", cardSetName) + ": ");
        System.out.println(this.cards);
    }

    public void removeCard(String card) {
        this.cards.remove(card);
    }

    public void addCard(String card) {
        this.cards.add(card);
    }

    public void clearCards() {
        this.cards.clear();
    }

    public Boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public void setName(String name) {
        this.cardSetName = name;
    }

    public String getFirstCard() {
        return this.cards.get(0);
    }

    public int getSize() {
        return this.cards.size();
    }
}
