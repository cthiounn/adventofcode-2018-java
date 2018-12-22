package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 {

	static int xTarget = 0;
	static int yTarget = 0;
	static int depth = 0;
	static int modErosion = 20183;

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day22-input.file"));
		depth = Integer.parseInt(input.get(0).split(" ")[1]);
		xTarget = Integer.parseInt(input.get(1).split(" ")[1].split(",")[0]);
		yTarget = Integer.parseInt(input.get(1).split(" ")[1].split(",")[1]);
		Map<String, Integer> grid = new HashMap<>();
		initGrid(grid);

		long totalRiskLevel = grid.entrySet().stream().filter(e -> inGrid(e.getKey()))
				.mapToInt(e -> valueRisk(e.getValue())).sum();
		System.out.println(totalRiskLevel);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static int valueRisk(int value) {
		return value % 3;
	}

	private static boolean inGrid(String key) {
		int x = Integer.parseInt(key.split("!")[0]);
		int y = Integer.parseInt(key.split("!")[1]);

		return x <= xTarget && x >= 0 && y >= 0 && y <= yTarget;
	}

	private static void initGrid(Map<String, Integer> grid) {
		int geologicIndex = 0;
		int erosionLevel = 0;
		for (int i = 0; i < depth; i++) {
			for (int j = 0; j < depth; j++) {
				if (i == j && j == 0) {
					geologicIndex = 0;
				} else if (i == xTarget && j == yTarget) {
					geologicIndex = 0;
				} else if (j == 0) {
					geologicIndex = i * 16807;
				} else if (i == 0) {
					geologicIndex = j * 48271;
				} else {
					geologicIndex = grid.get(i + "!" + (j - 1)) * grid.get((i - 1) + "!" + j);
				}
				erosionLevel = (geologicIndex + depth) % modErosion;
				grid.put(i + "!" + j, erosionLevel);
			}
		}
	}

}