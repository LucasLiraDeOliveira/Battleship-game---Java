import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int choosePlayer1;
        String assistanceMode, autofill = "";
        boolean assistanceModeStatus = false, autofillStatus = false,
                whosPlayer1 = false;

        Scanner keyboard = new Scanner(System.in);
        Random random = new Random();

        GameMap obj1 = new GameMap();
        GameMap obj2 = new GameMap();


        //to choose if the maps will be autofilled:
        while (!autofillStatus){
            System.out.println("Do you want your map to be autofilled? (Y, N)");
            autofill = (keyboard.next()).toUpperCase();

            if (autofill.equals("Y")){
                obj1.setWhosMap("PC");
                obj2.setWhosMap("PC");

                // Allocating the ships in each map:
                obj1.allocateShip(keyboard, random);
                obj2.allocateShip(keyboard, random);
                autofillStatus = true;
            }
            else if (autofill.equals("N")){
                autofillStatus = true;
            }
            else
                System.out.println("Invalid answer! Please choose between the choices!\n");
        }



        // To choose who's going to start:
        while (!whosPlayer1){
            System.out.println("Who will be player 1? (choose the number in parenteses) \n(1) For " +
                    "User \n(2) For PC");
            choosePlayer1 = keyboard.nextInt();

            if (autofill.equals("N")){
                if (choosePlayer1 == 1) {
                    obj1.setWhosMap("USER");
                    obj2.setWhosMap("PC");

                    obj1.allocateShip(keyboard, random);
                    obj2.allocateShip(keyboard, random);

                    whosPlayer1 = true;
                }
                else if (choosePlayer1 == 2) {
                    obj1.setWhosMap("PC");
                    obj2.setWhosMap("USER");

                    obj1.allocateShip(keyboard, random);
                    obj2.allocateShip(keyboard, random);

                    whosPlayer1 = true;
                } else
                    System.out.println("Invalid answer! Please choose between the choices!\n");
            }
            else {   // so autofill.equals("Y"))
                if (choosePlayer1 == 1) {
                    obj1.setWhosMap("USER");
                    whosPlayer1 = true;
                } else if (choosePlayer1 == 2) {
                    obj2.setWhosMap("USER");
                    whosPlayer1 = true;
                } else
                    System.out.println("Invalid answer! Please choose between the choices!\n");
            }
        }



        // To see with the players will play with assistance mode:
        while (!assistanceModeStatus) {
            System.out.println("Will the players play with the Assistance Mode on? (Y, N)");
            assistanceMode = (keyboard.next()).toUpperCase();

            if (assistanceMode.equals("Y")) {
                obj1.setAssistanceMode("ON");
                obj2.setAssistanceMode("ON");

                assistanceModeStatus = true;
            } else if (assistanceMode.equals("N")) {
                obj1.setAssistanceMode("OFF");
                obj2.setAssistanceMode("OFF");

                assistanceModeStatus = true;
            }
            else
                System.out.println("Invalid value! Please choose one of the alternatives");
        }



//        obj2.setWhosMap("USER");
//        System.out.println("\n\nObj2 battle map:");
//        obj2.showMap(obj2.battleMap);
//        System.out.println("\nObj1 battle map:");
//        obj1.showMap(obj1.battleMap);
        // Loop for the gameplay:
        do {
            obj1.playerTurn(obj2, keyboard, random);

            if (obj2.didILost()){
                System.out.println("\n\nPlayer 1 is the winner!!");
                break;
            }

            obj2.playerTurn(obj1, keyboard, random);

            if (obj1.didILost()){
                System.out.println("\n\nPlayer 2 is the winner!!");
                break;
            }
        }while (true);
    }
}


/*

    GAMEPLAY LOGIC:
    - Ask the user who will start
        - If is the user
            - obj1.setWhosMap("USER")
            - obj2.setWhosMap("PC")
        - If is the pc
            - obj1.setWhosMap("PC")
            - obj2.setWhosMap("USER")



    - Ask the user if he/she will want the assistance mode
        - if yes
            - userOBJ.setAssistanceMode("ON"); // CREATE THIS ATTRIBUTE AND ITS GET AND SET
            METHODS.
        - If not
            - userOBJ.setAssistanceMode("OFF");


    - obj1.allocateShips()
    - obj2.allocateShips()


    - (LOOP FOR REPEATING THE ROUNDS){
        - player 1 turn:
            - Call obj1.playerTurn(GameMap otherPlayer) //in this case otherPlayer will be obj2
                - Ask obj1 which coordinate he/she/it will shoot
                - Call shootBomb(otherPlayer, int x, int y), inside it:
                    - Call isLocationAvailable() and see what this space is
                    - say what the chosen space was
                    - It hit a ship?
                        - If yes
                            - mark in obj2.battleMap[chosenY][chosenX] with the corresponding value;
                            - mark in obj1.opponentMap[chosenY][chosenX] with the corresponding value;
                        - If not
                            - mark in obj1.opponentMap[chosenY][chosenX] with the corresponding value;

            - look in obj2.didILost() to see if all ships have been sunk; //SEE IF THE
                //battleMap STILL HAVE "X" INSIDE IT
                - If yes
                    - Announce the winner
                    - Exit the gameplay loop
                - If not
                    - continue;



        - player 2 turn:
            - Call obj2.playerTurn(GameMap otherPlayer)
                - Ask obj2 which coordinate he/she/it will shoot
                - Call shootBomb(otherPlayer, int x, int y), inside it:
                    - Call isLocationAvailable() and see what this space is
                    - say what the chosen space was
                    - It hit a ship?
                        - If yes
                            - mark in obj1.battleMap[chosenY][chosenX]
                            - mark in obj2.opponentMap[chosenY][chosenX]
                        - If not
                            - mark in obj2.opponentMap[chosenY][chosenX]

            - look in obj1.didILost() to see if all ships have been sunk; //SEE IF THE
                //battleMap STILL HAVE "X" INSIDE IT
                - If yes
                    - Announce the winner
                    - Exit the gameplay loop
                - If not
                    - continue;
    }

 */