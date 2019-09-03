package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class Day17 {

	public static void main(String[] args) throws IOException {
		Board.Builder builder = new Board.Builder();
		for (String line : FileUtils.readListFromFile("day17.txt")) {
			XY.parse(line).forEach(builder::addClay);
		}

		Board board = builder.build();
		// System.out.println(board);
		int step = 0;
		while (board.flow()) {
			System.out.println(step++);
			// System.out.println(board);
		}
		// System.out.println(board);
		System.out.println("A: " + board.countWaterTiles());
		System.out.println("B: " + board.countWaterAtRestTiles());
	}

	@EqualsAndHashCode
	private static class Board {

		private final int minX;
		private final int minY;
		private final int maxX;
		private final int maxY;
		private final char[][] data;
		private final int width;
		private final int height;

		public Board(int minX, int minY, int maxX, int maxY, char[][] data) {
			this.minX = minX;
			this.minY = minY;
			this.maxX = maxX;
			this.maxY = maxY;
			this.data = data;
			this.width = maxX - minX + 1;
			this.height = maxY - minY + 1;
		}

		public int countWaterTiles() {
			int count = 0;

			for (int y = 1; y < height - 1; y++) {
				for (int x = 1; x < width - 1; x++) {
					if (data[y][x] == '#' || data[y][x] == '.' || data[y][x] == '+') {
						// Do not count
					} else {
						count++;
					}
				}
			}
			return count;
		}

		public int countWaterAtRestTiles() {
			int count = 0;

			for (int y = 1; y < height - 1; y++) {
				for (int x = 1; x < width - 1; x++) {
					if (data[y][x] == '~') {
						count++;
					}
				}
			}
			return count;
		}

		public boolean flow() {
			boolean changed = false;
			for (int y = 1; y < height - 1; y++) {
				for (int x = 1; x < width - 1; x++) {

					char current = data[y][x];
					char above = data[y - 1][x];
					char left = data[y][x - 1];
					char right = data[y][x + 1];
					char below = data[y + 1][x];

					// Flow down
					if (current == '.' && isWater(above)) {
						data[y][x] = '|';
						changed = true;
					}
					// Flow down stopped
					else if (current == '|' && isBarrier(below)) {
						data[y][x] = '-';
						changed = true;
					}
					// Flow to right
					else if (current == '.' && isBarrier(below) && isStoppedWater(left)) {
						data[y][x] = '-';
						changed = true;
					}
					// Waterfall to right
					else if (current == '.' && below == '.' && isStoppedWater(left)) {
						data[y][x] = '|';
						changed = true;
					}
					// Flow to left
					else if (current == '.' && isBarrier(below) && isStoppedWater(right)) {
						data[y][x] = '-';
						changed = true;
					}
					// Waterfall to left
					else if (current == '.' && below == '.' && isStoppedWater(right)) {
						data[y][x] = '|';
						changed = true;
					}
					// Wall on right starts wave
					else if (current == '-' && isBarrier(below) && right == '#') {
						data[y][x] = ']';
						changed = true;
					}
					// Wave from right continues wave
					else if (current == '-' && isBarrier(below) && right == ']') {
						data[y][x] = ']';
						changed = true;
					}
					// Wall on left starts wave
					else if (current == '-' && isBarrier(below) && left == '#') {
						data[y][x] = '[';
						changed = true;
					}
					// Wave from left continues wave
					else if (current == '-' && isBarrier(below) && left == '[') {
						data[y][x] = '[';
						changed = true;
					}
					// Wave reaches barrier on right
					else if (current == '[' && isBarrier(right)) {
						data[y][x] = '~';
						changed = true;
					}
					// Two waves meet
					else if (current == '[' && right == ']') {
						data[y][x] = '~';
						changed = true;
					}
					// Wave reaches barrier on left
					else if (current == ']' && isBarrier(left)) {
						data[y][x] = '~';
						changed = true;
					}
					// Two waves meet
					else if (current == ']' && left == '[') {
						data[y][x] = '~';
						changed = true;
					}
				}
			}
			return changed;
		}

		private boolean isWater(char c) {
			return c == '+' || c == '|' || c == '[' || c == ']' || c == '-' || c == '~';
		}

		private boolean isStoppedWater(char c) {
			return c == '-' || c == ']' || c == '[';
		}

		private boolean isBarrier(char c) {
			return c == '#' || c == '~';
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for (int y = 0; y < height; y++) {
				if (y > 0) {
					sb.append('\n');
				}
				sb.append(data[y]);
			}
			return sb.toString();
		}

		public static class Builder {
			private List<XY> clay = new ArrayList<>(32768);
			private int minX = Integer.MAX_VALUE;
			private int minY = Integer.MAX_VALUE;
			private int maxX = Integer.MIN_VALUE;
			private int maxY = Integer.MIN_VALUE;

			public Builder addClay(XY xy) {
				clay.add(xy);
				minX = Math.min(minX, xy.getX());
				minY = Math.min(minY, xy.getY());
				maxX = Math.max(maxX, xy.getX());
				maxY = Math.max(maxY, xy.getY());
				return this;
			}

			public Board build() {
				if (minX > maxX || minY > maxY) {
					throw new IllegalStateException();
				}

				if (minX > 500 || maxX < 500) {
					throw new IllegalArgumentException();
				}

				int width = maxX - minX + 5;
				int height = maxY - minY + 3;
				final char[][] data = new char[height][width];
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						data[y][x] = '.';
					}
				}

				data[0][499 - minX + 3] = '+';

				clay.forEach(xy -> data[xy.getY() - minY + 1][xy.getX() - minX + 2] = '#');

				return new Board(minX - 2, minY - 1, maxX + 2, maxY + 1, data);
			}
		}
	}

	@RequiredArgsConstructor
	@EqualsAndHashCode
	@Getter
	@ToString
	private static class XY {

		private final int x;
		private final int y;

		private static final String REGEX_X = "^x=(\\d+), y=(\\d+)..(\\d+)$";
		private static final Pattern PATTERN_X = Pattern.compile(REGEX_X);
		private static final String REGEX_Y = "^y=(\\d+), x=(\\d+)..(\\d+)$";
		private static final Pattern PATTERN_Y = Pattern.compile(REGEX_Y);

		public static final Stream<XY> parse(String line) {
			final Matcher matcherX = PATTERN_X.matcher(line);

			if (matcherX.matches()) {
				final int x = Integer.parseInt(matcherX.group(1));
				final int y1 = Integer.parseInt(matcherX.group(2));
				final int y2 = Integer.parseInt(matcherX.group(3));
				return IntStream.range(y1, y2 + 1).mapToObj(y -> new XY(x, y));
			} else {
				final Matcher matcherY = PATTERN_Y.matcher(line);

				if (matcherY.matches()) {
					final int y = Integer.parseInt(matcherY.group(1));
					final int x1 = Integer.parseInt(matcherY.group(2));
					final int x2 = Integer.parseInt(matcherY.group(3));
					return IntStream.range(x1, x2 + 1).mapToObj(x -> new XY(x, y));

				} else {
					throw new IllegalArgumentException();
				}
			}

		}

	}
}
