package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.AllArgsConstructor;

public class Day04 {

	private static Map<Integer, Set<SleepRecord>> guardMap = new HashMap<>();

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day04.txt").stream().sorted().collect(Collectors.toList());

		int guardId = 0;
		int fromMinute = 0;
		int toMinute = 0;
		for (String line : lines) {
			if (line.contains("Guard")) {
				guardId = getIntBetween(line, '#', ' ');
			} else if (line.contains("falls")) {
				fromMinute = getIntBetween(line, ':', ']');
			} else if (line.contains("wakes")) {
				toMinute = getIntBetween(line, ':', ']');
				guardMap.putIfAbsent(guardId, new HashSet<>());
				guardMap.get(guardId).add(new SleepRecord(fromMinute, toMinute));
			}
		}

		int maxSleepGuardId = 0;
		int maxSleepMins = 0;
		for (Entry<Integer, Set<SleepRecord>> entry : guardMap.entrySet()) {
			int sleepMins = entry.getValue().stream().mapToInt(sr -> sr.getSleepMins()).sum();
			if (sleepMins > maxSleepMins) {
				maxSleepMins = sleepMins;
				maxSleepGuardId = entry.getKey();
			}
		}

		int maxSleepGuardMin = 0;
		int maxSleepCount = 0;
		for (int i = 0; i < 60; i++) {
			final int min = i;
			int sleepCount = (int) guardMap.get(maxSleepGuardId).stream().filter(sr -> sr.isAsleep(min)).count();
			if (sleepCount > maxSleepCount) {
				maxSleepCount = sleepCount;
				maxSleepGuardMin = i;
			}
		}
		System.out.println("A: " + maxSleepGuardId * maxSleepGuardMin);

		maxSleepCount = 0;
		int result = 0;
		for (Entry<Integer, Set<SleepRecord>> entry : guardMap.entrySet()) {
			for (int i = 0; i < 60; i++) {
				final int min = i;
				int sleepCount = (int) entry.getValue().stream().filter(sr -> sr.isAsleep(min)).count();
				if (sleepCount > maxSleepCount) {
					maxSleepCount = sleepCount;
					result = entry.getKey() * min;
				}
			}
		}
		System.out.println("B: " + result);
	}

	private static int getIntBetween(String s, char fromChar, char endChar) {
		int fromIndex = s.indexOf(fromChar);
		int toIndex = s.indexOf(endChar, fromIndex);
		return Integer.parseInt(s.substring(fromIndex + 1, toIndex));
	}

	@AllArgsConstructor
	private static class SleepRecord {

		int fromMinute;
		int toMinute;

		public boolean isAsleep(int min) {
			return min >= fromMinute && min < toMinute;
		}

		public int getSleepMins() {
			return toMinute - fromMinute;
		}
	}

}
