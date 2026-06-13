package com.example.chekers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int SIZE = 8;


    private static final int EMPTY  = 0;
    private static final int BLACK  = 1;
    private static final int WHITE  = 2;
    private static final int BLACK_KING = 3;
    private static final int WHITE_KING = 4;

    // ───── UI ─────
    private GridLayout boardGrid;
    private TextView   tvStatus, tvScoreBlack, tvScoreWhite;
    private Button     btnNewGame, btnUndo;
    private View[][]   cellViews = new View[SIZE][SIZE];

    // ─────Tryb gry     ─────
    private int[][] board = new int[SIZE][SIZE];
    private boolean isBlackTurn = true;   // true = ruch czarnych
    private int selectedRow = -1, selectedCol = -1;

    private int scoreBlack = 12, scoreWhite = 12;

    // Aby cofnąć ruch
    private int[][] prevBoard;
    private boolean prevIsBlackTurn;
    private int prevScoreBlack, prevScoreWhite;

    //Lista możliwych ruchów / uderzeń dla wybranej pionka
    private List<int[]> highlightedMoves = new ArrayList<>();

    // Seryjne pobieranie
    private boolean isChainCapture = false;
    private int chainRow = -1, chainCol = -1;

    // ───── Lifecycle ─────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardGrid     = findViewById(R.id.boardGrid);
        tvStatus      = findViewById(R.id.tvStatus);
        tvScoreBlack  = findViewById(R.id.tvScoreBlack);
        tvScoreWhite  = findViewById(R.id.tvScoreWhite);
        btnNewGame    = findViewById(R.id.btnNewGame);
        btnUndo       = findViewById(R.id.btnUndo);

        btnNewGame.setOnClickListener(v -> startNewGame());
        btnUndo.setOnClickListener(v -> undoMove());

        buildCellViews();
        startNewGame();
    }

    // ───── Tworzenie komórek widokuк ─────
    private void buildCellViews() {
        boardGrid.removeAllViews();
        boardGrid.post(() -> {
            int cellSize = boardGrid.getWidth() / SIZE;
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    View cell = new View(this);
                    GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                    lp.width  = cellSize;
                    lp.height = cellSize;
                    lp.rowSpec    = GridLayout.spec(r);
                    lp.columnSpec = GridLayout.spec(c);
                    cell.setLayoutParams(lp);

                    final int row = r, col = c;
                    cell.setOnClickListener(v -> onCellClick(row, col));

                    cellViews[r][c] = cell;
                    boardGrid.addView(cell);
                }
            }
            renderBoard();
        });
    }

    // ───── Nowa gra ─────
    private void startNewGame() {
        board = new int[SIZE][SIZE];


        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if ((r + c) % 2 == 1) {
                    if (r < 3) board[r][c] = WHITE;
                    else if (r > 4) board[r][c] = BLACK;
                }
            }
        }

        isBlackTurn    = true;
        scoreBlack     = 12;
        scoreWhite     = 12;
        selectedRow    = -1;
        selectedCol    = -1;
        isChainCapture = false;
        chainRow       = -1;
        chainCol       = -1;
        highlightedMoves.clear();
        prevBoard = null;

        updateStatus();
        renderBoard();
    }

    // ─────Kliknięcie w komórkę ─────
    private void onCellClick(int row, int col) {


        if (isChainCapture) {
            if (isMoveHighlighted(row, col)) {
                executeMove(chainRow, chainCol, row, col);
            }
            // Wszelkie inne kliknięcia ignorujemy
            return;
        }

        int piece = board[row][col];

        // 1. Jeśli pionek został już wybrany — sprawdzamy, czy kliknięto na ruch
        if (selectedRow >= 0) {
            if (isMoveHighlighted(row, col)) {
                savePrevState();
                executeMove(selectedRow, selectedCol, row, col);
                return;
            }

            selectedRow = -1;
            selectedCol = -1;
            highlightedMoves.clear();
        }

        // 2. Ruch aktualnego gracza
        if (isBlackTurn && (piece == BLACK || piece == BLACK_KING)) {
            selectedRow = row;
            selectedCol = col;
            highlightedMoves = getMovesFor(row, col);
        } else if (!isBlackTurn && (piece == WHITE || piece == WHITE_KING)) {
            selectedRow = row;
            selectedCol = col;
            highlightedMoves = getMovesFor(row, col);
        }

        renderBoard();
    }

    // ───── Wykonanie kroku ─────
    private void executeMove(int fromR, int fromC, int toR, int toC) {
        int piece = board[fromR][fromC];


        int dr = toR - fromR;
        int dc = toC - fromC;
        boolean wasCapture = Math.abs(dr) == 2;

        if (wasCapture) {
            int midR = fromR + dr / 2;
            int midC = fromC + dc / 2;
            board[midR][midC] = EMPTY;
            if (isBlackTurn) scoreWhite--;
            else             scoreBlack--;
        }

        //Przeniesienie
        board[toR][toC] = piece;
        board[fromR][fromC] = EMPTY;

        //  Przekształcenie w warcaby
        if (piece == BLACK && toR == 0)        board[toR][toC] = BLACK_KING;
        if (piece == WHITE && toR == SIZE - 1) board[toR][toC] = WHITE_KING;


        if (scoreWhite == 0) { endGame("The Blacks won! 🏆"); return; }
        if (scoreBlack == 0) { endGame("The Whites won! 🏆");  return; }

        // ── Seryjne pobieranie  ──
        if (wasCapture) {
            List<int[]> nextCaptures = getCapturesOnly(toR, toC);
            if (!nextCaptures.isEmpty()) {
                // Ta sama pionek bije dalej — nie oddajemy ruchu
                isChainCapture   = true;
                chainRow         = toR;
                chainCol         = toC;
                selectedRow      = toR;
                selectedCol      = toC;
                highlightedMoves = nextCaptures;
                renderBoard();
                return;
            }
        }

        //Zwykłe zakończenie ruchu
        isChainCapture = false;
        chainRow       = -1;
        chainCol       = -1;
        selectedRow    = -1;
        selectedCol    = -1;
        highlightedMoves.clear();
        isBlackTurn = !isBlackTurn;
        updateStatus();
        renderBoard();
    }

    // ───── Tylko pobieranie dla bieżącej pozycji (seryjne)─────
    private List<int[]> getCapturesOnly(int row, int col) {
        List<int[]> captures = new ArrayList<>();
        int piece = board[row][col];
        boolean isBlack = (piece == BLACK || piece == BLACK_KING);

        for (int dr : new int[]{-1, 1}) {
            for (int dc : new int[]{-1, 1}) {
                int midR  = row + dr;
                int midC  = col + dc;
                int landR = row + 2 * dr;
                int landC = col + 2 * dc;
                if (inBounds(midR, midC) && inBounds(landR, landC)) {
                    int midPiece = board[midR][midC];
                    boolean isEnemy = isBlack
                            ? (midPiece == WHITE || midPiece == WHITE_KING)
                            : (midPiece == BLACK || midPiece == BLACK_KING);
                    if (isEnemy && board[landR][landC] == EMPTY) {
                        captures.add(new int[]{landR, landC, 1});
                    }
                }
            }
        }
        return captures;
    }

    // ─────  Możliwe ruchy w warcabach ─────
    private List<int[]> getMovesFor(int row, int col) {
        List<int[]> moves = new ArrayList<>();
        int piece = board[row][col];
        boolean isBlack = (piece == BLACK || piece == BLACK_KING);
        boolean isKing  = (piece == BLACK_KING || piece == WHITE_KING);

        //  Kierunki: czarne poruszają się w górę (-1), białe w dół (+1); warcaby — w obie strony
        int[] rowDirs;
        if (isKing)        rowDirs = new int[]{-1, 1};
        else if (isBlack)  rowDirs = new int[]{-1};   // czarne — w górę
        else               rowDirs = new int[]{ 1};   // białe — w dół

        int[] colDirs = {-1, 1};


        for (int dr : new int[]{-1, 1}) {
            for (int dc : colDirs) {
                int midR = row + dr;
                int midC = col + dc;
                int landR = row + 2 * dr;
                int landC = col + 2 * dc;
                if (inBounds(midR, midC) && inBounds(landR, landC)) {
                    int midPiece = board[midR][midC];
                    boolean isEnemy = isBlack
                            ? (midPiece == WHITE || midPiece == WHITE_KING)
                            : (midPiece == BLACK || midPiece == BLACK_KING);
                    if (isEnemy && board[landR][landC] == EMPTY) {
                        moves.add(new int[]{landR, landC, 1}); // 1 = pobierania
                    }
                }
            }
        }


        if (!moves.isEmpty()) return moves;

        // Prosty ruch
        for (int dr : rowDirs) {
            for (int dc : colDirs) {
                int nr = row + dr;
                int nc = col + dc;
                if (inBounds(nr, nc) && board[nr][nc] == EMPTY) {
                    moves.add(new int[]{nr, nc, 0});
                }
            }
        }

        return moves;
    }

    private boolean isMoveHighlighted(int row, int col) {
        for (int[] m : highlightedMoves) {
            if (m[0] == row && m[1] == col) return true;
        }
        return false;
    }

    // ───── Anulowanie marszu ─────
    private void savePrevState() {
        prevBoard = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++)
            prevBoard[r] = board[r].clone();
        prevIsBlackTurn = isBlackTurn;
        prevScoreBlack  = scoreBlack;
        prevScoreWhite  = scoreWhite;
    }

    private void undoMove() {
        if (prevBoard == null) {
            Toast.makeText(this, "There is no way to revoke it", Toast.LENGTH_SHORT).show();
            return;
        }
        board          = prevBoard;
        isBlackTurn    = prevIsBlackTurn;
        scoreBlack     = prevScoreBlack;
        scoreWhite     = prevScoreWhite;
        prevBoard      = null;
        isChainCapture = false;
        chainRow       = -1;
        chainCol       = -1;
        selectedRow    = -1;
        selectedCol    = -1;
        highlightedMoves.clear();
        updateStatus();
        renderBoard();
    }

    // ───── Koniec gry ─────
    private void endGame(String message) {
        renderBoard();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        tvStatus.setText(message);
    }

    // ───── View of the desk ─────
    private void renderBoard() {
        if (cellViews[0][0] == null) return;

        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                View cell = cellViews[r][c];
                if (cell == null) continue;

                boolean isDark     = (r + c) % 2 == 1;
                boolean isSelected = (r == selectedRow && c == selectedCol);
                boolean isHint     = isMoveHighlighted(r, c);

                // Tło komórki
                if (isSelected)       cell.setBackgroundColor(Color.parseColor("#F0C040"));
                else if (isHint)      cell.setBackgroundColor(Color.parseColor("#4CAF50"));
                else if (isDark)      cell.setBackgroundColor(Color.parseColor("#2D2D44"));
                else                  cell.setBackgroundColor(Color.parseColor("#E8E0D0"));


                drawPiece(cell, board[r][c], isDark);
            }
        }

        tvScoreBlack.setText(String.valueOf(scoreBlack));
        tvScoreWhite.setText(String.valueOf(scoreWhite));
    }


    private void drawPiece(View cell, int piece, boolean isDark) {

        boolean isSelected = false;
        for (int r = 0; r < SIZE; r++)
            for (int c = 0; c < SIZE; c++)
                if (cellViews[r][c] == cell && r == selectedRow && c == selectedCol)
                    isSelected = true;

        if (isSelected) return;

        switch (piece) {
            case BLACK:
                cell.setBackgroundColor(Color.parseColor("#1A1A1A"));
                break;
            case WHITE:
                cell.setBackgroundColor(Color.parseColor("#F5F5F5"));
                break;
            case BLACK_KING:
                cell.setBackgroundColor(Color.parseColor("#8B0000"));
                break;
            case WHITE_KING:
                cell.setBackgroundColor(Color.parseColor("#DAA520"));
                break;
            default:
                break;
        }
    }


    private boolean inBounds(int r, int c) {
        return r >= 0 && r < SIZE && c >= 0 && c < SIZE;
    }

    private void updateStatus() {
        tvStatus.setText(isBlackTurn ? "Move: Black ●" : "Move: White ○");
    }
}