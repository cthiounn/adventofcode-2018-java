package aoc;

public class Day14 {

	// TODO bad perf
	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		int input = 540391;
		long iteration = input * 100;
		int index = 0;
		Day14Node node0 = new Day14Node(3, index++);
		Day14Node node1 = new Day14Node(7, index++);
		node0.setNext(node1);
		node0.setPrevious(node1);
		node1.setNext(node0);
		node1.setPrevious(node0);
		int sumValue = 0;
		String sumString = "";
		Day14Node current0 = node0;
		Day14Node current1 = node1;
		Day14Node save0;
		Day14Node save1;
		for (long j = 0; j < iteration + 10; j++) {
			save0 = current0;
			save1 = current1;
			sumValue = current0.getValue() + current1.getValue();
			sumString = ("" + sumValue);
			for (int i = 0; i < sumString.length(); i++) {
				Day14Node addNode = new Day14Node(Integer.parseInt(sumString.charAt(i) + ""), index++);
				Day14Node previous = node0.getPrevious();
				node0.setPrevious(addNode);
				previous.setNext(addNode);
				addNode.setNext(node0);
				addNode.setPrevious(previous);
			}

			for (int i = 0; i < (save0.getValue() + 1); i++) {
				current0 = current0.getNext();
			}
			for (int i = 0; i < (save1.getValue() + 1); i++) {
				current1 = current1.getNext();
			}
			// showAllPlacedMarbles(current0, current1, node0);
		}
		Day14Node nodeResult = node0.move(input);
		for (int i = 0; i < 10; i++) {
			System.out.print(nodeResult.getValue());
			nodeResult = nodeResult.getNext();
		}
		System.out.println();
		// System.out.println("size=" + node0.getPrevious().getIndex());
		// System.out.println(searchSubChain(node0, "51589"));
		// System.out.println(searchSubChain(node0, "01245"));
		// System.out.println(searchSubChain(node0, "92510"));
		// System.out.println(searchSubChain(node0, "59414"));
		System.out.println(searchSubChain(node0, input + ""));
		// System.out.println(searchSubChain(node0, searchString));
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static int searchSubChain(Day14Node rootNode, String input) {
		Day14Node nodeRead = rootNode.getNext();
		while (!rootNode.getPrevious().equals(nodeRead)) {
			String str = nextI(nodeRead, input.length());
			if (str.equals(input)) {
				break;
			}
			nodeRead = nodeRead.getNext();
		}

		return nodeRead.getIndex();
	}

	private static String nextI(Day14Node nodeRead, int i) {
		String strReturn = "";
		Day14Node nodeLoop = nodeRead;
		for (int j = 0; j < i; j++) {
			strReturn += nodeLoop.getValue();
			nodeLoop = nodeLoop.getNext();
		}
		return strReturn;
	}

	private static void showAllPlacedMarbles(Day14Node lastNode, Day14Node lastNode2, Day14Node rootNode) {
		String toPrint = "";
		Day14Node nodeRead = rootNode.getNext();
		if (lastNode.equals(rootNode)) {
			toPrint += "(" + rootNode.getValue() + ")";
		} else if (lastNode2.equals(rootNode)) {
			toPrint += "[" + rootNode.getValue() + "]";
		} else {
			toPrint += "" + rootNode.getValue();
		}

		if (lastNode.equals(nodeRead)) {
			toPrint += " (" + nodeRead.getValue() + ")";
		} else if (lastNode2.equals(nodeRead)) {
			toPrint += " [" + nodeRead.getValue() + "]";
		} else {
			toPrint += " " + nodeRead.getValue();
		}

		while (!rootNode.getPrevious().equals(nodeRead)) {
			nodeRead = nodeRead.getNext();
			if (lastNode.equals(nodeRead)) {
				toPrint += " (" + nodeRead.getValue() + ")";
			} else if (lastNode2.equals(nodeRead)) {
				toPrint += " [" + nodeRead.getValue() + "]";
			} else {
				toPrint += " " + nodeRead.getValue();
			}

		}
		System.out.println(toPrint);
	}
}

class Day14Node {
	private Day14Node previous;
	private Day14Node next;
	private int value;
	private int index;

	public Day14Node(int value, int index) {
		super();
		this.value = value;
		this.index = index;
	}

	public Day14Node getPrevious() {
		return previous;
	}

	public void setPrevious(Day14Node previous) {
		this.previous = previous;
	}

	public Day14Node getNext() {
		return next;
	}

	public void setNext(Day14Node next) {
		this.next = next;
	}

	public int getValue() {
		return value;
	}

	public Day14Node move(int i) {
		Day14Node next = this;
		for (int j = 0; j < i; j++) {
			next = next.getNext();
		}
		return next;
	}

	@Override
	public String toString() {
		return "Day14Node [" + previous.getValue() + "<" + value + "<" + next.getValue() + "]";
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}