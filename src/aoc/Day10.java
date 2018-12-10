package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day10-input.file"));
		List<Vector> listOfVector = new ArrayList<>();
		int x = 0;
		int y = 0;
		int xVelocity = 0;
		int yVelocity = 0;
		for (String line : input) {
			line = line.replaceAll(" ", "");
			Pattern pattern = Pattern.compile("position=<(-?\\d+),(-?\\d+)>velocity=<(-?\\d+),(-?\\d+)>");
			Matcher matcher = pattern.matcher(line);

			while (matcher.find()) {
				x = Integer.valueOf(matcher.group(1));
				y = Integer.valueOf(matcher.group(2));
				xVelocity = Integer.valueOf(matcher.group(3));
				yVelocity = Integer.valueOf(matcher.group(4));

			}
			Vector v = new Vector(x, y, xVelocity, yVelocity);
			listOfVector.add(v);
		}

		int maxCumulatedDistance = 99999;
		// while (maxCumulatedDistance <= 10) {
		// for (Vector vector : listOfVector) {
		// vector.move(1000);
		// maxCumulatedDistance = Math.max(maxCumulatedDistance,
		// vector.getCumulatedDistanceWithOthers());
		// }
		// }
		printGrid(listOfVector);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	public static void printGrid(List<Vector> list) {
		int maxX = 0;
		int maxY = 0;

		int minX = 90000;
		int minY = 90000;
		int maxXold = 90000;
		int maxYold = 90000;
		boolean first = true;
		Map<String, Integer> grid = new HashMap<>();
		Map<Integer, String> gridMetadata = new HashMap<>();
		int loopIndex = 0;
		while ((maxX <= maxXold && maxY <= maxYold) || first) {
			System.out.println("l =" + loopIndex + "c=" + maxX + "*" + maxY);
			loopIndex++;
			if (!first) {
				maxXold = maxX;
				maxYold = maxY;
			}
			first = false;
			if (loopIndex % 200 == 1) {
				grid = new HashMap<>();
				gridMetadata = new HashMap<>();
			}
			minX = 90000;
			minY = 90000;
			maxX = 0;
			maxY = 0;
			for (Vector vector : list) {
				vector.move(1);
				maxX = Math.max(maxX, vector.getX());
				maxY = Math.max(maxY, vector.getY());
				minX = Math.min(minX, vector.getX());
				minY = Math.min(minY, vector.getY());
				grid.put(vector.getX() + "*" + vector.getY(), loopIndex);

			}
			System.out.println("medata=" + minX + "!" + minY + "!" + maxX + "!" + maxY);
			gridMetadata.put(loopIndex, minX + "!" + minY + "!" + maxX + "!" + maxY);
			System.out.println("l =" + loopIndex + "c=" + maxX + "*" + maxY);

		}
		for (int i = 0; i < 10; i++) {
			System.out.println("+++++++++++++++++++++++++++++++++++++");
			printGridByNumber(gridMetadata.get(loopIndex - i), grid, loopIndex - i);
		}

	}

	private static void printGridByNumber(String metadata, Map<String, Integer> grid, int loopIndex) {
		System.out.println("me=" + metadata);
		int minX = Integer.valueOf(metadata.split("!")[0]);
		int maxX = Integer.valueOf(metadata.split("!")[2]);
		int minY = Integer.valueOf(metadata.split("!")[1]);
		int maxY = Integer.valueOf(metadata.split("!")[3]);
		for (int i = minX - 1; i < maxX + 2; i++) {
			for (int j = minY - 1; j < maxY + 2; j++) {
				if (j == maxY + 1) {
					if (grid.get(i + "*" + j) == null) {
						System.out.println(".");
					} else if (grid.get(i + "*" + j) == loopIndex) {
						System.out.println("#");
					} else {
						System.out.println(".");
					}
				} else {
					if (grid.get(i + "*" + j) == null) {
						System.out.print(".");
					} else if (grid.get(i + "*" + j) == loopIndex) {
						System.out.print("#");
					} else {
						System.out.print(".");
					}
				}
			}
		}
	}

}

class Vector {
	int x;
	int y;
	int xVelocity;
	int yVelocity;
	String label = "#";
	static List<Vector> listOfVector = new ArrayList<>();
	int cumulatedDistanceWithOthers = 99999;

	public Vector(int x, int y, int xVelocity, int yVelocity) {
		super();
		this.x = x;
		this.y = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		listOfVector.add(this);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void move(int i) {
		for (int j = 0; j < i; j++) {
			x += xVelocity;
			y += yVelocity;
		}
		int dist = 0;
//		for (Vector vector : listOfVector) {
//			dist += calculateManhattanDistance(vector);
//		}
//		setCumulatedDistanceWithOthers(dist);
	}

	public int getCumulatedDistanceWithOthers() {
		return cumulatedDistanceWithOthers;
	}

	public void setCumulatedDistanceWithOthers(int cumulatedDistanceWithOthers) {
		this.cumulatedDistanceWithOthers = cumulatedDistanceWithOthers;
	}

	public int calculateManhattanDistance(Vector o) {
		return Math.abs(x - o.getX()) + Math.abs(y - o.getY());
	}

	@Override
	public String toString() {
		return "Vector [x=" + x + ", y=" + y + ", xVelocity=" + xVelocity + ", yVelocity=" + yVelocity + ", label="
				+ label + ", cumulatedDistanceWithOthers=" + cumulatedDistanceWithOthers + "]";
	}

}
