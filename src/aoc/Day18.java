package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day18 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day18-input.file"));
		Map<String, String> grid = new HashMap<>();
		Map<Integer, Integer> allTotal = new HashMap<>();
		Map<Integer, Integer> totByMinutes = new HashMap<>();
		initGrid(grid, input);
		int minute = 0;
		int numberOfLumberyard = 0;
		int numberOfTrees = 0;
		while (minute < 1000) {
			// System.out.println(minute);
			// printDataViz(grid, 50, 50);
			grid = growGrid(grid);
			minute++;
			numberOfLumberyard = (int) grid.entrySet().stream().filter(e -> ("#".equals(e.getValue()))).count();
			numberOfTrees = (int) grid.entrySet().stream().filter(e -> ("|".equals(e.getValue()))).count();

			if (minute == 10) {
				System.out.println((numberOfTrees * numberOfLumberyard));
			}

			totByMinutes.put(minute, numberOfLumberyard * numberOfTrees);
			if (allTotal.get(numberOfLumberyard * numberOfTrees) == null) {
				allTotal.put(numberOfLumberyard * numberOfTrees, 1);
			} else {
				// System.out.println("already seen = " + minute + "," + (numberOfLumberyard *
				// numberOfTrees));
			}
		}

		System.out.println((totByMinutes.get(((1000000000 - 504) % 28) + 504)));
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static Map<String, String> growGrid(Map<String, String> grid) {
		Map<String, String> newGrid = new HashMap<>();
		for (Map.Entry<String, String> entry : grid.entrySet()) {

			if (".".equals(entry.getValue())) {
				if (countAdjacent(entry.getKey(), entry.getValue(), grid)) {
					newGrid.put(entry.getKey(), "|");
				} else {
					newGrid.put(entry.getKey(), entry.getValue());
				}
			} else if ("#".equals(entry.getValue())) {
				if (countAdjacent(entry.getKey(), entry.getValue(), grid)) {
					newGrid.put(entry.getKey(), "#");
				} else {
					newGrid.put(entry.getKey(), ".");
				}
			} else if ("|".equals(entry.getValue())) {
				if (countAdjacent(entry.getKey(), entry.getValue(), grid)) {
					newGrid.put(entry.getKey(), "#");
				} else {
					newGrid.put(entry.getKey(), entry.getValue());
				}
			}

		}
		return newGrid;
	}

	private static boolean countAdjacent(String key, String value, Map<String, String> grid) {
		boolean returnValue = false;
		int x = Integer.parseInt(key.split("!")[0]);
		int y = Integer.parseInt(key.split("!")[1]);
		List<String> listNeighbors = new ArrayList<>();
		listNeighbors.add(grid.get((x - 1) + "!" + (y)));
		listNeighbors.add(grid.get((x - 1) + "!" + (y + 1)));
		listNeighbors.add(grid.get((x - 1) + "!" + (y - 1)));
		listNeighbors.add(grid.get((x) + "!" + (y + 1)));
		listNeighbors.add(grid.get((x) + "!" + (y - 1)));
		listNeighbors.add(grid.get((x + 1) + "!" + (y)));
		listNeighbors.add(grid.get((x + 1) + "!" + (y - 1)));
		listNeighbors.add(grid.get((x + 1) + "!" + (y + 1)));
		long countTree = listNeighbors.stream().filter(e -> ("|".equals(e))).count();
		long countLumberyards = listNeighbors.stream().filter(e -> ("#".equals(e))).count();

		switch (value) {
		case ".":
			if (countTree >= 3) {
				returnValue = true;
			}
			break;
		case "|":
			if (countLumberyards >= 3) {
				returnValue = true;
			}
			break;
		case "#":
			if ((countLumberyards >= 1) && (countTree >= 1)) {
				returnValue = true;
			}
			break;
		default:
			break;
		}

		return returnValue;
	}

	private static void printDataViz(Map<String, String> grid, int x, int y) throws IOException {
		Path path = Paths.get("src/main/resources/output.txt");
		String toWrite = "";
		toWrite += ";";
		byte[] strToBytes = toWrite.getBytes();
		Files.write(path, strToBytes, StandardOpenOption.TRUNCATE_EXISTING);
		for (int i = 0; i <= x; i++) {
			toWrite = "";
			for (int j = 0; j <= y; j++) {
				String tileToDisplay = grid.get(i + "!" + j) != null ? grid.get(i + "!" + j) : ".";
				if (j == y) {
					System.out.println(tileToDisplay);
					toWrite += tileToDisplay + ";";
					strToBytes = toWrite.getBytes();
					Files.write(path, strToBytes, StandardOpenOption.APPEND);
				} else {
					System.out.print(tileToDisplay);
					toWrite += tileToDisplay;
				}
			}
		}
	}

	private static void initGrid(Map<String, String> grid, List<String> input) {
		int j = 0;
		for (String line : input) {
			for (int i = 0; i < line.length(); i++) {
				grid.put(j + "!" + i, line.charAt(i) + "");
			}
			j++;
		}
	}

}