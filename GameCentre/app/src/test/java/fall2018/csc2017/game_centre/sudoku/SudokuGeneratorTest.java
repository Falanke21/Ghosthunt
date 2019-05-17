package fall2018.csc2017.game_centre.sudoku;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Set;

import java.util.HashSet;

import static org.junit.Assert.*;

public class SudokuGeneratorTest {

    private SudokuGenerator generator;

    private static final int[][] ROOT_GRID = {
            {9, 3, 6, 4, 2, 7, 5, 1, 8},
            {7, 8, 2, 1, 5, 3, 4, 6, 9},
            {1, 4, 5, 6, 9, 8, 7, 2, 3},
            {5, 7, 1, 3, 8, 2, 9, 4, 6},
            {2, 9, 3, 5, 6, 4, 8, 7, 1},
            {8, 6, 4, 7, 1, 9, 3, 5, 2},
            {3, 5, 9, 2, 7, 6, 1, 8, 4},
            {4, 2, 7, 8, 3, 1, 6, 9, 5},
            {6, 1, 8, 9, 4, 5, 2, 3, 7}};

    private int[][] grid1;


    @Before
    public void setUp() {
        generator = new SudokuGenerator();
    }


    @Test
    public void generateCorrectSudoku() {
        grid1 = generator.generate(0);
        assertTrue(checkRows(ROOT_GRID, grid1));
        assertTrue(checkColumn(ROOT_GRID,grid1));
        assertTrue(checkSquare(ROOT_GRID,grid1));
    }

    @Test
    public void checkGenerate20Empty() {
        grid1 = generator.generate(20);
        int negativeCounter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid1[i][j] < 0){
                    negativeCounter++;
                }
            }
        }
        assertEquals(20, negativeCounter);
    }

    @Test
    public void checkGenerate40Empty() {
        grid1 = generator.generate(40);
        int negativeCounter = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid1[i][j] < 0){
                    negativeCounter++;
                }
            }
        }
        assertEquals(40, negativeCounter);
    }


    private Set<Integer> findSquare(int row, int col, int[][] a) {
        row -= row % 3;
        col -= col % 3;
        Set<Integer> result = new HashSet<>();
        for (int i = row; i <= row + 2; i++) {
            for (int j = col; j <= col + 2; j++) {
                result.add(a[i][j]);
            }
        }
        return result;
    }
    private boolean checkRows(int[][] a, int[][] b){
        for(int i = 0; i<= 8; i++){
            Arrays.sort(a[i]);
            Arrays.sort(b[i]);
            if  (! Arrays.equals(a[i],b[i])){
                return false;
            }
        }
        return true;
    }
    private boolean checkColumn(int[][] a, int[][] b){
        for(int i = 0; i<= 8; i++){
            int[] c = new int[a.length];
            int[] d = new int[b.length];
            for (int j=0; j<=8;j++){
                c[i] = a[i][j];
                d[i] = b[i][j];
            }
            Arrays.sort(c);
            Arrays.sort(d);
            if (! Arrays.equals(c,d)){
                return false;
            }
        }
        return true;
    }
    private boolean checkSquare(int[][] a, int[][] b){
        for (int i = 8; i>=0; i=i-3){
            Set<Integer> result1 = findSquare(i, i, a);
            Set<Integer> result2 = findSquare(i, i, b);
            if (! result1.equals(result2)){
                return false;
            }
        }
        return true;
    }
}