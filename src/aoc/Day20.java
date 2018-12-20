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

		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static String minify(String input) {
		while (input.contains("EW") || input.contains("WE") || input.contains("NS") || input.contains("SN")) {
			input = input.replace("EW", "");
			input = input.replace("WE", "");
			input = input.replace("NS", "");
			input = input.replace("SN", "");
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
}
