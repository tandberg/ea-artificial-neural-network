package TrackerGame;

import java.util.*;
import java.io.*;

public class World {

	private final static int AGENT_SIZE = 5;
	private final static int SHADOW_SENSORS = 5;
	
	private final static int FALLING_MIN = 1;
	private final static int FALLING_MAX = 5;

	private final static int ARENA_WIDTH = 30;
	private final static int ARENA_HEIGHT = 15;

	private List<String> states;

	private int timestamp;
	private int agentLeftPos;
	private int enemyLeftPos;
	private int enemyY;
	private int enemySize;
	private int[][] arena;
	private int score_hits;

	public World() {
		timestamp = 0;
		score_hits = 0;
		arena = new int[ARENA_WIDTH][ARENA_HEIGHT];
		states = new ArrayList<String>();
		setAgent(0);
		addRandomEnemy();

		states.add(this.toString());
	}

	public void setAgent(int leftPos) {
		this.agentLeftPos = leftPos;
		for(int i = leftPos; i < leftPos + AGENT_SIZE; i++) {
			int pos = i % ARENA_WIDTH;
			arena[pos][ARENA_HEIGHT-1] = 1;
		}
	}

	public void doMove(int dx) {
		if(this.agentLeftPos == -1) {
			this.agentLeftPos = ARENA_WIDTH+1;
		}

		if(dx == 1) {
			arena[Math.abs(this.agentLeftPos % ARENA_WIDTH)][ARENA_HEIGHT-1] = 0;
			arena[Math.abs((this.agentLeftPos+AGENT_SIZE) % ARENA_WIDTH)][ARENA_HEIGHT-1] = 1;
		} else if(dx == -1) {
			arena[Math.abs((this.agentLeftPos-1) % ARENA_WIDTH)][ARENA_HEIGHT-1] = 1;
			arena[Math.abs((this.agentLeftPos+AGENT_SIZE-1) % ARENA_WIDTH)][ARENA_HEIGHT-1] = 0;
		}

		this.agentLeftPos += dx;
		clocktick();
		
		states.add(this.toString());
	}

	public void clocktick() {

		updateFallingBlocks();
		timestamp += 1;
	}

	public void addRandomEnemy() {
		enemySize = (int) (Math.random() * 6);
		enemyLeftPos = (int) (Math.random() * (ARENA_WIDTH - enemySize));
	}

	public void updateFallingBlocks() {

		try{

			for(int i = enemyLeftPos; i < enemyLeftPos+enemySize; i++) {
				if(enemyY != 0) {
					arena[i][enemyY-1] = 0;
				}
				arena[i][enemyY] = 2;
			}
			enemyY += 1;

		} catch(ArrayIndexOutOfBoundsException e) {

			for(int i = enemyLeftPos; i < enemyLeftPos+enemySize; i++) {
				arena[i][ARENA_HEIGHT-1] = 0;
			}
			enemyY = 0;

			addRandomEnemy();
		}
	}

	public boolean[] getEnvironment() { //left to right
		boolean[] sensors = new boolean[AGENT_SIZE];

		int enemyRightPos = enemyLeftPos+enemySize;

		for(int i = enemyLeftPos, j = 0; i <= enemyRightPos; i++, j++) {
			sensors[j] = agentLeftPos == enemyLeftPos+i;
		}

		return sensors;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(int i = 0; i < ARENA_HEIGHT; i++) {
			sb.append("[");
			for(int j = 0; j < ARENA_WIDTH; j++) {
				sb.append(arena[j][i]);
				if(j != ARENA_WIDTH-1) {
					sb.append(",");
				}
			}
			sb.append("]");
			if(i != ARENA_HEIGHT-1) {
				sb.append(",");
			}
		}
		sb.append("]");

		return sb.toString();
	}

	public String allStatesToJSON() {
		return states.toString();
	}

	public void printToFile() {
		try {
			PrintWriter writer = new PrintWriter("src/main/javascript/tracker/output.txt", "UTF-8");
			writer.println(this.allStatesToJSON());
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		World w = new World();

		for(int i = 0; i < 1000; i++) {
			w.doMove(1);
		}

		w.printToFile();
	}
}