package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		int sum = 0;
		Map<Integer, Boolean> seen = new HashMap<>();
		boolean stop = false;
		while (!stop) {
			for (String line : lines) {
				sum += Integer.parseInt(line);
				if (seen.get(sum) == null) {
					seen.put(sum, true);
				} else {
					System.out.println(sum);
					stop = true;
					break;
				}
			}
		}
	}
}
