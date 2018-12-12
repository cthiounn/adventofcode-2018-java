package aoc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Day12 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> a = new ArrayList<>();
		String toto = ".#..##..#.....######.....#....####.##.#.#...#...##.#...###..####.##.##.####..######......#..##.##.##";
		a.add("#.... => .");
		a.add(".##.# => #");
		a.add("#..## => .");
		a.add("....# => .");
		a.add("###.# => #");
		a.add("...#. => #");
		a.add("#...# => #");
		a.add("#.### => .");
		a.add(".#... => #");
		a.add("...## => .");
		a.add("..### => .");
		a.add("####. => .");
		a.add("##.## => .");
		a.add("..##. => .");
		a.add(".#.## => #");
		a.add("#..#. => #");
		a.add("..... => .");
		a.add("#.#.. => .");
		a.add("##.#. => #");
		a.add(".#### => #");
		a.add("##### => .");
		a.add("#.##. => #");
		a.add(".#..# => #");
		a.add("##... => .");
		a.add("..#.# => #");
		a.add("##..# => #");
		a.add(".###. => .");
		a.add(".#.#. => #");
		a.add("#.#.# => #");
		a.add("###.. => .");
		a.add(".##.. => .");
		a.add("..#.. => .");
		String pat = "";
		String replacement = "";
		int numberOfPlant = 0;
		int oldNumber = numberOf(toto);
		List<Integer> listOfDiffNumber = new ArrayList<>();
		for (int j = 0; j < 1000; j++) {
			String stringWork = "";
			toto = "...." + toto + "....";
			for (int i = 2; i < toto.length() - 2; i++) {
				String substitute = toto.charAt(i) + "";
				for (String line : a) {
					pat = line.split(" ")[0];
					replacement = line.split(" ")[2];
					if ((toto.substring(i - 2, i + 3)).equals(pat)) {
						substitute = replacement;
					}
				}
				stringWork += substitute;
			}
			toto = stringWork;
			numberOfPlant = numberOf(toto); // new
			int deltaNbPlant = numberOfPlant - oldNumber;
			oldNumber = numberOfPlant; // old <- new
			if (j >= 900) {
				listOfDiffNumber.add(deltaNbPlant);
			}
			if (j == 19) {
				System.out.println(numberOfPlant);
			}
		}
		System.out.println(numberOfPlant);
		long sum = listOfDiffNumber.stream().mapToInt(i -> i.intValue()).sum();
		System.out.println(sum);
		double meand = new Double(sum);
		meand = meand / listOfDiffNumber.size();
		System.out.println(meand);
		BigDecimal mean = new BigDecimal(meand);
		System.out.println(mean);
		BigDecimal bigNumber = new BigDecimal("50000000000");
		bigNumber.subtract(new BigDecimal("1000"));
		System.out.println(bigNumber);
		BigDecimal forecast = (bigNumber.multiply(mean)).add(new BigDecimal(numberOfPlant));
		System.out.println(bigNumber.multiply(mean));
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
