package Flatland;

import java.util.*;
import java.io.*;

public class World {

	public int X, Y;
	public String[][] map;
	private List<String> states;
	public static final int MAX_MOVES = 50;
	private int food;
	private int poison;
	private int steps;

	private Random random;

	private World() {
		states = new ArrayList<String>();
		food = 0;
		poison = 0;
		steps = 0;
	}

	public World(int maptype) { // static runs
		this();
		switch (maptype) {
			case 1:
				map = map1();
				break;
			case 2:
				map = map2();
				break;
			case 3:
				map = map3();
				break;
			case 4:
				map = map4();
				break;
			default:
				map = map5();
		}

		states.add(this.toString());
		locatePlayer();
	}
	public World(long randomSeed, double food_percent, double poison_percent) { // dynamic runs
		this();
		this.random = new Random(randomSeed);
		map = createMap(food_percent, poison_percent);

		states.add(this.toString());
		locatePlayer();
	}

	public void locatePlayer() {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				if(map[i][j] == "rf" || map[i][j] == "rd" || map[i][j] == "rr" || map[i][j] == "rl") {
					this.X = j;
					this.Y = i;
				}
			}
		}
	}

	public boolean finished() {
		return states.size() >= MAX_MOVES || steps >= 1000;
	}

	public int[] getScores() { // [+, -]
		int[] result = {food, poison};
		return result;
	}

	public void printToFile() {
		try {
			PrintWriter writer = new PrintWriter("src/main/javascript/flatland/output.txt", "UTF-8");
			writer.println(this.allStatesToJSON());
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void doMove(char move) {
		steps++;
		char prevState = map[Y][X].charAt(1);

		if(prevState == 'f') {
			if(move == 'f') {
				doMove(0,-1);
			} else if(move == 'l') {
				doMove(-1, 0);
			} else if(move == 'r') {
				doMove(1, 0);
			}
		}
		else if(prevState == 'l') {
			if(move == 'f') {
				doMove(-1,0);
			} else if(move == 'l') {
				doMove(0, 1);
			} else if(move == 'r') {
				doMove(0, -1);
			}
		}
		else if(prevState == 'r') {
			if(move == 'f') {
				doMove(1,0);
			} else if(move == 'l') {
				doMove(0, -1);
			} else if(move == 'r') {
				doMove(0, 1);
			}
		}
		else if(prevState == 'd') {
			if(move == 'f') {
				doMove(0, 1);
			} else if(move == 'l') {
				doMove(1, 0);
			} else if(move == 'r') {
				doMove(-1, 0);
			}
		}
	}

	public void doMove(int dx, int dy) {
		try {
			char prevState = map[Y][X].charAt(1);
			char newState = 'f';


			if(prevState == 'f') {
				if(dx == 1) {
					newState = 'r';
				} else if(dx == -1) {
					newState = 'l';
				} else {
					newState = 'f';
				}
			} else if(prevState == 'r') {
				if(dy == 1) {
					newState = 'd';
				} else if(dy == -1) {
					newState = 'f';
				}
				else {
					newState = 'r';
				}
			} else if(prevState == 'd') {
				if(dx == 1) {
					newState = 'r';
				} else if(dx == -1) {
					newState = 'l';
				}
				else {
					newState = 'd';
				}
			} else if(prevState == 'l') {
				if(dy == 1) {
					newState = 'd';
				} else if(dy == -1) {
					newState = 'f';
				} else {
					newState = 'l';
				}
			}


			if(map[Y + dy][X + dx].equals("p")) {
				poison++;
			} else if(map[Y + dy][X + dx].equals("f")) {
				food++;
			}

			map[Y + dy][X + dx] = "r" + newState;
			map[Y][X] = "";
			Y += dy;
			X += dx;

			states.add(this.toString());

		} catch(ArrayIndexOutOfBoundsException e) {
		}
		
	}

	public int[] getEnvironment() { // returns (front, left, right) [foodfront, foodleft, foodright, poisonfront, poisonleft, poisionright]
		List<String> env = new ArrayList<String>();
		char state = map[Y][X].charAt(1);

		int foodfront = 0, foodleft = 0, foodright = 0, poisonfront = 0, poisonleft = 0, poisonright = 0;

		try {
			switch(state) {
			case 'f':
				env.add(map[Y-1][X]);
				env.add(map[Y][X-1]);
				env.add(map[Y][X+1]);
				break;
			case 'd':
				env.add(map[Y-1][X]);
				env.add(map[Y][X+1]);
				env.add(map[Y][X-1]);
				break;
			case 'l':
				env.add(map[Y][X-1]);
				env.add(map[Y+1][X]);
				env.add(map[Y-1][X]);
				break;
			case 'r':
				env.add(map[Y][X+1]);
				env.add(map[Y-1][X]);
				env.add(map[Y+1][X]);
				break;
			};

			
			if(env.get(0).equals("f")) {
				foodfront = 1;
			} else if(env.get(0).equals("p")) {
				poisonfront = 1;
			}

			if(env.get(1).equals("f")) {
				foodleft = 1;
			} else if(env.get(1).equals("p")) {
				poisonleft = 1;
			}

			if(env.get(2).equals("f")) {
				foodright = 1;
			} else if(env.get(2).equals("p")) {
				poisonright = 1;
			}			
		} catch(ArrayIndexOutOfBoundsException e) {
		}


		int[] environment = {foodfront, foodleft, foodright, poisonfront, poisonleft, poisonright};


		return environment;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < map.length; i++) {
			sb.append("[");
			for(int j = 0; j < map[i].length; j++) {
				sb.append("\""+map[i][j]+"\"");

				if(j != map[i].length-1) {
					sb.append(",");
				}
			}
			sb.append("]");
			if(i != map[i].length-1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public String allStatesToJSON() {
		return states.toString();
	}

	public String[][] createMap(double poison_percent, double food_percent) {
		int mapsize = 8;
		String[][] map = new String[mapsize][mapsize];

		for(int i = 0; i < mapsize; i++) {
			for(int j = 0; j < mapsize; j++) {

				String tmp = "";
				if(random.nextDouble() < poison_percent) {
					tmp = "p";
				}
				if(random.nextDouble() < food_percent) {
					tmp = "f";
				}
				map[i][j] = tmp;
			}
		}

		map[3][3] = "rf";

		return map;
	}

	public static String[][] map1() {
		String[][] map = {
		    {"", "", "", "p", "", "", "", ""},
		    {"", "", "p", "p", "p", "", "", ""},
		    {"p", "", "f", "", "", "p", "f", ""},
		    {"", "", "", "", "p", "p", "f", ""},
		    {"f", "rf", "", "", "", "", "", "p"},
		    {"", "", "", "", "p", "", "", ""},
		    {"", "", "", "", "", "", "", ""},
		    {"", "", "", "", "", "", "", ""}
		};
		return map;
	}

	public static String[][] map2() {
		String[][] map = {
		    {"", "", "", "p", "", "", "", ""},
		    {"", "", "p", "p", "p", "", "", ""},
		    {"p", "", "f", "", "", "p", "f", ""},
		    {"", "", "", "", "p", "p", "f", ""},
		    {"f", "", "rf", "", "", "", "", "p"},
		    {"", "p", "", "p", "", "", "", "f"},
		    {"", "", "", "", "p", "", "p", ""},
		    {"", "", "p", "", "f", "", "", "p"}
		};
		return map;
	}

	public static String[][] map3() {
		String[][] map = {
		    {"rr", "f", "f", "f", "f", "f", "f", "f"},
		    {"p", "p", "p", "p", "p", "p", "p", "f"},
		    {"f", "f", "f", "f", "f", "f", "p", "f"},
		    {"f", "p", "p", "p", "p", "f", "p", "f"},
		    {"f", "p", "f", "f", "p", "f", "p", "f"},
		    {"f", "p", "f", "f", "f", "f", "p", "f"},
		    {"f", "p", "p", "p", "p", "p", "p", "f"},
		    {"f", "f", "f", "f", "f", "f", "f", "f"}
		};
		return map;
	}

	public static String[][] map4() {
		String[][] map = {
		    {"p", "p", "p", "p", "p", "p", "p", "p"},
		    {"p", "f", "f", "f", "f", "f", "f", "p"},
		    {"p", "f", "", "", "", "", "f", "p"},
		    {"p", "f", "", "", "", "", "f", "p"},
		    {"p", "f", "rl", "", "", "", "f", "p"},
		    {"p", "f", "", "", "", "", "f", "p"},
		    {"p", "f", "f", "f", "f", "f", "f", "p"},
		    {"p", "p", "p", "p", "p", "p", "p", "p"},
		};
		return map;
	}

	public static String[][] map5() {
		String[][] map = {
		    {"", "", "", "p", "p", "", "", ""},
		    {"f", "", "f", "p", "p", "", "", ""},
		    {"", "", "", "", "", "", "p", ""},
		    {"f", "", "p", "", "", "f", "", ""},
		    {"", "f", "f", "p", "p", "", "", ""},
		    {"f", "", "", "", "f", "", "", ""},
		    {"", "", "", "f", "", "f", "p", "p"},
		    {"", "", "", "", "", "", "rf", "p"},
		};
		return map;
	}

	public static void main(String[] args) {
		World w = new World();

		System.out.println(Arrays.toString(w.getEnvironment()));

		w.printToFile();
	}
}
