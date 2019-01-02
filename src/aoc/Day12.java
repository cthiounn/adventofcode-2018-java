package aoc;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day12 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> inputFile = Files.readAllLines(Paths.get("src/main/resources/day12-input.file"));
		String input = (inputFile.remove(0)).split(" ")[2];
		inputFile.remove(0); // blank line
		List<String> listOfPatterns = new ArrayList<>();
		for (String line : inputFile) {
			listOfPatterns.add(line);

		}
		int numberOfiteration = 1000;
		int sizeOfSample = 100;
		String patternToMatch = "";
		String replacement = "";
		int numberOfPlant = 0;
		int oldNumber = numberOf(input);
		List<Integer> listOfDiffNumber = new ArrayList<>();
		for (int j = 0; j < numberOfiteration; j++) {
			String stringWork = "";
			input = "...." + input + "....";
			for (int i = 2; i < input.length() - 2; i++) {
				String potTargeted = input.charAt(i) + "";
				for (String line : listOfPatterns) {
					patternToMatch = line.split(" ")[0];
					replacement = line.split(" ")[2];
					if ((input.substring(i - 2, i + 3)).equals(patternToMatch)) {
						potTargeted = replacement;
					}
				}
				stringWork += potTargeted;
			}
			input = stringWork;
			numberOfPlant = numberOf(input); // new
			int deltaNbPlant = numberOfPlant - oldNumber;
			oldNumber = numberOfPlant; // old <- new
			if (j >= numberOfiteration - sizeOfSample) {
				listOfDiffNumber.add(deltaNbPlant);
			}
			if (j == 19) { // part 1 -gen20
				System.out.println(numberOfPlant);
			}
		}

		long sumGrowth = listOfDiffNumber.stream().mapToInt(Integer::intValue).sum();
		double meand = new Double(sumGrowth);
		meand = meand / listOfDiffNumber.size();
		BigDecimal meanGrowth = new BigDecimal(meand);
		BigDecimal targetNumberIteration = new BigDecimal("50000000000");
		targetNumberIteration = targetNumberIteration.subtract(new BigDecimal(numberOfiteration + ""));
		BigDecimal forecast = (targetNumberIteration.multiply(meanGrowth)).add(new BigDecimal(numberOfPlant));
		System.out.println(forecast);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static int numberOf(String s) {
		int grow = (s.length() - 100) / 2;
		int sum = 0;

		for (int i = 0; i < s.length(); i++) {
			if ((s.charAt(i) + "").equals("#")) {
				sum += i - grow;
			}
		}
		return sum;
	}
}
