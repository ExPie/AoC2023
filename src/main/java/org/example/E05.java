package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class E05 {

    private static final List<Long> seeds = new LinkedList<>();
    private static final List<Mapping> maps = new LinkedList<>();

    public static void main(String[] args) {
        parseInput();
        part1();
        //part2();
        part2Efficient();
    }

    private static void part1() {
        long min = -1;
        for(long seed : seeds) {
            for(Mapping mapping : maps) {
                seed = mapping.map(seed);
            }
            if(min < 0 || seed < min) {
                min = seed;
            }
        }
        System.out.println(min);
    }

    private static void part2Efficient() {
        List<long[]> mappedIntervals = new LinkedList<>();

        for(int i = 0; i < seeds.size(); i += 2) {
            mappedIntervals.add(new long[]{seeds.get(i), seeds.get(i) + seeds.get(i + 1)});
        }

        for(Mapping mapping : maps) {
            List<long[]> mapped = new LinkedList<>();

            for(long[] interval : mappedIntervals) {
                List<long[]> m = mapping.mapInterval(interval);
                mapped.addAll(m);
            }

            mappedIntervals = mapped;
        }

        long min = -1;
        for(long[] interval : mappedIntervals) {
            if(min < 0 || interval[0] < min) {
                min = interval[0];
            }
        }

        System.out.println(min);
    }

    private static void part2() {
        Thread[] threads = new Thread[10];
        ThreadSol[] s = new ThreadSol[10];

        for(int i = 0; i < seeds.size(); i+=2) {
            System.out.println("i=" + i);
            s[i/2] = new ThreadSol(i);
            threads[i/2] = new Thread(s[i/2]);
            threads[i/2].start();
        }

        for(int i = 0; i < 10; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long min = s[0].res;
        for(int i = 1; i < 10; i++) {
            if(s[i].res < min) {
                min = s[i].res;
            }
        }
        System.out.println(min);
    }

    private static class ThreadSol implements Runnable {

        private int i;
        public long res;

        public ThreadSol(int i) {
            this.i = i;
        }

        public void run() {
            System.out.println("Starting thread " + i);
            long min = -1;
            for (long j = seeds.get(i); j < seeds.get(i) + seeds.get(i + 1); j++) {
                if (j % 10_000_000 == 0) {
                    System.out.println("i=" + i + ", j=" + j);
                }
                long seed = j;
                for (Mapping mapping : maps) {
                    seed = mapping.map(seed);
                }
                if (min < 0 || seed < min) {
                    min = seed;
                }
            }
            res = min;
            System.out.println("Ending thread " + i);
        }
    }


    private static void parseInput() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./input/5.txt"));

            String line = reader.readLine();
            for(String num : line.split(":")[1].strip().split(" ")) {
                seeds.add(Long.parseLong(num));
            }
            line = reader.readLine();

            Mapping curr = null;
            while(line != null) {

                if(line.isBlank()) {
                    if(curr != null) {
                        maps.add(curr);
                    }
                    curr = new Mapping();
                    line = reader.readLine();
                    continue;
                }

                if (!Character.isDigit(line.charAt(0))) {
                    line = reader.readLine();
                    continue;
                }

                String[] nums = line.split(" ");
                long[] numsA = new long[3];
                for(int i = 0; i < 3; i++) {
                    numsA[i] = Long.parseLong(nums[i]);
                }
                curr.addRange(numsA);

                line = reader.readLine();
            }
            maps.add(curr);

            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class Mapping {
        List<long[]> ranges = new LinkedList<>();

        public void addRange(long[] range) {
            int i;
            for(i = 0; i < ranges.size(); i++) {
                if(range[1] < ranges.get(i)[1]) {
                    break;
                }
            }
            ranges.add(i, range);
        }

        public long map(long input) {

            if(input < ranges.get(0)[1]) {
                return input;
            }

            int i;
            for(i = 0; i < ranges.size(); i++) {
                if(i == ranges.size() - 1 || input >= ranges.get(i)[1] && input < ranges.get(i+1)[1]) {
                    break;
                }
            }
            long src = ranges.get(i)[1];
            long dst = ranges.get(i)[0];
            long len = ranges.get(i)[2];

            if(input >= src + len) {
                return input;
            }

            long diff = input - src;
            return dst + diff;
        }

        public List<long[]> mapInterval(long[] interval) {
            List<long[]> mappings = new LinkedList<>();

            long intStart = interval[0];
            long intEnd = interval[1];

            for(long[] range : ranges) {
                long ruleStart = range[1];
                long ruleEnd = range[1] + range[2];

                if(intStart < ruleStart) {
                    if(intEnd <= ruleStart) {
                        continue;
                    } else if(intEnd <= ruleEnd) {
                        long map1 = map(ruleStart, range[1], range[0], range[2]);
                        long map2 = map(intEnd, range[1], range[0], range[2]);
                        mappings.add(new long[]{intStart, ruleStart});
                        mappings.add(new long[]{map1, map2});
                        break;
                    } else { // intEnd > ruleEnd
                        long map1 = map(ruleStart, range[1], range[0], range[2]);
                        long map2 = map(ruleEnd, range[1], range[0], range[2]);
                        mappings.add(new long[]{intStart, ruleStart});
                        mappings.add(new long[]{map1, map2});
                        mappings.addAll(mapInterval(new long[]{ruleEnd, intEnd}));
                        break;
                    }
                } else if (intStart < ruleEnd) { // intStart >= ruleStart
                    if(intEnd <= ruleEnd) {
                        long map1 = map(intStart, range[1], range[0], range[2]);
                        long map2 = map(intEnd, range[1], range[0], range[2]);
                        mappings.add(new long[]{map1, map2});
                        break;
                    } else { // intEnd > ruleEnd
                        long map1 = map(intStart, range[1], range[0], range[2]);
                        long map2 = map(ruleEnd, range[1], range[0], range[2]);
                        mappings.add(new long[]{map1, map2});
                        mappings.addAll(mapInterval(new long[]{ruleEnd, intEnd}));
                        break;
                    }
                } // else intStart >= ruleEnd -> continue;
            }

            if(mappings.isEmpty()) {
                mappings.add(interval);
            }

            return mappings;
        }

        private long map(long input, long src, long dst, long len) {
            long diff = input - src;
            return dst + diff;
        }
    }
}
