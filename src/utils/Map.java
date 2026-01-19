package utils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * @author boaz.benmoshe
 *
 */
public class Map implements Map2D {
	private int[][] _map;
	private boolean _cyclicFlag = true;

    /**
     * Constructs a 2D map and initializes all cells with the given value.
     * @param w width of the map (number of columns), must be positive
     * @param h height of the map (number of rows), must be positive
     * @param v initial value assigned to every cell
     * @throws RuntimeException if w <= 0 or h <= 0
     */
    public Map(int w, int h, int v) {init(w, h, v);}

    /**
     * Constructs a square map (size*size).
     * @param size width and height of the map, must be positive
     * @throws RuntimeException if size <= 0
     */
    public Map(int size) {this(size,size, 0);}

    /**
     * Constructs a map from a given 2D array.
     * @param data int array used to initialize the map.
     * @throws RuntimeException if data is null or ragged
     */
    public Map(int[][] data) {
        init(data);
    }

    /**
     * Initializes this map to a new size and fills it with a given value.
     * @param w width of the map, must be positive
     * @param h height of the map, must be positive
     * @param v value to assign to every cell
     * @throws RuntimeException if w <= 0 or h <= 0
     */
    @Override
    public void init(int w, int h, int v) {
        if(w <= 0 || h <= 0)
            throw new RuntimeException("Given invalid width and height.");

        _map = new int[w][h];

        // fill the whole map with the initial value v
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                _map[i][j] = v;
            }
        }
    }

    /**
     * Deep copy of the array.
     * Initializes this map using a given 2D array.
     * @param arr a 2D int array
     * @throws RuntimeException if arr is null, empty, contains null rows, or is a ragged array
     */
    @Override
    public void init(int[][] arr) {
        if(arr == null || arr.length == 0)
            throw new RuntimeException("Given array is null or empty.");

        if(arr[0] == null || arr[0].length == 0)
            throw new RuntimeException("Given array has null or empty rows.");

        int widthRows = arr[0].length;

        // check if all the rows length are equals
        for (int i = 1; i < arr.length; i++) {
            if(arr[i] == null || arr[i].length != widthRows)
                throw new RuntimeException("Given Ragged 2D array.");
        }

        _map = new int[arr.length][widthRows];

        //deep copy the given array into map array
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < widthRows; j++) {
                _map[i][j] = arr[i][j];
            }
        }
    }

    /**
     * Returns a deep copy of the map.
     * @return a new 2D array containing the same values as this map
     */
    @Override
    public int[][] getMap() {
        int w = getWidth();
        int h = getHeight();

        int[][] ans = new int[w][h];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                ans[i][j] = _map[i][j];
            }
        }

        return ans;
    }

    /**
     * Returns the width of this map.
     * @return number of columns in the map
     */
    @Override
    public int getWidth() {
        return _map.length;
    }

    /**
     * Returns the height of this map.
     * @return number of rows in the map
     */
    @Override
    public int getHeight() {
        return _map[0].length;
    }

    /**
     * Returns the value stored at coordinate (x,y) in the map.
     * @param x x-coordinate
     * @param y y-coordinate
     * @return value of the pixel at (x,y) on the map
     * @throws RuntimeException if (x,y) is outside the map
     */
    @Override
    public int getPixel(int x, int y) {
        if(!isInside(new Index2D(x,y)))
            throw new RuntimeException("Given invalid coordinates.");

        return _map[x][y];
    }

    /**
     * Returns the value stored at the given pixel.
     * @param p pixel coordinate
     * @return value of the pixel
     * @throws RuntimeException if p is null or outside the map
     */
    @Override
    public int getPixel(Pixel2D p) {
        return getPixel(p.getX(), p.getY());
    }

    /**
     * Sets the value of the pixel at coordinate (x,y).
     * @param x x-coordinate
     * @param y y-coordinate
     * @param v new value to assign
     * @throws RuntimeException if (x,y) is outside the map
     */
    @Override
    public void setPixel(int x, int y, int v) {
        setPixel(new Index2D(x,y), v);
    }

    /**
     * Sets the value of the given pixel.
     * @param p pixel coordinate
     * @param v new value to assign
     * @throws RuntimeException if p is null or outside the map
     */
    @Override
    public void setPixel(Pixel2D p, int v) {
        if(!isInside(p))
            throw new RuntimeException("Given invalid coordinates.");

        _map[p.getX()][p.getY()] = v;
    }

    @Override
    /**
     * Fills the connected component of the given pixel with a new color.
     * @param p starting pixel
     * @param new_v new color value
     * @return number of pixels that were filled
     * @throws RuntimeException if p is null or outside the map when cyclic is false
     */
    public int fill(Pixel2D xy, int new_v) {
        final int CLOSEST_NODES_NUM = 4, X_INDEX = 0, Y_INDEX = 1;
        if(xy == null) throw new RuntimeException("start pixel cannot be null");

        // check if the given pixel is in the map
        if(!isInside(xy)) {
            // if the map is cyclic and the pixel isn't the map - normalize the pixel to be in the map
            if (_cyclicFlag)
                xy = normalizePixelCyclicMap(xy);
                // if the map isn't cyclic and the pixel isn't in the map - return 0
            else throw new RuntimeException("start pixel is outside the map");
        }

        int originalColor = this.getPixel(xy);
        // check if the pixel color is the same as the given color
        if(originalColor == new_v) return 0;

        int count = 0; // the number of "filled" pixels
        Queue<Pixel2D> visitedPixels = new LinkedList<>();
        this.setPixel(xy, new_v);
        count++;
        visitedPixels.add(xy);

        // relative 4-neighbor steps (right, down, left, up)
        int[][] stepsPixel = {{1,0}, {0,1}, {-1,0}, {0, -1}};

        // BFS loop: while queue not empty poll a pixel and check its 4 neighbors
        while(!visitedPixels.isEmpty()) {
            // poll the current Pixel 2D
            Pixel2D currPixel = visitedPixels.poll();
            int currPixelX = currPixel.getX();
            int currPixelY = currPixel.getY();

            // for each neighbor compute the coordinates, normalize if cyclic, check conditions and enqueue
            for (int i = 0; i < CLOSEST_NODES_NUM; i++) {
                // calc the step Pixel coordinates
                int stepPixelX = currPixelX + stepsPixel[i][X_INDEX];
                int stepPixelY = currPixelY + stepsPixel[i][Y_INDEX];
                Pixel2D stepPixel = new Index2D(stepPixelX, stepPixelY);

                // if the map is cyclic - normalize the pixel to be in the map
                if(_cyclicFlag) {
                    stepPixel = normalizePixelCyclicMap(stepPixel);
                }

                // if neighbor is inside and has the original color - paint it
                if(isInside(stepPixel) && getPixel(stepPixel) == originalColor) {
                    setPixel(stepPixel, new_v);
                    count++;
                    visitedPixels.add(stepPixel);
                }
            }
        }

        // return the number of filled pixels
        return count;
    }

    @Override
    /**
     * Computes the shortest path between two pixels avoiding obstacles.
     * @param p1 starting pixel
     * @param p2 destination pixel
     * @param obsColor obstacle color
     * @return array of pixels representing the shortest path
     * @throws RuntimeException if p1 or p2 is null or  no valid path exists
     */
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        final int CLOSEST_NODES_NUM = 4, X_INDEX = 0, Y_INDEX = 1;

        if(p1 == null || p2 == null)
            throw new RuntimeException("given pixels cannot be null");

        Map2D allDistanceMap = allDistance(p1, obsColor);

        // check if the given pixel is in the map
        if(!isInside(p2)) {
            // if the map is cyclic and the pixel isn't the map - normalize the pixel to be in the map
            if (_cyclicFlag)
                p2 = normalizePixelCyclicMap(p2);
                // if the map isn't cyclic and the pixel isn't in the map - throw an exception
            else
                throw new RuntimeException("destination pixel is outside the map.");
        }

        // get the destination pixel color
        int endColor = allDistanceMap.getPixel(p2);

        // check if the given destination pixel is obstacle
        if(endColor == -1)
            throw new RuntimeException("destination pixel is obstacle pixel");

        Pixel2D[] ans = new Pixel2D[endColor + 1];

        // step pixels to calc the 4 neighbors of the pixels
        int[][] stepsPixel = {{1,0}, {0,1}, {-1,0}, {0, -1}};
        Pixel2D currPixel = new Index2D(p2);
        ans[endColor] = currPixel;

        // loop to go through distance=endColor down to 0, at each step pick a neighbor with distance pNum
        for (int pNum = endColor - 1; pNum >= 0; pNum--) {
            int currPixelX = currPixel.getX();
            int currPixelY = currPixel.getY();

            // loop to go through all the 4 neighbors of the current pixel
            for (int i = 0; i < CLOSEST_NODES_NUM; i++) {
                // calc each neighbor pixel to the current pixel
                int stepPixelX = currPixelX + stepsPixel[i][X_INDEX];
                int stepPixelY = currPixelY + stepsPixel[i][Y_INDEX];
                Pixel2D stepPixel = new Index2D(stepPixelX, stepPixelY);

                // if the map is cyclic - normalize the pixel to be in the map
                if(_cyclicFlag)
                    stepPixel = normalizePixelCyclicMap(stepPixel);

                // check if the step pixel is inside the map, and it's distance equals the expected pNum
                if(isInside(stepPixel) && allDistanceMap.getPixel(stepPixel) == pNum) {
                    currPixel = new Index2D(stepPixel);
                    ans[pNum] = currPixel;
                    // after the pixel was found exit the for loop
                    break;
                }
            }
        }

        return ans;
    }

    /**
     * Checks if a pixel is inside the map or not.
     * @param p pixel coordinate
     * @return true if p is not null and inside the map, false otherwise
     */
    @Override
    public boolean isInside(Pixel2D p) {
        if(p == null) return false;

        int x = p.getX(), y = p.getY();

        return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
    }

    /**
     *
     * @return
     */
	@Override
	public boolean isCyclic() {
		return _cyclicFlag;
	}

    /**
     *
     * @param cy the value of the cyclic flag.
     */
	@Override
	public void setCyclic(boolean cy) {
        this._cyclicFlag = cy;
    }

    /**
     * Computes the shortest distance from a starting pixel to all other pixels.
     * @param start starting pixel
     * @param obsColor obstacle color
     * @return a new utils.Map2D containing distance values
     * @throws RuntimeException if start is null or outside the map when cyclic is false
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        final int CLOSEST_NODES_NUM = 4, X_INDEX = 0, Y_INDEX = 1;

        if(start == null)
            throw new RuntimeException("start pixel cannot be null");

        // check if the given pixel is in the map
        if(!isInside(start)) {
            // if the map is cyclic and the pixel isn't the map - normalize the pixel to be in the map
            if (_cyclicFlag)
                start = normalizePixelCyclicMap(start);
                // if the map isn't cyclic and the pixel isn't in the map - throw an exception
            else
                throw new RuntimeException("start pixel is outside the map");
        }

        // initialize answer map with -1 in all cells.
        Map2D ans = new Map(getWidth(), getHeight(), -1);

        // if starting cell is an obstacle — return ans with all -1
        if(getPixel(start) == obsColor)
            return ans;

        Queue<Pixel2D> visitedPixels = new LinkedList<>();
        // mark start distance = 0
        ans.setPixel(start, 0);
        visitedPixels.add(start);

        int[][] stepsPixel = {{1,0}, {0,1}, {-1,0}, {0, -1}};

        // BFS: while queue not empty, poll and check 4 neighbors
        while(!visitedPixels.isEmpty()) {
            // poll the current Pixed2D from the queue
            Pixel2D currPixel = visitedPixels.poll();
            int currPixelX = currPixel.getX();
            int currPixelY = currPixel.getY();

            // check all 4 neighbors of the current Pixel
            for (int i = 0; i < CLOSEST_NODES_NUM; i++) {
                // calc the neighbor Pixel
                int stepPixelX = currPixelX + stepsPixel[i][X_INDEX];
                int stepPixelY = currPixelY + stepsPixel[i][Y_INDEX];
                Pixel2D stepPixel = new Index2D(stepPixelX, stepPixelY);

                // if the map is cyclic - normalize the pixel to be in the map
                if(_cyclicFlag) {
                    stepPixel = normalizePixelCyclicMap(stepPixel);
                }

                // check if the step pixel is inside the map, if it's not equals to obsColor, and it's not been marked yet in ans map
                if(isInside(stepPixel) && getPixel(stepPixel) != obsColor && ans.getPixel(stepPixel) == -1) {
                    // set distance
                    ans.setPixel(stepPixel, ans.getPixel(currPixel) + 1);
                    visitedPixels.add(stepPixel);
                }
            }
        }

        return ans;
    }

    ////////////////////// Private Methods ///////////////////////
    /**
     * Normalize a possibly-outside pixel to its inside coordinates in a cyclic map.
     * This function computes the modulo mapping for x and y so that negative indices
     * and indices >= dimension wrap correctly into the [0,width-1] and [0,height-1] ranges of the map.
     * @param p the pixel to normalize
     * @return the pixel after normalization
     */
    private Pixel2D normalizePixelCyclicMap(Pixel2D p) {
        // get the width and height of the map
        int mapWidth = getWidth(), mapHeight = getHeight();

        // Computes the normalized x-coordinate:
        int newX = ((p.getX() % mapWidth) + mapWidth) % mapWidth;
        // Computes the normalized y-coordinate:
        int newY = ((p.getY() % mapHeight) + mapHeight) % mapHeight;

        // Returns a new utils.Pixel2D instance with the normalized coordinates.
        return new Index2D(newX, newY);
    }
}
