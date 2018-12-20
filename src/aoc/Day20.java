package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day20 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		String input = Files.readAllLines(Paths.get("src/main/resources/day20-input.file")).get(0);
		input = minify(input);
		System.out.println(unregexifyAll(input, false));
		System.out.println(unregexifyAll(input, false).length() - 2);

		System.out.println(unregexifyAllList(input).size());
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
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

	private static List<String> unregexify2List(String string) {
		List<String> allRegex = new ArrayList<>();
		List<String> tempWork = new ArrayList<>();
		int posBeginRegex = string.lastIndexOf("(");
		int posEndRegex = (string.substring(posBeginRegex)).indexOf(")");
		tempWork = unregexifyList(string.substring(posBeginRegex, posBeginRegex + posEndRegex + 1));
		for (String string2 : tempWork) {
			String result = string.substring(0, posBeginRegex) + string2
					+ string.substring(posBeginRegex + posEndRegex + 1);
			if (result.contains("(")) {
				allRegex.add(result);
			} else if (result.length() >= (1000 - 2)) {
				allRegex.add(result);
			} else {
				System.out.println("remove toto2");
			}
		}
		return allRegex;
	}

	private static List<String> unregexifyAllList(String string) {
		List<String> allRegex = new ArrayList<>();
		List<String> tempWork = new ArrayList<>();
		tempWork.add(string);
		while (tempWork.stream().filter(e -> e.contains("(")).count() != 0) {
			String current = tempWork.remove(0);
			if (current.contains("(")) {
				tempWork.addAll(unregexify2List(current));
			} else {
				if (current.length() >= (1000 - 2)) {
					tempWork.add(current);
				} else {
					System.out.println("remove toto");
				}
			}
		}
		allRegex.addAll(tempWork);
		return allRegex;
	}
}
