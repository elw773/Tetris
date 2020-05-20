package TetrisGame.Tetromino;


import TetrisGame.Move;

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

    /**
     * @return the orientation that is 90 degrees in the given direction from this one
     */
    public Orientation rotate(Move.Direction direction){
        switch (direction){
            case LEFT: return this.ccw();
            case RIGHT: return this.cw();
            default: return this;
        }
    }
}
