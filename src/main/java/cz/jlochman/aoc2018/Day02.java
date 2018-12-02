package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.jlochman.aoc2018.utils.FileUtils;

public class Day02 {

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day02.txt");
		long twoTimes = lines.stream().filter(s -> isLetterTimes(s, 2)).count();
		long threeTimes = lines.stream().filter(s -> isLetterTimes(s, 3)).count();
		System.out.println("A: " + twoTimes * threeTimes);

		for (int i = 0; i < lines.size(); i++) {
			String s1 = lines.get(i);
			for (int j = i + 1; j < lines.size(); j++) {
				String s2 = lines.get(j);
				if (difference(s1, s2) == 1) {
					System.out.print("B: ");
					for (int k = 0; k < s1.length(); k++) {
						if (s1.charAt(k) == s2.charAt(k)) {
							System.out.print(s1.charAt(k));
						}
					}
				}
			}
		}
	}

	private static boolean isLetterTimes(String s, int times) {
		Map<Character, Integer> charMap = new HashMap<>();
		for (char ch : s.toCharArray()) {
			charMap.putIfAbsent(ch, 0);
			charMap.merge(ch, 1, Integer::sum);
		}
		return charMap.values().contains(times);
	}

	private static int difference(String s1, String s2) {
		int result = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				result++;
			}
		}
		return result;
	}

}
