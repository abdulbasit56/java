import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BattleShip {
    public static Scanner in = new Scanner(System.in);
    public static Random r = new Random();
    public static String currentPlayerName;
    private static final String LEADERBOARD_FILE = "leaderboard.txt";
    private static final int MAX_ENTRIES = 10;

    private static void displayLeaderboard() {
        List<String[]> entries = readLeaderboard();

        System.out.println("\n\u001B[33m╔════════════ LEADERBOARD ═══════════╗");
        System.out.println("║ Rank  Player           Score       ║");
        System.out.println("╠════════════════════════════════════╣\u001B[0m");

        for (int i = 0; i < entries.size(); i++) {
            String[] entry = entries.get(i);
            System.out.printf("\u001B[33m║\u001B[0m %-5d %-15s %-12s \u001B[33m║%n\u001B[0m",
                    i + 1,
                    entry[0],
                    entry[1]);
        }

        for (int i = entries.size(); i < MAX_ENTRIES; i++) {
            System.out.printf("\u001B[33m║\u001B[0m %-5d %-15s %-12s \u001B[33m║%n\u001B[0m",
                    i + 1,
                    "---",
                    "---");
        }

        System.out.println("\u001B[33m╚════════════════════════════════════╝\u001B[0m\n");
    }

    private static void updateLeaderboard(String playerName, int score) {
        List<String[]> entries = readLeaderboard();

        entries.add(new String[] { playerName, String.valueOf(score) });

        Collections.sort(entries, (a, b) -> Integer.compare(Integer.parseInt(b[1]), Integer.parseInt(a[1])));

        if (entries.size() > MAX_ENTRIES) {
            entries = entries.subList(0, MAX_ENTRIES);
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(LEADERBOARD_FILE))) {
            for (String[] entry : entries) {
                writer.println(entry[0] + "," + entry[1]);
            }
        } catch (IOException e) {
            System.out.println("Error updating leaderboard: " + e.getMessage());
        }
    }

    private static List<String[]> readLeaderboard() {
        List<String[]> entries = new ArrayList<>();
        File file = new File(LEADERBOARD_FILE);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating leaderboard file: " + e.getMessage());
                return entries;
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    entries.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading leaderboard: " + e.getMessage());
        }

        return entries;
    }

    public static void main(String[] args) {
        try {
            
            String n = "PLEASE ENTER YOUR NAME: ";
            SlowWrite(n);
            
            currentPlayerName = in.nextLine();
            
            System.out.println();
            String s = ("WELCOME TO THE BATTLE-SHIP GAME \u001B[33m" + currentPlayerName
            + "\u001B[0m!\nGET READY TO SINK YOUR OPPONENT'S FLEET!\nHERE IS THE LEADEROARD SO FAR.\n");
            SlowWrite(s);
            displayLeaderboard();

            System.out.println();
            String strt1 = "TO START THE GAME, PRESS 1. IF NOT, ENTER ANY OTHER NUMBER TO EXIT.";
            SlowWrite(strt1);
            System.out.println();

            int choice = in.nextInt();
            while (true) {
                if (choice == 1) {
                    String inst = "\u001B[33mINSTRUCTIONS:\u001B[0m\n- The grid is your battlefield.\n- Your mission is to locate and sink all enemy ships.\n- Enter coordinates (e.g.,(2,1)) to attack enemy positions.\n- The game ends when all ships are destroyed.\n- Here is the sample Grid";
                    SlowWrite(inst);

                    System.out.println();
                    Grid();
                    System.out.println();
                    String strt = ("TO START LEVEL 1 PRESS 1. ELSE PRESS ANY KEY TO EXIT.");
                    SlowWrite(strt);
                    int L1choice = in.nextInt();
                    if (L1choice == 1) {

                        Level1();
                    } else {
                        System.out.println("\u001B[31mGAME EXITED WITHOUT PLAYING!\u001B[0m ");
                    }
                    break;
                } else {
                    System.out.println("\u001B[31mGAME EXITED WITHOUT PLAYING!\u001B[0m ");
                    break;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("\u001B[31mGAME EXITED WITHOUT PLAYING!\u001B[0m ");
        }

    }

    // LEVEL # 1

    public static int L1Score = 50;

    public static void Level1() {

        System.out.println();
        String sent = "\u001B[33mTHIS IS LEVEL 1.\u001B[0m\n- Level 1 has a 4x4 grid and a single ship to hit.\n- The Ship's Cell is Randomly generated.\n- Hit the Exact Ship cell to complete Level 1.\n- You have 5 Tries to complete this level.";
        SlowWrite(sent);

        int L1tries = 6;

        int rdm_row = (r.nextInt(4));
        int rdm_col = (r.nextInt(4));

        String[][] array = new String[4][4];
        while (L1tries > 0) {
            try {
                System.out.println("\n");
                Grid();
                String ask = ("\nEnter  Row and Column of the Target Cell(1 to 4)");
                SlowWrite(ask);
                System.out.println("\n");
                int inp_row = (in.nextInt() - 1);
                int inp_col = (in.nextInt() - 1);
                boolean isHit = false;
                if ((inp_row < 0 || inp_row > 4) || (inp_col < 0 || inp_col > 4)) {
                    System.out.println("Index out of Bounds Please Try Again.");
                    continue;
                }

                else {
                    // System.out.println("Your cell: " + inp_row + ", " + inp_col + " Randomly generated: " + rdm_row
                    //         + ", " + rdm_col);

                    populateArray(array);

                    if ((rdm_row == inp_row) && (rdm_col == inp_col)) {
                        array[rdm_row][rdm_col] = "\u001B[31m[ HIT ]\u001B[0m" + "  ";
                        isHit = true;
                    } else {
                        array[rdm_row][rdm_col] = "\u001B[32m[ SHP ]\u001B[0m" + "  ";

                    }

                    if (isHit == true) {
                        if (L1tries == 1) {

                            L1Score *= 2;
                        } else {
                            L1Score *= L1tries;
                        }
                        System.out.println();
                        String s = ("\u001B[31mIts a HIT!\u001B[0m\nCongrats you Sinked Your Enemy's Ship!\nLevel Completed.\nYour Score is "
                                + L1Score + "\nHere is the Distroyed Ship!\n");
                        SlowWrite(s);
                        System.out.println();
                        DisplayGrid(array);
                        System.out.println();
                        String s2 = ("\u001B[32mLEVEL 1 COMPLETED!\u001B[0m");
                        SlowWrite(s2);
                        System.out.println();

                        Level2();
                        return;

                    } else {
                        L1tries--;
                        String miss = "\u001B[33mIts a Miss\u001B[0m, try again.You have \u001B[31m" + L1tries + "\u001B[0m Tries left now.\n";
                        SlowWrite(miss);

                        int hintcount = 2;

                        if (L1tries <= 3 && L1tries > 1 && hintcount > 0) {
                            while (true) {
                                try {
                                    String s = ("\n\u001B[33mIF YOU WANT A HINT PRESS 1.BUT IT'S GONNA COST YOU A TRY! ELSE PRESS ANY NUM TO MOVE ON..\nENTER YOUR CHOICE: \u001B[0m");
                                    SlowWrite(s);
                                    int hint = in.nextInt();
                                    if (hint == 1) {
                                        System.out.println(
                                                "\u001B[33mYOU WANT HINT FOR ROW OR COLUMN? (ENTER 1 FOR ROW, 2 FOR COL)\u001B[0m");
                                        int inp = in.nextInt();
                                        if (inp == 1) {
                                            String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Row is "
                                                    + getRiddleForRow(rdm_row + 1));
                                            SlowWrite(r);
                                            L1tries--;
                                            hintcount--;
                                            break;
                                        } else if (inp == 2) {
                                            String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Column is "
                                                    + getRiddleForColumn(rdm_col + 1));
                                            SlowWrite(r);
                                            L1tries--;
                                            hintcount--;
                                            break;
                                        } else {
                                            System.out.println("Enter a valid input.");
                                            continue;
                                        }

                                    } else {
                                        System.out.println("HINT AVOIDED.");
                                        break;
                                    }

                                } catch (InputMismatchException ex) {
                                    System.out.println("Enter a valid input");
                                    in.nextLine();

                                }

                            }

                        }

                    }

                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Try Again.");
                in.nextLine();

            }
        }

        System.out.println();
        String s = ("\u001B[31mGame Over. Youre out of Tries!\nLEVEL 1 FAILED.\u001B[0m\nYour Score is " + L1Score
                + "\nThe Ship was at: " + (rdm_row + 1) + "," + (rdm_col + 1));
        SlowWrite(s);

        System.out.println();
        DisplayGrid(array);
        System.out.println();
        updateLeaderboard(currentPlayerName, 50);
        displayLeaderboard();
        try {

            String again = "WANNA PLAY AGAIN? JUST PRESS 1.\nNOT INTERESTED ANYMORE? PRESS ANY KEY.";
            SlowWrite(again);
            int n = in.nextInt();
            if (n == 1) {
                in = new Scanner(System.in); 
                main(null);
            } else {
                System.out.println("HAVE A GREAT DAY! COME BACK AGAIN.");
            }
        } catch (InputMismatchException e) {
            System.out.println("HAVE A GREAT DAY! COME BACK AGAIN.");
        }

    }

    // LEVEL # 2

    public static int L2Score = 100;

    public static void Level2() {
        String strt = ("To Start Level 2 Press 1.\nElse Press any key to EXIT.");
        SlowWrite(strt);
        int choice = in.nextInt();
        if (choice == 1) {
            System.out.println("\n");
            String s = ("\u001B[33mTHIS IS LEVEL 2.\u001B[0m");
            SlowWrite(s);
            System.out.println();
            System.out.println();

            String s1 = ("\u001B[33mINSTRUCTIONS:\u001B[0m\n- Level 2 has a 5x5 grid and TWO ships to hit. \n- The Ship is Generated Randomly. \n- Hit the Exact cell to Sink the Ship. \n- You have 10 Tries to complete this level.\n- Here is the Sample Grid");
            SlowWrite(s1);
            String[][] array2 = new String[5][5];
            System.out.println("\n");
            int L2tries = 10;
            int NoOfHits = 0;

            int rdm_row = r.nextInt(5);
            int rdm_col = r.nextInt(5);

            populateArray(array2);
            int hintcount = 4;
            while (L2tries > 0) {
                try {
                    System.out.println("\n");

                    DisplayGrid(array2);

                    String s3 = ("\nEnter  Row and Column of the Target Cell.");
                    SlowWrite(s3);
                    System.out.println("\n");
                    int inp_row = (in.nextInt() - 1);
                    int inp_col = (in.nextInt() - 1);
                    if ((inp_row < 0 || inp_row > 5) || (inp_col < 0 || inp_col > 5)) {
                        System.out.println("Index out of Bounds Please Try Again.");
                        continue;
                    } else {
                        // System.out.println("Your cell: " + inp_row + ", " + inp_col + " Randomly generated: " + rdm_row
                        //         + ", " + rdm_col);
                        boolean isHit = false;

                        if ((rdm_row == inp_row) && (rdm_col == inp_col)) {
                            array2[rdm_row][rdm_col] = "\u001B[31m[ HIT ]\u001B[0m" + "  ";
                            isHit = true;
                            NoOfHits++;

                        }

                        if (isHit == false) {
                            if (L2tries == 1) {

                                array2[rdm_row][rdm_col] = "\u001B[32m[ SHP ]\u001B[0m" + "  ";
                            }

                        }

                        if (isHit) {

                            if (NoOfHits < 2) {

                                String s4 = ("\u001B[31mIts a HIT!\u001B[0m.\nYou Sanked the First ship.\nHere is the Destroyed ship:\n");
                                SlowWrite(s4);
                                System.out.println("\n");

                                DisplayGrid(array2);
                                System.out.println();
                                String s5 = ("\u001B[32mNOW IT'S TIME TO SINK THE OTHER SHIP\u001B[0m");
                                SlowWrite(s5);
                                rdm_row = r.nextInt(5);
                                rdm_col = r.nextInt(5);

                            } else {
                                String s6 = ("\u001B[31mIT'S A HIT!\u001B[0m\nYou Sanked the Second ship.\nYour Score is "
                                        + (L2Score * L2tries + L1Score) + "\n\nHere is the Destroyed ship:\n");
                                SlowWrite(s6);
                                L2Score = L2Score * L2tries + L1Score;
                                System.out.println("\n");
                                DisplayGrid(array2);
                                System.out.println();
                                String s7 = ("\u001B[32mLEVEL 2 COMPLETED!\u001B[0m");
                                SlowWrite(s7);
                                System.out.println();
                                Level3();
                                return;

                            }
                        } else {
                            L2tries--;
                            System.out.println();
                            String miss = "\u001B[33mIt's a MISS.\u001B[0m Try Again\nYou have\u001B[31m " + L2tries + "\u001B[0m Tries left now.";
                            SlowWrite(miss);
                            System.out.println();

                            if (L2tries <= 6 && L2tries > 1) {
                                boolean ship1hit;
                                boolean ship2hit;
                                if (NoOfHits == 0) {

                                    ship1hit = false;
                                } else {
                                    ship1hit = true;
                                }

                                ship2hit = false;
                                while (true) {
                                    try {

                                        if (ship1hit == false && ship2hit == false && hintcount > 2) {

                                            String s8 = "\u001B[33mWANT HINT FOR FIRST SHIP? PRESS 1, BUT IT'S GONNA COST YOU A TRY!\nENTER YOUR CHOICE: \u001B[0m";
                                            SlowWrite(s8);
                                            int hint = in.nextInt();
                                            if (hint == 1) {
                                                System.out.println(
                                                        "\u001B[33mYOU WANT HINT FOR ROW OR COLUMN? (ENTER 1 FOR ROW, 2 FOR COL)\u001B[0m");
                                                int inp = in.nextInt();
                                                if (inp == 1) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Row is "
                                                            + getRiddleForRow(rdm_row + 1));
                                                    SlowWrite(r);
                                                    L2tries--;
                                                    hintcount--;
                                                    break;

                                                } else if (inp == 2) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Column is "
                                                            + getRiddleForColumn(rdm_col + 1));
                                                    SlowWrite(r);
                                                    L2tries--;
                                                    hintcount--;
                                                    break;
                                                } else {
                                                    System.out.println("ENTER A VALID INPUT.");
                                                }

                                            } else {
                                                System.out.println("HINT AVOIDED.");
                                                break;
                                            }
                                        } else if (ship1hit == true && ship2hit == false && hintcount >0) {

                                            String s8 = "\u001B[33mWANT HINT FOR SECOND SHIP? PRESS 1, BUT IT'S GONNA COST YOU A TRY! IF NOT THEN PRESS ANY NUM..\nENTER YOUR CHOICE: \u001B[0m";
                                            SlowWrite(s8);
                                            int hint = in.nextInt();
                                            if (hint == 1) {
                                                System.out.println(
                                                        "\u001B[33mYOU WANT HINT FOR ROW OR COLUMN? (ENTER ROW/COL)");
                                                int inp = in.nextInt();
                                                if (inp == 1) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Row is "
                                                            + getRiddleForRow(rdm_row + 1));
                                                    SlowWrite(r);
                                                    L2tries--;
                                                    break;
                                                } else if (inp == 2) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Column is "
                                                            + getRiddleForColumn(rdm_col + 1));
                                                    SlowWrite(r);
                                                    L2tries--;
                                                    break;
                                                } else {
                                                    System.out.println("ENTER A VALID INPUT");
                                                    continue;
                                                }

                                            } else {
                                                System.out.println("HINT AVOIDED");
                                                break;
                                            }
                                        } else {
                                            break;
                                        }

                                    } catch (InputMismatchException ex) {
                                        System.out.println("Enter a valid Input.");
                                        in.nextLine();
                                    }
                                }

                            }

                        }

                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Enter only Numbers.");
                    in.nextLine();
                }

            }
            System.out.println("\u001B[31mYoure out of Tries!\nLEVEL 2 FAILED.\u001B[0m\nYour Total Score is: "
                    + (L1Score + 100) + "\nThe ship was at\u001B[32m" + (rdm_row + 1) + ", " + (rdm_col + 1)+"\u001B[0m");

            DisplayGrid(array2);
            System.out.println("\n");
            updateLeaderboard(currentPlayerName, L1Score + 100);
            displayLeaderboard();
            try {

                String ex = "WANNA PLAY AGAIN? JUST PRESS 1.\nNOT INTERESTED ANYMORE? PRESS ANY KEY.";
                SlowWrite(ex);
                int n = in.nextInt();
                if (n == 1) {
                    L1Score = 50;
                    in = new Scanner(System.in); 
                    main(null);
                } else {
                    String ex1 = ("HAVE A GREAT DAY! COME BACK AGAIN.");
                    SlowWrite(ex1);
                }
            } catch (InputMismatchException e) {
                String ex2 = "HAVE A GREAT DAY! COME BACK AGAIN.";
                SlowWrite(ex2);
            }

        }

        else {
            updateLeaderboard(currentPlayerName, L1Score);
            displayLeaderboard();
            String ex = ("GAME EXITED AFTER LEVEL 1.");
            SlowWrite(ex);
        }
    }

    // LEVEL # 3

    public static void Level3() {
        System.out.println("\n");
        String s = ("THIS IS THE LAST LEVEL.\nWELLDONE YOU'RE GOING GREAT!\nPRESS 1 TO START LEVEL 3\nElse Enter any Key to EXIT.");
        SlowWrite(s);

        int choice1 = in.nextInt();
        if (choice1 == 1) {
            System.out.println();
            String s1 = ("\u001B[33mTHIS IS LEVEL 3..\nINSTRUCTIONS:\\u001B[0m\n- Level 3 has a 6x6 Grid and 3 Ships to Sink\n- You have only 10 Tries to complete this level.\n- Here is the sample Grid:");
            SlowWrite(s1);
            System.out.println("\n");
            String array3[][] = new String[6][6];

            int L3tries = 12;
            int NoOfHits = 0;
            int L3Score = 150;
            int rdm_row = r.nextInt(5);
            int rdm_col = r.nextInt(5);

            populateArray(array3);
            int hintcount = 6;
            while (L3tries > 0) {

                try {
                    System.out.println();
                    DisplayGrid(array3);
                    System.out.println();

                    String s3 = ("\nEnter  Row and Column of the Target Cell.");
                    SlowWrite(s3);
                    System.out.println();
                    int inp_row = (in.nextInt() - 1);
                    int inp_col = (in.nextInt() - 1);
                    if ((inp_row < 0 || inp_row > 6) || (inp_col < 0 || inp_col > 6)) {
                        System.out.println("Index out of Bounds Please Try Again.");
                        continue;
                    } else {
                        // System.out.println("Your cell: " + inp_row + ", " + inp_col + " Randomly generated: " + rdm_row
                        //         + ", " + rdm_col);
                        boolean isHit = false;

                        if ((rdm_row == inp_row) && (rdm_col == inp_col)) {
                            array3[rdm_row][rdm_col] = "\u001B[31m[ HIT ]\u001B[0m" + "  ";

                            isHit = true;
                            NoOfHits++;

                        }

                        if (isHit == false) {
                            if (L3tries == 1) {

                                array3[rdm_row][rdm_col] = "\u001B[32m[ SHP ]\u001B[0m" + "  ";
                            }

                        }

                        if (isHit) {
                            if (NoOfHits < 3) {

                                String s4 = ("\u001B[31mIT'S A HIT!\u001B[0m\nYou Sanked Ship No. " + NoOfHits
                                        + "\nHere is the Destroyed ship: ");
                                SlowWrite(s4);
                                System.out.println("\n");
                                DisplayGrid(array3);
                                System.out.println();
                                String s5 = ("Now its time to Sink the Ship No. " + (NoOfHits + 1));
                                SlowWrite(s5);
                                rdm_row = r.nextInt(6);
                                rdm_col = r.nextInt(6);

                            } else {
                                String s6 = ("\u001B[31mIT'S A HIT!\u001B[0m\nYOU SANK ALL TREE SHIPS!\nHere is the Destroyed ship:");
                                SlowWrite(s6);
                                System.out.println("\n");
                                DisplayGrid(array3);
                                System.out.println();
                                String s7 = ("\u001B[32mLEVEL 3 COMPLETED!\u001B[0m\nYou have now Completed all Levels!\n\u001b[33mYour Total Score is: "
                                        + (L3Score * L3tries + L2Score + L1Score) + "\u001B[0m\nCongrats You Won! ");
                                SlowWrite(s7);
                                System.out.println();
                                updateLeaderboard(currentPlayerName, L3Score * L3tries + L2Score + L1Score);
                                displayLeaderboard();

                                return;

                            }
                        }

                        else {
                            L3tries--;
                            System.out.println();
                            String s8 = "\u001B[33mIt's a MISS\u001B[0m. Try Again You have \u001B[31m" + L3tries + "\u001B[0m tries left now";
                            SlowWrite(s8);
                            System.out.println("\n");

                            if (L3tries <= 8 && L3tries > 1) {

                                while (true) {
                                    try {

                                        boolean ship1hit = false;
                                        boolean ship2hit = false;
                                        boolean ship3hit = false;
                                        if (NoOfHits == 0) {
                                            ship1hit = true;

                                        } else if (NoOfHits == 1) {
                                            ship2hit = true;
                                        } else {
                                            ship3hit = true;
                                        }
                                        if (ship1hit && hintcount >4) {

                                            String s9 = "\u001B[33mIF YOU WANT A HINT FOR FIRST SHIP PRESS 1.BUT IT'S GONNA COST YOU A TRY! F NOT PRESS ANY NUM TO MOVE ON..\nENTER YOUR CHOICE: \u001B[0m\n";
                                            SlowWrite(s9);
                                            int hint = in.nextInt();
                                            if (hint == 1) {
                                                System.out.println(
                                                        "\u001B[33mYOU WANT HINT FOR ROW OR COLUMN? (ENTER 1 FOR ROW, 2 FOR COL)");
                                                int inp = in.nextInt();
                                                if (inp == 1) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Row is "
                                                            + getRiddleForRow(rdm_row + 1));
                                                    SlowWrite(r);
                                                    L3tries--;
                                                    hintcount--;
                                                    break;
                                                } else if (inp == 2) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Column is "
                                                            + getRiddleForColumn(rdm_col + 1));
                                                    SlowWrite(r);
                                                    L3tries--;
                                                    hintcount--;
                                                    break;
                                                } else {
                                                    System.out.println("Enter a valid input.");
                                                    continue;
                                                }

                                            } else {
                                                System.out.println("HINT AVOIDED");
                                                break;
                                            }
                                        } else if (ship2hit && hintcount > 2) {

                                            String s9 = "\u001B[33mIF YOU WANT A HINT FOR SECOND SHIP PRESS 1.BUT IT'S GONNA COST YOU A TRY! IF NOT PRESS ANY NUM TO MOVE ON..\u001B[0m\n";
                                            SlowWrite(s9);
                                            int hint = in.nextInt();
                                            if (hint == 1) {
                                                System.out.println(
                                                        "\u001B[33mYOU WANT HINT FOR ROW OR COLUMN? (ENTER 1 FOR ROW, 2 FOR COL)");
                                                int inp = in.nextInt();
                                                if (inp == 1) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Row is "
                                                            + getRiddleForRow(rdm_row + 1));
                                                    SlowWrite(r);
                                                    L3tries--;
                                                    hintcount--;
                                                    break;
                                                } else if (inp == 2) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Column is "
                                                            + getRiddleForColumn(rdm_col + 1));
                                                    SlowWrite(r);
                                                    L3tries--;
                                                    hintcount--;
                                                    break;
                                                } else {
                                                    System.out.println("Enter a valid input.");
                                                    continue;
                                                }

                                            } else {
                                                System.out.println("HINT AVOIDED.");
                                                break;
                                            }
                                        } else if (ship3hit && hintcount > 0) {
                                            String s9 = "\u001B[33mIF YOU WANT A HINT FOR THIRD SHIP PRESS 1.BUT IT'S GONNA COST YOU A TRY! IF NOT PRESS ANY NUM TO MOVE ON..\u001B[0m\n";
                                            SlowWrite(s9);
                                            int hint = in.nextInt();
                                            if (hint == 1) {
                                                System.out.println(
                                                        "\u001B[33mYOU WANT HINT FOR ROW OR COLUMN? (ENTER 1 FOR ROW, 2 FOR COL)");
                                                int inp = in.nextInt();
                                                if (inp == 1) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Row is "
                                                            + getRiddleForRow(rdm_row + 1));
                                                    SlowWrite(r);
                                                    L3tries--;
                                                    hintcount--;
                                                    break;
                                                } else if (inp == 2) {
                                                    String r = ("\u001B[32mHINT: \u001B[0mThe Ship's Column is "
                                                            + getRiddleForColumn(rdm_col + 1));
                                                    SlowWrite(r);
                                                    L3tries--;
                                                    hintcount--;
                                                    break;
                                                } else {
                                                    System.out.println("Enter a valid input.");
                                                    continue;
                                                }

                                            } else {
                                                System.out.println("HINT AVOIDED.");
                                                break;
                                            }
                                        } else {
                                            break;
                                        }
                                    } catch (InputMismatchException ex) {
                                        System.out.println("Enter a Valid Input.");
                                        in.nextLine();
                                    }
                                }

                            }

                        }

                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Enter only Numbers.");
                    in.nextLine();
                }

            }
            System.out.println("\u001B[31mYoure out of Tries!\nLEVEL 3 FAILED.\u001B[0m\nYour Total Score is: "
                    + ((L3Score * L3tries + L2Score * L3tries + L1Score)) + "\nThe ship was at " + (rdm_row + 1) + ", "
                    + (rdm_col + 1));
            System.out.println("\n");
            DisplayGrid(array3);
            System.out.println("\n");
            updateLeaderboard(currentPlayerName, 150 + L2Score + L1Score);
            displayLeaderboard();
            try {

                String ex = "WANNA HAVE ANOTHER TRY? JUST PRESS 1.\nNOT INTERESTED ANYMORE? PRESS ANY KEY.";
                SlowWrite(ex);
                int n = in.nextInt();
                if (n == 1) {
                    L1Score = 50;
                    L2Score = 100;
                    in = new Scanner(System.in);
                    main(null);
                } else {
                    String ex1 = ("HAVE A GREAT DAY! COME BACK AGAIN.");
                    SlowWrite(ex1);
                }
            } catch (InputMismatchException e) {
                String ex2 = "HAVE A GREAT DAY! COME BACK AGAIN.";
                SlowWrite(ex2);
            }

        } else {
            updateLeaderboard(currentPlayerName, L2Score);
            displayLeaderboard();
            String ex = ("\u001B[31mGAME EXITED AFTER LEVEL 2.\u0001B[0m");
            SlowWrite(ex);
        }

    }

    // METHODS

    public static void DisplayGrid(String[][] grid) {

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);

            }
            System.out.println();
        }
    }

    public static void Grid() {
        String[][] grid = new String[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                grid[i][j] = "\u001B[33m[ " + (i + 1) + "," + (j + 1) + " ]\u001B[0m" + "  ";
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    public static void populateArray(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = "\u001B[33m[ " + (i + 1) + "," + (j + 1) + " ]\u001B[0m" + "  ";
            }
        }
    }

    public static void SlowWrite(String s) {
        String[] words = s.split(" ");

        for (String word : words) {
            System.out.print(word + " ");
            try {

                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("error");

            }
        }
    }

    public static String getRiddleForRow(int row) {
        switch (row) {
            case 1:
                return "The loneliest number.";
            case 2:
                return "The number of eyes most people have.";
            case 3:
                return "The sides of a perfect triangle.";
            case 4:
                return "The number of legs on a chair.";
            case 5:
                return "Fingers on one hand.";
            case 6:
                return "The number of legs on an insect.";
            default:
                return "A mystery beyond this game.";
        }
    }

    // Method to generate riddles for columns
    public static String getRiddleForColumn(int col) {
        switch (col) {
            case 1:
                return "The first number.";
            case 2:
                return "A pair of swans.";
            case 3:
                return "A triangle's sides.";
            case 4:
                return "The wheels on most cars.";
            case 5:
                return "Half a decade.";
            case 6:
                return "The number of faces on a die.";
            default:
                return "A number yet unknown.";
        }
    }

}
