package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Day07a {

	private static Set<Condition> conditions = new HashSet<>();
	private static Set<Character> allChars = new HashSet<>();
	private static List<Character> result = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day07.txt");
		for (String line : lines) {
			String[] split = line.split("\\s+");
			conditions.add(new Condition(split[1].charAt(0), split[7].charAt(0)));
		}
		for (char ch = 'A'; ch <= 'Z'; ch++) {
			allChars.add(ch);
		}

		for (int i = 0; i < 26; i++) {
			result.add(getNextChar());
		}
		System.out.println("A: " + result.stream().map(ch -> ch.toString()).collect(Collectors.joining("")));
	}

	private static char getNextChar() {
		Set<Character> chars = new HashSet<>(allChars);
		chars.removeAll(result);
		for (Condition condition : conditions) {
			if (!result.contains(condition.getFirst())) {
				chars.remove(condition.getSecond());
			}
		}
		return chars.stream().sorted().findFirst().get();
	}

	@Getter
	@AllArgsConstructor
	private static class Condition {
		private char first;
		private char second;
	}

}
