package assignment;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Test;

import assignment.BoggleGame.SearchTactic;

public class GMTest {
	@Test 
	public void testAddWord() {
		GameManager gm = new GameManager();
		GameDictionary gd = new GameDictionary();
		gd.loadDictionary("words.txt");
		gm.newGame(4, 1, "cubes.txt", gd);
		char[][] board = new char[4][4];
		board[0][0]='E'; board[0][1]='X'; board[0][2]='T'; board[0][3]='R';
		board[1][0]='B'; board[1][1]='X'; board[1][2]='R'; board[1][3]='U';
		board[2][0]='N'; board[2][1]='E'; board[2][2]='A'; board[2][3]='Q';
		board[3][0]='E'; board[3][1]='T'; board[3][2]='M'; board[3][3]='S';
		gm.setGame(board);
		gm.setSearchTactic(SearchTactic.SEARCH_BOARD);
		assertEquals("correct word", 1, gm.addWord("NEAT", 0)); //added word successfully
		ArrayList<Point> coords = new ArrayList<Point>();
		coords.add(new Point(2, 0)); coords.add(new Point(2, 1)); coords.add(new Point(2, 2)); coords.add(new Point(3, 1));
		assertEquals("coordinates", gm.getLastAddedWord(), coords); //gets correct coordinates
		assertEquals("duplicate", 0, gm.addWord("neat", 0)); //no duplicates
		assertEquals("short word", 0, gm.addWord("ten", 0)); //<4 letters
		assertEquals("not on board", 0, gm.addWord("mean", 0)); //cannot be found
		assertEquals("coordinates", gm.getLastAddedWord(), coords); //does not change last added word if failures are added
		assertEquals("correct word", 2, gm.addWord("smart", 0)); //added word successfully
		coords = new ArrayList<Point>();
		coords.add(new Point(3, 3)); coords.add(new Point(3, 2)); coords.add(new Point(2, 2)); coords.add(new Point(1, 2)); coords.add(new Point(0, 2));
		assertEquals("coordinates", gm.getLastAddedWord(), coords); //gets correct coordinates
		
		//works with a big board
		gm.newGame(6, 2, "cubes.txt", gd);
		board = new char[6][6];
		board[0][0]='B'; board[0][1]='L'; board[0][2]='A'; board[0][3]='C'; board[0][4]='K'; board[0][5]='B'; 
		board[1][0]='X'; board[1][1]='X'; board[1][2]='R'; board[1][3]='U'; board[1][4]='A'; board[1][5]='E'; 
		board[2][0]='N'; board[2][1]='E'; board[2][2]='A'; board[2][3]='Q'; board[2][4]='A'; board[2][5]='R'; 
		board[3][0]='E'; board[3][1]='T'; board[3][2]='M'; board[3][3]='S'; board[3][4]='A'; board[3][5]='R'; 
		board[4][0]='N'; board[4][1]='E'; board[4][2]='A'; board[4][3]='Q'; board[4][4]='A'; board[4][5]='I'; 
		board[5][0]='E'; board[5][1]='T'; board[5][2]='M'; board[5][3]='S'; board[5][4]='S'; board[5][5]='E'; 
		gm.setGame(board);
		gm.setSearchTactic(SearchTactic.SEARCH_DICT);
		assertEquals("correct word", 9, gm.addWord("blaCkberRies", 0)); //added word successfully
		coords = new ArrayList<Point>();
		coords.add(new Point(0, 0)); coords.add(new Point(0, 1)); coords.add(new Point(0, 2)); coords.add(new Point(0, 3)); coords.add(new Point(0, 4)); coords.add(new Point(0, 5));
		coords.add(new Point(1, 5)); coords.add(new Point(2, 5)); coords.add(new Point(3, 5)); coords.add(new Point(4, 5)); coords.add(new Point(5, 5)); coords.add(new Point(5, 4));
		assertEquals("coordinates", gm.getLastAddedWord(), coords); //gets correct coordinates
		gm.setSearchTactic(SearchTactic.SEARCH_BOARD);
		assertEquals("correct word", 2, gm.addWord("BLack", 1)); //added word successfully
		assertEquals("score", 9, gm.getScores()[0]);
		assertEquals("score", 2, gm.getScores()[1]);
		assertEquals("num players", 2, gm.getScores().length);
	}
	
	@Test //testing getAllWords
	public void testWords() {
		//testing that both methods of getting all words return same number of words
		GameManager gm = new GameManager();
		GameDictionary gd = new GameDictionary();
		gd.loadDictionary("words.txt");
		gm.newGame(4, 1, "cubes.txt", gd);
		gm.setSearchTactic(SearchTactic.SEARCH_BOARD);
		int num = gm.getAllWords().size();
		gm.setSearchTactic(SearchTactic.SEARCH_DICT);
		assertEquals("same size for diff searches", num, gm.getAllWords().size());
		ArrayList<String> words = (ArrayList<String>) gm.getAllWords();
		for(int i=0; i<words.size(); i++) {
			assertTrue(gd.contains(words.get(i))); //in dictionary
			assertTrue(words.get(i).length()>=4); //valid length
		}
		//testing that it returns 0 for a board with no words
		char[][] board = new char[4][4];
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[r].length; c++) {
				board[r][c]='A';
			}
		}
		gm.setGame(board);
		assertEquals("no words", 0, gm.getAllWords().size());
	}
	
	@Test //testing that set game correctly sets the board and that get board works
	public void testSetGameGetBoard() {
		GameManager gm = new GameManager();
		GameDictionary gd = new GameDictionary();
		gd.loadDictionary("words.txt");
		gm.newGame(4, 1, "cubes.txt", gd);
		char[][] board = new char[4][4];
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[r].length; c++) {
				board[r][c]='A';
			}
		}
		gm.setGame(board);
		board[0][0]='b';
		assertArrayEquals("Test", board, gm.getBoard());
	}
	
	@Test 
	public void testNull() {
		GameManager gm = new GameManager();
		GameDictionary gd = new GameDictionary();
		gd.loadDictionary("words.txt");
		gm.newGame(0,  1,  "cubes.txt", gd);
		gm.newGame(4,  0,  "cubes.txt", gd);
		gm.newGame(4,  1,  null, gd);
		gm.newGame(4,  1,  "cubes.txt", null);
	} 
	
	@Test
	public void testSearch() {
		GameManager gm = new GameManager();
		GameDictionary gd = new GameDictionary();
		gd.loadDictionary("words.txt");
		gm.newGame(2, 1, "cubes.txt", gd);
		char[][] board = new char[2][2];
		board[0][0]='w';
		board[0][1]='o';
		board[1][0]='r';
		board[1][1]='d';
		gm.setGame(board);
		gm.setSearchTactic(SearchTactic.SEARCH_DICT);
		System.out.println(gm.getAllWords().toString());
	}

}
