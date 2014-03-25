package javaWorld;
import java.util.*;
public class World {

	public int X, Y;
	public String[][] map;
	private List<String> states;
	public static final int MAX_MOVES = 50;
	private int food;
	private int poison;


	public World() {
		states = new ArrayList<String>();
		map = readMap();
		locatePlayer();

		food = 0;
		poison = 0;
	}

	public String[][] readMap() {
		return map1();
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
		return states.size() >= MAX_MOVES;
	}

	public int[] getScores() { // [+, -]
		int[] result = {food, poison};
		return result;
	}

	public void doMove(int dx, int dy) {
		try {
			String prevState = map[Y][X].split("")[2];
			String newState = prevState;

			if(prevState.equals("f")) {
				if(dx == 1) {
					newState = "r";
				} else if(dx == -1) {
					newState = "l";
				}
			} else if(prevState.equals("r")) {
				if(dy == 1) {
					newState = "d";
				} else if(dy == -1) {
					newState = "f";
				}
			} else if(prevState.equals("d")) {
				if(dx == 1) {
					newState = "r";
				} else if(dx == -1) {
					newState = "l";
				}
			} else if(prevState.equals("l")) {
				if(dy == 1) {
					newState = "f";
				} else if(dy == -1) {
					newState = "d";
				}
			}

			if(map[Y + dy][X + dx] == "p") {
				poison++;
			} else if(map[Y + dy][X + dx] == "f") {
				food++;
			}

			map[Y + dy][X + dx] = map[Y][X].split("")[1] + newState;
			map[Y][X] = "";
			Y += dy;
			X += dx;

			states.add(this.toString());

		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Illegal move");
		}
		
	}

	public int[] getEnvironment() { // returns (front, left, right) [foodfront, foodleft, foodright, poisonfront, poisonleft, poisionright]
		List<String> env = new ArrayList();
		String state = map[Y][X].split("")[1];

		int foodfront = 0, foodleft = 0, foodright = 0, poisonfront = 0, poisonleft = 0, poisonright = 0;

		try {
			switch(state) {
			case "f":
				env.add(map[Y-1][X]);
				env.add(map[Y][X-1]);
				env.add(map[Y][X+1]);
				break;
			case "d":
				env.add(map[Y-1][X]);
				env.add(map[Y][X+1]);
				env.add(map[Y][X-1]);
				break;
			case "l":
				env.add(map[Y][X-1]);
				env.add(map[Y+1][X]);
				env.add(map[Y-1][X]);
				break;
			case "r":
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

	public static void main(String[] args) {
		World a = new World();

		// System.out.println(a);
		// System.out.println(a.getEnvironment());


		ArrayList<String> pelle = new ArrayList<String>();
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(1,0);

		pelle.add(a.toString());
		a.doMove(0,-1);
		pelle.add(a.toString());
		a.doMove(0,-1);
		pelle.add(a.toString());
		a.doMove(0,-1);
		pelle.add(a.toString());
		a.doMove(0,-1);

		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(1,0);

		pelle.add(a.toString());
		a.doMove(0,1);
		pelle.add(a.toString());
		a.doMove(0,1);
		pelle.add(a.toString());
		a.doMove(0,1);
		pelle.add(a.toString());
		a.doMove(0,1);

		System.out.println(pelle);

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
}
