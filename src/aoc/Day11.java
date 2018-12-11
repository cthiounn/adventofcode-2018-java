package aoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Day11 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		int input = 3214;
		Map<String, Integer> grid = new HashMap<>();

		for (int i = 1; i < 301; i++) {
			for (int j = 1; j < 301; j++) {
				fillGridCalculus(input, grid, i, j);
			}
		}
		Map<String, Long> grid3x3 = new HashMap<>();
		Map<String, Long> grid3x3Prec = new HashMap<>();
		ArrayList<String> listOfMax = new ArrayList<>();
		long powerGrid3x3 = 0;
		String pos = "";
		for (int k = 1; k <= 300; k++) {
			for (int i = 1; i < 301 - k; i++) {
				for (int j = 1; j < 301 - k; j++) {
					powerGrid3x3 = grid.get((i) + "*" + (j));

					for (int j2 = 1; j2 < k; j2++) {
						powerGrid3x3 += grid.get((i + j2) + "*" + (j));
						powerGrid3x3 += grid.get((i) + "*" + (j + j2));
					}
					pos = (i + 1) + "*" + (j + 1) + "*" + (k - 1);

					powerGrid3x3 += (grid3x3Prec.get(pos) == null ? 0 : grid3x3Prec.get(pos));
					grid3x3.put(i + "*" + j + "*" + k, powerGrid3x3);
				}
			}
			String chosen = "";
			long max = 0;
			for (Map.Entry<String, Long> entry : grid3x3.entrySet()) {
				if (entry.getValue() > max) {
					max = entry.getValue();
					chosen = entry.getKey();
				}
			}
			listOfMax.add("max=" + max + ";chosen=" + chosen);
			System.out.println("max=" + max + ";chosen=" + chosen);
			grid3x3Prec = grid3x3;
			grid3x3 = new HashMap<>();
			System.out.println(k);
		}

		// printGridByNumber(grid);
		long max = grid3x3.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
		System.out.println(max);

		String chosen = "";
		max = 0;
		for (Map.Entry<String, Long> entry : grid3x3.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				chosen = entry.getKey();
			}
		}
		System.out.println("max=" + max + ";chosen=" + chosen);
		System.out.println();
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void fillGridCalculus(int input, Map<String, Integer> grid, int i, int j) {
		int powerLevel;
		powerLevel = (((i + 10) * j) + input) * (i + 10);
		String p = powerLevel + "";
		int pos = p.length() - 3;
		powerLevel = powerLevel > 99 ? (Integer.parseInt(p.charAt(pos) + "")) : 0;
		grid.put(i + "*" + j, powerLevel - 5);
	}

	private static void printGridByNumber(Map<String, Integer> grid) {
		int value;
		for (int i = 1; i < 301; i++) {
			for (int j = 1; j < 301; j++) {
				value = grid.get(i + "*" + j);
				if (j == 300) {
					System.out.println(value);
				} else {
					System.out.print(value);
				}

			}
		}
	}

}
