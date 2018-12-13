package aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day13 {

	private static List<Cart> listOfCart = new ArrayList<>();
	private static boolean first = false;

	public static void main(String[] args) throws IOException {
		long timeStart = System.currentTimeMillis();
		List<String> input = Files.readAllLines(Paths.get("src/main/resources/day13-input.file"));
		Map<String, String> grid = new HashMap<>();
		initGrid(input, grid);
		while (listOfCart.size() != 1) {
			moveCart(grid);
			removeCrash();
		}
		System.out.println(listOfCart.get(0).getX() + "," + listOfCart.get(0).getY());
		System.out.println("runned time : " + (System.currentTimeMillis() - timeStart) + " ms");
	}

	private static void removeCrash() {
		List<Cart> listOfCartNew = new ArrayList<>();
		for (Cart cart : listOfCart) {
			if (!cart.isCrashed()) {
				listOfCartNew.add(cart);
			} else {
				Cart.allCart.remove(cart);
				if (!first) {
					first = true;
					System.out.println(cart.getX() + "," + cart.getY());
				}
			}
		}
		listOfCart.clear();
		listOfCart.addAll(listOfCartNew);
	}

	private static void moveCart(Map<String, String> grid) {
		Collections.sort(listOfCart, new Comparator<Cart>() {
			@Override
			public int compare(Cart z1, Cart z2) {
				if (z1.getId() == z2.getId()) {
					return 0;
				}
				if (z1.getX() == z2.getX()) {
					if (z1.getY() > z2.getY()) {
						return 1;
					} else if (z1.getY() == z2.getY()) {
						return 0;
					}
				} else if (z1.getX() > z2.getX()) {
					return 1;

				}
				return -1;
			}
		});

		for (Cart cart : listOfCart) {
			cart.move(grid);
		}
	}

	public static void initGrid(List<String> list, Map<String, String> grid) {
		int j = 0;
		int id = 0;
		for (String line : list) {
			for (int i = 0; i < line.length(); i++) {
				String circuitLine = line.charAt(i) + "";
				if ("^".equals(circuitLine)) {
					Cart cart = new Cart(i, j, 1, ++id);
					listOfCart.add(cart);
					circuitLine = "|";
				} else if ("v".equals(circuitLine)) {
					Cart cart = new Cart(i, j, 3, ++id);
					listOfCart.add(cart);
					circuitLine = "|";
				} else if ("<".equals(circuitLine)) {
					Cart cart = new Cart(i, j, 0, ++id);
					listOfCart.add(cart);
					circuitLine = "-";
				} else if (">".equals(circuitLine)) {
					Cart cart = new Cart(i, j, 2, ++id);
					listOfCart.add(cart);
					circuitLine = "-";
				}
				grid.put(i + "!" + j, circuitLine);
			}
			j++;
		}
	}

}

class Cart {
	static List<Cart> allCart = new ArrayList<>();
	int x;
	int y;
	boolean crashed = false;
	int id;
	int nextDirection = 0; // 0 left 1 straight 2right
	int facing = 0; // 0 left 1 up 2 right 3 down

	public Cart(int x, int y, int facing, int id) {
		super();
		this.x = x;
		this.y = y;
		this.facing = facing;
		this.id = id;
		allCart.add(this);
	}

	public Cart() {
		super();
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

	public int getNextDirection() {
		return nextDirection;
	}

	public void setNextDirection(int nextDirection) {
		this.nextDirection = nextDirection;
	}

	@Override
	public String toString() {
		return "Cart [id = " + id + ";c=" + crashed + ";x=" + x + ", y=" + y + ", nextDirection=" + nextDirection
				+ ", facing=" + facing + "]";
	}

	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
	}

	public void move(Map<String, String> grid) {

		if (isCrashed()) { // dont move
		} else {
			String track = "";
			switch (facing) {
			case 0:
				if (grid.get((x - 1) + "!" + y) != null) {
					track = grid.get((x - 1) + "!" + y);
					x = x - 1;
				}
				break;
			case 1:
				if (grid.get(x + "!" + (y - 1)) != null) {
					track = grid.get(x + "!" + (y - 1));
					y = y - 1;
				}
				break;
			case 2:
				if (grid.get((x + 1) + "!" + y) != null) {
					track = grid.get((x + 1) + "!" + y);
					x = x + 1;
				}
				break;
			case 3:
				if (grid.get(x + "!" + (y + 1)) != null) {
					track = grid.get(x + "!" + (y + 1));
					y = y + 1;
				}
				break;
			default:
				break;
			}
			if ("+".equals(track)) {
				if (3 == facing) {
					// down
					if (nextDirection == 0) {
						facing = 2;
					} else if (nextDirection == 1) {
						// same facing
					} else if (nextDirection == 2) {
						facing = 0;
					}
				} else if (2 == facing) {
					// right
					if (nextDirection == 0) {
						facing = 1;
					} else if (nextDirection == 1) {
						// same facing
					} else if (nextDirection == 2) {
						facing = 3;
					}
				} else if (1 == facing) {
					// up
					if (nextDirection == 0) {
						facing = 0;
					} else if (nextDirection == 1) {
						// same facing
					} else if (nextDirection == 2) {
						facing = 2;
					}
				} else if (0 == facing) {
					// left
					if (nextDirection == 0) {
						facing = 3;
					} else if (nextDirection == 1) {
						// same facing
					} else if (nextDirection == 2) {
						facing = 1;
					}
				}
				// steer
				nextDirection = (nextDirection + 1);
				nextDirection %= 3;
			} else if ("/".equals(track)) {
				if (3 == facing) {
					// down to left
					facing = 0;
				} else if (2 == facing) {
					// right to up
					facing = 1;
				} else if (1 == facing) {
					// up to right
					facing = 2;
				} else if (0 == facing) {
					// left to down
					facing = 3;
				}

			} else if ("\\".equals(track)) {
				if (3 == facing) {
					// down to right
					facing = 2;
				} else if (2 == facing) {
					// right to d
					facing = 3;
				} else if (1 == facing) {
					// up to left
					facing = 0;
				} else if (0 == facing) {
					// left to up
					facing = 1;
				}
			}
		}
		for (Cart cart : allCart) {
			if (cart.getId() == id) {
				// skip
			} else {
				this.detectCartCrashed(cart);
			}
		}
	}

	private void detectCartCrashed(Cart o) {
		if (o.getX() == this.getX() && o.getY() == this.getY()) {
			o.setCrashed(true);
			this.setCrashed(true);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isCrashed() {
		return crashed;
	}

	public void setCrashed(boolean crashed) {
		this.crashed = crashed;
	}
}
