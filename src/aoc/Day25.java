package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day25 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day25-input.file"));
		List<SkyPoint> allPoints = new ArrayList<>();
		for (String line : input) {
			String[] lineList = line.split(",");
			SkyPoint sp = new SkyPoint(Integer.parseInt(lineList[0]), Integer.parseInt(lineList[1]),
					Integer.parseInt(lineList[2]), Integer.parseInt(lineList[3]));
			allPoints.add(sp);
		}
		int id = 1;
		while (allPoints.stream().filter(e -> e.getId() == 0).findAny().orElse(null) != null) {
			SkyPoint onePoint = allPoints.stream().filter(e -> e.getId() == 0).findAny().orElse(null);
			onePoint.setId(id);
			onePoint.checkDistance();
			id++;
		}
		System.out.println(allPoints.size());
		System.out.println(allPoints.stream().mapToInt(e -> e.getId()).max().orElse(0));
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}
}

class SkyPoint {
	static List<SkyPoint> allPoints = new ArrayList<>();
	int id = 0; // constellationId
	int x;
	int y;
	int z;
	int w;

	public SkyPoint(int x, int y, int z, int w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		allPoints.add(this);
	}

	public void checkDistance() {
		for (SkyPoint skyPoint : allPoints) {
			if (calculateManhattanDistance(skyPoint) <= 3) {
				if (skyPoint.getId() == 0) {
					skyPoint.setId(id);
					skyPoint.checkDistance();
				} else {
					setId(skyPoint.getId());
				}
			}
		}
	}

	public int calculateManhattanDistance(int i, int j, int k, int l) {
		return Math.abs(x - i) + Math.abs(y - j) + Math.abs(z - k) + Math.abs(w - l);
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

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int calculateManhattanDistance(SkyPoint o) {
		return Math.abs(x - o.getX()) + Math.abs(y - o.getY()) + Math.abs(z - o.getZ()) + Math.abs(w - o.getW());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SkyPoint [id=" + id + ", x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}

}