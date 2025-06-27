import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class SudokuGUI extends JFrame {
    private final JTextField[][] cells = new JTextField[9][9];
    private final sudoku solver = new sudoku();
    private char[][] currentPuzzle;
    private char[][] solution;

    private JLabel timerLabel, scoreLabel;
    private int timeInSeconds = 0;
    private int hintsUsed = 0;
    private int mistakes = 0;
    private Timer gameTimer;
    private int baseScore = 1000;

    public SudokuGUI() {
        setTitle("Sudoku Solver with Timer & Score - Sujal,Sanyam,Pawan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(620, 680);
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        Font cellFont = new Font("SansSerif", Font.BOLD, 18);

        // Grid setup
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(cellFont);
                cells[i][j] = cell;

                final int row = i, col = j;
                cell.addKeyListener(new KeyAdapter() {
                    public void keyReleased(KeyEvent e) {
                        validateInput(row, col);
                        checkCompletion();
                    }
                });

                gridPanel.add(cell);
            }
        }

        JButton solveBtn = new JButton("Solve");
        JButton resetBtn = new JButton("Reset");
        JButton hintBtn = new JButton("Hint");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(solveBtn);
        buttonsPanel.add(resetBtn);
        buttonsPanel.add(hintBtn);

        solveBtn.addActionListener(e -> {
            fillSolution();
            stopTimer();
            showCompletionDialog(true);
        });
        resetBtn.addActionListener(e -> loadRandomPuzzle());
        hintBtn.addActionListener(e -> giveHint());

        timerLabel = new JLabel("Time: 0s");
        scoreLabel = new JLabel("Score: 1000");

        timerLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        scoreLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel infoPanel = new JPanel();
        infoPanel.add(timerLabel);
        infoPanel.add(scoreLabel);

        add(infoPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        loadRandomPuzzle();
        setVisible(true);
    }

    private void startTimer() {
        stopTimer(); // reset if already running
        timeInSeconds = 0;
        gameTimer = new Timer(1000, e -> {
            timeInSeconds++;
            timerLabel.setText("Time: " + timeInSeconds + "s");
        });
        gameTimer.start();
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    private void updateScore() {
        int score = baseScore - (hintsUsed * 50) - (mistakes * 10);
        scoreLabel.setText("Score: " + Math.max(score, 0));
    }

    private void validateInput(int row, int col) {
        String input = cells[row][col].getText();
        if (input.length() == 1 && Character.isDigit(input.charAt(0))) {
            char entered = input.charAt(0);
            if (solution != null && entered != solution[row][col]) {
                cells[row][col].setBackground(Color.PINK);
                mistakes++;
                updateScore();
            } else {
                cells[row][col].setBackground(Color.WHITE);
            }
        } else {
            cells[row][col].setBackground(Color.WHITE);
        }
    }

    private void giveHint() {
        if (solution == null) return;
        List<int[]> options = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String txt = cells[i][j].getText();
                if (txt.isEmpty() || txt.charAt(0) != solution[i][j]) {
                    options.add(new int[]{i, j});
                }
            }
        }
        if (!options.isEmpty()) {
            Random rand = new Random();
            int[] hintCell = options.get(rand.nextInt(options.size()));
            int i = hintCell[0], j = hintCell[1];
            cells[i][j].setText(String.valueOf(solution[i][j]));
            cells[i][j].setBackground(new Color(198, 255, 198)); // light green
            hintsUsed++;
            updateScore();
        }
    }

    private void fillSolution() {
        if (solution == null) return;
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText(String.valueOf(solution[i][j]));
                cells[i][j].setBackground(Color.WHITE);
            }
    }

    private void checkCompletion() {
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                String txt = cells[i][j].getText();
                if (txt.isEmpty() || txt.charAt(0) != solution[i][j]) return;
            }
        stopTimer();
        showCompletionDialog(false);
    }

    private void showCompletionDialog(boolean autoSolved) {
        String msg = autoSolved ? "You viewed the solution." : "You completed the puzzle!";
        msg += "\nTime: " + timeInSeconds + " seconds\nHints used: " + hintsUsed +
                "\nMistakes: " + mistakes + "\nFinal Score: " + Math.max(baseScore - (hintsUsed * 50) - (mistakes * 10), 0);
        JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadRandomPuzzle() {
        char[][][] puzzles = {
                {
                        {'5','3','.','.','7','.','.','.','.'},
                        {'6','.','.','1','9','5','.','.','.'},
                        {'.','9','8','.','.','.','.','6','.'},
                        {'8','.','.','.','6','.','.','.','3'},
                        {'4','.','.','8','.','3','.','.','1'},
                        {'7','.','.','.','2','.','.','.','6'},
                        {'.','6','.','.','.','.','2','8','.'},
                        {'.','.','.','4','1','9','.','.','5'},
                        {'.','.','.','.','8','.','.','7','9'}
                },
                {
                        {'2','.','.','6','.','.','9','.','.'},
                        {'.','4','.','.','.','.','.','.','.'},
                        {'.','.','3','.','.','5','.','.','8'},
                        {'.','1','2','.','.','.','.','.','.'},
                        {'7','.','.','.','.','.','.','.','5'},
                        {'.','.','.','.','.','.','6','3','.'},
                        {'5','.','.','4','.','.','1','.','.'},
                        {'.','.','.','.','.','.','.','7','.'},
                        {'.','.','7','.','.','6','.','.','9'}
                }
        };

        Random rand = new Random();
        currentPuzzle = puzzles[rand.nextInt(puzzles.length)];

        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                char val = currentPuzzle[i][j];
                cells[i][j].setText(val == '.' ? "" : String.valueOf(val));
                cells[i][j].setBackground(Color.WHITE);
            }

        solution = new char[9][9];
        for (int i = 0; i < 9; i++)
            System.arraycopy(currentPuzzle[i], 0, solution[i], 0, 9);

        solver.solveSudoku(solution);

        hintsUsed = 0;
        mistakes = 0;
        baseScore = 1000;
        updateScore();
        startTimer();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGUI::new);
    }
}
