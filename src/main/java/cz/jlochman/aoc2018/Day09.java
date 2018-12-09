package cz.jlochman.aoc2018;

import java.util.Arrays;
import java.util.List;

import org.magicwerk.brownies.collections.GapList;

public class Day09 {

	private final static int NUM_PLAYERS = 459;
	private final static int LAST_MARBLE = 71320;

	private static List<Integer> marbles = new GapList<>(LAST_MARBLE * 100);

	public static void main(String[] args) {
		marbles.add(0);
		int index = 0;
		long[] scores = new long[NUM_PLAYERS];
		for (int marble = 1; marble <= 100 * LAST_MARBLE; marble++) {
			if (marble % 23 == 0) {
				index = getIndex(index - 7);
				scores[marble % NUM_PLAYERS] += marbles.get(index) + marble;
				marbles.remove(index);
			} else {
				index = getIndex(index + 2);
				marbles.add(index, marble);
			}
			if (marble == LAST_MARBLE) {
				System.out.println("A: " + Arrays.stream(scores).summaryStatistics().getMax());
			}
		}
		System.out.println("B: " + Arrays.stream(scores).summaryStatistics().getMax());
	}

	private static int getIndex(int i) {
		int result = i % marbles.size();
		if (result < 0) {
			result += marbles.size();
		}
		return result;
	}

}
