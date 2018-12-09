package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day5 {
	static String newInput = "";

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		String input = "";
		try {
			input = Files.readAllLines(Paths.get("src/main/resources/day5-input.file")).stream().findFirst()
					.orElse(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(part1(input));
		System.out.println(part2(input));
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");

	}

	// bad Perf
	private static int part1(String input) {
		String old = "";
		String newS = input;
		while (!old.equals(newS)) {
			old = newS;
			for (int i = 0; i < newS.length(); i++) {
				if (i < (newS.length() - 1) && isAReaction(newS.charAt(i), (char) newS.charAt(i + 1))) {
					String n = newS.substring(0, i) + newS.substring(i + 2);
					newS = n;
					break;
				}
			}
		}
		return newS.length();
	}

	private static int part2(String input) {
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		int min = 999999;
		for (int i = 0; i < alphabet.length(); i++) {
			String inputMinusLetter = input.replace(alphabet.charAt(i) + "", "");
			String inputMinusLetterAnyCase = inputMinusLetter.replace(Character.toUpperCase(alphabet.charAt(i)) + "",
					"");
			min = Math.min(min, part1(inputMinusLetterAnyCase));
		}
		return min;
	}

	private static boolean isAReaction(char a, char b) {
		boolean reactionThere = a != b && Character.toLowerCase(a) == Character.toLowerCase(b);
		return reactionThere;
	}
}
