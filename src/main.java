import java.util.*;

public class Main {
    public static Player[] Players = new Player[4];
    public static Deck deck = new Deck();
    public static Center center = new Center();
    public static int[] turns = { 1, 2, 3, 4 };
    public static Scanner scanner = new Scanner(System.in); // Create a Scanner object

    public static void init() {
        for (int i = 0; i < 4; i++) {
            Players[i] = new Player(i + 1);
        }

        deck.shuffleDeck();
        String firstCard = deck.getFirstCard();
        deck.removeCard(firstCard);
        center.addCard(firstCard);
        String cardRank = getCardRank(firstCard);
        if (cardRank.equals("2") || cardRank.equals("6") || cardRank.equals("10")) {
            turns[0] = 2;
        } else if (cardRank.equals("3") || cardRank.equals("7") || cardRank.equals("J")) {
            turns[0] = 3;
        } else if (cardRank.equals("4") || cardRank.equals("8") || cardRank.equals("Q")) {
            turns[0] = 4;
        }
        updateTurns();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                firstCard = deck.getFirstCard();
                Players[j].addCard(firstCard);
                deck.removeCard(firstCard);
            }
        }
    }

    public static void updateTurns() {
        for (int i = 1; i < 4; i++) {
            int currentTurn = turns[i - 1] + 1;
            if (currentTurn > 4) {
                currentTurn = currentTurn - 4;
            }
            turns[i] = currentTurn;
        }
    }

    public static void dealCard(int id) {
        String firstCard = deck.getFirstCard();
        Players[id].addCard(firstCard);
        deck.removeCard(firstCard);
    }

    public static String getCardRank(String Card) {
        String cardRank = Card.substring(1);
        return cardRank;
    }

    public static String getCardSuit(String Card) {
        String cardSuit = Card.substring(0, 1);
        return cardSuit;
    }

    public static Boolean cardsSameRank(String card1, String card2) {
        return getCardRank(card1).equals(getCardRank(card2));
    }

    public static Boolean cardsSameSuit(String card1, String card2) {
        return getCardSuit(card1).equals(getCardSuit(card2));
    }

    public static void mainDisp() {

    }

    public static void playerTurn(int id) {
        id = id - 1;
        Boolean cardValid = false;
        String card = "";
        while (!cardValid) {
            System.out.print("> ");
            card = scanner.nextLine();
            if (Players[id].haveCard(card)) {
                if (cardsSameRank(card, center.getFirstCard()) || cardsSameSuit(card, center.getFirstCard())) {
                    cardValid = true;
                } else {
                    System.out.println("Card is not the same suit or rank as the lead card.");
                }
            } else {
                System.out.println("Card doesn't exist.");
            }
        }
        Players[id].setPlayingCard(card);
        Players[id].removeCard(card);
        center.addCard(card);
    }

    public static void main(String[] args) {
        init();

        for (int i = 0; i < 4; i++) {
            Players[i].displayCards();
        }
        center.displayCards();
        deck.displayCards();
        System.out.println("Turn: Player " + turns[0]);
        System.out.println(Arrays.toString(turns));
        // playerTurn(turns[0]);
    }

}
