package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day24 {

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day24-input.file"));
		input.remove(0);
		parse(input);

		for (Group g : Group.listOfGroup) {
			System.out.println(g.id + ";" + g.damageType + ";" + g.nbUnits + ";" + g.fire + "," + g.cold + ","
					+ g.radiation + "," + g.slashing + "," + g.bludgeoning + ";" + (g.attackDamage * g.nbUnits));
		}
		int i = 0;
		while (!isAllImmune(Group.listOfGroup)) {
			i++;
			System.out.println("Turn " + i);
			System.out.println("Immune System:");
			for (Group g : Group.listOfGroup.stream().filter(e -> !e.isDead).filter(e -> e.isImmuneType)
					.collect(Collectors.toList())) {
				System.out.println("Group " + g.id + " contains " + g.nbUnits + " units");
			}
			System.out.println("Infection:");
			for (Group g : Group.listOfGroup.stream().filter(e -> !e.isDead).filter(e -> !e.isImmuneType)
					.collect(Collectors.toList())) {
				System.out.println("Group " + g.id + " contains " + g.nbUnits + " units");
			}
			List<Group> groupToAttack = new ArrayList<Group>();
			groupToAttack.addAll(Group.listOfGroup.stream().filter(e -> !e.isDead).collect(Collectors.toList()));
			for (Group g : Group.listOfGroup.stream().filter(e -> !e.isDead).sorted(new Comparator<Group>() {

				@Override
				public int compare(Group o1, Group o2) {
					int ep1 = o1.attackDamage * o1.nbUnits;
					int ep2 = o2.attackDamage * o2.nbUnits;
					if (ep1 > ep2) {
						return -1;
					} else if (ep1 == ep2) {
						if (o1.initiative > o2.initiative) {
							return -1;
						} else if (o1.initiative == o2.initiative) {
							return 0;
						} else {
							return 1;
						}
					} else {
						return 1;
					}
				}
			}).collect(Collectors.toList())) {
				targetSelect(g, groupToAttack);
			}

			// attack
			System.out.println(" ");
			for (Group g : Group.listOfGroup.stream().filter(e -> !e.isDead)
					.sorted(Comparator.comparing(Group::getInitiative).reversed()).collect(Collectors.toList())) {

				g.attack();
			}
		}

		System.out.println(Group.listOfGroup.stream().filter(e -> !e.isDead).mapToInt(e -> e.nbUnits).sum());
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static boolean isAllImmune(List<Group> listOfGroup) {
		boolean isAllGob = true;
		boolean isAllElf = true;
		for (Group g : listOfGroup) {
			if (!g.isDead) {
				isAllGob &= g.isImmuneType ? false : true;
				isAllElf &= g.isImmuneType ? true : false;
			}
		}
		return isAllElf || isAllGob;
	}

	private static void targetSelect(Group g, List<Group> groupToAttack) {
		if (!g.isDead) {
			int maxDamage = groupToAttack.stream().filter(e -> !e.isDead)
					.filter(e -> g.isImmuneType ? !e.isImmuneType : e.isImmuneType)
					.mapToInt(e -> (int) e.inflictedDamage(g.attackDamage, g.damageType, g.nbUnits)).max().orElse(0);

			if (maxDamage != 0) {
				Group ennemy = groupToAttack.stream().filter(e -> !e.isDead)
						.filter(e -> g.isImmuneType ? !e.isImmuneType : e.isImmuneType)
						.filter(e -> e.inflictedDamage(g.attackDamage, g.damageType, g.nbUnits) == maxDamage)
						.sorted(new Comparator<Group>() {

							@Override
							public int compare(Group o1, Group o2) {
								int ep1 = o1.attackDamage * o1.nbUnits;
								int ep2 = o2.attackDamage * o2.nbUnits;

								if (ep1 > ep2) {
									return -1;
								} else if (ep1 == ep2) {
									if (o1.initiative > o2.initiative) {
										return -1;
									} else if (o1.initiative == o2.initiative) {
										return 0;
									} else {
										return 1;
									}
								} else {
									return 1;
								}
							}
						}).findFirst().orElse(null);
				if (ennemy != null) {
					g.idNextAttack = ennemy.id;
					System.out.println((g.isImmuneType ? "Immune System group " : "Infection group ") + g.id
							+ " would deal to group " + ennemy.id + " " + maxDamage + " damage");
					groupToAttack.remove(ennemy);
				}
			}
		}
	}

	private static void parse(List<String> input) {
		// Immune System:
		// 585 units each with 7536 hit points (immune to fire, radiation, cold; weak to
		// bludgeoning) with an attack that does 116 radiation damage at initiative 3

		// Infection:
		// 4401 units each with 9823 hit points (weak to bludgeoning) with an attack
		// that does 3 radiation damage at initiative 6
		boolean isImmuneType = true;
		int id = 1;
		for (String line : input) {
			if (line.contains("Infection")) {
				isImmuneType = false;
			} else {
				String parsePattern = "(\\d+) units each with (\\d+) hit points \\((.*"
						+ ")\\) with an attack that does (\\d+) (.*) damage at initiative (\\d+)";
				Pattern pattern = Pattern.compile(parsePattern);
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					int nbUnits = Integer.parseInt(matcher.group(1));
					int initiative = Integer.parseInt(matcher.group(6));
					int hp = Integer.parseInt(matcher.group(2));
					int attackDamage = Integer.parseInt(matcher.group(4));
					int damageType = getDamageType(matcher.group(5));
					String weakness = matcher.group(3);
					new Group(id, initiative, damageType, attackDamage, nbUnits, hp, isImmuneType, weakness);
				}
				id++;
			}
		}
	}

	private static int getDamageType(String group) {
		int damageType = 0; // 0 fire 1 cold 2 radiation 3 slashing 4 bludgeoning
		switch (group) {
		case "fire":
			damageType = 0;
			break;
		case "cold":
			damageType = 1;
			break;
		case "radiation":
			damageType = 2;
			break;
		case "slashing":
			damageType = 3;
			break;
		case "bludgeoning":
			damageType = 4;
			break;

		default:
			break;
		}
		return damageType;
	}

}

class Group {
	static List<Group> listOfGroup = new ArrayList<Group>();
	int id;
	int idNextAttack;
	int initiative;
	int damageType; // 0 fire 1 cold 2 radiation 3 slashing 4 bludgeoning
	int attackDamage;
	int nbUnits;
	int hp;
	boolean isImmuneType;
	boolean isDead = false;

	public Group(int id, int initiative, int damageType, int attackDamage, int nbUnits, int hp, boolean isImmuneType,
			String weakness) {
		super();
		this.id = id;
		this.initiative = initiative;
		this.damageType = damageType;
		this.attackDamage = attackDamage;
		this.nbUnits = nbUnits;
		this.hp = hp;
		this.isImmuneType = isImmuneType;
		setWeakness(weakness);
		listOfGroup.add(this);
	}

	public int getInitiative() {
		return initiative;
	}

	public void setInitiative(int initiative) {
		this.initiative = initiative;
	}

	public void attack() {
		if (!isDead) {
			Group g = getById(idNextAttack);
			if (g != null) {
				long died = Math.round((g.inflictedDamage(attackDamage, damageType, nbUnits) / g.hp));
				g.nbUnits -= died;
				System.out.println((isImmuneType ? "Immune System group " : "Infection group ") + id
						+ " attack defending group " + g.id + ", killing " + died + " units");
				if (g.nbUnits <= 0) {
					g.isDead = true;
				}
			}
			idNextAttack = 0;
		}
	}

	public int inflictedDamage(int attackDamage2, int damageType2, int nbUnits) {
		int weakness = 1;
		// 0 fire 1 cold 2 radiation 3 slashing 4 bludgeoning
		switch (damageType2) {
		case 0:
			weakness = fire;
			break;
		case 1:
			weakness = cold;
			break;
		case 2:
			weakness = radiation;
			break;
		case 3:
			weakness = slashing;
			break;
		case 4:
			weakness = bludgeoning;
			break;
		default:
			break;
		}
		return attackDamage2 * weakness * nbUnits * 1;
	}

	public Group getById(int idSelect) {
		return listOfGroup.stream().filter(e -> e.id == idSelect).findAny().orElse(null);
	}

	private void setWeakness(String weakness) {
		String[] multiple = weakness.split(";");
		for (String string : multiple) {
			int value = 1;
			if (string.contains("immune")) {
				value = 0;
			} else if (string.contains("weak")) {
				value = 2;
			}
			setWeakness(string, value);
		}
	}

	private void setWeakness(String string, int value) {
		if (string.contains("fire")) {
			fire = value;
		}
		if (string.contains("cold")) {
			cold = value;
		}
		if (string.contains("radiation")) {
			radiation = value;
		}
		if (string.contains("slashing")) {
			slashing = value;
		}
		if (string.contains("bludgeoning")) {
			bludgeoning = value;
		}
	}

	int bludgeoning = 1;
	int fire = 1;
	int radiation = 1;
	int cold = 1;
	int slashing = 1;

	@Override
	public String toString() {
		return "Group [id=" + id + ", initiative=" + initiative + ", damageType=" + damageType + ", attackDamage="
				+ attackDamage + ", nbUnits=" + nbUnits + ", hp=" + hp + ", isImmuneType=" + isImmuneType + ", isdead ="
				+ isDead + "]";
	}

}
