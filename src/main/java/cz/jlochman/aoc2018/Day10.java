package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Set;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.Data;

public class Day10 {

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day10.txt");
		Set<Particle> particles = new HashSet<>();
		for (String line : lines) {
			Particle p = new Particle();
			p.setX(Integer.parseInt(line.substring(10, 16).replace(" ", "")));
			p.setY(Integer.parseInt(line.substring(18, 24).replace(" ", "")));
			p.setVelX(Integer.parseInt(line.substring(36, 38).replace(" ", "")));
			p.setVelY(Integer.parseInt(line.substring(40, 42).replace(" ", "")));
			particles.add(p);
		}

		int minGridSize = Integer.MAX_VALUE;
		for (int step = 0; step < 100000; step++) {
			int gridSize = getGridSize(particles);
			if (gridSize < minGridSize) {
				minGridSize = gridSize;
				if (gridSize < 80) {
					printGrid(particles);
					System.out.println("B: " + step);
				}
			}
			particles.stream().forEach(p -> p.move());
		}

	}

	private static int getGridSize(Set<Particle> particles) {
		IntSummaryStatistics xStats = particles.stream().mapToInt(p -> p.getX()).summaryStatistics();
		IntSummaryStatistics yStats = particles.stream().mapToInt(p -> p.getY()).summaryStatistics();
		return xStats.getMax() - xStats.getMin() + yStats.getMax() - yStats.getMin();
	}

	private static void printGrid(Set<Particle> particles) {
		IntSummaryStatistics xStats = particles.stream().mapToInt(p -> p.getX()).summaryStatistics();
		IntSummaryStatistics yStats = particles.stream().mapToInt(p -> p.getY()).summaryStatistics();
		for (int y = yStats.getMin(); y <= yStats.getMax(); y++) {
			final int yPos = y;
			for (int x = xStats.getMin(); x <= xStats.getMax(); x++) {
				final int xPos = x;
				if (particles.stream().filter(p -> p.getX() == xPos && p.getY() == yPos).count() == 0) {
					System.out.print(".");
				} else {
					System.out.print("#");
				}
			}
			System.out.println("");
		}
	}

	@Data
	private static class Particle {
		int x, y;
		int velX, velY;

		public void move() {
			x += velX;
			y += velY;
		}
	}

}
