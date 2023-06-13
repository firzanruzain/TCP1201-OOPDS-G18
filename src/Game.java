import java.util.*;

public class Game {
    public static Deck deck = new Deck();
    public static Center center = new Center();
    public static Player[] Players = new Player[4];
    public static int[] turns = { 0, 1, 2, 3 };
    public static Scanner scanner = new Scanner(System.in); // Create a Scanner object
    public static int trick = 1;
    public static int turn = 0;
    public static int round = 1;
    public static String leadCard;
    public static HashMap<Integer, Integer> sameSuitCards = new HashMap<Integer, Integer>();
    public static HashMap<String, Integer> playedCards = new HashMap<String, Integer>();

    public static void init() {
        // first lead card
        String firstCard = deck.getFirstCard();
        deck.removeCard(firstCard);
        center.addCard(firstCard);
        leadCard = firstCard;

        // determine first player
        int firstCardR = Card.rank(firstCard);
        HashSet<Integer> player2 = new HashSet<Integer>(Arrays.asList(2, 10, 6));
        HashSet<Integer> player3 = new HashSet<Integer>(Arrays.asList(3, 7, 11));
        HashSet<Integer> player4 = new HashSet<Integer>(Arrays.asList(4, 8, 12));
        if (player2.contains(firstCardR)) {
            turns[0] = 1;
        } else if (player3.contains(firstCardR)) {
            turns[0] = 2;
        } else if (player4.contains(firstCardR)) {
            turns[0] = 3;
        }
        updateTurns();

        // deal out cards
        Deck.dealCard();
    }

    public static void startNewGame() {
        // reset everything
        deck.reset();
        center.reset();
        Arrays.fill(Players, null); // clears players

        // create players
        for (int i = 0; i < 4; i++) {
            Players[i] = new Player(i);
        }

        init();
    }

    public static void updateTurns() {
        for (int i = 1; i < 4; i++) {
            int currentTurn = turns[i - 1] + 1;
            if (currentTurn > 3) {
                currentTurn = currentTurn - 4;
            }
            turns[i] = currentTurn;
        }
    }

    public static void startNewRound() {
        System.out.println("Player" + (Player.emptyId + 1) + " has cleared their cards!");
        System.out.println("Next Round Begins.");
        Player.calculateScores();
        round++;
        trick = 1;
        deck.reset();
        center.reset();
        Player.reset();
        init();
    }

    public static void mainDisp() {
        System.out.println("Trick #" + trick);
        Player.displayCards();
        center.displayCards();
        deck.displayCards();
        Player.displayScores();
        System.out.println(String.format("%-8s", "Turn") + ": Player" + (turns[turn] + 1));
    }

    public static void moveToNextPlayer() {
        turn = turn + 1;
        if (turn == 5) {
            turn = 0;
        }
    }

    public static void command(String command) {
        switch (command) {
            case "s":
                startNewGame();
                break;
            case "x":
                System.exit(turns[turn]);
                break;
            case "d":
                Players[turns[turn]].drawCard();
        }
    }

    public static void playerTurn(int id) {
        Boolean inputNotValid = true;
        String input = "";
        while (inputNotValid) {
            System.out.print("> ");
            input = scanner.nextLine();
            if (input.equals("s") || input.equals("x") || input.equals("d") || input.equals("card")) {
                command(input);
                inputNotValid = false;
            } else if (Card.valid(input) && Card.playable(input)) {
                Players[id].playCard(input);
                moveToNextPlayer();
                inputNotValid = false;
            } else {
                System.out.println("Invalid command/card.");
            }
        }
    }

    public static int getTrickWinnerId() {
        int winnerId = 0;

        ArrayList<Integer> sorted = new ArrayList<Integer>(sameSuitCards.keySet());
        Collections.sort(sorted, Collections.reverseOrder());
        winnerId = sameSuitCards.get(sorted.get(0));

        turn = 0;
        turns[0] = winnerId;
        updateTurns();
        center.reset();
        sameSuitCards.clear();
        playedCards.clear();
        trick++;

        return winnerId;
    }

    public static void playGame() {
        System.out.println("--Game Commands--");
        System.out.println("s --> Start a new game");
        System.out.println("x --> Exit the game");
        System.out.println("d --> Draw cards from deck");
        System.out.println("card --> A card played by the current player.\n");

        // game loop
        while (!Player.maxScore()) {
            // round loop
            while (!Player.emptyCards()) {
                // trick loop
                while (turn < 4) {
                    mainDisp();
                    playerTurn(turns[turn]);
                    if (!Player.emptyCards()) {
                        startNewRound();
                    }
                }
                System.out.println("Player" + (getTrickWinnerId() + 1) + " wins the trick! \n");
            }
            startNewRound();
        }
    }

    public static void main(String[] args) {
        Card.main(args); // init card class
        startNewGame();
        playGame();
    }
}
