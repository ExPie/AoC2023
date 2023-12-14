package org.example;

import javax.swing.text.html.BlockView;
import java.util.LinkedList;
import java.util.List;

public class E06 {

    private static final List<int[]> races = new LinkedList<>();

    public static void main(String[] args) {
        prepareInput();
        part1();
        part2();
    }

    private static void part1() {
        int mul = 1;
        for(int[] race : races) {
            mul *= possibleVictories(race[0], race[1]);
        }
        System.out.println(mul);
    }

    private static void part2() {
        long time = 40_709_879L;
        long distance = 215_105_121_471_005L;

        System.out.println(victories(time, distance));
    }

    private static long victories(long time, long highScore) {
        long upper;
        for(upper = time; ; upper--) {
            if(raceDistance(upper, time) > highScore) {
                break;
            }
        }
        long lower;
        for(lower = 0; ; lower++) {
            if(raceDistance(lower, time) > highScore) {
                break;
            }
        }
        return upper - lower + 1;
    }

    private static int raceDistance(int holdTime, int totalTime) {
        return (totalTime - holdTime) * holdTime;
    }

    private static long raceDistance(long holdTime, long totalTime) {
        return (totalTime - holdTime) * holdTime;
    }

    private static int possibleVictories(int time, int highScore) {
        int total = 0;
        for(int i = 0; i <= time; i++) {
            if(raceDistance(i, time) > highScore) {
                total++;
            }
        }
        return total;
    }

    private static void prepareInput() {
        races.add(new int[]{40, 215});
        races.add(new int[]{70, 1051});
        races.add(new int[]{98, 2147});
        races.add(new int[]{79, 1005});
    }
}
