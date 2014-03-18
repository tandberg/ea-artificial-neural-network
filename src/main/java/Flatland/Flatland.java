import java.util.*;
public class Flatland {

	public int X, Y;
	public String[][] map;

	public Flatland() {
		map = readMap();
		locatePlayer();
	}

	public String[][] readMap() {
		return map1();
	}

	public void locatePlayer() {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				if(map[i][j] == "r") { // TODO: Match at all robot states
					this.X = j;
					this.Y = i;
				}
			}
		}
	}

	public void doMove(int dx, int dy) {
		map[Y][X] = "";
		map[Y + dy][X + dx] = "r";
		Y += dy;
		X += dx;
	}

	/*
		sende tuple med omgivelsene
		domove
	*/

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


	public static void main(String[] args) {
		Flatland a = new Flatland();


		ArrayList<String> pelle = new ArrayList<String>();
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(0,1);
		pelle.add(a.toString());
		a.doMove(-1,0);
		pelle.add(a.toString());
		a.doMove(-1,0);
		pelle.add(a.toString());
		a.doMove(1,0);
		pelle.add(a.toString());
		a.doMove(0,-1);
		pelle.add(a.toString());
		a.doMove(0,-1);
		pelle.add(a.toString());
		a.doMove(0,-1);
		pelle.add(a.toString());
		a.doMove(0,1);
		pelle.add(a.toString());
		a.doMove(0,1);
		pelle.add(a.toString());
		a.doMove(0,-1);
		pelle.add(a.toString());
		a.doMove(-1,0);
		pelle.add(a.toString());

		System.out.println(pelle);
	}


	public static String[][] map1() {
		String[][] map = {
		    {"", "", "", "p", "", "", "", ""},
		    {"", "", "p", "p", "p", "", "", ""},
		    {"p", "", "f", "", "", "p", "f", ""},
		    {"", "", "", "", "p", "p", "f", ""},
		    {"f", "", "r", "", "", "", "", "p"},
		    {"", "", "", "", "p", "", "", ""},
		    {"", "", "", "", "", "", "", ""},
		    {"", "", "", "", "", "", "", ""}
		};
		return map;
	}
}