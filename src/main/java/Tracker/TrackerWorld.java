package tracker;

import java.util.*;
import java.io.*;
public class TrackerWorld implements TrackerInterface {

	private final static int AGENT_SIZE = 5;
	private final static int SHADOW_SENSORS = 5;
	
	private final static int FALLING_MIN = 1;
	private final static int FALLING_MAX = 5;

	private final static int ARENA_WIDTH = 30;
	private final static int ARENA_HEIGHT = 15;

	private int timestamp;
	private int agentLeftPos;
	private int enemyLeftPos;
	private int enemyY;
	private int enemySize;
	private int[][] arena;

	public TrackerWorld() {
		timestamp = 0;
		arena = new int[ARENA_WIDTH][ARENA_HEIGHT];
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
			System.out.println(agentLeftPos);
			System.out.println(Math.abs((this.agentLeftPos-1) % ARENA_WIDTH));
			System.out.println(Math.abs((this.agentLeftPos+AGENT_SIZE-1) % ARENA_WIDTH)+"\n\n");

			arena[Math.abs((this.agentLeftPos-1) % ARENA_WIDTH)][ARENA_HEIGHT-1] = 1;
			arena[Math.abs((this.agentLeftPos+AGENT_SIZE-1) % ARENA_WIDTH)][ARENA_HEIGHT-1] = 0;
		}

		this.agentLeftPos += dx;
		clocktick();
	}

	public void clocktick() {
		if(timestamp % ARENA_HEIGHT == 0) {
			addRandomEnemy();
		}

		updateFallingBlocks();
		timestamp += 1;
	}

	public void addRandomEnemy() {
		enemySize = (int) (Math.random() * 6);
		enemyLeftPos = (int) (Math.random() * ARENA_WIDTH);
		enemyY = 0;
	}

	public void updateFallingBlocks() {
		try{
			for(int i = enemyLeftPos; i < enemyLeftPos+enemySize; i++) {
				arena[i][enemyY] = 0;
			}


			for(int i = enemyLeftPos; i < enemyLeftPos+enemySize; i++) {
				arena[i][enemyY] = 2;
			}
			enemyY += 1;

		} catch(ArrayIndexOutOfBoundsException e) {

		}
		

	}

	public List getEnvironment() {
		return null;
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
}