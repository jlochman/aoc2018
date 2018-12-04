package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import cz.jlochman.aoc2018.utils.FileUtils;

public class Day03 {

	public static void main(String[] args) throws IOException {
		Square[][] fabric = new Square[1000][1000];
		for (int col = 0; col < 1000; col++) {
			for (int row = 0; row < 1000; row++) {
				fabric[col][row] = new Square();
			}
		}

		List<String> lines = FileUtils.readListFromFile("day03.txt");
		for (String line : lines) {
			String[] split = line.replaceAll("[^\\d.]", " ").split("\\s+");
			int id = Integer.parseInt(split[1]);
			int colStart = Integer.parseInt(split[2]);
			int rowStart = Integer.parseInt(split[3]);
			int colEnd = Integer.parseInt(split[4]) + colStart;
			int rowEnd = Integer.parseInt(split[5]) + rowStart;
			for (int col = colStart; col < colEnd; col++) {
				for (int row = rowStart; row < rowEnd; row++) {
					fabric[col][row].addClaim(id);
				}
			}
		}
		
		System.out.println("A: "
				+ Arrays.stream(fabric).flatMap(x -> Arrays.stream(x)).filter(s -> s.getClaims().size() > 1).count());

		Set<Integer> validClaims = IntStream.range(1, lines.size() + 1).boxed().collect(Collectors.toSet());
		Arrays.stream(fabric).flatMap(x -> Arrays.stream(x)).filter(s -> s.getClaims().size() > 1)
				.forEach(s -> validClaims.removeAll(s.getClaims()));
		System.out.println("B: " + validClaims.stream().findFirst().get());
	}

	private static class Square {
		private Set<Integer> claims = new HashSet<>();

		public void addClaim(int claim) {
			claims.add(claim);
		}

		public Set<Integer> getClaims() {
			return claims;
		}
	}

}
