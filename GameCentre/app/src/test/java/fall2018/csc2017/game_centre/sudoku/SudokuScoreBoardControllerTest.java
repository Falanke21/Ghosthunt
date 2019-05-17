package fall2018.csc2017.game_centre.sudoku;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SudokuScoreBoardControllerTest {
    private ArrayList<Integer> testList1 = new ArrayList<>();
    private ArrayList<Integer> testList2 = new ArrayList<>();


    @Before
    public void setUp() {
        testList1.add(0, 3);
        testList1.add(1, 0);
        testList1.add(2, 200);
        testList2.add(0, 0);
        testList2.add(1, 3);
        testList2.add(2, 300);
    }

    @Test
    public void calculateScore() {
        SudokuScoreBoardController scoreBoard = new SudokuScoreBoardController();
        assertEquals(10100, scoreBoard.calculateScore(testList1));
        assertEquals(9400, scoreBoard.calculateScore(testList2));
    }
}