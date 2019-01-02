package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day23 {

	static int minX = 0;
	static int maxX = 0;
	static int minY = 0;
	static int maxY = 0;
	static int minZ = 0;
	static int maxZ = 0;

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day23-input.file"));
		parse(input);

		// part1
		Nanobot e = Nanobot.listOfNanobots.stream().sorted(Comparator.comparing(Nanobot::getRadius).reversed())
				.findFirst().orElse(null);
		int nbNearNanobots = 0;
		for (Nanobot n : Nanobot.listOfNanobots) {
			if (e.calculateManhattanDistance(n) <= e.getRadius()) {
				nbNearNanobots++;
			}
		}
		System.out.println(nbNearNanobots);

		Map<Integer, Integer> frontiers = new TreeMap<>();
		for (Nanobot n : Nanobot.listOfNanobots) {
			int distFromOrigin = n.oneDimensionFromOrigin();
			frontiers.put(distFromOrigin - n.radius, 1); // interval start
			frontiers.put(distFromOrigin + n.radius, -1); // remove interval
		}
		int count = 0;
		int maxCount = 0;
		int minDistanceForMaxPoint = 0;
		for (Map.Entry<Integer, Integer> f : frontiers.entrySet()) {
			count += f.getValue();
			if (count > maxCount) {
				minDistanceForMaxPoint = f.getKey();
				maxCount = count;
			}
		}
		System.out.println(minDistanceForMaxPoint);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void parse(List<String> input) {
		int id = 0;
		for (String line : input) {
			Pattern pattern = Pattern.compile("pos=<(-?\\d+),(-?\\d+),(-?\\d+)>, r=(\\d+)");
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				int x = Integer.parseInt(matcher.group(1));
				int y = Integer.parseInt(matcher.group(2));
				int z = Integer.parseInt(matcher.group(3));
				int radius = Integer.parseInt(matcher.group(4));
				new Nanobot(id, x, y, z, radius, true);
			}
			id++;
		}
	}

}

class Nanobot {

	static List<Nanobot> listOfNanobots = new ArrayList<>();
	int id;
	int x;
	int y;
	int z;
	int radius;
	int min;
	int max;
	int nbNearBot;

	public Nanobot(int id, int x, int y, int z, int radius, boolean nanobot) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		this.min = oneDimensionFromOrigin() - radius;
		this.max = oneDimensionFromOrigin() + radius;
		if (nanobot) {
			listOfNanobots.add(this);
		}
	}

	public int checkDist() {
		int nbNearNanobots = 0;
		for (Nanobot n : Nanobot.listOfNanobots) {
			if (this.calculateManhattanDistance(n) <= n.getRadius()) {
				nbNearNanobots++;
			}
		}
		this.nbNearBot = nbNearNanobots;
		return this.nbNearBot;
	}

	public int oneDimensionFromOrigin() {
		return calculateManhattanDistance(0, 0, 0);
	}

	public int calculateManhattanDistance(int i, int j, int k) {
		return Math.abs(x - i) + Math.abs(y - j) + Math.abs(z - k);
	}

	public static List<Nanobot> getListOfNanobots() {
		return listOfNanobots;
	}

	public static void setListOfNanobots(List<Nanobot> listOfNanobots) {
		Nanobot.listOfNanobots = listOfNanobots;
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

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int calculateManhattanDistance(Nanobot o) {
		return Math.abs(x - o.getX()) + Math.abs(y - o.getY()) + Math.abs(z - o.getZ());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Nanobot [id=" + id + ", x=" + x + ", y=" + y + ", z=" + z + ", radius=" + radius + "]";
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public boolean equals(Nanobot b) {
		return x == b.getX() && y == b.getY() && z == b.getZ();
	}

}
