package server;

import algos.Ex3Algo;
import algos.ManualAlgo;

/**
 * This class contains all the needed parameters for the Pacman game.
 * Make sure you update your details below!
 */
public class GameInfo {
	public static final String MY_ID = "330851395";
	public static final int CASE_SCENARIO = 4; // [0,4] level
	public static final long RANDOM_SEED = 31; // Random seed
	public static final boolean CYCLIC_MODE = true;
	public static final int DT = 50; // [20,200]
	private static PacManAlgo _manualAlgo = new ManualAlgo();
	private static PacManAlgo _myAlgo = new Ex3Algo();
    public static final PacManAlgo MANUAL_ALGO = _manualAlgo;
	public static final PacManAlgo MY_ALGO = _myAlgo;
}
