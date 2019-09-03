package cz.jlochman.aoc2018;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cz.jlochman.aoc2018.utils.FileUtils;

public class Day15 {

	private static char[][] cave;
	private static List<Unit> elves;
	private static List<Unit> goblins;
	private static int rounds;

	private final static int GOBLIN_ATTACK_POWER = 3;

	private final static Comparator<Point> POINT_READING_ORDER = Comparator.comparingInt((Point point) -> point.y)
			.thenComparingInt(point -> point.x);

	private final static Comparator<Unit> UNIT_READING_ORDER = Comparator.comparingInt((Unit unit) -> unit.position.y)
			.thenComparingInt(unit -> unit.position.x);

	public static void main(String[] args) throws IOException {
		processInput(3);
		while (!isCombatOver()) {
			simulateRound();
		}
		System.out.println("A: " + getOutcome());

		for (int power = 4; power < 200; power++) {
			processInput(power);
			int elvesStart = elves.size();
			while (!isCombatOver()) {
				if (elves.size() != elvesStart) {
					break;
				}
				simulateRound();
			}
			if (elves.size() == elvesStart) {
				break;
			}
		}
		System.out.println("B: " + getOutcome());
	}

	private static boolean isCombatOver() {
		return elves.isEmpty() || goblins.isEmpty();
	}

	private static int getOutcome() {
		int totalHp = Stream.of(elves, goblins).flatMap(Collection::stream).mapToInt(unit -> unit.hitPoints).sum();
		return rounds * totalHp;
	}

	private static void simulateRound() {
		List<Unit> startingUnits = Stream.of(elves, goblins).flatMap(Collection::stream).sorted(UNIT_READING_ORDER)
				.collect(Collectors.toList());
		for (Unit unit : startingUnits) {
			if (isCombatOver()) {
				return;
			}
			if (!elves.contains(unit) && !goblins.contains(unit)) {
				continue;
			}
			if (attack(unit)) {
				continue;
			}
			move(unit);
			attack(unit);
		}
		rounds++;
	}

	private static void move(Unit unit) {
		ArrayList<Unit> enemies = new ArrayList<Unit>();
		if (elves.contains(unit)) {
			enemies.addAll(goblins);
		}
		if (goblins.contains(unit)) {
			enemies.addAll(elves);
		}

		PathResolver pathResolver = new PathResolver(cave, elves, goblins, unit.position);
		List<Point> destinations = enemies.stream()
				.flatMap(enemy -> getReachableAdjacentPoints(enemy.position).stream()).collect(Collectors.toList());

		if (destinations.isEmpty()) {
			return;
		}

		Point destination = destinations.stream()
				.collect(Collectors.groupingBy(pathResolver::getDistance, Collectors.toList())).entrySet().stream()
				.min(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getValue)
				.orElse(Collections.emptyList()).stream().min(POINT_READING_ORDER).orElseThrow(null);

		if (pathResolver.isUnreachable(destination)) {
			return;
		}
		LinkedList<Point> shortestPath = pathResolver.getShortestPath(destination);

		shortestPath.removeFirst();
		unit.position = shortestPath.removeFirst();
	}

	private static boolean attack(Unit unit) {
		ArrayList<Unit> enemies = new ArrayList<Unit>();
		if (elves.contains(unit)) {
			enemies.addAll(goblins);
		}
		if (goblins.contains(unit)) {
			enemies.addAll(elves);
		}

		List<Unit> adjacentEnemies = enemies.stream()
				.filter(enemy -> getAdjacentPoints(unit.position).stream()
						.anyMatch(point -> enemy.position.x == point.x && enemy.position.y == point.y))
				.collect(Collectors.toList());

		if (adjacentEnemies.isEmpty()) {
			return false;
		}

		Unit preferredEnemy = adjacentEnemies.stream()
				.collect(Collectors.groupingBy(enemy -> enemy.hitPoints, Collectors.toList())).entrySet().stream()
				.min(Comparator.comparingInt(Map.Entry::getKey)).map(Map.Entry::getValue)
				.orElse(Collections.emptyList()).stream().min(UNIT_READING_ORDER).orElseThrow(null);

		preferredEnemy.hitPoints -= unit.attackPower;

		if (preferredEnemy.hitPoints <= 0) {
			elves.remove(preferredEnemy);
			goblins.remove(preferredEnemy);
		}

		return true;
	}

	private static List<Point> getAdjacentPoints(Point point) {
		return Stream
				.of(new Point(point.x - 1, point.y), new Point(point.x + 1, point.y), new Point(point.x, point.y - 1),
						new Point(point.x, point.y + 1))
				.filter(p -> p.x >= 0 && p.x < cave.length).filter(p -> p.y >= 0 && p.y < cave[0].length)
				.filter(p -> cave[p.x][p.y] != '#').collect(Collectors.toList());
	}

	private static List<Point> getReachableAdjacentPoints(Point point) {
		return getAdjacentPoints(point).stream()
				.filter(p -> elves.stream().noneMatch(elf -> p.x == elf.position.x && p.y == elf.position.y))
				.filter(p -> goblins.stream().noneMatch(goblin -> p.x == goblin.position.x && p.y == goblin.position.y))
				.collect(Collectors.toList());
	}

	private static void processInput(int elvenAttackPower) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day15.txt");
		int width = lines.stream().mapToInt(String::length).max().orElseThrow(null);
		int height = lines.size();

		rounds = 0;
		elves = new ArrayList<>();
		goblins = new ArrayList<>();
		cave = new char[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				switch (lines.get(y).charAt(x)) {
				case '#':
				case '.':
					cave[x][y] = lines.get(y).charAt(x);
					break;
				case 'E':
					elves.add(new Unit(new Point(x, y), elvenAttackPower));
					break;
				case 'G':
					goblins.add(new Unit(new Point(x, y), GOBLIN_ATTACK_POWER));
					break;
				}
			}
		}
	}

	private static class Unit {
		Point position;
		int hitPoints = 200;
		int attackPower;

		Unit(Point position, int attackPower) {
			this.position = position;
			this.attackPower = attackPower;
		}
	}

	static class PathResolver {
		private char[][] cave;
		private List<Unit> elves;
		private List<Unit> goblins;

		private Map<Point, Integer> distanceTo = new HashMap<>();
		private Map<Point, Point> edgeTo = new HashMap<>();

		public PathResolver(char[][] cave, List<Unit> elves, List<Unit> goblins, Point startingPoint) {
			this.cave = cave;
			this.elves = elves;
			this.goblins = goblins;

			HashSet<Point> visited = new HashSet<Point>();
			Stack<Point> stack = new Stack<Point>();
			stack.push(startingPoint);
			while (!stack.isEmpty()) {
				Point p = stack.pop();
				if (visited.add(p)) {
					distanceTo.put(p, Integer.MAX_VALUE);
					visited.add(p);
					getReachableAdjacentEdges(p).forEach(stack::push);
				}
			}
			distanceTo.put(startingPoint, 0);

			Comparator<Point> comparator = Comparator.comparingInt((ToIntFunction<Point>) distanceTo::get)
					.thenComparing(POINT_READING_ORDER);

			TreeSet<Point> queue = new TreeSet<>(comparator);
			queue.addAll(visited);
			while (!queue.isEmpty()) {
				Point point = queue.pollFirst();
				getReachableAdjacentEdges(point).forEach(adjacent -> {
					if (distanceTo.get(point) == Integer.MAX_VALUE) {
						return;
					}
					Integer currentDistance = distanceTo.get(adjacent);
					int proposedDistance = distanceTo.get(point) + 1;
					if (proposedDistance < currentDistance) {
						distanceTo.put(adjacent, proposedDistance);
						edgeTo.put(adjacent, point);
						queue.remove(adjacent);
						queue.add(adjacent);
					}
					if (proposedDistance == currentDistance) {
						edgeTo.put(adjacent,
								Stream.of(point, edgeTo.get(adjacent)).min(POINT_READING_ORDER).orElseThrow(null));
					}
				});
			}
		}

		public int getDistance(Point point) {
			return distanceTo.getOrDefault(point, Integer.MAX_VALUE);
		}

		public boolean isUnreachable(Point point) {
			return !distanceTo.containsKey(point);
		}

		public LinkedList<Point> getShortestPath(Point point) {
			LinkedList<Point> path = new LinkedList<Point>();
			if (isUnreachable(point)) {
				return path;
			}
			for (Point p = point; p != null; p = edgeTo.get(p)) {
				path.addFirst(p);
			}
			return path;
		}

		private List<Point> getReachableAdjacentEdges(Point point) {
			return Stream
					.of(new Point(point.x - 1, point.y), new Point(point.x + 1, point.y),
							new Point(point.x, point.y - 1), new Point(point.x, point.y + 1))
					.filter(p -> p.x >= 0 && p.x < cave.length).filter(p -> p.y >= 0 && p.y < cave[0].length)
					.filter(p -> cave[p.x][p.y] != '#')
					.filter(p -> elves.stream().noneMatch(elf -> p.x == elf.position.x && p.y == elf.position.y))
					.filter(p -> goblins.stream()
							.noneMatch(goblin -> p.x == goblin.position.x && p.y == goblin.position.y))
					.collect(Collectors.toList());
		}
	}

}
