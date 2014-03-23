package tracker;

public class World {

	private final static int AGENT_SIZE = 5;
	private final static int SHADOW_SENSORS = 5;
	
	private final static int FALLING_MIN = 1;
	private final static int FALLING_MAX = 5;

	private final static int ARENA_WIDTH = 30;
	private final static int ARENA_HEIGHT = 15;

	private int timestamp;
	private int[][] arena;

	public World() {
		timestamp = 0;

		arena = new int[ARENA_WIDTH][ARENA_HEIGHT];

	}

	public void clocktick() {
		timestamp += 1;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < arena.length; i++) {
			sb.append("[");
			for(int j = 0; j < arena[i].length; j++) {
				sb.append(arena[i][j]);
				if(j != arena.length-1) {
					sb.append(",");
				}
			}
			sb.append("]");
			if(i != arena.length-1) {
				sb.append(",");
			}
		}
		sb.append("]");

		return sb.toString();
	}

	public static void main(String[] args) {
		World w = new World();
		System.out.println(w);
	}

}