package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day1-input.file"));
		part1And2(input);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");

	}

	public static void part1And2(List<String> lines) {
		System.out.println("" + lines.stream().collect(Collectors.summingDouble(Double::valueOf)).intValue());

		// TODO part2 to be implemented
	}
}
