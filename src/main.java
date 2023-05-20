import java.util.*;

public class Main {
    public static Player[] Players = new Player[4];
    public static Deck deck = new Deck();
    public static Center center = new Center();
    public static int[] turns = { 1, 2, 3, 4 };
    public static Scanner scanner = new Scanner(System.in); // Create a Scanner object
    public static int round = 1;
    public static int turn = 0;

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
        } else if (cardRank.equals("3") || cardRank.equals("7") || cardRank.equals("11")) {
            turns[0] = 3;
        } else if (cardRank.equals("4") || cardRank.equals("8") || cardRank.equals("12")) {
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
        if (cardRank.equals("J")) {
            cardRank = "11";
        } else if (cardRank.equals("Q")) {
            cardRank = "12";
        } else if (cardRank.equals("K")) {
            cardRank = "13";
        } else if (cardRank.equals("A")) {
            cardRank = "14";
        }
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
        int idArray = id - 1;
        Boolean cardValid = false;
        String card = "";
        while (!cardValid) {
            System.out.print("> ");
            card = scanner.nextLine();
            if (Players[idArray].haveCard(card)) {
                if (center.getSize() == 0) {
                    cardValid = true;
                    Players[idArray].setPlayingCard(card);
                    Players[idArray].removeCard(card);
                    center.addCard(card);
                    turn = turn + 1;
                    if (turn == 4) {
                        turn = 0;
                    }

                } else if (cardsSameRank(card, center.getFirstCard()) || cardsSameSuit(card, center.getFirstCard())) {
                    cardValid = true;
                    Players[idArray].setPlayingCard(card);
                    Players[idArray].removeCard(card);
                    center.addCard(card);
                    turn = turn + 1;
                    if (turn == 4) {
                        turn = 0;
                    }

                } else {
                    System.out.println("Card is not the same suit or rank as the lead card.");
                }
            } else {
                System.out.println("Card doesn't exist.");
            }

            System.out.println("\nTrick #" + round);

            for (int i = 0; i < 4; i++) {
                Players[i].displayCards();
            }
            center.displayCards();
            deck.displayCards();
            System.out.println("Turn: Player " + turns[turn]);
            System.out.println(Arrays.toString(turns));
        }
    }

    public static int getWinnerId() {
        int winnerId = 0;
        String leadCard = center.getFirstCard();
        ArrayList<Integer> sameSuit = new ArrayList<Integer>();

        for (int i = 0; i < 4; i++) { // get all players Id with same suit with lead card
            if (cardsSameSuit(Players[i].getPlayingCard(), leadCard)) {
                sameSuit.add(Players[i].getId());
            }
        }

        int highestRankId = 1; // get highestRank in center
        for (int i = 1; i < 4; i++) {
            String currentCard = Players[i].getPlayingCard();
            int currentCardRank = Integer.parseInt(getCardRank(currentCard));
            String prevCard = Players[i - 1].getPlayingCard();
            int prevCardRank = Integer.parseInt(getCardRank(prevCard));
            if (currentCardRank > prevCardRank) {
                highestRankId = Players[i].getId();
            }
        }

        if (sameSuit.size() == 0) { // no one same suit
            winnerId = highestRankId;
        } else if (sameSuit.size() > 1) { // more than one same suit
            int highestRankIdSuit = sameSuit.get(0);
            for (int i = 0; i < sameSuit.size(); i++) {
                int id = sameSuit.get(i);
                String currentCard = Players[id - 1].getPlayingCard();
                int currentCardRank = Integer.parseInt(getCardRank(currentCard));
                String prevCard = Players[highestRankIdSuit - 1].getPlayingCard();
                int prevCardRank = Integer.parseInt(getCardRank(prevCard));
                if (currentCardRank > prevCardRank) {
                    highestRankIdSuit = id;
                }
            }
            winnerId = highestRankIdSuit;
        } else { // only one same suit
            winnerId = sameSuit.get(0);
        }

        turns[0] = winnerId;
        updateTurns();
        return winnerId;
    }

    public static void main(String[] args) {
        Boolean endRound = true;
        System.out.println("--Game Commands--");
        System.out.println("s --> Start a new game");
        System.out.println("x --> Exit the game");
        System.out.println("d --> Draw cards from deck");
        System.out.println("card --> A card played by the current player.\n");

        init();
        while (endRound) {
            turn = 0;
            System.out.println("Trick #" + round);

            for (int i = 0; i < 4; i++) {
                Players[i].displayCards();
            }
            center.displayCards();
            deck.displayCards();
            System.out.println("Turn: Player " + turns[turn]);
            System.out.println(Arrays.toString(turns));
            for (int i = 0; i < 4; i++) {
                playerTurn(turns[i]);
            }
            System.out.println("Player" + getWinnerId() + " wins the trick! \n");
            for (int i = 0; i < 4; i++) {
                if (Players[i].cardAmount() == 0) {
                    System.out.println("Round end");
                    endRound = false;
                } else {
                    continue;
                }
            }
            center.clearCard();
            round++;
        }

    }

}