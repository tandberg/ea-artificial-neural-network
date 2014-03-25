package javaWorld;
import java.util.*;
public class World {

	public int X, Y;
	public String[][] map;
	private List<String> states;
	public static final int MAX_MOVES = 50;


	public World() {
		states = new ArrayList<String>();
		map = readMap();
		locatePlayer();
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

			map[Y + dy][X + dx] = map[Y][X].split("")[1] + newState;
			map[Y][X] = "";
			Y += dy;
			X += dx;

			states.add(this.toString());

		} catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Illegal move");
		}
		
	}

	public List getEnvironment() { // returns (front, left, right)
		List<String> env = new ArrayList();
		String state = map[Y][X].split("")[2];

		System.out.println(Arrays.toString(map[Y][X].split("")));

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

		return env;

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