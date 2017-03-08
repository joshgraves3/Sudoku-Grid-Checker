package com.SudokuChecker;

import java.io.*;
import java.util.*;

/**
 * Created by Josh on 9/29/16.
 */
public class CheckGrid {

    private static int grid[][] = new int[9][9];

    private static void readInGrid(String file) {

        try {
            BufferedReader readFile = new BufferedReader(new FileReader(file));
            String line;
            int row = 0;

            while ((line = readFile.readLine()) != null) {
                String[] vals = line.trim().split(",");

                for (int col = 0; col < 9; col++) {
                    grid[row][col] = Integer.parseInt(vals[col]);
                }

                row++;
            }

        } catch (IOException io) {
            io.printStackTrace();
        }

        //printGrid();

    }

    private static void printGrid() {
        for (int i =0; i < 9; ++i) {
            for (int j =0; j < 9; ++j) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    static private class CheckColumns extends Thread {

        private int columnError = -1;
        private HashSet<Integer> columnSet = new HashSet<>();

        public void run() {
            System.out.println("Checking columns");
            for (int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {

                    if(!columnSet.add(grid[j][i])) {

                        columnError = i;
                        System.out.println("Column thread found failure in column " + (i+1));
                        i = 9;j = 9;
                        columnSet.clear();

                    }

                    if (j == 8) {
                        columnSet.clear();
                    }
                }
            }
        }

        private int getColumnError() {
            return columnError;
        }
    }

    static private class CheckRows extends Thread {

        private int rowError = -1;
        private HashSet<Integer> rowSet = new HashSet<>();

        public void run() {
            System.out.println("Checking rows");
            for (int i = 0; i < 9; ++i) {
                for (int j = 0; j < 9; ++j) {

                    if(!rowSet.add(grid[i][j])) {

                        rowError = i;
                        System.out.println("Row thread found error in row " + (i+1));
                        i = 9;j = 9;
                        rowSet.clear();

                    }

                    if (j == 8) {
                        rowSet.clear();
                    }
                }
            }
        }

        private int getRowError() {
            return rowError;
        }

    }

    private static void subGridCheck(int rowStart, int rowStop, int columnStart, int columnStop, int subGridNum) {

        HashSet<Integer> checkSet = new HashSet<>();

        System.out.println("Checking subgrid " + subGridNum);

        for (int i = rowStart; i < rowStop; ++ i) {
            for (int j = columnStart; j < columnStop; ++j) {

                if (!checkSet.add(grid[i][j]))  {
                    System.out.println("Thread checking subgrid " + subGridNum + " found an error at row: " + (i+1) + ", column: " + (j+1));
                    i = rowStop; j = columnStop;
                    checkSet.clear();
                }
            }
        }
    }

    static private class CheckSubGrid1 extends Thread {

        public void run() {
            subGridCheck(0,3,0,3,1);
        }

    }

    static private class CheckSubGrid2 extends Thread {

        public void run() {
            subGridCheck(0,3,3,6,2);
        }

    }

    static private class CheckSubGrid3 extends Thread {

        public void run() {
            subGridCheck(0,3,6,9,3);
        }

    }

    static private class CheckSubGrid4 extends Thread {

        public void run() {
            subGridCheck(3,6,0,3,4);
        }

    }

    static private class CheckSubGrid5 extends Thread {

        public void run() {
            subGridCheck(3,6,3,6,5);
        }

    }

    static private class CheckSubGrid6 extends Thread {

        public void run() {
            subGridCheck(3,6,6,9,6);
        }

    }

    static private class CheckSubGrid7 extends Thread {

        public void run() {
            subGridCheck(6,9,0,3,7);
        }

    }

    static private class CheckSubGrid8 extends Thread {

        public void run() {
            subGridCheck(6,9,3,6,8);
        }

    }

    static private class CheckSubGrid9 extends Thread {

        public void run() {
            subGridCheck(6,9,6,9,9);
        }

    }

    public static void main(String args[]) {

        readInGrid("/Users/Josh/Google Drive/IdeaProjects/SudokuChecker/src/com/SudokuChecker/grid.csv");

        ArrayList<Thread> threads = new ArrayList<>();

        CheckColumns columnThread = new CheckColumns();
        CheckRows rowThread = new CheckRows();
        CheckSubGrid1 subGrid1 = new CheckSubGrid1();
        CheckSubGrid2 subGrid2 = new CheckSubGrid2();
        CheckSubGrid3 subGrid3 = new CheckSubGrid3();
        CheckSubGrid4 subGrid4 = new CheckSubGrid4();
        CheckSubGrid5 subGrid5 = new CheckSubGrid5();
        CheckSubGrid6 subGrid6 = new CheckSubGrid6();
        CheckSubGrid7 subGrid7 = new CheckSubGrid7();
        CheckSubGrid8 subGrid8 = new CheckSubGrid8();
        CheckSubGrid9 subGrid9 = new CheckSubGrid9();

        threads.add(new Thread(columnThread));
        threads.add(new Thread(rowThread));
        threads.add(new Thread(subGrid1));
        threads.add(new Thread(subGrid2));
        threads.add(new Thread(subGrid3));
        threads.add(new Thread(subGrid4));
        threads.add(new Thread(subGrid5));
        threads.add(new Thread(subGrid6));
        threads.add(new Thread(subGrid7));
        threads.add(new Thread(subGrid8));
        threads.add(new Thread(subGrid9));

        for (Thread t : threads) {
            t.start();
        }

        try {
            for (Thread t : threads) {
                //System.out.println("joining threads");
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int cError = columnThread.getColumnError();

        int rError = rowThread.getRowError();

        if(cError == -1 && rError == -1) {
            System.out.println("There were no errors in the sudoku grid.");
        } else {
            System.out.println("Threads done. The error occured at [" + (rError+1) + ',' + (cError+1) + "].");
        }
    }
}


