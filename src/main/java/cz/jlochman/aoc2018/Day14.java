package cz.jlochman.aoc2018;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day14 {

	private static List<Integer> scores = new ArrayList<>();
	private static int elf1Idx = 0;
	private static int elf2Idx = 1;

	private final static String INPUT = "165061";

	public static void main(String[] args) {
		scores.add(3);
		scores.add(7);
		int recipes = 0;
		while (getInputIdx() == -1) {
			recipes++;
			int sum = scores.get(elf1Idx) + scores.get(elf2Idx);
			if (sum < 10) {
				scores.add(sum);
			} else {
				scores.add(1);
				scores.add(sum % 10);
			}
			elf1Idx = (elf1Idx + scores.get(elf1Idx) + 1) % scores.size();
			elf2Idx = (elf2Idx + scores.get(elf2Idx) + 1) % scores.size();

			if (recipes == Integer.parseInt(INPUT) + 9) {
				System.out.println("A: " + scores.subList(Integer.parseInt(INPUT), Integer.parseInt(INPUT) + 10)
						.stream().map(i -> Integer.toString(i)).collect(Collectors.joining("")));
			}
		}
		System.out.println("B: " + getInputIdx());

	}

	private static int getInputIdx() {
		if (scores.size() < INPUT.length() + 1) {
			return -1;
		}
		if (scores.subList(scores.size() - INPUT.length() - 1, scores.size() - 1).stream().map(i -> Integer.toString(i))
				.collect(Collectors.joining("")).equals(INPUT)) {
			return scores.size() - INPUT.length() - 1;
		}
		if (scores.subList(scores.size() - INPUT.length(), scores.size()).stream().map(i -> Integer.toString(i))
				.collect(Collectors.joining("")).equals(INPUT)) {
			return scores.size() - INPUT.length();
		}
		return -1;
	}

}
