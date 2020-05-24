package TetrisGame;

public class Board {
    public final int WIDTH, HEIGHT, VISIBLE_HEIGHT, BUFFER, FIRST_VISIBLE_Y, SPAWN_X, SPAWN_Y;

    private Mino[][] board;

    public Board(int width, int height, int buffer){
        WIDTH = width;
        VISIBLE_HEIGHT = height;
        HEIGHT = height+buffer;
        BUFFER = buffer;
        FIRST_VISIBLE_Y = buffer;
        SPAWN_X = (WIDTH - 4) / 2;
        SPAWN_Y = FIRST_VISIBLE_Y - 2;


        board = new Mino[WIDTH][HEIGHT];
    }

    public Mino getMino(int x, int y){
        return board[x][y];
    }

    public boolean isOpen(int x, int y){
        return (0 <= x && x < WIDTH) && (0 <= y && y < HEIGHT) && board[x][y] == Mino.NONE;
    }

    public boolean notOpen(int x, int y){
        return (x < 0 || WIDTH <= x) || (y < 0 || HEIGHT <= y) || board[x][y] != Mino.NONE;
    }
}
