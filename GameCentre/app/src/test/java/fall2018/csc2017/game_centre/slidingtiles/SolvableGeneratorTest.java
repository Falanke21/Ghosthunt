package fall2018.csc2017.game_centre.slidingtiles;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SolvableGeneratorTest {
    private SolvableGenerator generator;

    @Before
    public void setUp() {
        generator = new SolvableGenerator();
    }

    /**
     * @param  array an array of integers
     * @return a list of tiles that are ordered as array.
     */
    private List<Tile> createTileList(int[] array) {
        List<Tile> tiles = new ArrayList<>();
        for (int i : array) {
            tiles.add(new Tile(i));
        }
        return tiles;
    }

    @Test
    public void generateTileListLength3() {
        List<Tile> tiles = generator.generateTileList(3);
        assertTrue(generator.isSolvable(tiles, 3));
    }

    @Test
    public void generateTileListLength4() {
        List<Tile> tiles = generator.generateTileList(4);
        assertTrue(generator.isSolvable(tiles, 4));
    }

    @Test
    public void generateTileListLength5() {
        List<Tile> tiles = generator.generateTileList(5);
        assertTrue(generator.isSolvable(tiles, 5));
    }

    @Test
    public void isSolvableLengthThreeOrdered() {
        List<Tile> tiles = createTileList(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
        assertTrue(generator.isSolvable(tiles, 3));
    }

    @Test
    public void isSolvableLengthThreeOneInversion() {
        List<Tile> tiles = createTileList(new int[]{0, 1, 3, 2, 4, 5, 6, 7, 8});
        assertFalse(generator.isSolvable(tiles, 3));
    }

    @Test
    public void isSolvableLengthThreeRandom() {
        List<Tile> tiles = createTileList(new int[]{3, 2, 8, 0, 5, 1, 6, 7, 4});
        assertFalse(generator.isSolvable(tiles, 3));
    }


    @Test
    public void isSolvableLengthThreeRandomTwo() {
        List<Tile> tiles = createTileList(new int[]{3, 2, 8, 0, 5, 1, 6, 4, 7});
        assertTrue(generator.isSolvable(tiles, 3));
    }

    @Test
    public void isSolvableLengthReverseOrder() {
        List<Tile> tiles = createTileList(new int[]{8, 7, 6, 5, 4, 3, 2, 1, 0});
        assertTrue(generator.isSolvable(tiles, 3));
    }

    @Test
    public void isSolvableLengthFourOrdered() {
        List<Tile> tiles = createTileList(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});

        assertTrue(generator.isSolvable(tiles, 4));
    }
    @Test
    public void isSolvableLengthFourRandom() {
        List<Tile> tiles = createTileList(new int[]{3, 2, 8, 0, 5, 1, 6, 7, 4, 9, 10, 11, 12, 13, 15, 14});
        assertFalse(generator.isSolvable(tiles, 4));
        }
    @Test
    public void isSolvableLengthFiveOrdered() {
        List<Tile> tiles = createTileList(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
                                         16, 17, 18, 19, 20, 21, 22, 23, 24});
        assertTrue(generator.isSolvable(tiles, 5));
        }
    @Test
    public void isSolvableLengthFiveOneInversion() {
        List<Tile> tiles = createTileList(new int[]{0, 1, 2, 3, 4, 5, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24});
        assertFalse(generator.isSolvable(tiles, 5));
        }
    }