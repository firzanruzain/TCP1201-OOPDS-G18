import java.util.ArrayList;
import java.util.List;

public class Player {
    private int score = 0;
    private int id;
    private String currentPlayingCard;
    private CardSet cards = new CardSet();
    public static int emptyId = 0;
    Deck deck = Game.deck;

    Player(int x) {
        this.id = x;
        cards.setName("Player " + (x + 1));
    }

    public void playCard(String card) {
        if (Game.center.isEmpty()) {
            Game.leadCard = card;
        }

        this.cards.removeCard(card);
        currentPlayingCard = card;
        Game.center.addCard(card);
        Game.playedCards.put(card, this.id);
        if (Card.sameSuit(card, Game.leadCard)) {
            Game.sameSuitCards.put(Card.rank(card), this.id);
        }
    }

    public void drawCard() {
        if (deck.isEmpty()) {
            System.out.println("Deck is empty");
            System.out.println("Moving to next player");
            Game.moveToNextPlayer();
        } else {
            String card = deck.getFirstCard();
            this.cards.addCard(card);
            Game.deck.removeCard(card);
        }
        currentPlayingCard = null;
    }

    public void displayCard() {
        this.cards.displayCards();
    }

    public Boolean haveCard(String card) {
        return this.cards.cards.contains(card);
    }

    public int getScore() {
        return score;
    }

    public int getId() {
        return id;
    }

    public String getPlayingCard() {
        return currentPlayingCard;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public ArrayList<String> getCards() {
        return this.cards.cards;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCards(List<String> list) {
        this.cards.setCards(list);
    }

    public static void displayCards() {
        for (int i = 0; i < 4; i++) {
            Game.Players[i].displayCard();
        }
    }

    public static void displayScores() {
        System.out.print(String.format("%-8s", "Score") + ": ");
        for (int i = 0; i < 4; i++) {
            int id = Game.Players[i].getId() + 1;
            int score = Game.Players[i].getScore();
            System.out.print("Player" + id + " = " + score);
            if (i != 3) {
                System.out.print(" | ");
            } else {
                System.out.println("");
            }
        }
    }

    public static Boolean emptyCards() {
        Boolean output = false;
        for (int i = 0; i < 4; i++) {
            if (Game.Players[i].cards.getSize() == 0) {
                emptyId = i;
                output = true;
            }
        }
        return output;
    }

    public static Boolean maxScore() {
        Boolean output = false;
        for (int i = 0; i < 4; i++) {
            if (Game.Players[i].score == 100) {
                output = true;
            }
        }
        return output;
    }

    public static void calculateScores() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < Game.Players[i].cards.getSize(); j++) {
                String card = Game.Players[i].cards.getCard(j);
                int cardValue = Card.value(card);
                Game.Players[i].addScore(cardValue);
            }
        }
    }

    public static void reset() {
        for (int i = 0; i < 4; i++) {
            Game.Players[i].cards.clearCards();
        }
    }
}
