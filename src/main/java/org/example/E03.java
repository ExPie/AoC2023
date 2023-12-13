package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class E03 {
    private static char[][] grid;
    private final static Integer DIM = 140;

    public static void main(String[] args) {
        parseInput();
        part1();
        part2();
    }

    private static void part1() {
        int sum = 0;
        for(int j = 0; j < DIM; j++) {
            for(int i = 0; i < DIM; i++) {
                char symbol = grid[j][i];
                if (!Character.isDigit(symbol) && symbol != '.') {
                    sum += sumNumAroundSymbol(j, i);
                }
            }
        }
        System.out.println(sum);
    }

    private static void part2() {
        int sum = 0;
        for(int j = 0; j < DIM; j++) {
            for(int i = 0; i < DIM; i++) {
                char symbol = grid[j][i];
                if (symbol == '*') {
                    sum += getGearRatioIfIsGear(j, i);
                }
            }
        }
        System.out.println(sum);
    }

    private static int getGearRatioIfIsGear(int j, int i) {
        List<Integer> adjNums = new LinkedList<>();

        // Order of checking symbols
        // 2 1 0
        // 3 s 4
        // 7 6 5

        if(j != 0 && i + 1 < DIM && Character.isDigit(grid[j-1][i+1]) && !Character.isDigit(grid[j-1][i])) {
            adjNums.add(readNumAtPos(j - 1, i + 1));
        }
        if(j != 0 && Character.isDigit(grid[j-1][i]) && !(i - 1 >= 0 && Character.isDigit(grid[j-1][i-1]))) {
            adjNums.add(readNumAtPos(j - 1, i));
        }
        if(j != 0 && i - 1 >= 0 && Character.isDigit(grid[j-1][i-1])) {
            adjNums.add(readNumAtPos(j - 1, i - 1));
        }
        if(i - 1 >= 0 && Character.isDigit(grid[j][i-1])){
            adjNums.add(readNumAtPos(j, i - 1));
        }
        if(i + 1 < DIM && Character.isDigit(grid[j][i+1])){
            adjNums.add(readNumAtPos(j, i + 1));
        }
        if(j + 1 < DIM && i + 1 < DIM && Character.isDigit(grid[j+1][i+1]) && !Character.isDigit(grid[j+1][i])) {
            adjNums.add(readNumAtPos(j + 1, i + 1));
        }
        if(j + 1 < DIM && Character.isDigit(grid[j+1][i]) && !(i - 1 >= 0 && Character.isDigit(grid[j+1][i-1]))) {
            adjNums.add(readNumAtPos(j + 1, i));
        }
        if(j + 1 < DIM && i - 1 >= 0 && Character.isDigit(grid[j+1][i-1])) {
            adjNums.add(readNumAtPos(j + 1, i - 1));
        }

        return adjNums.size() == 2 ? adjNums.get(0) * adjNums.get(1) : 0;
    }

    private static int sumNumAroundSymbol(int j, int i) {
        int sum = 0;

        // Order of checking symbols
        // 2 1 0
        // 3 s 4
        // 7 6 5

        if(j != 0 && i + 1 < DIM && Character.isDigit(grid[j-1][i+1]) && !Character.isDigit(grid[j-1][i])) {
            sum += readNumAtPos(j - 1, i + 1);
        }
        if(j != 0 && Character.isDigit(grid[j-1][i]) && !(i - 1 >= 0 && Character.isDigit(grid[j-1][i-1]))) {
            sum += readNumAtPos(j - 1, i);
        }
        if(j != 0 && i - 1 >= 0 && Character.isDigit(grid[j-1][i-1])) {
            sum += readNumAtPos(j - 1, i - 1);
        }
        if(i - 1 >= 0 && Character.isDigit(grid[j][i-1])){
            sum += readNumAtPos(j, i - 1);
        }
        if(i + 1 < DIM && Character.isDigit(grid[j][i+1])){
            sum += readNumAtPos(j, i + 1);
        }
        if(j + 1 < DIM && i + 1 < DIM && Character.isDigit(grid[j+1][i+1]) && !Character.isDigit(grid[j+1][i])) {
            sum += readNumAtPos(j + 1, i + 1);
        }
        if(j + 1 < DIM && Character.isDigit(grid[j+1][i]) && !(i - 1 >= 0 && Character.isDigit(grid[j+1][i-1]))) {
            sum += readNumAtPos(j + 1, i);
        }
        if(j + 1 < DIM && i - 1 >= 0 && Character.isDigit(grid[j+1][i-1])) {
            sum += readNumAtPos(j + 1, i - 1);
        }

        return sum;
    }

    private static int readNumAtPos(int j, int i) {
        StringBuilder sb = new StringBuilder();

        for(; i >= 0 && Character.isDigit(grid[j][i]); i--);
        i++;

        for(int k = i; k < DIM; k++) {
            char atPos = grid[j][k];
            if(!Character.isDigit(atPos)) {
                break;
            }
            sb.append(atPos);
        }
        return Integer.parseInt(sb.toString());
    }

    private static void parseInput() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./input/3.txt"));

            String line = reader.readLine();
            E03.grid = new char[DIM][line.length()];
            for(int j = 0;line != null; j++) {
                for(int i = 0; i < line.length(); i++) {
                    E03.grid[j][i] = line.charAt(i);
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
