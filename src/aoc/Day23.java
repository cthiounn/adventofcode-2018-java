package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
		Nanobot e = Nanobot.listOfNanobots.stream().sorted(Comparator.comparing(Nanobot::getRadius).reversed())
				.findFirst().orElse(null);
		int nbNearNanobots = 0;
		for (Nanobot n : Nanobot.listOfNanobots) {
			if (e.calculateManhattanDistance(n) <= e.getRadius()) {
				nbNearNanobots++;
			}
		}
		System.out.println(nbNearNanobots);
		Map<Nanobot, Integer> gridNanobot = new HashMap<>();
		for (Nanobot f : Nanobot.listOfNanobots) {
			nbNearNanobots = 0;
			for (Nanobot n : Nanobot.listOfNanobots) {
				if (f.calculateManhattanDistance(n) <= f.getRadius()) {
					nbNearNanobots++;
				}
			}
//			System.out.println("put=" + f + "," + nbNearNanobots);
			gridNanobot.put(f, nbNearNanobots);
		}
		int maxNearBots = gridNanobot.entrySet().stream().mapToInt(f -> f.getValue()).max().orElse(0);
		Nanobot centerNanobot = gridNanobot.entrySet().stream().filter(f -> f.getValue() == maxNearBots).findFirst()
				.orElse(null).getKey();
		int x = centerNanobot.getX();
		int y = centerNanobot.getY();
		int z = centerNanobot.getZ();
		int radius = centerNanobot.getRadius();
//		System.out.println(gridNanobot);
//		minX = Nanobot.listOfNanobots.stream().mapToInt(g -> g.getX()).min().orElse(0);
//		maxX = Nanobot.listOfNanobots.stream().mapToInt(g -> g.getX()).max().orElse(0);
//		minY = Nanobot.listOfNanobots.stream().mapToInt(g -> g.getY()).min().orElse(0);
//		maxY = Nanobot.listOfNanobots.stream().mapToInt(g -> g.getY()).max().orElse(0);
//		minZ = Nanobot.listOfNanobots.stream().mapToInt(g -> g.getZ()).min().orElse(0);
//		maxZ = Nanobot.listOfNanobots.stream().mapToInt(g -> g.getZ()).max().orElse(0);
		Map<String, Integer> grid = new HashMap<>();
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				for (int k = -radius; k <= radius; k++) {
					int nb = 0;
					for (Nanobot n : Nanobot.listOfNanobots) {
						if (n.calculateManhattanDistance(i, j, k) <= n.getRadius()) {
							nb++;
						}
					}
					if (nb > maxNearBots) {
						grid.put(i + "!" + j + "!" + k, nb);
					}
				}
			}
		}
		System.out.println(grid.size());
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
				new Nanobot(id, x, y, z, radius);
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

	public Nanobot(int id, int x, int y, int z, int radius) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		listOfNanobots.add(this);
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

}