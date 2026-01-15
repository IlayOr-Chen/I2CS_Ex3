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

            // Mode 1
            int dir = escapeFromBlackGhosts(ghosts, dists);
            if(dir != Game.STAY) return dir;
            
            // Mode 2
            dir = eatWhiteGhosts(ghosts, dists);
            if(dir != Game.STAY) return dir;

            // Mode 3 - Go to green if it is useful
            Pixel2D[] closestGreenPath = closestTargetPath(GREEN);
            int greenPathLength = 0;
            if(closestGreenPath != null) {
                int distToGreen = dists.getPixel(closestGreenPath[closestGreenPath.length - 1]);

                // only go to green if it is within 8 steps (safe to reach before being in danger)
                if(distToGreen >= 0 && distToGreen < 4) {
                    return directionTo(closestGreenPath[1]);
                }
            }

            // Mode 4
            Pixel2D[] closestPinkPath = closestTargetPath(PINK);
            if(closestPinkPath != null)
                return directionTo(closestPinkPath[1]);
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

    private int escapeFromBlackGhosts(GhostCL[] ghosts, Map2D dists) {
        int minGhostPath = -1;
        Pixel2D closestGhost = null;

        // check every ghost
        for(GhostCL g : ghosts) {
            // if the ghost is white - eatable
            if(g.remainTimeAsEatable(CODE) > 0) continue;

            // get the current ghost pixel
            String[] ghostPos = g.getPos(CODE).toString().split(",");
            Pixel2D ghostPixel = new Index2D(Integer.parseInt(ghostPos[0]), Integer.parseInt(ghostPos[1]));

            // find the shortest path from the pacman to the current ghost
            Pixel2D[] ghostPath = map.shortestPath(pacmanPos, ghostPixel, BLUE);
            int ghostPathLength = ghostPath.length;

            // find the closest ghost - the smallest path between the pacman and the ghost
            if(minGhostPath == -1 || minGhostPath > ghostPathLength) {
                minGhostPath = ghostPathLength;
                closestGhost = ghostPixel;
            }
        }

        // if I haven't found any ghosts of the smallest path from the pacman to a ghost is smaller than 8 - ignore
        if(closestGhost == null || minGhostPath >= 8) return Game.STAY;

        return escapeGhost(closestGhost, dists);
    }

    private int escapeGhost(Pixel2D ghost, Map2D dists) {
        int bestDir = Game.STAY;
        int bestDist = dists.getPixel(ghost);

        int[] dirs = {Game.UP, Game.DOWN, Game.LEFT, Game.RIGHT};

        // check every direction the ghost can do
        for(int dir : dirs) {
            Pixel2D next = pixelStep(pacmanPos, dir);
            // check if the pixel is valid
            if(!map.isInside(next)) continue;
            if(map.getPixel(next) == BLUE) continue;

            // check the distance between the pacman and the ghost
            int ghostD = map.allDistance(next, BLUE).getPixel(ghost);
            // find the biggest distance
            if(ghostD > bestDist) {
                bestDist = ghostD;
                bestDir = dir;
            }
        }

        return bestDir;
    }

    private Pixel2D pixelStep(Pixel2D from, int dir) {
        int x = from.getX();
        int y = from.getY();

        int w = map.getWidth();
        int h = map.getHeight();

        if(dir == Game.UP) {
            y = (y + 1) % h;
        }
        else if(dir == Game.DOWN) {
            y = (y - 1 + h) % h;
        }
        else if(dir == Game.LEFT) {
            x = (x - 1 + w) % w;
        }
        else if(dir == Game.RIGHT) {
            x = (x + 1) % w;
        }

        return new Index2D(x, y);
    }

    private Pixel2D[] closestTargetPath(int color) {
        Pixel2D[] ans = null;
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
                            ans = path;
                        }
                    }
                }
            }
        }
        return ans;
    }

    private int eatWhiteGhosts(GhostCL[] ghosts, Map2D dists) {
        Pixel2D target = null;
        int minDist = Integer.MAX_VALUE;
        final int maxEatDist = 4;

        for(GhostCL g : ghosts) {
            double time = g.remainTimeAsEatable(CODE);
            if(time <= 0) continue; // isn't a white ghost

            // get the white ghost pixel
            String[] ghostPos = g.getPos(CODE).toString().split(",");
            Pixel2D ghostPixel = new Index2D(Integer.parseInt(ghostPos[0]), Integer.parseInt(ghostPos[1]));
            int ghostDist = dists.getPixel(ghostPixel);

            // check if this ghost is the closest and the pacman has enough time to eat the ghost,
            // and the white ghost is close to the pacman - 4 squares long
            if(ghostDist >= 0 && ghostDist < minDist && ghostDist < time - 1 && ghostDist <= maxEatDist) {
                minDist = ghostDist;
                target = ghostPixel;
            }
        }

        if(target == null) return Game.STAY;

        Pixel2D[] path = map.shortestPath(pacmanPos, target, BLUE);
        if(path == null || path.length < 2) return Game.STAY;

        return directionTo(path[1]);
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