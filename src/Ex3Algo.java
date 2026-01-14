import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.*;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;
    private Pixel2D pacmanPos;
    private Map2D map;

    private static final int CODE = 0;
    private static final int BLUE = Game.getIntColor(Color.BLUE, CODE);
    private static final int PINK = Game.getIntColor(Color.PINK, CODE);
    private static final int BLACK = Game.getIntColor(Color.BLACK, CODE);
    private static final int GREEN = Game.getIntColor(Color.GREEN, CODE);
    private static final int WHITE = Game.getIntColor(Color.WHITE, CODE);

	public Ex3Algo() {_count=0;}
	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}
	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		if(_count==0 || _count==300) {
			int[][] board = game.getGame(0);
			printBoard(board);
			System.out.println("Blue=" + BLUE + ", Pink=" + PINK + ", Black=" + BLACK + ", Green=" + GREEN);
			String pos = game.getPos(CODE).toString();
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(CODE);
			printGhosts(ghosts);

            String[] p = pos.split(",");
            int x = Integer.parseInt(p[0]);
            int y = Integer.parseInt(p[1]);
            pacmanPos = new Index2D(x, y);
            map = new Map(board);

            Map2D dists = map.allDistance(pacmanPos, BLUE);

            // Mode 2 -
            Pixel2D closestPink = closestTarget(PINK);
            if(closestPink != null)
                return directionTo(closestPink);

            // Mode 3 -
            Pixel2D closestGreen = closestTarget(GREEN);
            if(closestGreen != null)
                return directionTo(closestGreen);

            // Mode 4 -

		}

		_count++;
		return randomDir();
	}
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0;i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}




    private Pixel2D closestTarget(int color) {
        Pixel2D ans = null;
        int minPath = -1;

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Pixel2D currPixel = new Index2D(x,y);
                int currPixelColor = map.getPixel(currPixel);

                if(currPixelColor == color) {
                    Pixel2D[] path = map.shortestPath(pacmanPos, currPixel, BLUE);
                    if(path != null) {
                        int pathLength = path.length;

                        if(minPath == -1 || minPath > pathLength) {
                            minPath = pathLength;
                            ans = new Index2D(path[1]);
                        }
                    }
                }
            }
        }
        return ans;
    }

    private int directionTo(Pixel2D next) {
        int px = pacmanPos.getX();
        int py = pacmanPos.getY();

        int nx = next.getX();
        int ny = next.getY();

        int w = map.getWidth();
        int h = map.getHeight();

        if (nx == px && ( (py + 1) % h ) == ny)
            return Game.UP;
        else if (nx == px && ( (py - 1 + h) % h ) == ny)
            return Game.DOWN;
        else if (ny == py && ( (px - 1 + w) % w ) == nx)
            return Game.LEFT;
        else if (ny == py && ( (px + 1) % w ) == nx)
            return Game.RIGHT;

        return Game.STAY;
    }

}