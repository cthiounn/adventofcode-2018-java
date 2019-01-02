package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day3-input.file"));

		part1And2(input);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");

	}

	public static void part1And2(List<String> lines) {
		int numberOfConflict = 0;
		Map<String, Integer> grid = new HashMap<>();
		for (String line : lines) {
			numberOfConflict += fillGrid(line, grid);
		}
		System.out.println(numberOfConflict);
		Map<String, Integer> grid2 = new HashMap<>();
		for (Map.Entry<String, Integer> entry : grid.entrySet()) {
			if (entry.getValue() == 1) {
				grid2.put(entry.getKey(), entry.getValue());
			}
		}

		for (String line : lines) {
			checkGrid(line, grid2);
		}
	}

	public static int fillGrid(String str, Map<String, Integer> map) {
		// #10 @ 257,504: 10x15
		Pattern pattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
		Matcher matcher = pattern.matcher(str);
		int numberConflict = 0;
		while (matcher.find()) {
			int x = Integer.parseInt(matcher.group(2));
			int y = Integer.parseInt(matcher.group(3));
			int sizeX = Integer.parseInt(matcher.group(4));
			int sizeY = Integer.parseInt(matcher.group(5));
			for (int i = 0; i < sizeX; i++) {
				for (int j = 0; j < sizeY; j++) {
					int calculX = x + i;
					int calculY = y + j;
					int count = map.get(calculX + "*" + calculY) == null ? 0 : map.get(calculX + "*" + calculY);
					if (count == 1) {
						numberConflict++;
					}
					map.put(calculX + "*" + calculY, count + 1);
				}
			}
		}
		return numberConflict;
	}

	public static boolean checkGrid(String str, Map<String, Integer> map) {
		Pattern pattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)");
		Matcher matcher = pattern.matcher(str);
		int id = 0;
		while (matcher.find()) {
			id = Integer.parseInt(matcher.group(1));
			int x = Integer.parseInt(matcher.group(2));
			int y = Integer.parseInt(matcher.group(3));
			int sizeX = Integer.parseInt(matcher.group(4));
			int sizeY = Integer.parseInt(matcher.group(5));
			for (int i = 0; i < sizeX; i++) {
				for (int j = 0; j < sizeY; j++) {
					int calculX = x + i;
					int calculY = y + j;
					int count = map.get(calculX + "*" + calculY) == null ? 0 : map.get(calculX + "*" + calculY);
					if (count != 1) {

						return false;
					}
				}
			}
		}
		System.out.println(id);
		return true;
	}
}
