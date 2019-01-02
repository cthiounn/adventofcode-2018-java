package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {

	static int maxX = 0;
	static int maxY = 0;

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input2 = Files.readAllLines(Paths.get("src/main/resources/day6-input.file"));
		List<Coordinate> listRefCoordinate = fillGrid(input2);
		part1And2(listRefCoordinate);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static List<Coordinate> fillGrid(List<String> input2) {
		List<Coordinate> listRefCoordinate = new ArrayList<>();
		for (String line : input2) {
			int x = 0;
			int y = 0;
			Pattern pattern = Pattern.compile("(\\d+), (\\d+)");
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				x = Integer.parseInt(matcher.group(1));
				y = Integer.parseInt(matcher.group(2));
				maxX = Math.max(x, maxX);
				maxY = Math.max(y, maxY);
			}
			Coordinate c = new Coordinate(x, y);
			c.setLibelle(x + "*" + y);
			listRefCoordinate.add(c);
		}
		return listRefCoordinate;
	}

	private static void part1And2(List<Coordinate> listRefCoordinate) {
		Map<String, Coordinate> grid = new HashMap<>();
		Set<String> outsiders = new HashSet<>();
		for (Coordinate c : listRefCoordinate) {
			grid.put(c.getX() + "*" + c.getY(), c);
		}
		for (int i = 0; i < maxX + 1; i++) {
			for (int j = 0; j < maxY + 1; j++) {
				if (grid.get(i + "*" + j) == null) {
					Coordinate c = new Coordinate(i, j);
					Coordinate neighbourRefCoordinate = null;
					int distanceMin = 99999;
					for (Coordinate ref : listRefCoordinate) {
						c.setDistanceRef(c.getDistanceRef() + c.calculateManhattanDistance(ref));
						if (c.calculateManhattanDistance(ref) < distanceMin) {
							distanceMin = c.calculateManhattanDistance(ref);
							neighbourRefCoordinate = ref;
						} else if (c.calculateManhattanDistance(ref) == distanceMin) {
							neighbourRefCoordinate = null;
						}
					}
					c.setLibelle((neighbourRefCoordinate == null ? "." : neighbourRefCoordinate.getLibelle()));
					grid.put(i + "*" + j, c);

					if (i == 0 || i == maxX || j == 0 || j == maxY) {
						outsiders.add(c.getLibelle());
					}
				} else {
					Coordinate c = grid.get(i + "*" + j);
					for (Coordinate ref : listRefCoordinate) {
						c.setDistanceRef(c.getDistanceRef() + c.calculateManhattanDistance(ref));
					}
				}

			}
		}
		int j = 0;
		Map<String, Integer> count = new HashMap<>();
		for (Map.Entry<String, Coordinate> entry : grid.entrySet()) {
			String libelle = entry.getValue().getLibelle();
			if (!outsiders.contains(libelle)) {
				count.put(libelle, (count.get(libelle) == null ? 0 : count.get(libelle)) + 1);
			}
			int cumulatedDistance = entry.getValue().getDistanceRef();
			if (cumulatedDistance < 10000) {
				j++;
			}
		}
		int max = count.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
		System.out.println(max);
		System.out.println(j);
	}
}

class Coordinate {
	int x;
	int y;
	String libelle;
	int distanceRef;

	public int getDistanceRef() {
		return distanceRef;
	}

	public void setDistanceRef(int distanceRef) {
		this.distanceRef = distanceRef;
	}

	public void setLibelle(String st) {
		libelle = st;
	}

	@Override
	public String toString() {
		return "Coordinate [x=" + x + ", y=" + y + ", libelle=" + libelle + "]";
	}

	public String getLibelle() {
		return libelle;
	}

	public Coordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
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

	public int calculateManhattanDistance(Coordinate o) {
		return Math.abs(x - o.getX()) + Math.abs(y - o.getY());
	}
}