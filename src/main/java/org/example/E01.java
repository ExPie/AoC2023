package org.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class E01 {

    public static void main(String[] args) throws IOException {

        File input = new File("./input/01_1.txt");
        BufferedReader reader = new BufferedReader(new FileReader(input));

        String line = reader.readLine();

        int sum = 0;

        while (line != null) {
            //sum += findNumber(line);
            sum += findNumber2(line);
            // read next line
            line = reader.readLine();
        }

        reader.close();

        System.out.println(sum);
    }

    private static int findNumber(String line) {

        Character first = null, last = null;

        for(char c : line.toCharArray()) {
            if(c - 48 >= 0 && c -48 < 10) {
                if(first == null) {
                    first = c;
                } else {
                    last = c;
                }
            }
        }

        String num = "" + first + (last == null ? first : last);

        return Integer.parseInt(num);
    }

    private static int findNumber2(String line) {

        List<String> subs = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            subs.add(Integer.toString(i));
        }
        subs.add("one");
        subs.add("two");
        subs.add("three");
        subs.add("four");
        subs.add("five");
        subs.add("six");
        subs.add("seven");
        subs.add("eight");
        subs.add("nine");

        int index1 = -1, index2 = -1;
        int first = 0, last = 0;

        for(int i = 0; i < subs.size(); i++) {
            String sub = subs.get(i);
            int index = line.indexOf(sub);
            if(index >= 0 && (index1 < 0 || index < index1)) {
                if(index1 > index2) {
                    index2 = index1;
                    last = first;
                }
                index1 = index;
                first = i < 10 ? i : i - 9;
            }

            index = line.lastIndexOf(sub);
            if(index > index2) {
                index2 = index;
                last = i < 10 ? i : i - 9;
            }
        }

        String num = "" + first + (index2 >= 0 ? last : first);

        return Integer.parseInt(num);
    }

}

