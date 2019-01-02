package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO bad perf
public class Day22 {

	static int xTarget = 0;
	static int yTarget = 0;
	static int depth = 0;
	static int modErosion = 20183;
	static String keyTarget = "";

	// static int equipement = 1; // 0 neither 1 torch 2 climbing gear

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day22-input.file"));
		depth = Integer.parseInt(input.get(0).split(" ")[1]);
		xTarget = Integer.parseInt(input.get(1).split(" ")[1].split(",")[0]);
		yTarget = Integer.parseInt(input.get(1).split(" ")[1].split(",")[1]);
		keyTarget = xTarget + "!" + yTarget;
		Map<String, Integer> grid = new HashMap<>();
		initGrid(grid);

		long totalRiskLevel = grid.entrySet().stream().filter(e -> inGrid(e.getKey()))
				.mapToInt(e -> valueRisk(e.getValue())).sum();
		System.out.println(totalRiskLevel);
		shortestPathTo(grid);
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

	private static void shortestPathTo(Map<String, Integer> grid) {
		// from 0,0
		Map<String, Integer> finalDest = new HashMap<>();
		Map<String, Integer> queueGrid = new HashMap<>();
		Map<String, Integer> newNodes = new HashMap<>();
		Map<String, Integer> gridByMinutes = new HashMap<>();
		queueGrid.put("0!0#1#0", 0);
		gridByMinutes.put("0!0", 0);
		int i = 0;
		while (!queueGrid.isEmpty()) {
			newNodes.clear();
			for (Map.Entry<String, Integer> e : queueGrid.entrySet()) {
				String node = e.getKey();
				int x = Integer.parseInt(node.split("#")[0].split("!")[0]);
				int y = Integer.parseInt(node.split("#")[0].split("!")[1]);
				int tool = Integer.parseInt(node.split("#")[1]);
				int dist = e.getValue();

				int current = grid.get(x + "!" + y) % 3;
				int bottom = grid.get((x + 1) + "!" + y) == null ? -1 : grid.get((x + 1) + "!" + y) % 3;
				int top = grid.get((x - 1) + "!" + y) == null ? -1 : grid.get((x - 1) + "!" + y) % 3;
				int left = grid.get(x + "!" + (y - 1)) == null ? -1 : grid.get(x + "!" + (y - 1)) % 3;
				int right = grid.get(x + "!" + (y + 1)) == null ? -1 : grid.get(x + "!" + (y + 1)) % 3;

				// r0 w1 n2
				// 0 ->1,2 1->0,2 2-> 0,1
				if (bottom != -1) {
					int newDist = dist;
					int newTool = intersect(current, bottom, tool);
					String key = (x + 1) + "!" + y + "#" + newTool;
					if ((current == bottom) || newTool == tool) { // same type
						newDist += 1;
					} else {
						newDist += 8;
					}
					if (gridByMinutes.get(key) == null || gridByMinutes.get(key) > newDist) {
						gridByMinutes.put(key, newDist);
						if (newNodes.get(key) == null || newNodes.get(key) > newDist) {
							newNodes.put(key, newDist);
						}
						if (key.startsWith(keyTarget)) {
							if (finalDest.get(key) == null || finalDest.get(key) > newDist) {
								finalDest.put(key, newDist);
							}
						}
					}
				}
				if (top != -1) {
					int newDist = dist;
					int newTool = intersect(current, top, tool);
					String key = (x - 1) + "!" + y + "#" + newTool;
					if (current == top || newTool == tool) { // same type
						newDist += 1;
					} else {
						newDist += 8;
					}
					if (gridByMinutes.get(key) == null || gridByMinutes.get(key) > newDist) {
						gridByMinutes.put(key, newDist);
						if (newNodes.get(key) == null || newNodes.get(key) > newDist) {
							newNodes.put(key, newDist);
						}
						if (key.startsWith(keyTarget)) {
							if (finalDest.get(key) == null || finalDest.get(key) > newDist) {
								finalDest.put(key, newDist);
							}
						}
					}
				}
				if (left != -1) {
					int newDist = dist;
					int newTool = intersect(current, left, tool);
					String key = x + "!" + (y - 1) + "#" + newTool;
					if (current == left || newTool == tool) { // same type
						newDist += 1;
					} else {
						newDist += 8;
					}
					if (gridByMinutes.get(key) == null || gridByMinutes.get(key) > newDist) {
						gridByMinutes.put(key, newDist);
						if (newNodes.get(key) == null || newNodes.get(key) > newDist) {
							newNodes.put(key, newDist);
						}
						if (key.startsWith(keyTarget)) {
							if (finalDest.get(key) == null || finalDest.get(key) > newDist) {
								finalDest.put(key, newDist);
							}
						}
					}
				}
				if (right != -1) {
					int newDist = dist;
					int newTool = intersect(current, right, tool);
					String key = x + "!" + (y + 1) + "#" + newTool;
					if (current == right || newTool == tool) { // same type
						newDist += 1;
					} else {
						newDist += 8;
					}
					if (gridByMinutes.get(key) == null || gridByMinutes.get(key) > newDist) {
						gridByMinutes.put(key, newDist);
						if (newNodes.get(key) == null || newNodes.get(key) > newDist) {
							newNodes.put(key, newDist);
						}
						if (key.startsWith(keyTarget)) {
							if (finalDest.get(key) == null || finalDest.get(key) > newDist) {
								finalDest.put(key, newDist);
							}
						}
					}
				}
			}
			queueGrid.clear();
			queueGrid.putAll(newNodes);
		}
		int minMinutes = finalDest.entrySet().stream().mapToInt(e -> e.getValue() + (e.getKey().contains("#1") ? 0 : 7))
				.min().orElse(9999999);
		System.out.println(minMinutes);
	}

	private static int intersect(int current, int bottom, int tool) {
		if ((current == 1 && bottom == 2) || (current == 2 && bottom == 1)) {
			return 0;
		} else if ((current == 0 && bottom == 2) || (current == 2 && bottom == 0)) {
			return 1;
		} else if ((current == 1 && bottom == 0) || (current == 0 && bottom == 1)) {
			return 2;
		}
		return tool;
	}
}