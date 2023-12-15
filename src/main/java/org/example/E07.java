package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class E07 {

    private static final List<Hand> hands = new LinkedList<>();
    private static final Map<Character, Integer> scoreTable = new HashMap<>();
    private static final Map<Character, Integer> scoreTableJolly = new HashMap<>();
    public static void main(String[] args) {
        prepare();
        parseInput();
        part1();
        part2();

        Hand h1 = new Hand("A4J4Q", 1);
        Hand h2 = new Hand("J2AQA", 1);
        h1.compareToJolly(h2);
    }

    private static void part1() {
        int sum = 0;

        hands.sort(Hand::compareTo);
        for(int i = 0; i < hands.size(); i++) {
            Hand hand = hands.get(i);
            sum += hand.bid * (i + 1);
        }

        System.out.println(sum);
    }

    private static void part2() {
        int sum = 0;

        hands.sort(Hand::compareToJolly);
        for(int i = 0; i < hands.size(); i++) {
            Hand hand = hands.get(i);
            if(hand.cards.contains("J"))
                System.out.println("Hand " + hand.cards + "; normal " + hand.handClass(hand.cardOrder) + ", jolly " + hand.handClass(hand.cardOrderJolly));
            sum += hand.bid * (i + 1);
        }

        System.out.println(sum);
    }



    private static void parseInput() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./input/7.txt"));

            String line = reader.readLine();
            while(line != null) {
                String[] parts = line.split(" ");
                hands.add(new Hand(parts[0], Integer.parseInt(parts[1])));
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void prepare() {
        String highToLow = "AKQJT98765432";
        int len = highToLow.length();
        for(int i = 0; i < len; i++) {
            scoreTable.put(highToLow.charAt(i), len - i - 1);
        }

        highToLow = "AKQT98765432J";
        len = highToLow.length();
        for(int i = 0; i < len; i++) {
            scoreTableJolly.put(highToLow.charAt(i), len - i - 1);
        }
    }

    private static class Hand implements Comparable<Hand> {
        String cards;
        int bid;
        private Map<Character, Integer> map = null;
        private Map<Integer, Integer> cardOrder = null;
        private Map<Integer, Integer> cardOrderJolly = null;

        public Hand(String cards, int bid) {
            this.cards = cards;
            this.bid = bid;
            makeMap();
        }

        @Override
        public int compareTo(Hand o) {
            int diff = this.handClass(this.cardOrder) - o.handClass(o.cardOrder);
            if(diff != 0) {
                return diff;
            }

            for(int i = 0; i < cards.length(); i++) {
                char me = this.cards.charAt(i);
                char you = o.cards.charAt(i);

                diff = scoreTable.get(me) - scoreTable.get(you);
                if(diff != 0) {
                    return diff;
                }
            }

            return 0;
        }

        public int compareToJolly(Hand o) {
            int diff = this.handClass(this.cardOrderJolly) - o.handClass(o.cardOrderJolly);
            if(diff != 0) {
                return diff;
            }

            for(int i = 0; i < cards.length(); i++) {
                char me = this.cards.charAt(i);
                char you = o.cards.charAt(i);

                diff = scoreTableJolly.get(me) - scoreTableJolly.get(you);
                if(diff != 0) {
                    return diff;
                }
            }

            return 0;
        }

        public int handClass(Map<Integer, Integer> cOrder) {
            if(cOrder.get(5) != null) {
                return 6;
            }

            if(cOrder.get(4) != null) {
                return 5;
            }

            if(cOrder.get(3) != null) {
                if(cOrder.get(2) != null) {
                    return 4;
                }
                return 3;
            }

            if(cOrder.get(2) != null) {
                return cOrder.get(2);
            }

            return 0;
        }

        private void makeMap() {
            map = new HashMap<>();
            for(Character c : cards.toCharArray()) {
                map.putIfAbsent(c, 0);
                int val = map.get(c);
                map.put(c, val + 1);
            }
            cardOrder = new TreeMap<>((o1, o2) -> o2 - o1);
            for(int value : map.values()) {
                cardOrder.putIfAbsent(value, 0);
                int val = cardOrder.get(value);
                cardOrder.put(value, val + 1);
            }

            cardOrderJolly = new TreeMap<>((o1, o2) -> o2 - o1);
            char bestCardToAdd = findHighestMostRepeatedCard();
            Map<Character, Integer> jollyMap = new HashMap<>();
            for(Character cr : cards.toCharArray()) {
                char c = cr == 'J' ? bestCardToAdd : cr;
                jollyMap.putIfAbsent(c, 0);
                int val = jollyMap.get(c);
                jollyMap.put(c, val + 1);
            }
            for(int value : jollyMap.values()) {
                cardOrderJolly.putIfAbsent(value, 0);
                int val = cardOrderJolly.get(value);
                cardOrderJolly.put(value, val + 1);
            }
        }

        private char findHighestMostRepeatedCard() {
            char most = 0;
            int num = 0;
            for(Map.Entry<Character, Integer> entry : map.entrySet()) {
                char key = entry.getKey();
                int val = entry.getValue();

                if(val >= num && key != 'J') {
                    most = key;
                    num = val;
                }
            }

            return most;
        }
    }
}
