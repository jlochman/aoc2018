package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.jlochman.aoc2018.utils.FileUtils;

public class Day12 {

	private final static long NUM_GENERATIONS = 50000000000L;
	private static Map<String, Character> potMap = new HashMap<>();
	private static long firstPotIndex = 0;

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day12.txt");
		String generation = lines.get(0).substring(15, lines.get(0).length());
		for (int i = 2; i < lines.size(); i++) {
			String[] split = lines.get(i).split(" => ");
			potMap.put(split[0], split[1].charAt(0));
		}
		for (long i = 1; i <= NUM_GENERATIONS; i++) {
			String prevGeneration = generation;
			generation = getNextGeneration(prevGeneration);
			if (i == 20) {
				System.out.println("A: " + getResult(generation));
			}
			if (prevGeneration.equals(generation)) {
				firstPotIndex += NUM_GENERATIONS - i;
				System.out.println("B: " + getResult(generation));
				break;
			}
		}
	}

	private static long getResult(String generation) {
		long result = 0;
		for (int i = 0; i < generation.length(); i++) {
			if (generation.charAt(i) == '#') {
				result += i + firstPotIndex;
			}
		}
		return result;
	}

	private static String getNextGeneration(String prevGeneration) {
		StringBuilder sb = new StringBuilder();
		for (int i = -2; i < prevGeneration.length() + 2; i++) {
			String input = prevGeneration.substring(Math.max(i - 2, 0), Math.min(i + 3, prevGeneration.length()));
			if (i <= 2) {
				while (input.length() < 5) {
					input = "." + input;
				}
			}
			if (i >= prevGeneration.length() - 2) {
				while (input.length() < 5) {
					input = input + ".";
				}
			}
			char nextChar = potMap.getOrDefault(input, '.');
			if (sb.length() == 0 && nextChar == '.') {
				continue;
			} else {
				sb.append(nextChar);
				if (sb.length() == 1) {
					firstPotIndex = firstPotIndex + i;
				}
			}
		}
		String result = sb.toString();
		while (result.charAt(result.length() - 1) == '.') {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

}
