package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Day8 {
	static int sumPart1 = 0;
	static ArrayList<Integer> bufferPrefix = new ArrayList<>();
	static ArrayList<Integer> listIntParsed = new ArrayList<>();
	static ArrayList<String> bufferPrefix2 = new ArrayList<>();
	static ArrayList<String> listString = new ArrayList<>();

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		String input = "";
		Path p = Paths.get("src/main/resources", "day8-input.file");
		try {
			input = Files.lines(p).findFirst().orElse(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] list = input.split(" ");

		for (int i = 0; i < list.length; i++) {
			listIntParsed.add(Integer.parseInt(list[i]));
			listString.add(list[i]);
		}
		while (!listIntParsed.isEmpty()) {
			part1();
		}
		System.out.println(sumPart1);
		while (!(listString.isEmpty())) {
			part2();
		}
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");

	}

	private static void part1() {
		int nbNoeud = listIntParsed.remove(0);
		int nbMeta = listIntParsed.remove(0);
		if (nbNoeud == 0) {
			for (int i = 0; i < nbMeta; i++) {
				sumPart1 += listIntParsed.remove(0);
			}
			if (bufferPrefix.size() >= 2) {
				bufferPrefix.set(bufferPrefix.size() - 2, bufferPrefix.get(bufferPrefix.size() - 2) - 1);
			}
			listIntParsed.addAll(0, bufferPrefix);
			bufferPrefix.clear();
		} else {
			bufferPrefix.add(nbNoeud);
			bufferPrefix.add(nbMeta);
		}

	}

	private static void part2() {
		if (listString.size() == 1 && listString.get(0).startsWith("#")) {// result
			System.out.println(listString.remove(0).replaceAll("#", ""));
		} else {
			String first = listString.remove(0);
			int nbNode = Integer.parseInt(first);
			int nbMeta = Integer.parseInt(listString.remove(0));
			if (nbNode == 0) {
				int sumTerminalNode = 0;
				for (int i = 0; i < nbMeta; i++) {
					sumTerminalNode += Integer.parseInt(listString.remove(0));
				}
				bufferPrefix2.add("#" + sumTerminalNode);
				listString.addAll(0, bufferPrefix2);
				bufferPrefix2.clear();
			} else if (numberAdjacentHash(listString) == nbNode) {
				int sumNode = 0;
				ArrayList<Integer> valueChildNode = new ArrayList<>();
				for (int i = 0; i < nbNode; i++) { // readValue node
					valueChildNode.add(Integer.parseInt(listString.remove(0).replace("#", "")));
				}
				for (int i = 0; i < nbMeta; i++) {
					int index = Integer.parseInt(listString.remove(0));
					if (index <= valueChildNode.size()) {
						sumNode += valueChildNode.get(index - 1);
					}
				}
				bufferPrefix2.add("#" + sumNode);
				listString.addAll(0, bufferPrefix2);
				bufferPrefix2.clear();

			} else {
				bufferPrefix2.add("" + nbNode);
				bufferPrefix2.add("" + nbMeta);
				while (listString.get(0).startsWith("#")) {
					bufferPrefix2.add(listString.remove(0));
				}
			}
		}
	}

	private static int numberAdjacentHash(ArrayList<String> chaineOriginal2) {
		int count = 0;
		for (int i = 0; i < listString.size(); i++) {
			if (listString.get(i).startsWith("#")) {
				count++;
			} else {
				break;
			}
		}
		return count;
	}
}