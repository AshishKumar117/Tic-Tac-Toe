import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TicTacToe {
    int boardWidth = 600;
    int boardHeight = 700;

    JFrame frame = new JFrame("Tic-Tac-Toe");
    JLabel textLabel = new JLabel();
    JLabel scoreLabel = new JLabel();
    JPanel topPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    JButton restartButton = new JButton("Restart");

    JButton[][] board = new JButton[3][3];
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX;

    boolean gameOver = false;
    int turns = 0;
    int scoreX = 0, scoreO = 0, ties = 0;

    Color bgDark = new Color(30, 30, 35);
    Color tileColor = new Color(45, 45, 52);
    Color hoverColor = new Color(60, 60, 70);
    Color xColor = new Color(255, 99, 99);
    Color oColor = new Color(97, 175, 255);
    Color winColor = new Color(80, 200, 120);
    Color tieColor = new Color(230, 180, 70);

    TicTacToe() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(bgDark);

        // Top panel: title + score
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(bgDark);
        topPanel.setBorder(new EmptyBorder(15, 20, 10, 20));

        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("X's turn");
        topPanel.add(textLabel, BorderLayout.NORTH);

        scoreLabel.setForeground(new Color(180, 180, 190));
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        updateScoreLabel();
        topPanel.add(scoreLabel, BorderLayout.SOUTH);

        frame.add(topPanel, BorderLayout.NORTH);

        // Board
        boardPanel.setLayout(new GridLayout(3, 3, 8, 8));
        boardPanel.setBackground(bgDark);
        boardPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        frame.add(boardPanel, BorderLayout.CENTER);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                tile.setBackground(tileColor);
                tile.setFont(new Font("Segoe UI", Font.BOLD, 100));
                tile.setFocusable(false);
                tile.setBorderPainted(false);
                tile.setOpaque(true);
                tile.setText("");

                tile.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (!gameOver && tile.getText().isEmpty()) tile.setBackground(hoverColor);
                    }
                    public void mouseExited(MouseEvent e) {
                        if (!gameOver && tile.getText().isEmpty()) tile.setBackground(tileColor);
                    }
                });

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver || !tile.getText().isEmpty()) return;

                        tile.setText(currentPlayer);
                        tile.setForeground(currentPlayer.equals(playerX) ? xColor : oColor);
                        turns++;
                        checkWinner();

                        if (!gameOver) {
                            currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                            textLabel.setText(currentPlayer + "'s turn");
                        }
                    }
                });
            }
        }

        // Bottom panel: restart
        bottomPanel.setBackground(bgDark);
        bottomPanel.setBorder(new EmptyBorder(5, 20, 20, 20));
        restartButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        restartButton.setBackground(new Color(65, 65, 75));
        restartButton.setForeground(Color.white);
        restartButton.setFocusable(false);
        restartButton.setBorderPainted(false);
        restartButton.setPreferredSize(new Dimension(160, 45));
        restartButton.addActionListener(e -> resetBoard());
        bottomPanel.add(restartButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    void checkWinner() {
        for (int r = 0; r < 3; r++) {
            if (!board[r][0].getText().isEmpty() &&
                board[r][0].getText().equals(board[r][1].getText()) &&
                board[r][1].getText().equals(board[r][2].getText())) {
                declareWin(board[r][0], board[r][1], board[r][2]);
                return;
            }
        }
        for (int c = 0; c < 3; c++) {
            if (!board[0][c].getText().isEmpty() &&
                board[0][c].getText().equals(board[1][c].getText()) &&
                board[1][c].getText().equals(board[2][c].getText())) {
                declareWin(board[0][c], board[1][c], board[2][c]);
                return;
            }
        }
        if (!board[0][0].getText().isEmpty() &&
            board[0][0].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][2].getText())) {
            declareWin(board[0][0], board[1][1], board[2][2]);
            return;
        }
        if (!board[0][2].getText().isEmpty() &&
            board[0][2].getText().equals(board[1][1].getText()) &&
            board[1][1].getText().equals(board[2][0].getText())) {
            declareWin(board[0][2], board[1][1], board[2][0]);
            return;
        }
        if (turns == 9) {
            for (int r = 0; r < 3; r++)
                for (int c = 0; c < 3; c++)
                    setTie(board[r][c]);
            ties++;
            updateScoreLabel();
            textLabel.setText("It's a tie!");
            gameOver = true;
        }
    }

    void declareWin(JButton a, JButton b, JButton c) {
        setWinner(a); setWinner(b); setWinner(c);
        if (currentPlayer.equals(playerX)) scoreX++; else scoreO++;
        updateScoreLabel();
        textLabel.setText(currentPlayer + " wins!");
        gameOver = true;
    }

    void setWinner(JButton tile) {
        tile.setBackground(winColor);
        tile.setForeground(Color.white);
    }

    void setTie(JButton tile) {
        tile.setBackground(tieColor);
    }

    void updateScoreLabel() {
        scoreLabel.setText("X: " + scoreX + "   |   O: " + scoreO + "   |   Ties: " + ties);
    }

    void resetBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setBackground(tileColor);
            }
        }
        turns = 0;
        gameOver = false;
        currentPlayer = playerX;
        textLabel.setText("X's turn");
    }
}
