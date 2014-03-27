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

	private static final double FOOD_PERCENT = 0.5;
	private static final double POISON_PERCENT = 0.5;


	public World() {
		states = new ArrayList<String>();
		map = readMap();
		locatePlayer();

		states.add(this.toString());

		food = 0;
		poison = 0;
		steps = 0;
	}

	public String[][] readMap() {
		return map2(); //createMap();
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

	public static String[][] createMap() {
		int mapsize = 8;
		String[][] map = new String[mapsize][mapsize];
		Random r = new Random();
		for(int i = 0; i < mapsize; i++) {
			for(int j = 0; j < mapsize; j++) {

				String tmp = "";
				if(r.nextDouble() < POISON_PERCENT) {
					tmp = "p";
				}
				if(r.nextDouble() < FOOD_PERCENT) {
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

	public static void main(String[] args) {
		World w = new World();

		System.out.println(Arrays.toString(w.getEnvironment()));

		w.printToFile();
	}
}
