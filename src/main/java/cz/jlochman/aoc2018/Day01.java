package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.jlochman.aoc2018.utils.FileUtils;

public class Day01 {

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day01.txt");

		int frequency = 0;
		Set<Integer> frequencies = new HashSet<>();
		frequencies.add(frequency);

		int cycles = 0;
		while (true) {
			for (String line : lines) {
				char sign = line.charAt(0);
				int number = Integer.parseInt(line.substring(1, line.length()));

				if (sign == '+') {
					frequency += number;
				} else {
					frequency -= number;
				}
				if (frequencies.contains(frequency)) {
					System.out.println("B: " + frequency);
					return;
				}
				frequencies.add(frequency);
			}
			if (cycles == 0) {
				System.out.println("A: " + frequency);
			}
			cycles++;
		}

	}

}
