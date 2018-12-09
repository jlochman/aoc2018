package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class Day07b {

	private static Set<Condition> conditions = new HashSet<>();
	private static Set<Character> allChars = new HashSet<>();
	private static Set<Character> processing = new HashSet<>();
	private static List<Character> processed = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day07.txt");
		for (String line : lines) {
			String[] split = line.split("\\s+");
			conditions.add(new Condition(split[1].charAt(0), split[7].charAt(0)));
		}
		for (char ch = 'A'; ch <= 'Z'; ch++) {
			allChars.add(ch);
		}

		Workers workers = new Workers();
		for (int second = 0; second < Integer.MAX_VALUE; second++) {
			if (processed.size() == allChars.size()) {
				System.out.println("B: " + second);
				break;
			}
			Optional<Character> optChar = getNextChar();
			while (workers.isFreeWorker() && optChar.isPresent()) {
				workers.assignToWorker(optChar.get());
				optChar = getNextChar();
			}
			workers.tick();
		}
	}

	private static Optional<Character> getNextChar() {
		Set<Character> chars = new HashSet<>(allChars);
		chars.removeAll(processed);
		chars.removeAll(processing);
		for (Condition condition : conditions) {
			if (!processed.contains(condition.getFirst())) {
				chars.remove(condition.getSecond());
			}
		}
		return chars.stream().sorted().findFirst();
	}

	@Getter
	@AllArgsConstructor
	private static class Condition {
		private char first;
		private char second;
	}

	private static class Workers {
		private Set<Worker> workers = new HashSet<>();

		public Workers() {
			for (int i = 0; i < 5; i++) {
				workers.add(new Worker());
			}
		}

		public void assignToWorker(char letter) {
			workers.stream().filter(w -> w.isFree()).findFirst().get().setLetter(letter);
		}

		public boolean isFreeWorker() {
			return workers.stream().filter(w -> w.isFree()).count() > 0;
		}

		public void tick() {
			workers.stream().forEach(w -> w.tick());
		}
	}

	private static class Worker {
		private int secsToFinish;
		private char letter;

		public void setLetter(char letter) {
			this.letter = letter;
			processing.add(letter);
			this.secsToFinish = 60 + (byte) letter - 64;
		}

		public boolean isFree() {
			return secsToFinish == 0;
		}

		public void tick() {
			if (secsToFinish > 0) {
				secsToFinish--;
				if (secsToFinish == 0) {
					processing.remove(letter);
					processed.add(letter);
				}
			}
		}
	}

}
