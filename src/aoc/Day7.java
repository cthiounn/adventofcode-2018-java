package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input2 = Files.readAllLines(Paths.get("src/main/resources/day7-input.file"));
		Map<String, Node> grid = fillGrid(input2);
		part1(grid);
		part2(grid);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void part1(Map<String, Node> grid) {
		ArrayList<String> listJobsDone = new ArrayList<>();
		String jobsFinished = "";
		while (jobsFinished.length() != 26) {
			String nextJobAlpha = "";
			ArrayList<String> listOfJobsToDoFirst = new ArrayList<>();
			for (Map.Entry<String, Node> entry : grid.entrySet()) {
				ArrayList<String> listToDoBeforeForJob = entry.getValue().getPrec();
				listToDoBeforeForJob.removeAll(listJobsDone);
				if (listToDoBeforeForJob.isEmpty()) {
					if (!listOfJobsToDoFirst.contains(entry.getKey()) && !listJobsDone.contains(entry.getKey())) {
						listOfJobsToDoFirst.add(entry.getKey());
					}
				}
			}
			Collections.sort(listOfJobsToDoFirst);
			nextJobAlpha = listOfJobsToDoFirst.remove(0);
			listJobsDone.add(nextJobAlpha);
			jobsFinished += nextJobAlpha;
		}
		System.out.println(jobsFinished);
	}

	private static void part2(Map<String, Node> grid) {
		ArrayList<String> listJobsDone = new ArrayList<>();
		ArrayList<Worker> poolOfWorker = new ArrayList<>();
		poolOfWorker.add(new Worker("Dobby1"));
		poolOfWorker.add(new Worker("Dobby2"));
		poolOfWorker.add(new Worker("Dobby3"));
		poolOfWorker.add(new Worker("Dobby4"));
		poolOfWorker.add(new Worker("Dobby5"));
		String jobsFinished = "";
		int time = 0;
		while (jobsFinished.length() != 26) {
			// release first eventually
			String jobsJustFinished = finishJobsAndincreaseTimeForWorkers(poolOfWorker);
			for (int i = 0; i < jobsJustFinished.length(); i++) {
				listJobsDone.add(jobsJustFinished.substring(i, i + 1));
			}
			jobsFinished += jobsJustFinished;

			// get new jobs to do
			ArrayList<String> listOfJobsToDoFirst = new ArrayList<>();
			for (Map.Entry<String, Node> entry : grid.entrySet()) {
				ArrayList<String> listToDoBeforeForJob = entry.getValue().getPrec();
				listToDoBeforeForJob.removeAll(listJobsDone);
				if (listToDoBeforeForJob.isEmpty()) {
					if (!listOfJobsToDoFirst.contains(entry.getKey()) && !listJobsDone.contains(entry.getKey())) {
						listOfJobsToDoFirst.add(entry.getKey());
					}
				}
			}
			for (Worker w : poolOfWorker) { // if a worker is already doing X job
				listOfJobsToDoFirst.remove(w.letter);
			}
			Collections.sort(listOfJobsToDoFirst);

			// plan job for workers
			if (!listOfJobsToDoFirst.isEmpty()) {
				for (String job : listOfJobsToDoFirst) {
					Node node = grid.get(job);
					if (oneIsAvailable(poolOfWorker, node)) {
						Worker w = firstAvailable(poolOfWorker);
						w.available = false;
						w.release = node.getCost();
						w.letter = node.getLabel();
					}
				}
			}

			if (jobsFinished.length() != 26) {
				time++;
			} // else don't increase, last loop only for releasing last letter
		}

		System.out.println(time);
	}

	private static Map<String, Node> fillGrid(List<String> input2) {
		Map<String, Node> grid = new HashMap<>();
		String before = "";
		String after = "";
		for (String line : input2) {
			Pattern pattern = Pattern.compile("Step ([A-Z]) must be finished before step ([A-Z]) can begin.");
			Matcher matcher = pattern.matcher(line);
			while (matcher.find()) {
				before = matcher.group(1);
				after = matcher.group(2);
			}
			// create all alphabet from @after field
			if (grid.get(after) == null) {
				Node newNode = new Node(after);
				newNode.getPrec().add(before);
				grid.put(newNode.getLabel(), newNode);
			} else {
				Node node = grid.get(after);
				node.getPrec().add(before);
				grid.put(node.getLabel(), node);
			}
			// complete with @before field
			if (grid.get(before) == null) {
				grid.put(before, new Node(before));
			}
		}
		return grid;
	}

	private static String finishJobsAndincreaseTimeForWorkers(ArrayList<Worker> poolWorker) {
		String st = "";
		for (Worker worker : poolWorker) {
			if (!worker.available) {
				worker.time++;
				if (worker.time == worker.release) {
					worker.available = true;
					worker.release = 0;
					worker.time = 0;
					st += worker.letter;
					worker.letter = "";
				}
			}
		}
		return st;
	}

	private static Worker firstAvailable(ArrayList<Worker> poolWorker) {
		for (Worker worker : poolWorker) {
			if (worker.available) {
				return worker;
			}
		}
		return null;
	}

	private static boolean oneIsAvailable(ArrayList<Worker> poolWorker, Node c) {
		for (Worker worker : poolWorker) {
			if (worker.available) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAfterList(String c, Map<String, Node> co) {
		boolean retour = true;
		for (Map.Entry<String, Node> entry : co.entrySet()) {
			retour = retour && isAfter(c, entry.getValue());
		}
		return retour;
	}

	public static boolean isAfter(String c, Node co) {
		boolean retour = true;
		for (String string : co.getPrec()) {
			retour = retour && c.indexOf(co.getLabel()) < c.indexOf(string);
		}
		return retour;
	}

}

class Worker {
	public boolean available = true;
	public int time = 0;
	public int release = 0;
	String name;
	public String letter;

	public Worker(String name) {
		super();
		this.name = name;
	}
}

class Node {
	private String label;
	private int cost;
	private ArrayList<String> prec = new ArrayList<>();
	private boolean done = false;

	public void setLabel(String st) {
		label = st;
	}

	public String getLabel() {
		return label;
	}

	public ArrayList<String> getPrec() {
		return prec;
	}

	public void setPrec(ArrayList<String> prec) {
		this.prec = prec;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public Node(String libelle) {
		super();
		this.label = libelle;
		this.cost = stringToInt(this.label);
	}

	public static int stringToInt(String st) {
		String a = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		return 60 + a.indexOf(st) + 1;
	}

	public int getCost() {
		return cost;
	}

	@Override
	public String toString() {
		return "Node [libelle=" + label + ", cost=" + cost + ", prec=" + prec + ", done=" + done + "]";
	}
}