package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class E04 {
    private static final List<Card> cards = new LinkedList<>();

    public static void main(String[] args) {
        parseInput();
        part1();
        part2();
    }

    private static void part1() {
        int total = 0;
        for(Card card : cards) {
            total += card.cardScore();
        }
        System.out.println(total);
    }

    private static void part2() {
        int total = 0;
        for(Card card : cards) {
            total += solve(card);
        }
        System.out.println(total);
    }

    private static int solve(Card card) {
        int total = 1;

        for(int i = card.id; i < card.id + card.numWinningNumbers(); i++) {
            total += solve(cards.get(i));
        }

        return total;
    }

    private static void parseInput() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./input/4.txt"));

            String line = reader.readLine();
            while(line != null) {
                cards.add(new Card(line));
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class Card {
        int id;
        List<Integer> winning = new LinkedList<>();
        Set<Integer> numbers = new HashSet<>();

        public Card(String line) {
            String[] parts = line.split(":");
            String[] idString = parts[0].split(" ");
            id = Integer.parseInt(idString[idString.length - 1]);

            parts = parts[1].split("\\|");
            for (String num : parts[0].split(" ")) {
                if(num.isBlank()) continue;
                winning.add(Integer.parseInt(num));
            }
            for (String num : parts[1].split(" ")) {
                if(num.isBlank()) continue;
                numbers.add(Integer.parseInt(num));
            }
        }

        public int numWinningNumbers() {
            int numWinning = 0;
            for(int winning : this.winning) {
                if(this.numbers.contains(winning)) {
                    numWinning++;
                }
            }
            return numWinning;
        }

        public int cardScore() {
            int score = 0;
            for(int winning : this.winning) {
                if(this.numbers.contains(winning)) {
                    if(score == 0) {
                        score++;
                    } else {
                        score *= 2;
                    }
                }
            }
            return score;
        }
    }
}
