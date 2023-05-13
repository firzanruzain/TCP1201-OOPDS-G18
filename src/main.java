public class Main {
    int x = 5;

    public static void main(String[] args) {
        Player[] Players;
        Players = new Player[4];

        Players[0] = new Player();
        Players[0].addCard("s3");
        System.out.println(Players[0].score);
        Players[0].displayCards();
    }

}
