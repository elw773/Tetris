import TetrisGame.Mino;

public class TetrominoTest {

    public void printBoard(Mino[][] board){
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if(board[x][y] == Mino.NONE){
                    System.out.println(" ");
                } else {
                    System.out.print(board[x][y]);
                }
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        Mino[][] board = new Mino[6][9];
        for (int x = 0; x < board.length; x++) {
            for (int i = 0; i < board.length; i++) {

            }
        }
    }
}

/*
 0123456789
0
1
2   Z
3   ZZ
4OOOOZ OOO
5OOO  OOOO
*/

