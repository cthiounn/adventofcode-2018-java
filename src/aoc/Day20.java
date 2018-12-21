package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day20 {

	static StringBuilder buff = new StringBuilder();
	static String begin = "";
	static String middle = "";
	static String end = "";
	static int posBeginRegex = 0;
	static int posEndRegex = 0;

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		String input = Files.readAllLines(Paths.get("src/main/resources/day20-input.file")).get(0);

		String inputPart1 = minify(input);
		System.out.println(unregexifyAll(inputPart1, false).length() - 2);
		parsePart2(input);

		// System.out.println(unregexifyAllList(input).size());
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void parsePart2(String input) {
		Map<String, Integer> grid = new HashMap<>();
		List<String> positions = new ArrayList<>();
		int x = 0;
		int y = 0;
		grid.put(x + "!" + y, 0);
		for (int i = 1; i < input.length() - 1; i++) {
			int xPrec = x;
			int yPrec = y;
			switch (input.charAt(i)) {
			case 'E':
				y += 1;
				if (grid.get(x + "!" + y) == null) {
					grid.put(x + "!" + y, grid.get(xPrec + "!" + yPrec) + 1);
				} else {
					grid.put(x + "!" + y, Math.min(grid.get(x + "!" + y), grid.get(xPrec + "!" + yPrec) + 1));
				}
				break;
			case 'W':
				y -= 1;
				if (grid.get(x + "!" + y) == null) {
					grid.put(x + "!" + y, grid.get(xPrec + "!" + yPrec) + 1);
				} else {
					grid.put(x + "!" + y, Math.min(grid.get(x + "!" + y), grid.get(xPrec + "!" + yPrec) + 1));
				}
				break;
			case 'S':
				x += 1;
				if (grid.get(x + "!" + y) == null) {
					grid.put(x + "!" + y, grid.get(xPrec + "!" + yPrec) + 1);
				} else {
					grid.put(x + "!" + y, Math.min(grid.get(x + "!" + y), grid.get(xPrec + "!" + yPrec) + 1));
				}
				break;
			case 'N':
				x -= 1;
				if (grid.get(x + "!" + y) == null) {
					grid.put(x + "!" + y, grid.get(xPrec + "!" + yPrec) + 1);
				} else {
					grid.put(x + "!" + y, Math.min(grid.get(x + "!" + y), grid.get(xPrec + "!" + yPrec) + 1));
				}
				break;
			case '(':
				positions.add(x + "!" + y);
				break;
			case '|':
				String st = positions.get(positions.size() - 1);
				x = Integer.parseInt(st.split("!")[0]);
				y = Integer.parseInt(st.split("!")[1]);
				break;
			case ')':
				positions.remove(positions.size() - 1);
				break;
			default:
				break;
			}
		}
		System.out.println(grid.entrySet().stream().filter(e -> e.getValue() >= 1000).count());
	}

	private static String minify(String input) {
		while (input.contains("(|)") || input.contains("EW") || input.contains("WE") || input.contains("NS")
				|| input.contains("SN")) {
			input = input.replace("EW", "");
			input = input.replace("WE", "");
			input = input.replace("NS", "");
			input = input.replace("SN", "");
			input = input.replace("(|)", "");
		}
		return input;
	}

	private static String unregexify(String string, boolean min) {
		List<String> allRegex = new ArrayList<>();
		if (string.indexOf("(") >= 0) {
			string = string.replace("(", "");
			string = string.replace(")", "");
			String[] listOptions = string.split("\\|");
			for (String string2 : listOptions) {
				allRegex.add(string2);
			}
		}
		String returnString = "";
		if (!min) {
			int maxLength = allRegex.stream().mapToInt(m -> m.length()).max().orElse(0);
			returnString = allRegex.stream().filter(m -> m.length() == maxLength).findFirst().orElse("");
		} else {
			int minLength = allRegex.stream().mapToInt(m -> m.length()).min().orElse(0);
			returnString = allRegex.stream().filter(m -> m.length() == minLength).findFirst().orElse("");
		}
		return returnString;
	}

	private static String unregexify2(String string, boolean min) {
		int posBeginRegex = string.lastIndexOf("(");
		int posEndRegex = (string.substring(posBeginRegex)).indexOf(")");
		String tempWork = unregexify(string.substring(posBeginRegex, posBeginRegex + posEndRegex + 1), min);
		tempWork = string.substring(0, posBeginRegex) + tempWork + string.substring(posBeginRegex + posEndRegex + 1);
		return tempWork;
	}

	private static String unregexifyAll(String string, boolean min) {
		while (string.contains("(")) {
			string = unregexify2(string, min);
		}
		return string;
	}

	private static List<String> unregexifyList(String string) {
		List<String> allRegex = new ArrayList<>();
		if (string.indexOf("(") >= 0) {
			string = string.replace("(", "");
			string = string.replace(")", "");
			String[] listOptions = string.split("\\|");
			for (String string2 : listOptions) {
				allRegex.add(string2);
			}
		}
		return allRegex;
	}

	private static String buffAppend(String m) {
		buff = new StringBuilder();
		buff.append(begin);
		buff.append(m);
		buff.append(end);
		return buff.toString();
	}

	private static List<String> unregexify2List(String string) {
		posBeginRegex = string.lastIndexOf("(");
		posEndRegex = (string.substring(posBeginRegex)).indexOf(")");
		begin = string.substring(0, posBeginRegex);
		end = string.substring(posBeginRegex + posEndRegex + 1);
		middle = string.substring(posBeginRegex, posBeginRegex + posEndRegex + 1);
		return unregexifyList(middle).stream().map(m -> buffAppend(m))
				// .filter(e -> e.replace("(", "").replace("|", "").replace(")", "").length() >=
				// 1002)
				.collect(Collectors.toList());
	}

	private static List<String> unregexifyAllList(String string) {
		List<String> allRegex = new ArrayList<>();
		Map<String, String> tempWork2 = new HashMap<>();
		Map<String, String> tempHashMap = new HashMap<>();
		tempHashMap.put(string, "");
		while (!tempHashMap.isEmpty()) {
			System.out.println(tempHashMap.size());
			tempWork2 = new HashMap<>();
			for (Map.Entry<String, String> entry : tempHashMap.entrySet()) {
				for (String string3 : unregexify2List(entry.getKey())) {
					string3 = minify(string3);
					if (!string3.contains("(") && string3.length() >= (1000 - 2) && !allRegex.contains(string3)) {
						allRegex.add(string3);
					} else if (tempHashMap.get(string3) == null && tempWork2.get(string3) == null) {
						tempWork2.put(string3, "");
					}
				}
			}
			tempHashMap.clear();
			tempHashMap.putAll(tempWork2);
		}
		return allRegex;
	}
}
