package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class E06 {

    public static void main(String[] args) {
        parseInput();
        part1();
        part2();
    }

    private static void part1() {
    }

    private static void part2() {
    }



    private static void parseInput() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./input/5.txt"));

            String line = reader.readLine();
            while(line != null) {



                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
