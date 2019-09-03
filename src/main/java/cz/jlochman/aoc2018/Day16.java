package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.Getter;
import lombok.ToString;

public class Day16 {

	public static void main(String[] args) throws IOException {
		List<String> lines = FileUtils.readListFromFile("day16a.txt");
		List<Sample> samples = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) {
			Sample sample = new Sample();
			sample.setInput(lines.get(i++));
			sample.setInstruction(lines.get(i++));
			sample.setOutput(lines.get(i++));
			samples.add(sample);
		}

		Set<Instruction> allInstructions = getAllInstructions();
		int resultA = 0;
		for (Sample sample : samples) {
			if (allInstructions.stream().filter(i -> isValid(sample, i)).count() >= 3) {
				resultA++;
			}
		}
		System.out.println("A: " + resultA);

		Map<Integer, Set<Instruction>> instructionCodeMap = new HashMap<>();
		for (int i = 0; i < 16; i++) {
			instructionCodeMap.put(i, new HashSet<>(allInstructions));
		}
		for (Sample sample : samples) {
			instructionCodeMap.put(sample.getInstruction()[0], instructionCodeMap.get(sample.getInstruction()[0])
					.stream().filter(i -> isValid(sample, i)).collect(Collectors.toSet()));
		}

		Map<Integer, Instruction> instructionCodes = new HashMap<>();
		while (!instructionCodeMap.isEmpty()) {
			for (Entry<Integer, Set<Instruction>> entry : instructionCodeMap.entrySet()) {
				if (entry.getValue().size() == 1) {
					instructionCodes.put(entry.getKey(), entry.getValue().stream().findFirst().get());
				}
			}
			instructionCodeMap.entrySet().removeIf(e -> e.getValue().size() == 1);
			for (Entry<Integer, Set<Instruction>> entry : instructionCodeMap.entrySet()) {
				for (Instruction instruction : instructionCodes.values()) {
					entry.getValue().removeIf(i -> instruction.getName().equals(i.getName()));
				}
			}
		}

		int[] input = new int[] { 0, 0, 0, 0 };
		List<int[]> ins = FileUtils.readListFromFile("day16b.txt").stream()
				.map(line -> Arrays.asList(line.split(" ")).stream().mapToInt(Integer::parseInt).toArray())
				.collect(Collectors.toList());
		for (int[] in : ins) {
			input = instructionCodes.get(in[0]).getOutput(input, in[1], in[2], in[3]);
		}
		System.out.println("B: " + input[0]);

	}

	private static boolean isValid(Sample sample, Instruction instruction) {
		return Arrays.equals(instruction.getOutput(sample.getInput(), sample.getInstruction()[1],
				sample.getInstruction()[2], sample.getInstruction()[3]), sample.getOutput());
	}

	@Getter
	@ToString
	private static class Sample {
		int[] input;
		int[] output;
		int[] instruction;

		public void setInput(String line) {
			input = Arrays.asList(line.substring(line.indexOf('[') + 1, line.indexOf(']')).split(", ")).stream()
					.mapToInt(Integer::parseInt).toArray();
		}

		public void setOutput(String line) {
			output = Arrays.asList(line.substring(line.indexOf('[') + 1, line.indexOf(']')).split(", ")).stream()
					.mapToInt(Integer::parseInt).toArray();
		}

		public void setInstruction(String line) {
			instruction = Arrays.asList(line.split(" ")).stream().mapToInt(Integer::parseInt).toArray();
		}

	}

	private static Set<Instruction> getAllInstructions() {
		Set<Instruction> ins = new HashSet<>();
		ins.add(new Addr());
		ins.add(new Addi());
		ins.add(new Mulr());
		ins.add(new Muli());
		ins.add(new Banr());
		ins.add(new Bani());
		ins.add(new Borr());
		ins.add(new Bori());
		ins.add(new Setr());
		ins.add(new Seti());
		ins.add(new Gtir());
		ins.add(new Gtri());
		ins.add(new Gtrr());
		ins.add(new Eqir());
		ins.add(new Eqri());
		ins.add(new Eqrr());
		return ins;
	}

	private interface Instruction {
		int[] getOutput(int[] input, int a, int b, int c);

		default String getName() {
			return this.getClass().getSimpleName();
		}
	}

	private static class Addr implements Instruction {
		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] + input[b];
			return result;
		}

	}

	private static class Addi implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] + b;
			return result;
		}

	}

	private static class Mulr implements Instruction {
		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] * input[b];
			return result;
		}
	}

	private static class Muli implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] * b;
			return result;
		}
	}

	private static class Banr implements Instruction {
		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] & input[b];
			return result;
		}
	}

	private static class Bani implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] & b;
			return result;
		}
	}

	private static class Borr implements Instruction {
		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] | input[b];
			return result;
		}
	}

	private static class Bori implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] | b;
			return result;
		}
	}

	private static class Setr implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a];
			return result;
		}
	}

	private static class Seti implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = a;
			return result;
		}
	}

	private static class Gtir implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = a > input[b] ? 1 : 0;
			return result;
		}
	}

	private static class Gtri implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] > b ? 1 : 0;
			return result;

		}
	}

	private static class Gtrr implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] > input[b] ? 1 : 0;
			return result;
		}
	}

	private static class Eqir implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = a == input[b] ? 1 : 0;
			return result;
		}
	}

	private static class Eqri implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] == b ? 1 : 0;
			return result;
		}
	}

	private static class Eqrr implements Instruction {

		@Override
		public int[] getOutput(int[] input, int a, int b, int c) {
			int[] result = Arrays.copyOf(input, input.length);
			result[c] = input[a] == input[b] ? 1 : 0;
			return result;
		}
	}
}