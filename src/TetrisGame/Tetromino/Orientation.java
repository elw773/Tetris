package TetrisGame.Tetromino;


/**
 * Represents the orientation of a tetromino
 */
public enum Orientation {
    NORTH, EAST, SOUTH, WEST;

    /**
     * @return the orientation that is 90 degrees cw from this one
     */
    public Orientation cw(){
        switch (this){
            case NORTH: return EAST;
            case EAST: return SOUTH;
            case SOUTH: return WEST;
            default: return NORTH;
        }
    }

    /**
     * @return the orientation that is 90 degrees ccw from this one
     */
    public Orientation ccw(){
        switch (this){
            case NORTH: return WEST;
            case WEST: return SOUTH;
            case SOUTH: return EAST;
            default: return NORTH;
        }
    }
}
