import java.util.*;

public class Main {
    public static Player[] Players;
    public static Deck decktest = new Deck();

    public static void init() {
        Players = new Player[4];
        for (int i = 0; i<4; i++){
            Players[i] = new Player(i+1);
        }
    }

    public static void dealCard(String Card, int x) {
        Players[x].addCard(Card);
        decktest.removeCard(Card);
    }

    public static void main(String[] args) {
        init();

        dealCard("s3", 0);
        Players[0].displayCards();
        decktest.displayCards();

    }

}
