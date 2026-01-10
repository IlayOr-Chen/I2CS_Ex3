public class Index2D implements Pixel2D {
    private int _x, _y;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y;}
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}
    @Override
    public int getX() {
        return _x;
    }
    @Override
    public int getY() {
        return _y;
    }

    /**
     * This function calc the distance between two point (this and the given one) and return it
     * @param p2 the second point to calc the distance from him to this
     * @return the distance between this point to the given one
     * @throws RuntimeException if the given object is null
     */
    @Override
    public double distance2D(Pixel2D p2) {
        if(p2 == null)
            throw new RuntimeException("p2 is null");

        // Set the variables
        int x1 = _x, y1 = _y;
        int x2 = p2.getX(), y2 = p2.getY();

        // calc the distance between each coordinate
        int dx = x2 - x1;
        int dy = y2 - y1;

        // Calc and return the distance between the points
        return Math.sqrt(dx*dx + dy*dy);
    }

    @Override
    public String toString() {
        return getX()+","+getY();
    }
    @Override
    public boolean equals(Object t) {
        boolean ans = false;
       /////// you do NOT need to add your code below ///////
        if(t instanceof Pixel2D) {
            Pixel2D p = (Pixel2D) t;
            ans = (this.distance2D(p)==0);
        }
       ///////////////////////////////////
        return ans;
    }
}
