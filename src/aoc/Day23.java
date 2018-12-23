package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

		// part2

		Set<Nanobot> pointsToCheckSet = new HashSet<>();
		Set<Nanobot> pointsToCheckSet2 = new HashSet<>();
		Map<Nanobot, Integer> gridNanobot = new HashMap<>();

		pointsToCheckSet.addAll(Nanobot.listOfNanobots);
		int newMax = 0;
		int maxNb = 1;
		int i = 0;
		while (!pointsToCheckSet.isEmpty() && (maxNb != newMax || i < 10)) {
			if (maxNb == newMax) {
				i++;
			}
			maxNb = newMax;
			gridNanobot.clear();
			pointsToCheckSet2.clear();
			for (Nanobot n : pointsToCheckSet) {
				n.checkDist();
				gridNanobot.put(n, n.nbNearBot);
				for (Nanobot m : pointsToCheckSet) {
					Nanobot newPoint = newPoints(n, m);
					if (!pointsToCheckSet2.contains(newPoint)) {
						pointsToCheckSet2.add(newPoint);
					}
				}
			}
			newMax = pointsToCheckSet2.stream().mapToInt(f -> f.checkDist()).max().orElse(0);
			final int ref = newMax;
			System.out.println(ref);
			pointsToCheckSet.clear();
			pointsToCheckSet.addAll(Nanobot.listOfNanobots);
			pointsToCheckSet
					.addAll(pointsToCheckSet2.stream().filter(f -> f.nbNearBot == ref).collect(Collectors.toList()));
		}
		System.out.println("final=" + newMax);
//		int tempMax = gridNanobot.entrySet().stream().mapToInt(g -> g.getValue()).max().orElse(0);
//		System.out.println("final=" + tempMax);
//		List<Nanobot> candidatesPoints = gridNanobot.entrySet().stream().filter(g -> g.getValue() == tempMax)
//				.map(g -> g.getKey()).collect(Collectors.toList());
		final int ref = newMax;
		for (Nanobot nanobot : pointsToCheckSet2.stream().filter(f -> f.nbNearBot == ref)
				.collect(Collectors.toList())) {
			crawl(nanobot, nanobot.oneDimensionFromOrigin(), newMax);
		}
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void crawl(Nanobot nanobot, int oneDimensionFromOrigin, int tempMax) {
		int x = 0;
		int y = 0;
		int z = 0;
		int newx = nanobot.getX();
		int newy = nanobot.getY();
		int newz = nanobot.getZ();
		int lowerPoint = oneDimensionFromOrigin;
		while (x != newx && y != newy && z != newz) {
			x = newx;
			y = newy;
			z = newz;
			for (int i = -100; i < 100; i++) {
				for (int j = -100; j < 100; j++) {
					for (int j2 = -100; j2 < 100; j2++) {
						if (Math.abs(x + i) + Math.abs(y + j) + Math.abs(z + j2) < lowerPoint) {
							int nbNearNanobots = 0;
							for (Nanobot n : Nanobot.listOfNanobots) {
								if (n.calculateManhattanDistance(x + i, y + j, z + j2) <= n.getRadius()) {
									nbNearNanobots++;
								}
							}
							if (nbNearNanobots >= tempMax) {
								newz = z + j2;
								newy = y + j;
								newx = x + i;
								tempMax = nbNearNanobots;
								lowerPoint = Math.abs(x + i) + Math.abs(y + j) + Math.abs(z + j2);
							}
						}
					}
				}
			}
		}
		System.out.println("x=" + x + "," + y + "," + z + ";" + tempMax + ";" + lowerPoint);
	}

	private static Nanobot newPoints(Nanobot n, Nanobot f) {
		return new Nanobot(777, (n.getX() + f.getX()) / 2, (n.getY() + f.getY()) / 2, (n.getZ() + f.getZ()) / 2, 0,
				false);
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
