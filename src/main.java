import java.util.*;

public class Main {
    public static Player[] Players = new Player[4];
    public static Deck deck = new Deck();
    public static Center center = new Center();
    public static int[] turns = { 1, 2, 3, 4 };

    public static void init() {
        for (int i = 0; i < 4; i++) {
            Players[i] = new Player(i + 1);
        }

        deck.shuffleDeck();
        String firstCard = deck.getFirstCard();
        deck.removeCard(firstCard);
        center.addCard(firstCard);
        String cardRank = firstCard.substring(1);
        if (cardRank.equals("2") || cardRank.equals("6") || cardRank.equals("10")) {
            turns[0] = 2;
        } else if (cardRank.equals("3") || cardRank.equals("7") || cardRank.equals("J")) {
            turns[0] = 3;
        } else if (cardRank.equals("4") || cardRank.equals("8") || cardRank.equals("Q")) {
            turns[0] = 4;
        }
        updateTurns();

        for (int i = 0; i<7; i++){
            for (int j = 0; j<4; j++){
                firstCard = deck.getFirstCard();
                Players[j].addCard(firstCard);
                deck.removeCard(firstCard);
            }
        }
    }

    public static void updateTurns() {
        for (int i = 1; i < 4; i++) {
            turns[i] = turns[i - 1] + 1;
        }
    }

    public static void dealCard(int id) {
        String firstCard = deck.getFirstCard();
        Players[id].addCard(firstCard);
        deck.removeCard(firstCard);
    }

    public static void mainDisp(){

    }
    public static void main(String[] args) {
        init();

        for (int i = 0; i<4; i++){
            Players[i].displayCards();
        }
        center.displayCards();
        deck.displayCards();
        System.out.println("Turns: " + Arrays.toString(turns));

    }

}
