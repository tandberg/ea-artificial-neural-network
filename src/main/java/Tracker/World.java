package Tracker;

import java.util.*;
import java.io.*;

public class World {

	private final static int AGENT_SIZE = 5;
	private final static int SHADOW_SENSORS = 5;
	
	private final static int FALLING_MIN = 1;
	private final static int FALLING_MAX = 5;

	private final static int ARENA_WIDTH = 30;
	private final static int ARENA_HEIGHT = 15;

	private final static int MAX_STEPS = ARENA_HEIGHT * 40;

	private List<String> states;

	private int timestamp;
	private int agentLeftPos;
	private int enemyLeftPos;
	private int enemyY;
	private int enemySize;
	private int enemyHorizontal;
	private int[][] arena;

	private int positives;
	private int negatives;
	private int miss;
	private int partialHits;

	private Random random;

	public World(long randomSeed) {
		timestamp = 0;
		positives = 0;
		negatives = 0;
		partialHits = 0;
		miss = 0;

		random = new Random(randomSeed);
		arena = new int[ARENA_WIDTH][ARENA_HEIGHT];
		states = new ArrayList<String>();
		this.agentLeftPos = 0;
		addRandomEnemy();

		states.add(this.toString());
	}

	private void clearBoard() {
		for(int i = 0; i < ARENA_WIDTH; i++) {
			for(int j = 0; j < ARENA_HEIGHT; j++) {
				arena[i][j] = 0;
			}
		}
	}

	public void setAgent() {
		for(int i = agentLeftPos; i < agentLeftPos + AGENT_SIZE; i++) {
			int pos = i % ARENA_WIDTH;
			arena[pos][ARENA_HEIGHT-1] = 1;
		}
	}

	public void setEnemy() {
		try {
			for(int i = enemyLeftPos; i < enemyLeftPos+enemySize; i++) {
				arena[(ARENA_WIDTH + i) % ARENA_WIDTH][enemyY] += 2;
			}
			enemyLeftPos = (enemyLeftPos + enemyHorizontal) % ARENA_WIDTH;
		} catch(Exception e) {
			enemyY = -1;
			addRandomEnemy();
		}
		
	}

	public void doMove(int dx) {
		this.agentLeftPos += dx;

		if(this.agentLeftPos <= -1) {
			this.agentLeftPos = ARENA_WIDTH;
		} else if(this.agentLeftPos >= ARENA_WIDTH) {
			this.agentLeftPos = 0;
		}

		clocktick();		
		states.add(this.toString());
	}

	public void clocktick() {
		timestamp += 1;
		clearBoard();
		setAgent();
		setEnemy();

		this.enemyY += 1;

		int tmpHits = 0;
		for(int i = 0; i < ARENA_WIDTH; i++) {
			if(arena[i][ARENA_HEIGHT-1] == 3) {
				tmpHits++;
			}
		}

		if(tmpHits == 0) {
			for(int i = 0; i < ARENA_WIDTH; i++) {
				if(arena[i][ARENA_HEIGHT-1] == 2 && enemySize < AGENT_SIZE) {
					miss++;
					break;
				}
			}	
		}
		

		if(tmpHits >= AGENT_SIZE) {
			negatives++;
		} else if(tmpHits == enemySize) {
			positives++;
		} else if(tmpHits > 0) {
			partialHits++;
		}
	}

	public boolean finished() {
		return timestamp >= MAX_STEPS;
	}

	public int getScore() {
		/*
			positives   = 100% hit of smaller objects
			miss 	    = Smaller who did'nt hit
			negatives   = Hits from >=AGENT_SIZE
			partialHits = Hits on the edges
		*/

		return (int)((positives + 0.5 * partialHits) - miss - negatives);
	}

	public void addRandomEnemy() {
		// System.out.println("New enemy");

		enemySize = (int) (this.random.nextDouble() * 6) + 1;
		enemyLeftPos = (int) (this.random.nextDouble() * (ARENA_WIDTH - enemySize));
		enemyHorizontal = (int) (this.random.nextInt(3) - 1);
	}

	public boolean[] getEnvironment() { //left to right
		boolean[] sensors = new boolean[AGENT_SIZE];

		for(int i = 0; i < AGENT_SIZE; i++) {
			int tmpPos = (agentLeftPos+i) % ARENA_WIDTH;
			try {
				sensors[i] = arena[tmpPos][enemyY-1] == 2;
			} catch(ArrayIndexOutOfBoundsException e) {}
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
		// System.out.println("Final score: " + score_hits);
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
		World w = new World(1);
		for(int i = 0; i < 100; i++) {
			w.doMove(1);
			w.getScore();
		}

		w.printToFile();
	}
}