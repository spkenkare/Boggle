package assignment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import assignment.BoggleGame.SearchTactic;

public class Boggle {
	private char[][] board;
	private boolean[][] cases; //keeps track of what was just selected
	public static void main(String[] args) {	
		setupGame();				
	}
	
	private static void setupGame() {
		try {
			boolean check=true;
			Scanner reader = new Scanner(System.in); 
			while(check) {
				
				System.out.println("Please enter the size n of the nxn board.");
				int n = reader.nextInt();
				if(n<1) {
					System.out.println("Board size cannot be 0 or less.");
					setupGame();
				}
				System.out.println("Please enter the number of players.");
				int players = reader.nextInt();
				if(players<1) {
					System.out.println("Players cannot be 0 or less.");
					setupGame();
				}
				System.out.println("Please enter the name of the cubes input file.");
				String cubes = reader.next();
				File cubesFile = new File(cubes);
				if(!cubesFile.exists()) {
					System.out.println("Invalid file.");
					setupGame();
				}
				System.out.println("Please enter the name of the dictionary input file.");
				String dict = reader.next();
				File dictFile = new File(dict);
				if(!dictFile.exists()) {
					System.out.println("Invalid file.");
					setupGame();
				}
				
				Boggle b = new Boggle();
				check = b.setup(n, players, cubes, dict);
				
			}
			reader.close();
		}
		
		catch(NullPointerException e) {
			e.printStackTrace();
			System.out.println("User code may be trying to use a null pointer or call code out of order. Please create a new game before adding a word, setting a game, or getting all words.");
			setupGame(); //reprompts until params are valid so program won't terminate
		}
		catch(Exception e) {
			System.out.println("A parameter was invalid.");
			setupGame(); //reprompts until params are valid so program won't terminate
		}
	}
	
	private boolean setup(int size, int players, String cubes, String dict) {
		GameManager gm = new GameManager();
		GameDictionary d = new GameDictionary();
		if(dict!=null) {
			d.loadDictionary(dict);
		}
		gm.newGame(size, players, cubes, d);
		board = new char[size][size];
		board = gm.getBoard();
		cases=new boolean[board.length][board.length];
		Scanner reader = new Scanner(System.in);  
		HashMap<Integer, ArrayList<String>> playerWords = new HashMap<Integer, ArrayList<String>>();
		for(int i=0; i<gm.getScores().length; i++) {
			for(int r=0; r<cases.length; r++) {
				for(int c=0; c<cases[r].length; c++) {
					cases[r][c]=false;
				}
			}
			
			System.out.println("PLAYER "+i+" TURN");
			printBoard();
			String n = reader.next();
			int score=0;
			ArrayList<String> guessedWords = new ArrayList<String>();
			while(!n.equals("end")) {
				System.out.println("Please type in a word or type end to finish the game.");
				for(int r=0; r<cases.length; r++) {
					for(int c=0; c<cases[r].length; c++) {
						cases[r][c]=false;
					}
				}
				int newScore = gm.addWord(n.toUpperCase(), i);
				if(newScore!=0) {
					score+=newScore;
					guessedWords.add(n);
					//sets just selected word to lowercase
					for(int j=0; j<gm.getLastAddedWord().size(); j++) {
						cases[(int) gm.getLastAddedWord().get(j).getX()][(int) gm.getLastAddedWord().get(j).getY()]=true;
					}
				}
				playerWords.put(i, guessedWords);
				printBoard();
				System.out.println("SCORE: " + score+" "+guessedWords.toString());
				n=reader.next();
			}
			
			
		}
		
		//finding words which player did not
		ArrayList<String> list = (ArrayList<String>) gm.getAllWords();
		for(int i=0; i<playerWords.size(); i++) {
			ArrayList<String> words = playerWords.get(i);
			for(int j=0; j<words.size(); j++) {
				if(list.contains(words.get(j).toUpperCase())) {
					list.remove(words.get(j).toUpperCase());
				}
			}
		}
		
		int compScore=0;
		if(size*size<1000) {
			gm.setSearchTactic(SearchTactic.SEARCH_BOARD);
		}
		else {
			gm.setSearchTactic(SearchTactic.SEARCH_DICT);
		}
		
		System.out.println("Missed words: ");
		for(int i=0; i<list.size(); i++) {
			System.out.print(list.get(i)+" ");
			compScore+=list.get(i).length()-3;
		}
		System.out.println();
		for(int i=0; i<gm.getScores().length; i++) {
			System.out.println("PLAYER " + i +": " +gm.getScores()[i]);
		}
		System.out.println("Computer: " + compScore);
		System.out.println("Would you like to play again? Y/N");
		String n = reader.next();
		
		if(n.toUpperCase().equals("Y")) {
			return true;
		}
		
		return false;
		//returns if player wants to play again
	}
		
	//uppercase if not part of just selected word, lowercase otherwise
	//kept track of in cases
	private void printBoard() {
		for(int r=0; r<board.length; r++) {
			for(int c=0; c<board[r].length; c++) {
				if(this.cases[r][c]) {
					System.out.print(Character.toLowerCase(board[r][c])+" ");
				}
				else {
					System.out.print(Character.toUpperCase(board[r][c])+" ");
				}
				
			}
			System.out.println();
		}
	}
}
