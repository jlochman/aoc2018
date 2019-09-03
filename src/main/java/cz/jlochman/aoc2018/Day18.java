package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.jlochman.aoc2018.utils.FileUtils;

public class Day18 {

	private final static char OPEN = '.';
	private final static char TREES = '|';
	private final static char LUMBERYARD = '#';

	private static int rows;
	private static int cols;
	private static char[][] world;

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day18.txt");
		rows = lines.size();
		cols = lines.get(0).length();
		world = new char[rows][cols];
		for (int row = 0; row < rows; row++) {
			String line = lines.get(row);
			for (int col = 0; col < cols; col++) {
				world[row][col] = line.charAt(col);
			}
		}

		for (long i = 0; i < 1000000000L; i++) {
			if (i == 10) {
				System.out.println("A: " + getResult());
			}
			step();
			if (i == 1000) {
				i += 28 * (1000000000L - 1000) / 28 - 28;
			}
		}
		System.out.println("B: " + getResult());
	}

	private static int getResult() {
		int trees = 0;
		int lumberyards = 0;
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				if (world[row][col] == TREES) {
					trees++;
				} else if (world[row][col] == LUMBERYARD) {
					lumberyards++;
				}
			}
		}
		return trees * lumberyards;
	}

	private static void step() {
		char[][] newWorld = new char[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				Map<Character, Integer> surroundingMap = getSurroundingMap(row, col);
				char newCh = world[row][col];
				switch (world[row][col]) {
				case OPEN:
					if (surroundingMap.get(TREES) >= 3) {
						newCh = TREES;
					}
					break;
				case TREES:
					if (surroundingMap.get(LUMBERYARD) >= 3) {
						newCh = LUMBERYARD;
					}
					break;
				case LUMBERYARD:
					if (surroundingMap.get(LUMBERYARD) == 0 || surroundingMap.get(TREES) == 0) {
						newCh = OPEN;
					}
					break;
				}
				newWorld[row][col] = newCh;
			}
		}
		world = newWorld;
	}

	private static Map<Character, Integer> getSurroundingMap(int row, int col) {
		Map<Character, Integer> map = new HashMap<>();
		map.put(OPEN, 0);
		map.put(TREES, 0);
		map.put(LUMBERYARD, 0);
		for (int r = Math.max(row - 1, 0); r <= Math.min(rows - 1, row + 1); r++) {
			for (int c = Math.max(col - 1, 0); c <= Math.min(cols - 1, col + 1); c++) {
				if (r == row && c == col) {
					continue;
				}
				map.merge(world[r][c], 1, Integer::sum);
			}
		}
		return map;
	}

}
