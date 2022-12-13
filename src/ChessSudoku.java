package ChessSudokuSolver.src;

import java.util.*;
import java.io.*;


public class ChessSudoku {
    /* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For
     * a standard Sudoku puzzle, SIZE is 3 and N is 9.
     */
    public int SIZE, N;

    /* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
     * not yet been revealed are stored as 0.
     */
    public int grid[][];

    /* Booleans indicating whether of not one or more of the chess rules should be
     * applied to this Sudoku.
     */
    public boolean knightRule;
    public boolean kingRule;
    public boolean queenRule;


    // Field that stores the same Sudoku puzzle solved in all possible ways
    public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();

    private Random random;

    private boolean validation(int row, int column, int value, boolean knightRule, boolean kingRule, boolean queenRule) {

        // Store the variables to know which subsection we're on
        int row_start = (row / SIZE) * SIZE;
        int row_end = row_start + SIZE;
        int column_start = (column / SIZE) * SIZE;
        int column_end = column_start + SIZE;

        // Check each sections
        // Iterate through the section's row and column
        for (int sub_row = row_start; sub_row < row_end; sub_row++) {

            // Iterate through all the numbers in the sections
            for (int sub_column = column_start; sub_column < column_end; sub_column++) {

                // If any of the values in the section match, return false
                if (grid[sub_row][sub_column] == value) {
                    return false;
                }
            }
        }

        // Check the row and column
        // Iterate by the size of the table
        for (int i = 0; i < N; i++) {

            // If the value is not unique, return false
            if (grid[row][i] == value || grid[i][column] == value) {
                return false;
            }
        }

        if (knightRule) {

            try {
                if (grid[row - 1][column - 2] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row - 1][column + 2] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row - 2][column - 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row - 2][column + 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row + 1][column - 2] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row + 1][column + 2] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row + 2][column - 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row + 2][column + 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

        }
        if (kingRule) {

            try {
                if (grid[row - 1][column - 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row - 1][column] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row - 1][column + 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row][column - 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row][column] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row][column + 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row + 1][column - 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row + 1][column + 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }

            try {
                if (grid[row + 1][column + 1] == value) {
                    return false;
                }
            } catch (Exception ignored) {
            }
        }
        if (queenRule && value == N) {
            for (int i = 1; i < N; i++) {
                // Upper left diagonal
                try {
                    if (grid[row - i][column - i] == value) {
                        return false;
                    }
                } catch (Exception ignored) {
                }

                try {
                    if (grid[row - i][column + i] == value) {
                        return false;
                    }
                } catch (Exception ignored) {
                }

                try {
                    if (grid[row + i][column - i] == value) {
                        return false;
                    }
                } catch (Exception ignored) {
                }

                try {
                    if (grid[row + i][column + i] == value) {
                        return false;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return true;
    }

    private class options {
        private int[] option = new int[N];
        private int index = 0;
    }

    private options fill(int row, int column) {
        options temp = new options();
        for (int value = 0; value <= N; value++) {
            if (validation(row, column, value, knightRule, kingRule, queenRule)) {
                temp.option[temp.index] = value;
                temp.index++;
            }
        }
        return temp;
    }

    private boolean isempty() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void update(options small, int small_row, int small_column, Random random) {

        if (small.index == 0) {
            for (int row = 0; row < N; row++) {
                for (int col = 0; col < N; col++) {

                    if (grid[row][col] != 0) {

                        if (random.nextFloat() <= 0.099) {
                            grid[row][col] = 0;
                        }
                    }
                }
            }
        } else {
            grid[small_row][small_column] = small.option[random.nextInt(small.index)];
        }


    }

    private void randomize() {

        int small_row = 0;
        int small_column = 0;

        while (!isempty()) {


            options small = new options();
            small.index = N + 1;

            for (int row = 0; row < N; row++) {
                for (int column = 0; column < N; column++) {

                    if (grid[row][column] == 0) {
                        options temp;
                        temp = fill(row, column);

                        if (small.index > temp.index) {

                            // Update the smallest node
                            small.index = temp.index;

                            // Copy the array
                            if (small.index >= 0) System.arraycopy(temp.option, 0, small.option, 0, small.index);

                            // Find the smallest indices
                            small_row = row;
                            small_column = column;
                        }
                    }
                }
            }
            update(small, small_row, small_column, random);
        }
    }

    // Store the locations of zeros and randomize the start
    private boolean recursion_solve(boolean allSolutions) {

        // Iterate through both row and column of the grid
        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid.length; column++) {

                // Check if the number is 0(empty)
                if (grid[row][column] == 0) {

                    // If so, iterate through the numbers to see which value is the correct solution
                    for (int value = 1; value <= N; value++) {

                        if (validation(row, column, value, knightRule, kingRule, queenRule)) {

                            grid[row][column] = value;
                            if (recursion_solve(allSolutions)) {

                                if (isempty() && allSolutions) {
                                    ChessSudoku copy = new ChessSudoku(SIZE);
                                    for (int r = 0; r < N; r++) {
                                        for (int c = 0; c < N; c++) {
                                            copy.grid[r][c] = grid[r][c];
                                        }
                                    }
                                    solutions.add(copy);
                                    grid[row][column] = 0;
                                } else {
                                    return true;
                                }
                            } else {
                                grid[row][column] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /* The solve() method should remove all the unknown characters ('x') in the grid
     * and replace them with the numbers in the correct range that satisfy the constraints
     * of the Sudoku puzzle. If true is provided as input, the method should find finds ALL
     * possible solutions and store them in the field named solutions. */
    public void solve(boolean allSolutions) {

        random = new Random();
        if (SIZE != 3) {
            randomize();
        } else {
            recursion_solve(allSolutions);
        }
    }

    /* Default constructor.  This will initialize all positions to the default 0
     * value.  Use the read() function to load the Sudoku puzzle from a file or
     * the standard input. */
    public ChessSudoku(int size) {
        SIZE = size;
        N = size * size;

        grid = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                grid[i][j] = 0;
    }


    /* readInteger is a helper function for the reading of the input file.  It reads
     * words until it finds one that represents an integer. For convenience, it will also
     * recognize the string "x" as equivalent to "0". */
    static int readInteger(InputStream in) throws Exception {
        int result = 0;
        boolean success = false;

        while (!success) {
            String word = readWord(in);

            try {
                result = Integer.parseInt(word);
                success = true;
            } catch (Exception e) {
                // Convert 'x' words into 0's
                if (word.compareTo("x") == 0) {
                    result = 0;
                    success = true;
                }
                // Ignore all other words that are not integers
            }
        }

        return result;
    }


    /* readWord is a helper function that reads a word separated by white space. */
    static String readWord(InputStream in) throws Exception {
        StringBuffer result = new StringBuffer();
        int currentChar = in.read();
        String whiteSpace = " \t\r\n";
        // Ignore any leading white space
        while (whiteSpace.indexOf(currentChar) > -1) {
            currentChar = in.read();
        }

        // Read all characters until you reach white space
        while (whiteSpace.indexOf(currentChar) == -1) {
            result.append((char) currentChar);
            currentChar = in.read();
        }
        return result.toString();
    }


    /* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
     * grid is filled in one row at at time, from left to right.  All non-valid
     * characters are ignored by this function and may be used in the Sudoku file
     * to increase its legibility. */
    public void read(InputStream in) throws Exception {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = readInteger(in);
            }
        }
    }


    /* Helper function for the printing of Sudoku puzzle.  This function will print
     * out text, preceded by enough ' ' characters to make sure that the printint out
     * takes at least width characters.  */
    void printFixedWidth(String text, int width) {
        for (int i = 0; i < width - text.length(); i++)
            System.out.print(" ");
        System.out.print(text);
    }


    /* The print() function outputs the Sudoku grid to the standard output, using
     * a bit of extra formatting to make the result clearly readable. */
    public void print() {
        // Compute the number of digits necessary to print out each number in the Sudoku puzzle
        int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;

        // Create a dashed line to separate the boxes
        int lineLength = (digits + 1) * N + 2 * SIZE - 3;
        StringBuffer line = new StringBuffer();
        for (int lineInit = 0; lineInit < lineLength; lineInit++)
            line.append('-');

        // Go through the grid, printing out its values separated by spaces
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                printFixedWidth(String.valueOf(grid[i][j]), digits);
                // Print the vertical lines between boxes
                if ((j < N - 1) && ((j + 1) % SIZE == 0))
                    System.out.print(" |");
                System.out.print(" ");
            }
            System.out.println();

            // Print the horizontal line between boxes
            if ((i < N - 1) && ((i + 1) % SIZE == 0))
                System.out.println(line.toString());
        }
    }


    /* The main function reads in a Sudoku puzzle from the standard input,
     * unless a file name is provided as a run-time argument, in which case the
     * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
     * outputs the completed puzzle to the standard output. */
    public static void main(String args[]) throws Exception {
        InputStream in = new FileInputStream("veryEasy3x3.txt");

        // The first number in all Sudoku files must represent the size of the puzzle.  See
        // the example files for the file format.
        int puzzleSize = readInteger(in);
        if (puzzleSize > 100 || puzzleSize < 1) {
            System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
            System.exit(-1);
        }

        ChessSudoku s = new ChessSudoku(puzzleSize);

        // You can modify these to add rules to your sudoku
        s.knightRule = false;
        s.kingRule = false;
        s.queenRule = false;

        // read the rest of the Sudoku puzzle
        s.read(in);

        System.out.println("Before solving:");
        s.print();
        System.out.println();

        // Solve the puzzle by finding one solution.
        s.solve(false);

        // Print out the (hopefully completed!) puzzle
        System.out.println("After solving:");
        s.print();
    }
}

