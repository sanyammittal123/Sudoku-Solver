public class sudoku{

    public boolean isSafe(char[][] board, int row, int col, int number) {
        char numChar = (char) (number + '0');
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == numChar || board[row][i] == numChar) return false;
        }
        int sr = (row / 3) * 3;
        int sc = (col / 3) * 3;
        for (int i = sr; i < sr + 3; i++) {
            for (int j = sc; j < sc + 3; j++) {
                if (board[i][j] == numChar) return false;
            }
        }
        return true;
    }

    public boolean helper(char[][] board, int row, int col) {
        if (row == board.length) return true;

        int nrow = (col == 8) ? row + 1 : row;
        int ncol = (col == 8) ? 0 : col + 1;

        if (board[row][col] != '.') {
            return helper(board, nrow, ncol);
        }

        for (int i = 1; i <= 9; i++) {
            if (isSafe(board, row, col, i)) {
                board[row][col] = (char) (i + '0');
                if (helper(board, nrow, ncol)) return true;
                board[row][col] = '.';
            }
        }
        return false;
    }

    public boolean solveSudoku(char[][] board) {
        return helper(board, 0, 0);
    }
}
