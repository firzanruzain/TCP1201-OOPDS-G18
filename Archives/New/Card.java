import java.util.*;

public class Card {
    public static ArrayList<String> cardsList = new ArrayList<String>(Arrays.asList(
            "cA", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "cJ", "cQ", "cK",
            "dA", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "d10", "dJ", "dQ", "dK",
            "hA", "h2", "h3", "h4", "h5", "h6", "h7", "h8", "h9", "h10", "hJ", "hQ", "hK",
            "sA", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "sJ", "sQ", "sK"));
    public static HashMap<String, Integer> cardValue = new HashMap<String, Integer>();

    public static int rank(String card) {
        String cardRankS = card.substring(1);
        int cardRank = 0;
        if (cardRankS.equals("J")) {
            cardRank = 11;
        } else if (cardRankS.equals("Q")) {
            cardRank = 12;
        } else if (cardRankS.equals("K")) {
            cardRank = 13;
        } else if (cardRankS.equals("A")) {
            cardRank = 14;
        } else {
            cardRank = Integer.parseInt(cardRankS);
        }
        return cardRank;
    }

    public static String suit(String card) {
        String cardSuit = card.substring(0, 1);
        return cardSuit;
    }

    public static int value(String card) {
        return cardValue.get(card);
    }

    public static Boolean valid(String card) {
        return cardsList.contains(card);
    }

    public static Boolean sameRank(String card1, String card2) {
        return rank(card1) == rank(card2);
    }

    public static Boolean sameSuit(String card1, String card2) {
        return suit(card1).equals(suit(card2));
    }

    public static Boolean playable(String card) {
        int id = Game.turns[Game.turn];
        String leadCard = Game.leadCard;
        Boolean playable = false;
        if (valid(card)) {
            if (Game.Players[id].haveCard(card)) {
                if (Game.center.getSize() > 0) {
                    if (sameRank(card, leadCard) || sameSuit(card, leadCard)) {
                        playable = true;
                    } else {
                        System.out.println("Card aren't the same suit/rank");
                    }
                } else {
                    playable = true;
                }

            } else {
                System.out.println("The player doen't have the card");
            }
        }
        return playable;
    }

    public static void main(String[] args) {
        for (String card : cardsList) {
            int rank = rank(card);
            int value = 0;

            if (rank == 14) {
                value = 1;
            } else if (rank <= 10) {
                value = rank;
            } else {
                value = 10;
            }

            cardValue.put(card, value);
        }
    }

}
