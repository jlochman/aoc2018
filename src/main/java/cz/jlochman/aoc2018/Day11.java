package cz.jlochman.aoc2018;

public class Day11 {

	private final static int SERIAL_NUMBER = 7989;

	public static void main(String[] args) {
		int[][] grid = new int[300][300];
		int maxPower = 0;
		String res = "";
		for (int size = 1; size <= 300; size++) {
			for (int x = 1; x <= 300 - size + 1; x++) {
				for (int y = 1; y <= 300 - size + 1; y++) {
					if (size == 1) {
						grid[x - 1][y - 1] = getPowerLevel(x, y);
					}
					int power = 0;
					for (int x0 = x; x0 <= x + size - 1; x0++) {
						for (int y0 = y; y0 <= y + size - 1; y0++) {
							power += grid[x0 - 1][y0 - 1];
						}
					}
					if (power > maxPower) {
						maxPower = power;
						res = "" + x + "," + y + "," + size;
					}
				}
			}
			if (size == 3) {
				System.out.println("A: " + res.substring(0, res.length() - 2));
			}
		}
		System.out.println("B: " + res);
	}

	private static int getPowerLevel(int x, int y) {
		int id = x + 10;
		int power = (id * y + SERIAL_NUMBER) * id;
		int digit = power / 100 % 10;
		return digit - 5;
	}

}
