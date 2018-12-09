package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day4 {
	private final static String SEPARATOR = "!";

	public static void main(String[] args) throws IOException, ParseException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day4-input.file"));
		Collections.sort(input);
		part1(input);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");

	}

	public static void part1(List<String> lines) throws ParseException {
		Map<String, Integer> map = new HashMap<>();
		List<Guard> listGuard = new ArrayList<>();
		for (String line : lines) {
			parseLine(line, listGuard, map);
		}

		int idSleepiestGuard = 0;
		long maxSleepTotal = 0;
		for (Guard guard : listGuard) {
			if (maxSleepTotal < guard.getTotalSleep()) {
				idSleepiestGuard = guard.getId();
				maxSleepTotal = guard.getTotalSleep();
			}
		}
		final int idSleepiestGuardFinal = idSleepiestGuard;
		int maxMinForSleepiestGuard = 0;
		int maxSleepByMinute = 0;
		int maxMaxSleepByMinute = 0;
		String resultPart2ToParse = "";
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (maxSleepByMinute < entry.getValue() && entry.getKey().contains(idSleepiestGuardFinal + SEPARATOR)) {
				maxSleepByMinute = entry.getValue();
				maxMinForSleepiestGuard = Integer.valueOf(entry.getKey().split(SEPARATOR)[1]);
			}
			if (maxMaxSleepByMinute < entry.getValue()) {
				maxMaxSleepByMinute = entry.getValue();
				resultPart2ToParse = entry.getKey();
			}
		}
		System.out.println((idSleepiestGuard * maxMinForSleepiestGuard));
		System.out.println((Integer.valueOf(resultPart2ToParse.split(SEPARATOR)[0])
				* Integer.valueOf(resultPart2ToParse.split(SEPARATOR)[1])));
	}

	public static void parseLine(String str, List<Guard> listGuards, Map<String, Integer> minutes)
			throws ParseException {
		String[] data = str.split("]");
		String date = data[0].substring(1);
		String info = data[1];
		if (info.contains("Guard")) {
			final int id = Integer.valueOf(info.split("#")[1].split(" ")[0]);
			Guard suspect = listGuards.stream().filter(g -> g.getId() == id).findFirst().orElse(null);
			if (suspect != null) {
				listGuards.remove(suspect);
				listGuards.add(suspect);
			} else {
				listGuards.add(new Guard(id));
			}

		} else if (info.contains("falls")) {
			SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			listGuards.get(listGuards.size() - 1).setDateSleep(parser.parse(date));
			listGuards.get(listGuards.size() - 1).setAwaken(false);
		} else {
			SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date dateAwake = parser.parse(date);
			Date dateSleep = listGuards.get(listGuards.size() - 1).getDateSleep();
			long dateTotal = (dateAwake.getTime() - dateSleep.getTime());
			listGuards.get(listGuards.size() - 1).setAwaken(true);
			long total = listGuards.get(listGuards.size() - 1).getTotalSleep();
			int id = listGuards.get(listGuards.size() - 1).getId();
			for (int i = dateSleep.getMinutes(); i < dateAwake.getMinutes(); i++) {
				minutes.put(id + SEPARATOR + i,
						(minutes.get(id + SEPARATOR + i) == null ? 0 : minutes.get(id + SEPARATOR + i)) + 1);
			}
			listGuards.get(listGuards.size() - 1).setTotalSleep(total + (dateTotal / 60000));
		}
	}

}

class Guard {
	private int id;
	private Date dateSleep;
	private long totalSleep = 0;
	private boolean awaken = true;

	public Guard(int id) {
		super();
		this.id = id;
	}

	public Date getDateSleep() {
		return dateSleep;
	}

	public void setDateSleep(Date dateSleep) {
		this.dateSleep = dateSleep;
	}

	public long getTotalSleep() {
		return totalSleep;
	}

	public void setTotalSleep(long totalSleep) {
		this.totalSleep = totalSleep;
	}

	public boolean isAwaken() {
		return awaken;
	}

	public void setAwaken(boolean awaken) {
		this.awaken = awaken;
	}

	public int getId() {
		return this.id;
	}

}