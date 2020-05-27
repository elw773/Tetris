package TetrisGame;

/**
 * Represents a board in the tetris game
 */
public class Board {
    public final int
            WIDTH,
            HEIGHT, // the total height of the board, including the buffer
            VISIBLE_HEIGHT, // the height of the visible portion of the board, the given height of the board
            BUFFER,
            FIRST_VISIBLE_Y, // the y value of the first square that is in the visible portion of the board (under the buffer)
            SPAWN_X, SPAWN_Y; // x and y for new tetrominos to spawn at

    private Mino[][] board;

    public Board(int width, int height, int buffer){
        WIDTH = width;
        VISIBLE_HEIGHT = height;
        HEIGHT = height+buffer;
        BUFFER = buffer;
        FIRST_VISIBLE_Y = buffer;
        SPAWN_X = (width - 4) / 2;
        SPAWN_Y = buffer - 2;


        board = new Mino[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = Mino.NONE;
            }
        }
    }

    /**
     * Returns the Mino type of a square
     * @param x the x of the square
     * @param y the y of the square
     * @return the Mino at those coordinates, or Mino.O of the coordinates are out of bounds
     */
    public Mino get(int x, int y){
        if(validX(x) && validY(y)){
            return board[x][y];
        }
        return Mino.O;
    }

    /**
     * Sets the square to the given mino
     * @param x the x of the square
     * @param y the y of the square
     * @param mino the type to set it to
     */
    public void set(int x, int y, Mino mino){
        if(validX(x) && validY(y)){
            board[x][y] = mino;
        }
    }

    /**
     * Determines if a square is empty
     * @param x
     * @param y
     * @return true if the square is in bounds and is Mino.NONE
     */
    public boolean isOpen(int x, int y){
        return validX(x) && validY(y) && board[x][y] == Mino.NONE;
    }

    /**
     * @param y
     * @return true if the y value is in bounds
     */
    public boolean validY(int y){
        return (0 <= y && y < HEIGHT);
    }

    /**
     * @param x
     * @return true if the x value is in bounds
     */
    public boolean validX(int x){
        return (0 <= x && x < WIDTH);
    }

    /**
     * Determines the graphical x coordinate of a square
     * @param boardX the grapical x of the board the square is on
     * @param x the x of the square
     * @param squareSize the width of a square on the board
     * @return graphical x of the square
     */
    public double toGraphicX(double boardX, int x, double squareSize){
        return boardX + (x * squareSize);
    }

    /**
     * Determines the graphical y coordinate of a square
     * @param boardY the grapical y of the board the square is on
     * @param y the y of the square
     * @param squareSize the height of a square on the board
     * @return graphical y of the square
     */
    public double toGraphicY(double boardY, int y, double squareSize){
        return boardY + ((y-FIRST_VISIBLE_Y) * squareSize);
    }

    public Mino[][] getBoard() {
        return board;
    }
}
