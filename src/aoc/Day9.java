package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day9 {

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		String input = "";
		Path p = Paths.get("src/main/resources", "day9-input.file");
		try (Stream<String> st = Files.lines(p)) {
			input = st.findFirst().orElse(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Pattern pattern = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points");
		Matcher matcher = pattern.matcher(input);
		int players = 0; // = 448;
		int baseMarble = 0; // = 71628;
		while (matcher.find()) {
			players = Integer.parseInt(matcher.group(1));
			baseMarble = Integer.parseInt(matcher.group(2));
		}
		// 448 players; last marble is worth 71628 points

		Map<Integer, Long> gridScore = new HashMap<>();
		int multiplyBy = 100;
		int countMarble = 0;
		int score = 0;
		int currentPlayer;
		Day9Node rootNode = new Day9Node(0);
		rootNode.setNext(rootNode);
		rootNode.setPrevious(rootNode);

		Day9Node lastNode = rootNode;
		for (countMarble = 1; countMarble <= baseMarble * multiplyBy; countMarble++) {
			score = 0;
			currentPlayer = countMarble % players;
			Day9Node nextNode = lastNode.getNext(); // +1
			Day9Node nextNextNode = nextNode.getNext(); // +1
			if (countMarble % 23 == 0) {
				Day9Node noeud7 = lastNode;
				for (int i = 0; i < 7; i++) {
					noeud7 = noeud7.getPrevious();
				}
				int marble7 = noeud7.getValue();
				score = countMarble + marble7;
				Day9Node previous = noeud7.getPrevious();
				Day9Node next = noeud7.getNext();
				next.setPrevious(previous);
				previous.setNext(next);
				lastNode = next;
			} else {
				Day9Node newNode = new Day9Node(countMarble);
				newNode.setNext(nextNextNode);
				newNode.setPrevious(nextNode);
				nextNextNode.setPrevious(newNode);
				nextNode.setNext(newNode);
				lastNode = newNode;
			}

			if (score != 0) {
				long scorePlayer = gridScore.get(currentPlayer) == null ? 0 : gridScore.get(currentPlayer);
				gridScore.put(currentPlayer, scorePlayer + score);
			}

			// showAllPlacedMarbles(lastNode, rootNode, currentPlayer);
			if (countMarble == baseMarble) {
				long max = gridScore.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
				System.out.println(max);
			}
		}

		long max = gridScore.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();

		System.out.println(max);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void showAllPlacedMarbles(Day9Node lastNode, Day9Node rootNode, int currentPlayer) {
		String toPrint = "[" + currentPlayer + "] " + rootNode.getValue() + "";
		Day9Node nodeRead = rootNode.getNext();
		if (lastNode.equals(nodeRead)) {
			toPrint += " (" + nodeRead.getValue() + ")";
		} else {
			toPrint += " " + nodeRead.getValue();
		}

		while (!rootNode.getPrevious().equals(nodeRead)) {
			nodeRead = nodeRead.getNext();
			if (lastNode.equals(nodeRead)) {
				toPrint += " (" + nodeRead.getValue() + ")";
			} else {
				toPrint += " " + nodeRead.getValue();
			}

		}
		System.out.println(toPrint);
	}

}

class Day9Node {
	private Day9Node previous;
	private Day9Node next;
	private int value;

	public Day9Node(int valeur) {
		super();
		this.value = valeur;
	}

	public Day9Node getPrevious() {
		return previous;
	}

	public void setPrevious(Day9Node previous) {
		this.previous = previous;
	}

	public Day9Node getNext() {
		return next;
	}

	public void setNext(Day9Node next) {
		this.next = next;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "Day9Node [" + previous.getValue() + "<" + value + "<" + next.getValue() + "]";
	}

}