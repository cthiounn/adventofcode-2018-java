package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day16 {
	static List<String> allMatchedOperation = new ArrayList<>();
	static int register0 = 0;
	static int register1 = 0;
	static int register2 = 0;
	static int register3 = 0;
	static String before = "";
	static String operation = "";
	static String after = "";
	static int nbline = 0;
	static int nbLineBlank = 0;
	static boolean partTwo = false;
	static boolean resetOncePartTwo = false;
	static Map<String, String> mapOpCodeToOperation = new HashMap<>();
	static List<Integer> numberOfPossibleOpCodes = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day16-input.file"));
		for (String line : input) {
			parse(line);
		}
		long numberTest = numberOfPossibleOpCodes.stream().filter(m -> m.intValue() > 2).count();

		// System.out.println(nbline);
		// System.out.println(numberOfPossibleOpCodes);
		System.out.println(numberTest);
		System.out.println(getRegister(0));
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void parse(String line) {
		if (line.contains("Before")) {
			nbLineBlank = 0;
			before = line;
			nbline++;
		} else if (line.contains("After")) {
			nbLineBlank = 0;
			after = line;
			numberOfPossibleOpCodes.add(tryOpCodes());
		} else if ("".equals(line)) {
			nbLineBlank++;
			if (nbLineBlank == 3) {
				partTwo = true;
				resolveCodes();
			}
		} else {
			if (!partTwo) {
				nbLineBlank = 0;
				operation = line;
			} else {
				// reset
				if (!resetOncePartTwo) {
					before = "Before: [0, 0, 0, 0]";
					setRegisters();
					resetOncePartTwo = true;
				}
				operation = line;
				doOperation(line);
			}
		}
	}

	private static void resolveCodes() {
		List<String> allMatchedOperationToResolve = new ArrayList<>();
		allMatchedOperationToResolve.addAll(allMatchedOperation);
		while (!allMatchedOperationToResolve.isEmpty()) {
			for (String matchedOperation : allMatchedOperationToResolve) {
				int counter = 0;
				for (int i = 0; i < matchedOperation.length(); i++) {
					counter += "!".equals(matchedOperation.charAt(i) + "") ? 1 : 0;
				}
				if (counter == 1) {
					String[] operationToRead = matchedOperation.split(",");
					mapOpCodeToOperation.put(operationToRead[0], operationToRead[1].replace("!", ""));
				}
			}
			List<String> toIterate = new ArrayList<>();
			for (String matchedOperation : allMatchedOperationToResolve) {
				String toAdd = matchedOperation;
				for (Map.Entry<String, String> entry : mapOpCodeToOperation.entrySet()) {
					toAdd = toAdd.replaceAll(entry.getKey() + ",\\d+!", "");
				}
				if (toAdd.length() > 0) {
					toIterate.add(toAdd);
				}
			}
			allMatchedOperationToResolve.clear();
			allMatchedOperationToResolve.addAll(toIterate);
		}

	}

	private static void doOperation(String line) {
		String op = line.split(" ")[0];
		if (op.equals(mapOpCodeToOperation.get("addr"))) {
			addr(operation);
		} else if (op.equals(mapOpCodeToOperation.get("addi"))) {
			addi(operation);
		} else if (op.equals(mapOpCodeToOperation.get("mulr"))) {
			mulr(operation);
		} else if (op.equals(mapOpCodeToOperation.get("muli"))) {
			muli(operation);
		} else if (op.equals(mapOpCodeToOperation.get("borr"))) {
			borr(operation);
		} else if (op.equals(mapOpCodeToOperation.get("bori"))) {
			bori(operation);
		} else if (op.equals(mapOpCodeToOperation.get("banr"))) {
			banr(operation);
		} else if (op.equals(mapOpCodeToOperation.get("bani"))) {
			bani(operation);
		} else if (op.equals(mapOpCodeToOperation.get("setr"))) {
			setr(operation);
		} else if (op.equals(mapOpCodeToOperation.get("seti"))) {
			seti(operation);
		} else if (op.equals(mapOpCodeToOperation.get("gtrr"))) {
			gtrr(operation);
		} else if (op.equals(mapOpCodeToOperation.get("gtri"))) {
			gtri(operation);
		} else if (op.equals(mapOpCodeToOperation.get("gtir"))) {
			gtir(operation);
		} else if (op.equals(mapOpCodeToOperation.get("eqrr"))) {
			eqrr(operation);
		} else if (op.equals(mapOpCodeToOperation.get("eqri"))) {
			eqri(operation);
		} else if (op.equals(mapOpCodeToOperation.get("eqir"))) {
			eqir(operation);
		}
	}

	private static Integer tryOpCodes() {
		int counter = 0;
		String MatchedOperation = "";
		setRegisters();
		addr(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "addr," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		addi(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "addi," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		mulr(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "mulr," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		muli(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "muli," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		setr(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "setr," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		seti(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "seti," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		borr(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "borr," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		bori(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "bori," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		banr(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "banr," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		bani(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "bani," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		eqrr(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "eqrr," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		eqri(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "eqri," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		eqir(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "eqir," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		gtri(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "gtri," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		gtrr(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "gtrr," + operation.split(" ")[0] + "!" : "";
		setRegisters();
		gtir(operation);
		counter += checkResults() ? 1 : 0;
		MatchedOperation += checkResults() ? "gtir," + operation.split(" ")[0] + "!" : "";

		allMatchedOperation.add(MatchedOperation);

		return counter;
	}

	private static boolean checkResults() {
		boolean isEqual = true;
		Pattern pattern = Pattern.compile("After:  \\[(\\d+), (\\d+), (\\d+), (\\d+)\\]");
		Matcher matcher = pattern.matcher(after);
		while (matcher.find()) {
			isEqual &= (getRegister(0) == Integer.parseInt(matcher.group(1)));
			isEqual &= (getRegister(1) == Integer.parseInt(matcher.group(2)));
			isEqual &= (getRegister(2) == Integer.parseInt(matcher.group(3)));
			isEqual &= (getRegister(3) == Integer.parseInt(matcher.group(4)));
		}
		return isEqual;
	}

	private static void setRegisters() {
		// Before: [1, 3, 2, 2]
		Pattern pattern = Pattern.compile("Before: \\[(\\d+), (\\d+), (\\d+), (\\d+)\\]");
		Matcher matcher = pattern.matcher(before);
		while (matcher.find()) {
			setRegister(0, Integer.parseInt(matcher.group(1)));
			setRegister(1, Integer.parseInt(matcher.group(2)));
			setRegister(2, Integer.parseInt(matcher.group(3)));
			setRegister(3, Integer.parseInt(matcher.group(4)));
		}
	}

	public static int getRegister(int i) {
		switch (i) {
		case 0:
			return register0;
		case 1:
			return register1;
		case 2:
			return register2;
		case 3:
			return register3;
		default:
			break;
		}
		return 0;
	}

	public static void setRegister(int i, int value) {
		switch (i) {
		case 0:
			register0 = value;
			break;
		case 1:
			register1 = value;
			break;
		case 2:
			register2 = value;
			break;
		case 3:
			register3 = value;
			break;
		default:
			break;
		}
	}

	// 9 2 1 2
	public static void addr(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = getRegister(Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a + b);
	}

	public static void addi(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = (Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a + b);
	}

	public static void mulr(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = getRegister(Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a * b);
	}

	public static void muli(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = (Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a * b);
	}

	public static void banr(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = getRegister(Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a & b);
	}

	public static void bani(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = (Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a & b);
	}

	public static void borr(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = getRegister(Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a | b);
	}

	public static void bori(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = (Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a | b);
	}

	public static void setr(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a);
	}

	public static void seti(String str) {
		int a = (Integer.parseInt(str.split(" ")[1]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a);
	}

	public static void gtir(String str) {
		int a = (Integer.parseInt(str.split(" ")[1]));
		int b = getRegister(Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a <= b ? 0 : 1);
	}

	public static void gtri(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = (Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a <= b ? 0 : 1);
	}

	public static void gtrr(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = getRegister(Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a <= b ? 0 : 1);
	}

	public static void eqir(String str) {
		int a = (Integer.parseInt(str.split(" ")[1]));
		int b = getRegister(Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a == b ? 1 : 0);
	}

	public static void eqri(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = (Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a == b ? 1 : 0);
	}

	public static void eqrr(String str) {
		int a = getRegister(Integer.parseInt(str.split(" ")[1]));
		int b = getRegister(Integer.parseInt(str.split(" ")[2]));
		setRegister(Integer.parseInt(str.split(" ")[3]), a == b ? 1 : 0);
	}
}
