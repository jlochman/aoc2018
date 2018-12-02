package cz.jlochman.aoc2018.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Scanner;

public class FileUtils {

	public static Scanner getScanner(String fileName) {
		return new Scanner(FileUtils.class.getClassLoader().getResourceAsStream(fileName));
	}

	public static List<String> readListFromFile(String fileName) throws IOException {
		return Files.readAllLines(new File(FileUtils.class.getClassLoader().getResource(fileName).getPath()).toPath());
	}

}
