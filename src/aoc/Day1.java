package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day1 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day1-input.file"));
		part1And2(input);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");

	}

	public static void part1And2(List<String> lines) {
		int sum = 0;
		Set<Integer> seen = new HashSet<>();
		boolean stop = false;
		boolean first = true;
		while (!stop) {
			for (String line : lines) {
				sum += Integer.parseInt(line);
				if (seen.add(sum)) {
					// good
				} else {
					System.out.println(sum);
					stop = true;
					break;
				}
			}
			if (first) {
				first = false;
				System.out.println(sum);
			}
		}
	}
}
