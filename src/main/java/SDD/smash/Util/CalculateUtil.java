package SDD.smash.Util;

import java.util.List;
import java.util.Objects;

public class CalculateUtil {
    public static Double mean(List<Integer> xs) {
        if (xs == null || xs.isEmpty()) return null;
        long sum = 0;
        int count = 0;
        for (Integer x : xs) {
            if (x != null) {
                sum += x;
                count++;
            }
        }
        if (count == 0) return null;

        double avg = sum * 1.0 / count;
        return Math.round(avg * 10.0) / 10.0;
    }
    public static Integer median(List<Integer> xs) {
        if (xs == null) return null;
        List<Integer> s = xs.stream().filter(Objects::nonNull).sorted().toList();
        int n = s.size(); if (n == 0) return null;
        if ((n & 1) == 1) return s.get(n/2);
        return (int)Math.round((s.get(n/2 - 1) + s.get(n/2)) / 2.0);
    }
}
