package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day21 {
	static int register0 = 0;
	static int register1 = 0;
	static int register2 = 0;
	static int register3 = 0;
	static int register4 = 0;
	static int register5 = 0;

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day21-input.file"));
		String firstline = input.remove(0);
		int indexOfIntructionPointer = 0;
		Map<Integer, Integer> mapNbOperations = new HashMap<>();
		indexOfIntructionPointer = Integer.parseInt(firstline.split(" ")[1]);

		// register0 is untouched but referenced by eqrr 3 0 4 (ip28 that breaks the
		// loop)
		// register1 is my ip
//		for (int i = 1; i < 2; i++) {
		int i = 7216956;
		int ip = 0;
		setRegister(0, i);
		setRegister(1, 0);
		setRegister(2, 0);
		setRegister(3, 0);
		setRegister(4, 0);
		setRegister(5, 0);

		int nbOfOperations = 0;
		while (ip < input.size() && nbOfOperations < 100000000) {
			if (ip == 28) { // value of register 3 = 0, that breaks the loop
				System.out.print("ip=" + ip + " ");
				System.out.print("[" + register0 + ", " + register1 + ", " + register2 + ", " + register3 + ", "
						+ register4 + ", " + register5 + "]");
				System.out.println();
			}
			setRegister(indexOfIntructionPointer, ip);
			String lineToRead = input.get(ip);
			process(lineToRead);
			ip = getRegister(indexOfIntructionPointer);
			ip++;
			nbOfOperations++;
		}
		mapNbOperations.put(i, nbOfOperations);
//		}
		int opMin = mapNbOperations.entrySet().stream().mapToInt(e -> e.getValue()).min().orElse(10000);
		int intMin = mapNbOperations.entrySet().stream().filter(e -> e.getValue() == opMin).collect(Collectors.toList())
				.get(0).getKey();
		System.out.println(opMin);
		System.out.println(intMin);

		List<Integer> listOfRegisterBreaker = new ArrayList<>();
		ip = 0;
		setRegister(0, 0);
		setRegister(1, 0);
		setRegister(2, 0);
		setRegister(3, 0);
		setRegister(4, 0);
		setRegister(5, 0);

		while (ip < input.size()) {
			if (ip == 28) { // value of register 3 = 0, that breaks the loop
				if (!listOfRegisterBreaker.contains(register3)) {
					listOfRegisterBreaker.add(register3);
				} else {
					System.out.println(listOfRegisterBreaker.get(listOfRegisterBreaker.size() - 1));
					break;
				}

			}
			setRegister(indexOfIntructionPointer, ip);
			String lineToRead = input.get(ip);
			process(lineToRead);
			ip = getRegister(indexOfIntructionPointer);
			ip++;
		}

		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void process(String lineToRead) {
		switch (lineToRead.split(" ")[0]) {
		case "addr":
			addr(lineToRead);
			break;
		case "addi":
			addi(lineToRead);
			break;
		case "mulr":
			mulr(lineToRead);
			break;
		case "muli":
			muli(lineToRead);
			break;
		case "setr":
			setr(lineToRead);
			break;
		case "seti":
			seti(lineToRead);
			break;
		case "gtrr":
			gtrr(lineToRead);
			break;
		case "gtri":
			gtri(lineToRead);
			break;
		case "gtir":
			gtir(lineToRead);
			break;
		case "eqrr":
			eqrr(lineToRead);
			break;
		case "eqri":
			eqri(lineToRead);
			break;
		case "eqir":
			eqir(lineToRead);
			break;
		case "banr":
			banr(lineToRead);
			break;
		case "bani":
			bani(lineToRead);
			break;
		case "borr":
			borr(lineToRead);
			break;
		case "bori":
			bori(lineToRead);
			break;
		default:
			break;
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
		case 4:
			return register4;
		case 5:
			return register5;
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
		case 4:
			register4 = value;
			break;
		case 5:
			register5 = value;
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
