package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.Data;

public class Day13 {

	private static char[][] map;
	private static Set<Train> trains = new HashSet<>();

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day13.txt");
		map = new char[lines.size()][lines.get(0).length()];
		for (int row = 0; row < lines.size(); row++) {
			for (int col = 0; col < lines.get(row).length(); col++) {
				char ch = lines.get(row).charAt(col);
				if (ch == '>') {
					trains.add(new Train(row, col, Direction.RIGHT));
					map[row][col] = '-';
				} else if (ch == '<') {
					trains.add(new Train(row, col, Direction.LEFT));
					map[row][col] = '-';
				} else if (ch == 'v') {
					trains.add(new Train(row, col, Direction.DOWN));
					map[row][col] = '|';
				} else if (ch == '^') {
					trains.add(new Train(row, col, Direction.UP));
					map[row][col] = '|';
				} else {
					map[row][col] = ch;
				}
			}
		}

		boolean noCollision = true;
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			for (Train train : trains.stream().filter(t -> !t.isRemoved()).sorted().collect(Collectors.toList())) {
				if (train.isRemoved()) {
					continue;
				}
				train.move();
				if (getCollision() != null) {
					if (noCollision) {
						System.out.println("A: " + getCollision());
						noCollision = false;
					}
					removeCollidingTrains();
				}
			}
			if (trains.stream().filter(t -> !t.isRemoved()).count() == 1) {
				Train lastTrain = trains.stream().filter(t -> !t.isRemoved()).findFirst().get();
				System.out.println("B: " + lastTrain.getCol() + "," + lastTrain.getRow());
				break;
			}
		}

	}

	private static String getCollision() {
		Set<String> coordinates = new HashSet<>();
		for (Train train : trains.stream().filter(t -> !t.isRemoved()).collect(Collectors.toSet())) {
			String coordinate = "" + train.getCol() + "," + train.getRow();
			if (coordinates.contains(coordinate)) {
				return coordinate;
			}
			coordinates.add(coordinate);
		}
		return null;
	}

	private static void removeCollidingTrains() {
		Set<String> coordinates = new HashSet<>();
		for (Train train : trains.stream().filter(t -> !t.isRemoved()).collect(Collectors.toSet())) {
			String coordinate = "" + train.getCol() + "," + train.getRow();
			if (coordinates.contains(coordinate)) {
				final int cRow = train.getRow();
				final int cCol = train.getCol();
				trains.stream().filter(t -> t.getRow() == cRow && t.getCol() == cCol).forEach(t -> t.setRemoved(true));
			}
			coordinates.add(coordinate);
		}
	}

	private static void print() {
		for (int row = 0; row < map.length; row++) {
			for (int col = 0; col < map[0].length; col++) {
				final int fRow = row;
				final int fCol = col;
				Optional<Train> optTrain = trains.stream().filter(t -> t.getRow() == fRow && t.getCol() == fCol)
						.findFirst();
				if (optTrain.isPresent()) {
					switch (optTrain.get().getDirection()) {
					case UP:
						System.out.print('^');
						break;
					case DOWN:
						System.out.print('v');
						break;
					case LEFT:
						System.out.print('<');
						break;
					case RIGHT:
						System.out.print('>');
						break;
					}
				} else {
					System.out.print(map[row][col]);
				}
			}
			System.out.println();
		}
	}

	@Data
	private static class Train implements Comparable<Train> {
		private int row;
		private int col;
		private boolean removed = false;
		private Turn turn = Turn.LEFT;
		private Direction direction;

		public Train(int row, int col, Direction direction) {
			this.row = row;
			this.col = col;
			this.direction = direction;
		}

		public void move() {
			switch (map[row][col]) {
			case '|':
				switch (direction) {
				case UP:
					row--;
					break;
				case DOWN:
					row++;
					break;
				default:
					System.out.println("PROBLEM! Train on | with direction:" + direction);
					break;
				}
				break;
			case '-':
				switch (direction) {
				case LEFT:
					col--;
					break;
				case RIGHT:
					col++;
					break;
				default:
					System.out.println("PROBLEM! Train on - with direction:" + direction);
					break;
				}
				break;
			case '\\':
				switch (direction) {
				case UP:
					direction = Direction.LEFT;
					col--;
					break;
				case DOWN:
					direction = Direction.RIGHT;
					col++;
					break;
				case LEFT:
					direction = Direction.UP;
					row--;
					break;
				case RIGHT:
					direction = Direction.DOWN;
					row++;
					break;
				}
				break;
			case '/':
				switch (direction) {
				case UP:
					direction = Direction.RIGHT;
					col++;
					break;
				case DOWN:
					direction = Direction.LEFT;
					col--;
					break;
				case LEFT:
					direction = Direction.DOWN;
					row++;
					break;
				case RIGHT:
					direction = Direction.UP;
					row--;
					break;
				}
				break;
			case '+':
				switch (direction) {
				case UP:
					switch (turn) {
					case LEFT:
						direction = Direction.LEFT;
						col--;
						break;
					case RIGHT:
						direction = Direction.RIGHT;
						col++;
						break;
					case STRAIGHT:
						row--;
						break;
					}
					break;
				case DOWN:
					switch (turn) {
					case LEFT:
						direction = Direction.RIGHT;
						col++;
						break;
					case RIGHT:
						direction = Direction.LEFT;
						col--;
						break;
					case STRAIGHT:
						row++;
						break;
					}
					break;
				case LEFT:
					switch (turn) {
					case LEFT:
						direction = Direction.DOWN;
						row++;
						break;
					case RIGHT:
						direction = Direction.UP;
						row--;
						break;
					case STRAIGHT:
						col--;
						break;
					}
					break;
				case RIGHT:
					switch (turn) {
					case LEFT:
						direction = Direction.UP;
						row--;
						break;
					case RIGHT:
						direction = Direction.DOWN;
						row++;
						break;
					case STRAIGHT:
						col++;
						break;
					}
					break;
				}
				turn = Turn.values()[(turn.ordinal() + 1) % Turn.values().length];
				break;
			default:
				System.out.println("PROBLEM! Train outside track");
				print();
				System.exit(0);
				break;
			}
		}

		@Override
		public int compareTo(Train other) {
			if (this.row != other.row) {
				return Integer.compare(this.row, other.row);
			} else {
				return Integer.compare(this.col, other.col);
			}
		}
	}

	private static enum Turn {
		LEFT, STRAIGHT, RIGHT
	}

	private static enum Direction {
		UP, DOWN, LEFT, RIGHT
	}

}
