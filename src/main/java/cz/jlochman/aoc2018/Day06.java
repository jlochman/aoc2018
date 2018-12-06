package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Day06 {

	private static List<Coordinate> coordinates = new ArrayList<>();
	private static Set<Location> locations = new HashSet<>();

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day06.txt");

		for (String line : lines) {
			String[] split = line.split(", ");
			coordinates.add(new Coordinate(Integer.parseInt(split[0]), Integer.parseInt(split[1])));
		}
		IntSummaryStatistics rowStats = coordinates.stream().mapToInt(c -> c.getRow()).summaryStatistics();
		IntSummaryStatistics colStats = coordinates.stream().mapToInt(c -> c.getCol()).summaryStatistics();

		for (int row = rowStats.getMin(); row <= rowStats.getMax(); row++) {
			for (int col = colStats.getMin(); col <= colStats.getMax(); col++) {
				locations.add(new Location(row, col));
			}
		}

		Set<Integer> finiteCoords = IntStream.range(0, coordinates.size()).boxed().collect(Collectors.toSet());
		locations.stream()
				.filter(l -> l.getRow() == rowStats.getMin() || l.getRow() == rowStats.getMax()
						|| l.getCol() == colStats.getMin() || l.getCol() == colStats.getMax())
				.forEach(l -> finiteCoords.remove(l.getClosestTo()));

		System.out.println("A: " + finiteCoords.stream().mapToInt(Day06::getAreaSize).summaryStatistics().getMax());
		System.out.println("B: " + locations.stream().filter(l -> l.getTotalDistane() < 10000).count());
	}

	private static int getAreaSize(int coordinate) {
		return (int) locations.stream().filter(l -> l.getClosestTo() == coordinate).count();
	}

	@Getter
	@Setter
	@AllArgsConstructor
	private static class Coordinate {
		private int row;
		private int col;
	}

	@Getter
	@Setter
	private static class Location extends Coordinate {

		private int closestTo;
		private int totalDistane;

		public Location(int row, int col) {
			super(row, col);
			this.closestTo = getClosestCoordinateIdx(row, col);
			this.totalDistane = getTotalDistance(row, col);
		}

		private int getTotalDistance(int row, int col) {
			return coordinates.stream().mapToInt(c -> Math.abs(row - c.getRow()) + Math.abs(col - c.getCol())).sum();
		}

		private int getClosestCoordinateIdx(int row, int col) {
			int minDistance = Integer.MAX_VALUE;
			int result = 0;
			for (int i = 0; i < coordinates.size(); i++) {
				int distance = Math.abs(row - coordinates.get(i).getRow())
						+ Math.abs(col - coordinates.get(i).getCol());
				if (distance < minDistance) {
					minDistance = distance;
					result = i;
				} else if (distance == minDistance) {
					result = -1;
				}
			}
			return result;
		}

	}

}
