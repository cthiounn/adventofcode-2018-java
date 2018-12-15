package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.sun.org.apache.bcel.internal.classfile.PMGClass;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

public class Day15 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day15-input.file"));
		List<Player> listOfPlayers = new ArrayList<>();
		Map<String, String> grid = new HashMap<>();
		initGrid(input, grid, listOfPlayers);
		int round = 1;
		while (!isFinished(listOfPlayers)) {
			// System.out.println("Begin of turn " + round);
//			printDataViz(grid);
			listOfPlayers = listOfPlayers.stream().sorted().collect(Collectors.toList());

			for (Player player : listOfPlayers) {

				// System.out.println("Turn=" + player);
				player.move(grid);
				player.attack(grid);
			}
			System.out.println("End of turn " + round);
			listOfPlayers.clear();
			listOfPlayers.addAll(Player.listOfPlayer);
			round++;
		}
		int hpLeft = listOfPlayers.stream().mapToInt(m -> m.getHp()).sum();
		System.out.println(hpLeft);
		System.out.println(round);
		System.out.println((round - 2) * hpLeft);
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void printDataViz(Map<String, String> grid) {
		for (int i = 0; i < 33; i++) {
			for (int j = 0; j < 33; j++) {
				if (grid.get(i + "!" + j) != null) {
					System.out.print(grid.get(i + "!" + j));
				}
				if (j == 32) {
					System.out.println();
				}
			}
		}
	}

	private static boolean isFinished(List<Player> listOfPlayers) {
		boolean isAllGob = true;
		boolean isAllElf = true;
		for (Player player : listOfPlayers) {
			isAllGob &= player.isElf() ? false : true;
			isAllElf &= player.isElf() ? true : false;
		}
		return isAllElf || isAllGob;
	}

	public static void initGrid(List<String> list, Map<String, String> grid, List<Player> listOfPlayers) {
		int j = 0;
		int id = 0;
		for (String line : list) {
			for (int i = 0; i < line.length(); i++) {
				switch (line.charAt(i)) {
				case '#':
					grid.put(j + "!" + i, "#");
					break;
				case '.':
					grid.put(j + "!" + i, ".");
					break;
				case 'G':
					Player gob = new Player(id++, j, i, false);
					grid.put(j + "!" + i, "G");
					listOfPlayers.add(gob);
					break;
				case 'E':
					Player elf = new Player(id++, j, i, true);
					grid.put(j + "!" + i, "E");
					listOfPlayers.add(elf);
					break;
				default:
					break;
				}
			}
			j++;
		}
	}
}

class Player implements Comparable<Player> {
	static List<Player> listOfPlayer = new ArrayList<>();
	int id;
	int x;
	int y;
	int hp = 200;
	int attack = 3;
	int roundPlayed = 0;
	boolean dead = false;
	boolean elf;

	public Player(int id, int x, int y, boolean elf) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.elf = elf;
		listOfPlayer.add(this);
	}

	public String getPos4() {
		return (x - 1) + "!" + y;
	}

	public String getPos1() {
		return (x + 1) + "!" + y;
	}

	public String getPos2() {
		return x + "!" + (y - 1);
	}

	public String getPos3() {
		return x + "!" + (y + 1);
	}

	public String getPos() {
		return x + "!" + y;
	}

	public void move(Map<String, String> grid) {
		if (isDead()) {
			listOfPlayer.remove(this);
			grid.put(x + "!" + y, ".");
		} else if (hasAdjacentEnnemies()) {
			// dont move
		} else {// move

			String coordinate = searchBestMove(grid);
			int newX = Integer.parseInt(coordinate.split("/")[0].split("!")[0]);
			int newY = Integer.parseInt(coordinate.split("/")[0].split("!")[1]);
			grid.put(x + "!" + y, ".");
			grid.put(newX + "!" + newY, isElf() ? "E" : "G");
			x = newX;
			y = newY;
		}

	}

	private boolean hasAdjacentEnnemies() {
		List<Player> subListPlayer = listOfPlayer.stream().filter(m -> isElf() ? !m.isElf() : m.isElf())
				.collect(Collectors.toList());
		for (Player player : subListPlayer) {
			int xp = player.getX();
			int yp = player.getY();
			if ((xp + 1 == x && yp == y) || (xp - 1 == x && yp == y) || (yp + 1 == y && xp == x)
					|| (yp - 1 == y && xp == x)) {
				return true;
			}
		}
		return false;
	}

	private String searchBestMove(Map<String, String> grid) {
		String coordinateToReturn = x + "!" + y;
		List<Player> subListOfPlayer = isElf()
				? listOfPlayer.stream().filter(m -> !m.isElf()).sorted().collect(Collectors.toList())
				: listOfPlayer.stream().filter(m -> m.isElf()).sorted().collect(Collectors.toList());

//		System.out.println("--------------------------");
//		System.out.println(this);
//		for (Player player : subListOfPlayer) {
//			System.out.println(player);
//		}
		Map<String, Integer> playerAndDist = new HashMap<>();
		for (Player player : subListOfPlayer) {
			List<String> list = new ArrayList<>();
			list.addAll(pathfindingNextStep(player.getPos(), grid));
//			list.addAll(pathfindingNextStep(player.getPos1(), grid));
//			list.addAll(pathfindingNextStep(player.getPos2(), grid));
//			list.addAll(pathfindingNextStep(player.getPos3(), grid));
//			list.addAll(pathfindingNextStep(player.getPos4(), grid));
//			System.out.println(list);

			if (!list.isEmpty()) {
				coordinateToReturn = "";
				for (String string : list) {
					// if (playerAndDist.get(string.split("#")[0]) == null
					// || playerAndDist.get(string.split("#")[0]) >
					// Integer.parseInt(string.split("#")[1])) {
					playerAndDist.put(string.split("#")[0] + "/" + player.getPos(),
							Integer.parseInt(string.split("#")[1]));
					// }
				}
			}
		}
		int min = 100;
//		System.out.println("-----------------------------------------");
		List<Entry<String, Integer>> list = new ArrayList<>(playerAndDist.entrySet());
		list.sort(Entry.comparingByValue());

		Map<String, Integer> result = new LinkedHashMap<>();
		for (Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<String, Integer> entry : result.entrySet()) {
//			if (id == 7) {
//			System.out.println("k=" + entry.getKey() + "||" + entry.getValue());
//			}
			if (min > entry.getValue()) {
				coordinateToReturn = entry.getKey();
				min = entry.getValue();
			} else if (min == entry.getValue()) {
				int xTarget = Integer.parseInt(coordinateToReturn.split("/")[1].split("!")[0]);
				int yTarget = Integer.parseInt(coordinateToReturn.split("/")[1].split("!")[1]);
				int xCo = Integer.parseInt(coordinateToReturn.split("/")[0].split("!")[0]);
				int yCo = Integer.parseInt(coordinateToReturn.split("/")[0].split("!")[1]);
				int xEn = Integer.parseInt(entry.getKey().split("/")[0].split("!")[0]);
				int yEn = Integer.parseInt(entry.getKey().split("/")[0].split("!")[1]);
				int xEnTarget = Integer.parseInt(entry.getKey().split("/")[1].split("!")[0]);
				int yEnTarget = Integer.parseInt(entry.getKey().split("/")[1].split("!")[1]);
				if ((xTarget > xEnTarget) || (xTarget == xEnTarget && yTarget >= yEnTarget)) {
					if ((xCo > xEn) || (xCo == xEn && yCo > yEn)) {
						coordinateToReturn = entry.getKey();
					}
				}
			}
		}
//		System.out.println("c=" + coordinateToReturn);
		return coordinateToReturn;
	}

//	private String moveToward(Player player, Map<String, String> grid) {
//		String returnStr = x + "!" + y;
//		List<String> toto = pathfindingNextStep(player.getPos(), grid);
//		if (!toto.isEmpty()) {
//			return toto.get(0).split("#")[0];
//		}
//		return returnStr;
//	}

	private List<String> pathfindingNextStep(String posEnd, Map<String, String> grid) {
		List<String> reachable = new ArrayList<>();
		Map<String, Integer> queue = new HashMap<>();
		queue.put(posEnd, 0);
		int queueSize = queue.size();
		int oldQueue = 0;
//		int counterLoop = 0;
		while (reachable.isEmpty() && queueSize != oldQueue) {
//			counterLoop++;
			oldQueue = queueSize;
			List<String> elementToAdd = new ArrayList<>();
			for (Map.Entry<String, Integer> entry : queue.entrySet()) {
				String pos = entry.getKey();
				int posX = Integer.parseInt(pos.split("#")[0].split("!")[0]);
				int posY = Integer.parseInt(pos.split("#")[0].split("!")[1]);
				int counterDist = entry.getValue();
				counterDist++;
				String posUp = posX + "!" + (posY - 1);
				String posDown = posX + "!" + (posY + 1);
				String posLeft = (posX - 1) + "!" + posY;
				String posRight = (posX + 1) + "!" + posY;

				String posOfPlayer = x + "!" + y;
				if (posOfPlayer.equals(posUp) || posOfPlayer.equals(posDown) || posOfPlayer.equals(posLeft)
						|| posOfPlayer.equals(posRight)) {
					String element = pos + "#" + entry.getValue();
					if (!reachable.contains(element)) {
						reachable.add(element);
					}
				}

				String gridElementUp = grid.get(posUp);
				String gridElementDown = grid.get(posDown);
				String gridElementLeft = grid.get(posLeft);
				String gridElementRight = grid.get(posRight);
				switch (gridElementUp) {
				case ".":
					if (queue.get(posUp) == null || queue.get(posUp) > counterDist) {
						elementToAdd.add(posUp + "#" + counterDist);
					}
					break;

				default:
					break;
				}
				switch (gridElementDown) {
				case ".":
					if (queue.get(posDown) == null || queue.get(posDown) > counterDist) {
						elementToAdd.add(posDown + "#" + counterDist);
					}
					break;

				default:
					break;
				}
				switch (gridElementLeft) {
				case ".":
					if (queue.get(posLeft) == null || queue.get(posLeft) > counterDist) {
						elementToAdd.add(posLeft + "#" + counterDist);
					}
					break;

				default:
					break;
				}
				switch (gridElementRight) {
				case ".":
					if (queue.get(posRight) == null || queue.get(posRight) > counterDist) {
						elementToAdd.add(posRight + "#" + counterDist);
					}
					break;

				default:
					break;
				}

			}

			for (String str : elementToAdd) {
				int value = queue.get(str.split("#")[0]) == null ? 99 : queue.get(str.split("#")[0]);
				int valueToAdd = Integer.parseInt(str.split("#")[1]);
				if (value > valueToAdd) {
					queue.put(str.split("#")[0], valueToAdd);
				}
			}
			queueSize = queue.size();
			if (!reachable.isEmpty()) {
				break;
			}
		}
		reachable = reachable.stream().sorted().collect(Collectors.toList());
		return reachable;
	}

	public void attack(Map<String, String> grid) {
		if (!isDead()) {
			List<Player> gobs = listOfPlayer.stream().filter(m -> isElf() ? !m.isElf() : m.isElf())
					.collect(Collectors.toList());
			List<Player> adjacentGobs = new ArrayList<>();
			for (Player player : gobs) {
				int xPlayer = player.getX();
				int yPlayer = player.getY();

				if ((xPlayer == x) && (yPlayer + 1 == y || yPlayer - 1 == y)) {
					adjacentGobs.add(player);
				} else if ((yPlayer == y) && (xPlayer + 1 == x || xPlayer - 1 == x)) {
					adjacentGobs.add(player);
				}
			}
			if (!adjacentGobs.isEmpty()) {
				adjacentGobs = adjacentGobs.stream().sorted().collect(Collectors.toList());
				int minHp = 400;
				// System.out.println("Potentiel threats=" + adjacentGobs);
				Player gobelinToAttack = adjacentGobs.get(0);
				for (Player player : adjacentGobs) {
					if (minHp > player.getHp()) {
						minHp = player.getHp();
						gobelinToAttack = player;
					}
				}

				// System.out.println("attacking Player =" + gobelinToAttack);
				gobelinToAttack.setHp(gobelinToAttack.getHp() - getAttack());
				if (gobelinToAttack.getHp() <= 0) {
					gobelinToAttack.setDead(true);
					gobelinToAttack.move(grid); // remove from grid
				}

				// System.out.println("status of victim =" + gobelinToAttack);
			}

		} else {
			// System.out.println("died in this round :" + this);
		}
		roundPlayed++;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getRoundPlayed() {
		return roundPlayed;
	}

	public void setRoundPlayed(int roundPlayed) {
		this.roundPlayed = roundPlayed;
	}

	public boolean isDead() {
		if (getHp() <= 0) {
			return true;
		}
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public boolean isElf() {
		return elf;
	}

	public void setElf(boolean elf) {
		this.elf = elf;
	}

	@Override
	public int compareTo(Player z2) {
		if (this.getId() == z2.getId()) {
			return 0;
		}
		if (this.getX() == z2.getX()) {
			if (this.getY() > z2.getY()) {
				return 1;
			} else if (this.getY() == z2.getY()) {
				return 0;
			}
		} else if (this.getX() > z2.getX()) {
			return 1;

		}
		return -1;
	}

	@Override
	public String toString() {
		return isElf() ? "Elf :" + id + ", " + x + "!" + y + ", hp=" + hp + (dead ? ",T" : "")
				: "Gob :" + id + ", " + x + "!" + y + ", hp=" + hp + (dead ? ",T" : "");
	}

}
