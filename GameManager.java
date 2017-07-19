package assignment;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager implements BoggleGame{
	
	private char[][] board;
	private BoggleDictionary dictionary;
	private ArrayList<ArrayList<Character>> cubeList;
	private int[] scores;
	private ArrayList<String> words;
	private HashMap<Integer, ArrayList<String>> playerWords;
	private SearchTactic tactic;
	private ArrayList<Point> wordCoords;
	
	public void newGame(int size, int numPlayers, String cubeFile, BoggleDictionary dict) {
		try {
			if(size<1 || numPlayers<1) {
				System.out.println("Game could not be created due to incorrect parameters.");
				return;
			}
			File cubesFile = new File(cubeFile);
			if(!cubesFile.exists()) {
				throw new FileNotFoundException();
			}
			if(cubeFile==null||dict==null) {
				throw new NullPointerException();
			}
			board = new char[size][size];
			cubeList = new ArrayList<ArrayList<Character>>();
			setCubes(cubeFile);
			dictionary=dict;
			scores = new int[numPlayers];
			words = new ArrayList<String>();
			playerWords = new HashMap<Integer, ArrayList<String>>();
			for(int i=0; i<numPlayers; i++) {
				ArrayList<String> list = new ArrayList<String>();
				playerWords.put(i, list);
			}
			wordCoords=new ArrayList<Point>();
			tactic=SearchTactic.SEARCH_BOARD;
		}		
		catch(FileNotFoundException e) {
			System.out.println("The cubes file could not be found, so a new game could not be created.");
		}
		catch(NullPointerException e) {
			System.out.println("A null parameter was provided so a new game could not be created.");
		}
	}
	
	private void setCubes(String file) {
		try 
    	{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line=new String(); 
			//loads in cubes to arraylist of cubes
			//if cube has less than six sides, will choose from previous sides to add more
			while((line = br.readLine()) != null) 
			{
				if(line.equals("")) {
					continue;
				}
				ArrayList<Character> cube = new ArrayList<Character>();
				for(int i=0; i<line.length(); i++) {
					cube.add(line.charAt(i));
				}
				if(cube.size()<6) {
					System.out.println("Cube given has less than six sides. Will choose remaining characters from current.");
				}
				while(cube.size()<6) {
					Random place = new Random();
					int count = place.nextInt(cube.size());
					cube.add(cube.get(count));
				}
				cubeList.add(cube);
			} 
			
			fr.close();
			//if not enough cubes, will redraw from existing
			if(board.length*board.length>cubeList.size()) {
				System.out.println("Not enough cubes provided. Will draw required number from existing cubes,");
			}
			while(board.length*board.length>cubeList.size()) {
				Random place = new Random();
				int count = place.nextInt(cubeList.size());
				cubeList.add(cubeList.get(count));
			}
			setBoardCubes();
		
		} 
    	catch (FileNotFoundException e) { //should never happen as validated in Boggle
    		e.printStackTrace();
		} 
		catch (IOException e) {
			System.out.println("Error reading the file.");
		}
	}
	
	private void setBoardCubes() {
		
		Random place = new Random();
		int count = place.nextInt(cubeList.size());
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[r].length; c++) {
				ArrayList<Character> chars = cubeList.get(count);
				Random rand = new Random();
				int index = rand.nextInt(chars.size());
				board[r][c]=Character.toUpperCase(chars.get(index));
				cubeList.remove(count);
				if(cubeList.size()>0) {
					count = place.nextInt(cubeList.size());
				}
				
			}
		}
		//randomly picks place for cube, then "rolls" each to determine which side is shown
	}

	public char[][] getBoard() {
		return board;
	}

	public int addWord(String word, int player) {
		if(word==null||player>=scores.length) {
			return 0;
		}
		//prints error messages or adds word
		if(dictionary.contains(word)&&checkValid(word.toUpperCase())&&!playerWords.get(player).contains(word.toUpperCase())) {
			ArrayList<String> current = playerWords.get(player);
			current.add(word.toUpperCase());
			playerWords.put(player, current); //updating HashMap with player's new word
			scores[player]+=word.length()-3;
			words.add(word);
			return word.length()-3;
		}
		else if(word.length()<4) {
			System.out.println("Word is too short.");
		}
		else if(!dictionary.contains(word)) {
			System.out.println("This does not exist in the dictionary.");
		}
		else if(!checkValid(word.toUpperCase())) {
			System.out.println("This word cannot be found on the board.");
		}
		else if(playerWords.get(player).contains(word.toUpperCase())) {
			System.out.println("Word was already counted.");
		}
		
		return 0; 
	}

	public List<Point> getLastAddedWord() {
		if(wordCoords.size()==0) {
			return null;
		}
		return wordCoords;
	}

	public void setGame(char[][] board) {	
		if(board==null||board.length<1) {
			System.out.println("Invalid board passed in.");
			return;
		}
		if(board!=null&&board.length==board[0].length) {
			for(int r=0; r<board.length; r++) {
				for(int c=0; c<board[r].length; c++) {
					this.board[r][c]=Character.toUpperCase(board[r][c]);
				}
			}
		}
		else {
			System.out.println("Invalid board passed in.");
		}
		for(int i=0; i<scores.length; i++) {
			scores[i]=0;
		}
		words=new ArrayList<String>();
		playerWords=new HashMap<Integer, ArrayList<String>>();
		for(int i=0; i<scores.length; i++) {
			ArrayList<String> list = new ArrayList<String>();
			list.add("");
			playerWords.put(i, list);
		}
		tactic=SEARCH_DEFAULT;
		//resetting all values except board
	}

	public Collection<String> getAllWords() {
		
		if(tactic.equals(SearchTactic.SEARCH_BOARD)) {
			ArrayList<Point> coords = new ArrayList<Point>();
			coords.add(new Point(0, 0)); //adding point so can set it later
			for(int r=0; r<board.length; r++) {
				for(int c=0; c<board[r].length; c++) {
					coords.set(0, new Point(r, c));
					searchBoard(coords, "", r, c);
				}
			}
			
			return words;
		}
		else { //should be SEARCH_DICT
			return dictionarySearch();
		}
		
	}
	
	private ArrayList<String> dictionarySearch() {
		ArrayList<String> validWords = new ArrayList<String>();
		Iterator<String> x = dictionary.iterator();
		while(x.hasNext()) {
			String word = x.next();
			if(checkValid(word.toUpperCase())&&!validWords.contains(word.toUpperCase())) {
				validWords.add(word.toUpperCase());
			}
		}
 		return validWords;
 		//simply iterate through and see if each word can be made
	}
	
	private void searchBoard(ArrayList<Point> coords, String word, int r, int c) {
		//coords makes sure point has not been traversed already
		word+=board[r][c];
		if(dictionary.isPrefix(word)) { //if it isn't, do nothing
			for(int x=-1; x<=1; x++) {
				for(int y=-1; y<=1; y++) { //go through surrounding squares
					if(valid(r+x, c+y)&&!coords.contains(new Point(r+x, c+y))) {
						ArrayList<Point> newCoords = new ArrayList<Point>(coords);
						newCoords.add(new Point(r+x, c+y));
						String testWord = word+board[r+x][c+y];
						if(testWord.length()>=4&&!words.contains(testWord.toUpperCase())&&!words.contains(testWord.toLowerCase())&&dictionary.contains(testWord.toLowerCase())) {
							words.add(testWord.toUpperCase());
						}
						searchBoard(newCoords, word, r+x, c+y);
					}
				}
			}
		}
		
	}
	
	private boolean checkValid(String word) {
		if(word.length()<4) {
			return false;
		}
		boolean isValid=false;
		ArrayList<Point> coords = new ArrayList<Point>();
		coords.add(new Point(0, 0));
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[r].length; c++) {				
				if(board[r][c]==word.charAt(0)) {
					coords.set(0, new Point(r, c));
					if(recurseValid(coords, word.substring(1), r, c)) {
						isValid=true;
					}
				}				
			}
		}
		
		return isValid;
	}
	
	private boolean recurseValid(ArrayList<Point> coords, String word, int r, int c) {
		if(word.length()<1) {
			return true;
		}
		boolean isValid=false;
		for(int x=-1; x<=1; x++) {
			for(int y=-1; y<=1; y++) { //check surrounding coordinates to see if they contain the char
				if(valid(r+x, c+y)&&word.charAt(0)==board[r+x][c+y]&&!coords.contains(new Point(r+x, c+y))) {
					isValid=true;
					ArrayList<Point> newCoords = new ArrayList<Point>(coords);
					newCoords.add(new Point(r+x, c+y));
					if(!recurseValid(newCoords, word.substring(1), r+x, c+y)) {
						isValid=false;
						continue; //move to next iteration if this one wasn't
					}
					else{
						if(word.length()==1) {
							wordCoords=newCoords;
						}
						return true;
					}
				}
			}
		}
		if(isValid==false) {
			return false;
		}
		return true;
	}
	
	private boolean valid(int r, int c) { //checks if coord is valid
		if(r<board.length&&r>=0&&c<board[r].length&&c>=0) {
			return true;
		}
		return false;
	}

	public void setSearchTactic(SearchTactic tactic) {
		if(tactic==null) {
			return;
		}
		if(tactic!=SearchTactic.SEARCH_BOARD&&tactic!=SearchTactic.SEARCH_DICT) {
			this.tactic=SEARCH_DEFAULT;
		}
		this.tactic=tactic;
	}

	public int[] getScores() {
		return scores;
	}


}
