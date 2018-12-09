package cz.jlochman.aoc2018;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cz.jlochman.aoc2018.utils.FileUtils;
import lombok.Getter;

public class Day08 {

	public static void main(String[] args) throws IOException {
		List<Integer> input = Arrays.asList(FileUtils.readStringFromFile("day08.txt").split("\\s+")).stream()
				.mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

		List<Node> nodes = new ArrayList<>();
		Node actualNode = new Node(input.get(0), input.get(1), null);
		nodes.add(actualNode);
		for (int i = 2; i < input.size(); i++) {
			while (actualNode.isAllChildren() && actualNode.isAllData()) {
				actualNode = actualNode.getParent();
			}
			if (!actualNode.isAllChildren()) {
				Node node = new Node(input.get(i), input.get(i + 1), actualNode);
				nodes.add(node);
				actualNode = node;
				i++;
			} else if (!actualNode.isAllData()) {
				actualNode.getData().add(input.get(i));
			}
		}

		System.out.println(
				"A: " + nodes.stream().flatMapToInt(n -> n.getData().stream().mapToInt(Integer::valueOf)).sum());
		System.out.println("B: " + getNodeValueB(nodes.get(0)));
	}

	private static int getNodeValueB(Node node) {
		if (node.getChildren().size() == 0) {
			return node.getData().stream().mapToInt(Integer::valueOf).sum();
		} else {
			int result = 0;
			for (int index : node.getData()) {
				if (index > 0 && index <= node.getChildren().size()) {
					result += getNodeValueB(node.getChildren().get(index - 1));
				}
			}
			return result;
		}
	}

	private static class Node {

		private int numChildren;
		private int numData;

		@Getter
		private Node parent;
		@Getter
		private List<Node> children = new ArrayList<>();
		@Getter
		private List<Integer> data = new ArrayList<>();

		public Node(int numChildren, int numData, Node parent) {
			this.numChildren = numChildren;
			this.numData = numData;
			this.parent = parent;
			if (parent != null) {
				parent.getChildren().add(this);
			}
		}

		public boolean isAllChildren() {
			return children.size() == numChildren;
		}

		public boolean isAllData() {
			return data.size() == numData;
		}

	}

}
