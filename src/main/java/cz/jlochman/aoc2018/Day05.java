package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.magicwerk.brownies.collections.GapList;

import cz.jlochman.aoc2018.utils.FileUtils;

public class Day05 {

	public static void main(String[] args) throws IOException {
		List<Byte> input = new GapList<>();
		for (char ch : FileUtils.readListFromFile("day05.txt").get(0).toCharArray()) {
			input.add((byte) ch);
		}
		System.out.println("A: " + getLength(input));

		int minLength = Integer.MAX_VALUE;
		for (char ch = 'A'; ch <= 'Z'; ch++) {
			int length = getLength(input, (byte) ch, (byte) (ch + 32));
			if (length < minLength) {
				minLength = length;
			}
		}
		System.out.println("B: " + minLength);
	}

	private static int getLength(List<Byte> input, Byte... bytesToRemove) {
		List<Byte> reducedInput = new GapList<>(input);
		reducedInput.removeAll(Arrays.asList(bytesToRemove));

		boolean removed = true;
		int lastIndex = 0;
		while (removed) {
			removed = false;
			for (int i = Math.max(0, lastIndex - 1); i < reducedInput.size() - 1; i++) {
				if (Math.abs(reducedInput.get(i) - reducedInput.get(i + 1)) == 32) {
					lastIndex = i;
					reducedInput.remove(i);
					reducedInput.remove(i);
					removed = true;
					break;
				}
			}
		}
		return reducedInput.size();
	}

}
