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
        SPAWN_X = (width - 4) / 2;
        SPAWN_Y = buffer - 2;


        board = new Mino[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                board[x][y] = Mino.NONE;
            }
        }
    }

    public Mino get(int x, int y){
        if(validX(x) && validY(y)){
            return board[x][y];
        }
        return Mino.O;
    }

    public void set(int x, int y, Mino mino){
        if(validX(x) && validY(y)){
            board[x][y] = mino;
        }
    }

    public boolean isOpen(int x, int y){
        return validX(x) && validY(y) && board[x][y] == Mino.NONE;
    }

    public boolean notOpen(int x, int y){
        return (x < 0 || WIDTH <= x) || (y < 0 || HEIGHT <= y) || board[x][y] != Mino.NONE;
    }

    public boolean validY(int y){
        return (0 <= y && y < HEIGHT);
    }

    public boolean validX(int x){
        return (0 <= x && x < WIDTH);
    }

    public double toGraphicX(double boardX, int x, double squareSize){
        return boardX + (x * squareSize);
    }

    public double toGraphicY(double boardY, int y, double squareSize){
        return boardY + ((y-FIRST_VISIBLE_Y) * squareSize);
    }

    public Mino[][] getBoard() {
        return board;
    }
}
