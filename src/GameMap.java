import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class GameMap {
    private final int BOARDLENGTH = 11;
    protected String[][] battleMap = new String[BOARDLENGTH][BOARDLENGTH];
    protected String[][] opponentMap = new String[BOARDLENGTH][BOARDLENGTH];
    private String whosMap; //to se if is a player or a pc map
    private String assistanceMode;
    protected int[] shipHold;
    private int i, j;

    //now will be variables exclusive if this obj will be conducted by the pc:
    int auxIndex;

    // Contructor to generate the object's map right after create the GameMap obj
    public GameMap() {
        setWhosMap(whosMap);
        //filling the coordinates of the battleMap
        for (j = 0; j < BOARDLENGTH; j++) {
            for (i = 0; i < BOARDLENGTH; i++) {
                // The IF and the 1st ELSE IF are to fill the reference coordinates
                // de base
                if (i == 0) {
                    if (j < 10)
                        //battleMap[i][j] = " " + (Integer.toString(j)); if you want the numbers
                        // on the left
                        battleMap[i][j] = (Integer.toString(j)) + " ";
                    else
                        battleMap[i][j] = Integer.toString(j);
                }
                else if (j == 0) {
                    battleMap[i][j] = (Integer.toString(i));
                }

                // To clean the battlefield
                else
                    battleMap[i][j] = " ";
            }
        }

        //filling the coordinates of the opponentMap
        for (j = 0; j < BOARDLENGTH; j++) {
            for (i = 0; i < BOARDLENGTH; i++) {
                // The IF and the 1st ELSE IF are to fill the reference coordinates
                // de base
                if (i == 0) {
                    if (j < 10)
                        //battleMap[i][j] = " " + (Integer.toString(j)); if you want the numbers
                        // on the left
                        opponentMap[i][j] = (Integer.toString(j)) + " ";
                    else
                        opponentMap[i][j] = Integer.toString(j);
                }
                else if (j == 0) {
                    opponentMap[i][j] = (Integer.toString(i));
                }

                // To clean the battlefield
                else
                    opponentMap[i][j] = " ";
            }
        }

        //Now "putting" the ships in the shipStore
        shipHold = new int[]{3, 2, 2, 1, 1, 1};
    }


    //getter and setter parts:
    public String getWhosMap() {
        return whosMap;
    }

    public void setWhosMap(String whosMap) {
        this.whosMap = whosMap;
    }

    public String getAssistanceMode() {
        return assistanceMode;
    }

    public void setAssistanceMode(String assistanceMode) {
        this.assistanceMode = assistanceMode;
    }




    //

    //THE NEXT 5 METHODS ARE PART OF THE ALLOCATESHIPS METHOD:

    // Method to check if still has ship to allocate
    private boolean isShipAvailable(int[] shipAvailable) {
        int i, isAvailable = 0;

        for (i = 0; i < shipAvailable.length; i++) {
            if (shipAvailable[i] > 0)
                isAvailable++;
        }

        if (isAvailable > 0)
            return true;
        else
            return false;
    }


    // Method to show the availables ships
    private void showShips(int[] shipAvailable) {
        int i;

        System.out.println("******************************************************");
        System.out.println("Available Ships:");

        for (i = 0; i < shipHold.length; i++) {
            if (shipAvailable[i] == 0)
                continue;
            else {
                System.out.printf("(%d)", i);
                if (shipAvailable[i] == 3)
                    System.out.println("Aircraft Carrier - 3 spaces size ship");
                if (shipAvailable[i] == 2)
                    System.out.println("Destroyer - 2 spaces size ship");
                if (shipAvailable[i] == 1)
                    System.out.println("Frigrate - 1 space size ship");
            }
        }
    }


    protected int isLocationAvailable(int x, int y) {
        // Return 0 means space available;
        // Return 1 means space has water but, with assistance mode ON, the payer already hit here;
        // Return 2 means space has a ship;
        // Return 3 means space has water (after a part of a ship sunk);
        // Return 4 means space is a Coordinate reference (one of the numbers used for reference);
        // Return 5 means out of Board length.

        // I'll not opt for a switch case because the fourth option wil be "numbers", because this
        // option is for all Coordinate reference
        if (x < BOARDLENGTH || y < BOARDLENGTH){
            if (Objects.equals(battleMap[y][x], " "))
                return 0;
            else if (Objects.equals(battleMap[y][x], "W"))
                return 1;
            else if (Objects.equals(battleMap[y][x], "X"))
                return 2;
            else if (Objects.equals(battleMap[y][x], "O")) {
                return 3;
            } else
                return 4;
        }
        else
            return 5;
    }


    // Method that, considering the direction that the user chose, see if the designated ship can be
    // placed
    private boolean canPlace(int size, String direction, int x, int y) {
        /* the logic:
            - if the ship size is bigger than 1, the priority to look/put is right, if
            its Horizontal, and downward, if its Vertical
            - based on the priority, look if it's available
        */
        int locationCheck;
        locationCheck = isLocationAvailable(x, y);


        /* Two-dimensional arrays work like arrayTest[row][col] and  if we put in a cartesian
            perspective will be like arrayTest[y][x] . Normally I work in a [x][y] perspective,
            so I'll be using [y][x] instead of [x][y] because that's how it works here and in
            markInMap() method:
         */
        if (locationCheck == 0){
            switch (size) {
                case 1:
                    return true;
                case 2:
                    if (Objects.equals(direction, "H")) {
                        if (y + 1 >= BOARDLENGTH){
                            // in case the user put some coordination in the "outer boarder" (Y =
                            // n instead of n-1)
                            if (Objects.equals(battleMap[y - 1][x], " "))
                                return true;
                            else
                                return false;
                        }
                        else {
                            if (Objects.equals(battleMap[y + 1][x], " ")) {
                                return true;
                            }
                            else if (Objects.equals(battleMap[y - 1][x], " ")) {
                                return true;
                            } else
                                return false;
                        }
                    }
                    else if (Objects.equals(direction, "V")) {
                        if (x + 1 >= BOARDLENGTH){
                            if (Objects.equals(battleMap[x - 1][x], " "))
                                return true;
                            else
                                return false;
                        }
                        else {
                            if (Objects.equals(battleMap[y][x + 1], " ")) {
                                return true;
                            }
                            else if (Objects.equals(battleMap[y][x - 1], " ")) {
                                return true;
                            } else
                                return false;
                        }
                    }

                    return false; //in case any criteria is correct
                case 3:
                    if (Objects.equals(direction, "V")) {
                        if (x + 1 >= BOARDLENGTH){
                            if (Objects.equals(battleMap[y][x - 1], " ")) {
                                if (Objects.equals(battleMap[y][x - 2], " "))
                                    return true;
                                else
                                    return false;
                            }
                            else
                                return false;
                        }
                        else {
                            if (Objects.equals(battleMap[y][x + 1], " ")) {
                                if (Objects.equals(battleMap[y][x - 1], " ")) {
                                    return true;
                                }
                                else if (x + 2 >= BOARDLENGTH) {
                                    return false;
                                }
                                else if (Objects.equals(battleMap[y][x+ 2], " ")) {
                                    return true;
                                }
                                else
                                    return false;
                            }
                            else if (Objects.equals(battleMap[y][x - 1], " ")) {
                                if (Objects.equals(battleMap[y][x - 2], " ")) {
                                    return true;
                                } else
                                    return false;
                            }
                            else
                                return false;
                        }
                    }
                    else if (Objects.equals(direction, "H")) {
                        if (y + 1 >= BOARDLENGTH){
                            if (Objects.equals(battleMap[y - 1][x], " ")) {
                                if (Objects.equals(battleMap[y - 2][x], " "))
                                    return true;
                                else
                                    return false;
                            }
                            else
                                return false;
                        }
                        else {
                            if (Objects.equals(battleMap[y + 1][x], " ")) {
                                if (Objects.equals(battleMap[y - 1][x], " ")) {
                                    return true;
                                }
                                else if (y + 2 >= BOARDLENGTH) {
                                    return false;
                                }
                                else if (Objects.equals(battleMap[y + 2][x], " ")) {
                                    return true;
                                }
                                else
                                    return false;
                            }
                            else if (Objects.equals(battleMap[y - 1][x], " ")) {
                                if (Objects.equals(battleMap[y - 2][x], " ")) {
                                    return true;
                                } else
                                    return false;
                            }
                            else
                                return false;
                        }
                    }
                default:
                    return false;
            }
        }
        else
            return false;
    }


    //Method to mark the ship in the map
    private void markInMap(int size, String direction, int x, int y) {
        // I'll not put case if size is one, because in all the cases we'll mark the chosen
        //    place, so to avoid repeating this line e the 3 cases, I'll put here and avoid
        //    to put the case 1

        /*
            I don't know why, if is [row][col] if I want to run into X axis, I should incrise or
              decrease the COL section but when I'm doing it, it's walking through Y axis. I
              tried a lot to make some changes and didn't get anywhere, so, in the end, I just
              gave up and change the "V" section with the "H" section (swipe the letters places)
         */
        battleMap[y][x] = "X";

        switch (size) {
            case 2:
                if (Objects.equals(direction, "V")) {
                    if (x + 1 >= BOARDLENGTH){
                        if (Objects.equals(battleMap[y][x - 1], " "))
                                battleMap[y][x - 1] = "X";
                    }
                    else {
                        if (Objects.equals(battleMap[y][x + 1], " ")) {
                            battleMap[y][x + 1] = "X";
                        } else if (Objects.equals(battleMap[y][x - 1], " ")) {
                            battleMap[y][x - 1] = "X";
                        }
                    }
                }
                else if (Objects.equals(direction, "H")) {
                    if (y + 1 >= BOARDLENGTH){
                        if (Objects.equals(battleMap[y - 1][x], " "))
                                battleMap[y - 1][x] = "X";
                    }
                    else {
                        if (Objects.equals(battleMap[y + 1][x], " ")) {
                            battleMap[y + 1][x] = "X";
                        } else if (Objects.equals(battleMap[y - 1][x], " ")) {
                            battleMap[y - 1][x] = "X";
                        }
                    }
                }
                break;
            case 3:
                if (Objects.equals(direction, "V")) {
                    if (x + 1 >= BOARDLENGTH){
                        if (Objects.equals(battleMap[y][x - 2], " ")) {
                            // we first will check if the ship's length will fit, that's why we'll
                            // be doing x-2 before x-1. we already made this checkup, but we're
                            // being purposefully redundant for security
                            battleMap[y][x - 2] = "X";

                            if (Objects.equals(battleMap[y][x - 1], " "))
                                battleMap[y][x - 1] = "X";
                        }
                        else
                            return;
                    }
                    else{
                        if (Objects.equals(battleMap[y][x + 1], " ")) {
                            battleMap[y][x + 1] = "X";

                            if (Objects.equals(battleMap[y][x - 1], " ")) {
                                battleMap[y][x - 1] = "X";
                            } else if (Objects.equals(battleMap[y][x + 2], " ")) {
                                battleMap[y][x + 2] = "X";
                            }
                        } else if (Objects.equals(battleMap[x][y - 2], " ")) {
                            battleMap[x][y - 2] = "X";
                            if (Objects.equals(battleMap[x][y - 1], " ")) {
                                battleMap[x][y - 1] = "X";
                            }
                        }
                    }
                }
                else if (Objects.equals(direction, "H")) {
                    if (y + 1 >= BOARDLENGTH){
                        if (Objects.equals(battleMap[y - 2][x], " ")) {
                            // we first will check if the ship's length will fit, that's why we'll
                            // be doing x-2 before x-1. we already made this checkup, but we're
                            // being purposefully redundant for security
                            battleMap[y - 2][x] = "X";

                            if (Objects.equals(battleMap[y - 1][x], " "))
                                battleMap[y - 1][x] = "X";
                        }
                        else
                            return;
                    }
                    else {
                        if (Objects.equals(battleMap[y + 1][x], " ")) {
                            battleMap[y + 1][x] = "X";

                            if (Objects.equals(battleMap[y - 1][x], " ")) {
                                battleMap[y - 1][x] = "X";
                            } else if (Objects.equals(battleMap[y + 2][x], " ")) {
                                battleMap[y + 2][x] = "X";
                            }
                        } else if (Objects.equals(battleMap[x - 2][y], " ")) {
                            battleMap[x - 2][y] = "X";
                            if (Objects.equals(battleMap[x - 1][y], " ")) {
                                battleMap[x - 1][y] = "X";
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
    }


    public void allocateShip(Scanner keyboard, Random random) {
        boolean confirmDirection, confirmPlace, confirmIfFit,
                confirmChange;
        int shipChoice, x = 0, y = 0, aux1, aux2, aux3, aux4, changeChoice;
        String shipLocation, wannaChange, shipDirection, verification111;
        String[] coordinate;


        while (isShipAvailable(shipHold)){
            if (Objects.equals(getWhosMap(), "USER")) {
                showShips(shipHold);
                showMap(battleMap);
                System.out.println("Choose a ship from the number in the parenteses!");
                shipChoice = keyboard.nextInt(); //now the user will choose which ship to place
            }
            else {
                do {
                    // print an int number between 0 and (length - 1)
                    auxIndex = random.nextInt(shipHold.length);
                } while(shipHold[auxIndex] == 0);

                shipChoice = auxIndex;
            }


            do {
                confirmDirection = true;

                if (Objects.equals(whosMap, "USER")) {
                    System.out.println("Do you want to place it in a Horizontal or Vertical " +
                            "direction? (H, V)");
                    shipDirection = (keyboard.next()).toUpperCase();
                }
                else {
                    auxIndex = (3 + random.nextInt(2));
                    if (auxIndex % 2 == 0)
                        shipDirection = "H";
                    else
                        shipDirection = "V";
                }

                do {
                    /*
                            RECIVE ORDERED PAIR LOGIC:
                            - recive the ordered pair in a String type;
                            - split every char of the string into some array;
                            - pic each value of this array and convert to a int type
                            - and these 2 new ints and assign to X and Y variables to be in the
                            [][]of the map
                         */

                    confirmPlace = true;

                    if (Objects.equals(whosMap, "USER")) {
                        System.out.println("Where do you want to place the ship? \nPlease put the" +
                                " Ordered Pair way (e.g. 11 or 36 or 92)");
                        shipLocation = keyboard.next();


                        // array to receive each char of the string and attribute to os space of it
                        coordinate = shipLocation.split(""); //if I don't put any parameter it
                        // will separate in every char

                        //We have to verify if the Ordered Pair is composed of 2, 3 or 4 numbers, so
                        // we'll put a switch case to cover all up:
                        switch (shipLocation.length()) {
                            case 2:
                                x = Integer.parseInt(coordinate[1]);
                                y = Integer.parseInt(coordinate[0]);
                                break;
                            case 3:
                                aux1 = Integer.parseInt(coordinate[0]);
                                aux2 = Integer.parseInt(coordinate[1]);
                                aux3 = Integer.parseInt(coordinate[2]);

                                //Conditionals to se if this 1 in the middle is part of X coordinate
                                // or Y coordinate:
                                if (Objects.equals(coordinate[1], "1")) {
                                    if (Objects.equals(coordinate[0], "1")) {
                                        if (Objects.equals(coordinate[2], "1")) {
                                            System.out.println("Do you press it (11, 1) or (1, 11)? " +
                                                    "\nIf it is the first option press F \nIf it is " +
                                                    "the second option press S");
                                            verification111 = (keyboard.next()).toUpperCase();
                                            if (verification111.equals("F")) {
                                                // remembering that we have to attribute the value
                                                // that was supposed to be for X in Y and vice-versa
                                                y = (aux1 * 10) + aux2; //this value was supposed to be
                                                // for X
                                                x = aux3; //this value was supposed to be for Y
                                            }
                                            else if (verification111.equals("S")) {
                                                x = (aux1 * 10) + aux2;
                                                y = aux3;
                                            }
                                        }
                                        else if (Objects.equals(coordinate[2], "0")) {
                                            x = (aux2 * 10) + aux3;
                                            y = aux1;
                                        }
                                    }
                                    else {
                                        x = (aux2 * 10) + aux3;
                                        y = aux1;
                                    }
                                }
                                else {
                                    y = (aux1 * 10) + aux2;
                                    x = aux3;
                                }
                                break;
                            case 4:
                                aux1 = Integer.parseInt(coordinate[0]);
                                aux2 = Integer.parseInt(coordinate[1]);
                                aux3 = Integer.parseInt(coordinate[2]);
                                aux4 = Integer.parseInt(coordinate[3]);

                                y = (aux1 * 10) + aux2;
                                x = (aux3 * 10) + aux4;
                                break;
                        }
                    }
                    else {
                        do {
                            auxIndex = (1 + random.nextInt(BOARDLENGTH));
                        } while (auxIndex >= BOARDLENGTH);
                        x = auxIndex;

                        do {
                            auxIndex = (1 + random.nextInt(BOARDLENGTH));
                        } while (auxIndex >= BOARDLENGTH);
                        y = auxIndex;
                    }

                    confirmIfFit = canPlace(shipHold[shipChoice], shipDirection, x, y);
                    if (confirmIfFit) {
                        confirmChange = true;
                        if (Objects.equals(whosMap, "USER")) {
                            do {
                                System.out.println("This place is available. \nDo you want to change " +
                                        "something? (Y, N)");
                                wannaChange = (keyboard.next()).toUpperCase();

                                if (wannaChange.equals("Y")) {
                                    System.out.println("Choose what you want to change: \n(1) The " +
                                            "location of the ship \n(2) The DIRECTION of the ship \n" +
                                            "(3) The type of ship");
                                    changeChoice = keyboard.nextInt();
                                    switch (changeChoice) {
                                        case 1:
                                            confirmChange = false;
                                            break;
                                        case 2:
                                            confirmPlace = false;
                                            confirmChange = false;
                                            break;
                                        case 3:
                                            confirmDirection = false;
                                            confirmPlace = false;
                                            confirmChange = false;
                                            break;
                                        default:
                                            System.out.println("Invalid option");
                                            break;
                                    }
                                }
                                else if (wannaChange.equals("N")) {
                                    markInMap(shipHold[shipChoice], shipDirection, x, y);
                                    confirmDirection = false;
                                    confirmPlace = false;
                                    confirmChange = false;

                                    shipHold[shipChoice] = 0;
                                }
                            } while (confirmChange);
                        }
                        else {
                            /* //IN CASE OF TEST, TO SEE THE PC COORDINATE, UNWRAP THIS
                            System.out.println("\nPc alocation test:");
                            System.out.printf("shipChoice: %d\nShipDirection: %s\nX: %d\nY: " +
                                            "%d\n\n",
                                    shipChoice, shipDirection, y, x);
                            */
                            markInMap(shipHold[shipChoice], shipDirection, x, y);
                            confirmDirection = false;
                            confirmPlace = false;

                            shipHold[shipChoice] = 0;
                        }

                    } else {
                        System.out.println("Ship doesn't fit in this location. Please choose" +
                                " other coordination point!\\n");
                    }
                } while (confirmPlace);
            } while (confirmDirection);
        }

        System.out.println("All ships ready to battle!");
    }


    public void showMap(String[][] currentMap) {
        for (j = 0; j < BOARDLENGTH; j++) {
            for (i = 0; i < BOARDLENGTH; i++){
                System.out.printf(currentMap[i][j] + "  ");
                }
            System.out.println(" ");
        }
    }




    // Now the methods created during the development of the Gampley part:
    public void playerTurn(GameMap otherPlayer, Scanner keyboard, Random random){
        boolean confirmPlace, confirmChange, xStatus, yStatus;
        String shipLocation2, verification111, wannaChange;
        String[] coordinate;
        int x = 0, y = 0, aux1, aux2, aux3, aux4;


        do {
            confirmPlace = true;

            //picking the coordinate of the attack:
            if (Objects.equals(whosMap, "USER")) {
//                System.out.println("PC map TEST:");   // TEST ! ! !
//                showMap(otherPlayer.battleMap);
                do {
                    System.out.println("\n\n\n\nPlayer's move: \nThis is your currently view of " +
                            "the Opponent's map:");
                    showMap(opponentMap);

                    System.out.println("\nWhere do you want to shoot? \nPlease put the Ordered " +
                            "Pair way (e.g. 11 or 36 or 92)");
                    shipLocation2 = keyboard.next();

                    coordinate = shipLocation2.split(""); //if I don't put any parameter it
                    // will separate in every char

                    //Verifying if it has a number with 1 or 2 digits in X an Y axis:
                    switch (shipLocation2.length()) {
                        case 2:
                            x = Integer.parseInt(coordinate[1]);
                            y = Integer.parseInt(coordinate[0]);
                            break;
                        case 3:
                            aux1 = Integer.parseInt(coordinate[0]);
                            aux2 = Integer.parseInt(coordinate[1]);
                            aux3 = Integer.parseInt(coordinate[2]);

                            //Conditionals to se if this 1 in the middle is part of X coordinate
                            // or Y coordinate:
                            if (Objects.equals(coordinate[1], "1")) {
                                if (Objects.equals(coordinate[0], "1")) {
                                    if (Objects.equals(coordinate[2], "1")) {
                                        System.out.println("Do you press it (11, 1) or (1, 11)? " +
                                                "\nIf it is the first option press F \nIf it is " +
                                                "the second option press S");
                                        verification111 = (keyboard.next()).toUpperCase();
                                        if (verification111.equals("F")) {
                                            // remembering that we have to attribute the value
                                            // that was supposed to be for X in Y and vice-versa
                                            y = (aux1 * 10) + aux2; //this value was supposed to be
                                            // for X
                                            x = aux3; //this value was supposed to be for Y
                                        } else if (verification111.equals("S")) {
                                            x = (aux1 * 10) + aux2;
                                            y = aux3;
                                        }
                                    } else if (Objects.equals(coordinate[2], "0")) {
                                        x = (aux2 * 10) + aux3;
                                        y = aux1;
                                    }
                                } else {
                                    x = (aux2 * 10) + aux3;
                                    y = aux1;
                                }
                            } else {
                                y = (aux1 * 10) + aux2;
                                x = aux3;
                            }
                            break;
                        case 4:
                            aux1 = Integer.parseInt(coordinate[0]);
                            aux2 = Integer.parseInt(coordinate[1]);
                            aux3 = Integer.parseInt(coordinate[2]);
                            aux4 = Integer.parseInt(coordinate[3]);

                            y = (aux1 * 10) + aux2;
                            x = (aux3 * 10) + aux4;
                            break;
                    }

                    //System.out.printf("AXIS TEST\nX: %d\nY: %d", x, y);

                    if (x > 0) {
                        if (x < BOARDLENGTH)
                            xStatus = true;
                        else
                            xStatus = false;
                    }
                    else
                        xStatus = false;

                    if (y > 0) {
                        if (y < BOARDLENGTH)
                            yStatus = true;
                        else
                            yStatus = false;
                    }
                    else
                        yStatus = false;

                } while (!xStatus || !yStatus);
            }
            else {
                do {
                    auxIndex = (1 + random.nextInt(BOARDLENGTH));
                } while (auxIndex >= BOARDLENGTH);
                x = auxIndex;

                do {
                    auxIndex = (1 + random.nextInt(BOARDLENGTH));
                } while (auxIndex >= BOARDLENGTH);
                y = auxIndex;
            }

            confirmChange = true;
            if (Objects.equals(whosMap, "USER")) {
                do {
                    System.out.println("This place is available. \nDo you want to change " +
                            "something? (Y, N)");
                    wannaChange = (keyboard.next()).toUpperCase();

                    if (wannaChange.equals("Y")) {
                        confirmChange = false;
                    }
                    else if (wannaChange.equals("N")) {
                        shootBomb(otherPlayer, x, y);

                        confirmPlace = false;
                        confirmChange = false;
                    }
                } while (confirmChange);
            }
            else {
                System.out.println("\n\n\nOpponent's (PC) move:");
                System.out.printf("Shot coordinate: [%d][%d]\n", y, x);
                shootBomb(otherPlayer, x, y);
                System.out.println("Opponent's view of your Map:");
                showMap(opponentMap);
                confirmPlace = false;
            }
        }while (confirmPlace);
    }



    private void shootBomb(GameMap otherPlayer, int x, int y){
        int shotStatus;
        shotStatus = otherPlayer.isLocationAvailable(x, y);

        switch (shotStatus) {
            case 0:
                System.out.println("You hit water!");
                if (Objects.equals(getAssistanceMode(), "ON"))
                    opponentMap[y][x] = "W";
                break;

            case 1:
                System.out.println("You already hit this place. Here it's water!");
                break;

            case 2:
                System.out.println("You hit a ship!");
                otherPlayer.battleMap[y][x] = "O";
                opponentMap[y][x] = "O";
                break;

            case 3:
                System.out.println("You already hit this place. Here was a ship, now it's water!");
                break;

            default:
                System.out.println("This place is not suitable to shoot!");
                break;

            // I'll not put case 4 and 5 because I already avoid those cases in the part where
            // player choose where to hit
        }
    }

    public boolean didILost(){
        int mapHasX = 0; // this attribute  is to count the number of "X" in the map
        // In the map, the "X" represent the spaces with ships, so if it has any, this player lost

        for (j = 0; j < BOARDLENGTH; j++) {
            for (i = 0; i < BOARDLENGTH; i++){
                if (Objects.equals(battleMap[i][j], "X"))
                    mapHasX++;
            }
        }

        if (mapHasX > 0)
            return false; //so player didn't lost
        else
            return true;
    }
}
