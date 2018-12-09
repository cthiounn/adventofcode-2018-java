package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day3-input.file"));

		part1(input);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");

	}

	public static void part1(List<String> lines) {
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
			if (fillGrid2(line, grid2)) {
				System.out.println(line.split(" ")[0].replace("#", ""));
			}
		}
	}

	public static int fillGrid(String str, Map<String, Integer> map) {
		int numberConflict = 0;
		String[] liste = (str.split("@")[1]).split(":");
		String[] pos = liste[0].split(",");
		String[] size = liste[1].split("x");
		String posX = pos[0].trim();
		String posy = pos[1].trim();
		String tailleX = size[0].trim();
		String tailley = size[1].trim();
		for (int i = 0; i < Integer.parseInt(tailleX); i++) {
			for (int j = 0; j < Integer.parseInt(tailley); j++) {
				int calculX = Integer.parseInt(posX) + i;
				int calculY = Integer.parseInt(posy) + j;
				int count = map.get(calculX + "*" + calculY) == null ? 0 : map.get(calculX + "*" + calculY);
				if (count == 1) {
					numberConflict++;
				}
				map.put(calculX + "*" + calculY, count + 1);
			}
		}
		return numberConflict;
	}

	public static boolean fillGrid2(String str, Map<String, Integer> map) {

		boolean retour = true;
		String[] liste = (str.split("@")[1]).split(":");

		String[] pos = liste[0].split(",");
		String[] size = liste[1].split("x");
		String posX = pos[0].trim();
		String posy = pos[1].trim();
		String tailleX = size[0].trim();
		String tailley = size[1].trim();
		for (int i = 0; i < Integer.parseInt(tailleX); i++) {
			for (int j = 0; j < Integer.parseInt(tailley); j++) {
				int calculX = Integer.parseInt(posX) + i;
				int calculY = Integer.parseInt(posy) + j;

				int count = map.get(calculX + "*" + calculY) == null ? 0 : map.get(calculX + "*" + calculY);
				if (count != 1) {
					return false;
				}
			}

		}
		return retour;
	}

}
