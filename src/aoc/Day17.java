package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {

	static List<String> queue = new ArrayList<>();
	static List<String> source = new ArrayList<>();
	static List<String> oldSources = new ArrayList<>();

	static int xMax;
	static int yMax;

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day17-input.file"));
		Map<String, String> grid = new HashMap<>();
		List<Integer> xmetadata = new ArrayList<>();
		List<Integer> ymetadata = new ArrayList<>();
		initGrid(grid, input, xmetadata, ymetadata);
		xMax = xmetadata.stream().max(Comparator.naturalOrder()).orElse(0);
		yMax = ymetadata.stream().max(Comparator.naturalOrder()).orElse(0);
		source.add("0!500");
		while (!source.isEmpty() || !"".equals(findLastPosStream(grid))) {
			boolean infinite = false;
			queue.clear();
			if (!source.isEmpty()) {
				String currentSource = source.remove(0);
				oldSources.add(currentSource);
				int xStreamSource = Integer.parseInt(currentSource.split("!")[0]);
				int yStreamSource = Integer.parseInt(currentSource.split("!")[1]);
				infinite = fillWater(grid, xStreamSource, yStreamSource);
			} else if (!"".equals(findLastPosStream(grid))) {
				infinite = false;
				queue.add(findLastPosStream(grid));
			}
			boolean spill = false;
			while (!spill && !infinite) {
				if (!queue.isEmpty()) {
					String lastTile = queue.remove(queue.size() - 1);
					spill = hfill(grid, Integer.parseInt(lastTile.split("!")[0]),
							Integer.parseInt(lastTile.split("!")[1]));
				} else {
					break;
				}
			}
		}

		// printDataViz(grid, xMax, yMax);
		int xMin = xmetadata.stream().min(Comparator.naturalOrder()).orElse(0);

		long numberOfWaterTiles = grid.entrySet().stream().filter(

				e -> Integer.parseInt(e.getKey().split("!")[0]) <= xMax
						&& Integer.parseInt(e.getKey().split("!")[0]) >= xMin

		).filter(e -> ("|".equals(e.getValue()) || "~".equals(e.getValue()))).count();
		System.out.println((numberOfWaterTiles));
		numberOfWaterTiles = grid.entrySet().stream().filter(

				e -> Integer.parseInt(e.getKey().split("!")[0]) <= xMax
						&& Integer.parseInt(e.getKey().split("!")[0]) >= xMin

		).filter(e -> ("~".equals(e.getValue()))).count();
		System.out.println((numberOfWaterTiles));
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static String findLastPosStream(Map<String, String> grid) {
		int xMax = 0;
		String candidate = "";
		for (Map.Entry<String, String> entry : grid.entrySet()) {
			if ("|".equals(entry.getValue())) {
				int posX = Integer.parseInt(entry.getKey().split("!")[0]);
				int posY = Integer.parseInt(entry.getKey().split("!")[1]);
				String bottom = grid.get((posX + 1) + "!" + posY);
				String left = grid.get((posX) + "!" + (posY - 1));
				String right = grid.get((posX) + "!" + (posY + 1));
				if ("~".equals(bottom) && (left == null || right == null)) {
					if (posX > xMax) {
						xMax = posX;
						candidate = entry.getKey();
					}
				}
			}
		}
		return candidate;
	}

	private static boolean fillWater(Map<String, String> grid, int xStreamSource, int yStreamSource) {
		queue.add(xStreamSource + "!" + yStreamSource);
		boolean infinite = false;
		int xCo = xStreamSource + 1;
		int yCo = yStreamSource;
		String nextBottomTile = grid.get(xCo + "!" + yCo) == null ? "" : grid.get(xCo + "!" + yCo);
		while (!("#".equals(nextBottomTile) || "~".equals(nextBottomTile))) { // vfill
			String currentTile = xCo + "!" + yCo;
			grid.put(currentTile, "|");
			nextBottomTile = grid.get((++xCo) + "!" + yCo);
			queue.add(currentTile);
			if (xCo > xMax) {
				infinite = true;
				break;
			}
		}
		return infinite;
	}

	private static boolean hfill(Map<String, String> grid, int xCo, int yCo) {
		boolean spill = false;
		List<String> spillCoordinates = new ArrayList<>();
		// left fill

		int xCoLeft = xCo;
		int yCoLeft = yCo - 1;
		String nextLeftTile = grid.get(xCoLeft + "!" + yCoLeft) == null ? "" : grid.get(xCoLeft + "!" + yCoLeft);
		while (!"#".equals(nextLeftTile)) {
			String bottomTile = grid.get((xCoLeft + 1) + "!" + yCoLeft);
			if ("#".equals(bottomTile) || "~".equals(bottomTile)) {
				grid.put(xCoLeft + "!" + yCoLeft, "~");
				nextLeftTile = grid.get(xCoLeft + "!" + (--yCoLeft));
			} else {
				grid.put(xCoLeft + "!" + yCoLeft, "|");
				spill = true;
				spillCoordinates.add(xCoLeft + "!" + yCoLeft);
				nextLeftTile = "#";
			}
		}
		// right fill
		int xCoRight = xCo;
		int yCoRight = yCo + 1;
		String nextRightTile = grid.get(xCoRight + "!" + yCoRight) == null ? "" : grid.get(xCoRight + "!" + yCoRight);
		while (!"#".equals(nextRightTile)) {
			String bottomTile = grid.get((xCoRight + 1) + "!" + yCoRight);
			if ("#".equals(bottomTile) || "~".equals(bottomTile)) {
				grid.put(xCoRight + "!" + yCoRight, "~");
				nextRightTile = grid.get(xCoRight + "!" + (++yCoRight));
			} else {
				grid.put(xCoRight + "!" + yCoRight, "|");
				spill = true;
				spillCoordinates.add(xCoRight + "!" + yCoRight);
				nextRightTile = "#";
			}
		}

		String bottomTile = grid.get((xCo + 1) + "!" + yCo);
		if ("#".equals(bottomTile) || "~".equals(bottomTile)) {
			grid.put(xCo + "!" + yCo, "~");
		}

		if (spill) {
			for (int i = yCoLeft; i <= yCoRight; i++) {
				String tile = grid.get(xCo + "!" + i);
				if ("~".equals(tile)) {
					grid.put(xCo + "!" + i, "|");
				}
			}
			for (String coord : spillCoordinates) {
				if (!source.contains(coord)) {
					source.add(coord);
				}
			}
		}
		return spill;
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

	private static void initGrid(Map<String, String> grid, List<String> input, List<Integer> xmetadata,
			List<Integer> ymetadata) {
		for (String line : input) {
			readLine(line, grid, xmetadata, ymetadata);
		}
	}

	private static void readLine(String line, Map<String, String> grid, List<Integer> xmetadata,
			List<Integer> ymetadata) {
		int x = 0;
		int xMax = 0;
		int y = 0;
		int yMax = 0;
		Pattern pattern = Pattern.compile("x=(\\d+), y=(\\d+)..(\\d+)");
		Pattern pattern2 = Pattern.compile("y=(\\d+), x=(\\d+)..(\\d+)");
		Matcher matcher = pattern.matcher(line);
		Matcher matcher2 = pattern2.matcher(line);
		while (matcher.find()) {
			x = Integer.parseInt(matcher.group(1));
			y = Integer.parseInt(matcher.group(2));
			yMax = Integer.parseInt(matcher.group(3));
			xmetadata.add(yMax);
			xmetadata.add(y);
			ymetadata.add(x);
			for (int i = y; i <= yMax; i++) {
				grid.put(i + "!" + x, "#");
			}
		}
		while (matcher2.find()) {
			y = Integer.parseInt(matcher2.group(1));
			x = Integer.parseInt(matcher2.group(2));
			xMax = Integer.parseInt(matcher2.group(3));
			ymetadata.add(xMax);
			ymetadata.add(x);
			xmetadata.add(y);
			for (int i = x; i <= xMax; i++) {
				grid.put(y + "!" + i, "#");
			}
		}
	}
}