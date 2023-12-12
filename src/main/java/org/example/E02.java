package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class E02 {
    public static void main(String[] args) {
        List<Game> games = parseGames();
        round1(games);
        round2(games);
    }

    static void round1(List<Game> games) {
        int valid = 0;
        for (Game game : games) {
            if(game.isValidForLimit(12, 13, 14)) {
                valid += game.id;
            }
        }
        System.out.println(valid);
    }

    static void round2(List<Game> games) {
        int sum = 0;
        for (Game game : games) {
            sum += game.powerOfMinimalSet();
        }
        System.out.println(sum);
    }

    static List<Game> parseGames() {
        List<Game> list = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./input/input2.txt"));

            String line = reader.readLine();
            while (line != null) {
                list.add(new Game(line));
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}

class Game {

    int id;
    List<Round> rounds = new ArrayList<>();

    public Game(String line) {
        String[] parts = line.split(":");
        this.id = Integer.parseInt(parts[0].split(" ")[1]);

        parts = parts[1].split(";");
        for(String round : parts) {
            String[] cubes = round.split(",");
            int r = 0, g = 0, b = 0;
            for(String cube : cubes) {
                String[] p = cube.split(" ");
                switch (p[2]) {
                    case "red":
                        r = Integer.parseInt(p[1]);
                        break;
                    case "green":
                        g = Integer.parseInt(p[1]);
                        break;
                    case "blue":
                        b = Integer.parseInt(p[1]);
                }
            }
            this.rounds.add(new Round(r, g, b));
        }
    }

    public boolean isValidForLimit(int r, int g, int b) {
        for(Round round : rounds) {
            if(round.red > r) {
                return false;
            }
            if(round.green > g) {
                return false;
            }
            if(round.blue > b) {
                return false;
            }
        }
        return true;
    }

    private Integer[] minumumValidSet() {
        int r = 0, g = 0, b = 0;

        for(Round round : rounds) {
            if(round.red > r) {
                r = round.red;
            }
            if(round.green > g) {
                g = round.green;
            }
            if(round.blue > b) {
                b = round.blue;
            }
        }

        Integer[] result = new Integer[3];
        result[0] = r;
        result[1] = g;
        result[2] = b;
        return result;
    }

    public int powerOfMinimalSet() {
        int power = 1;
        for(int min : minumumValidSet()) {
            power *= min;
        }
        return power;
    }
}

class Round {
    int red;
    int green;
    int blue;

    Round(int r, int g, int b) {
        this.red = r;
        this.green = g;
        this.blue = b;
    }
}
