import java.util.*;

public class Deck extends CardSet {

        public Deck() {
                super("Deck");
                reset();
        }

        public void shuffle() {
                Collections.shuffle(this.cards);
        }

        public void reset() {
                clearCards();
                this.cards.addAll(Card.cardsList);
                shuffle();
        }

        public static void dealCard() {
                for (int i = 0; i < 7; i++) {
                        for (int j = 0; j < 4; j++) {
                                Game.Players[j].drawCard();
                        }
                }
        }

        public Boolean isEmpty() {
                return this.cards.isEmpty();
        }

}