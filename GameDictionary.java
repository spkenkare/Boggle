package assignment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;

public class GameDictionary implements BoggleDictionary {
	
	private class DictIterator implements Iterator<String> {
		private int index=0;
		public boolean hasNext() {
			return index < dict.size();
		}
		public String next() {
			index++;
			return dict.get(index-1);
		}
		
	}

	private static class Trie {
		class TrieNode {
		    TreeMap<Character, TrieNode> children = new TreeMap<Character, TrieNode>();
		    boolean isLeaf; //true if end of a word
		 
		    private TrieNode() {
		    	
		    }
		}
	    private TrieNode root;
	    private Trie() {
	        root = new TrieNode();
	    }
	 	    
	    //adds word, each character at the level below the one before it
	    private void addWord(String word) {
	        TreeMap<Character, TrieNode> children = root.children;
	        for(int i=0; i<word.length(); i++){
	            char c = word.charAt(i);
	            TrieNode t;
	            if(children.containsKey(c)){ //char already exists in trie
	                t = children.get(c);
	            }
	            else{ //add it
	                t = new TrieNode();
	                children.put(c, t);
	            }
	            //move onto next level for next char
	            children = t.children;
	            if(i==word.length()-1) { //at last char in word, means it is a word
	            	t.isLeaf = true;
	            }
	                    
	        }
	    }
	 
	    //if the word can be found in the trie and is a leaf, it exists in the dictionary
	    private boolean contains(String word) {
	        TrieNode t = findNode(word);
	        if(t != null && t.isLeaf) 
	            return true;
	        else
	            return false;
	    }
	    //since this is not necessarily the end of a word
	    //it does not need to be a leaf
	    private boolean isPrefix(String prefix) {
	    	if(prefix.equals("")) {
	    		return true;
	    	}
	        return findNode(prefix)!=null;
	    }
	 
	    //go down char by char checking if it exists
	    //if not it returns null to symbolize that the node cannot be found
	    private TrieNode findNode(String word){
	        TreeMap<Character, TrieNode> children = root.children; //start at top
	        TrieNode node = null;
	        for(int i=0; i<word.length(); i++){
	            char c = word.charAt(i);
	            if(children.containsKey(c)){
	                node = children.get(c);
	                children = node.children; //move down a level
	            }
	            else{
	                return null; //as soon as the char can't be found at that level, the word does not exist
	            }
	        }
	 
	        return node;
	    }

	}
	private ArrayList<String> dict;
	private Trie trie;
	
	public Iterator<String> iterator() {
		DictIterator di = new DictIterator();
		return di;		
	}
	
	public GameDictionary() {
		trie = new Trie();
		dict = new ArrayList<String>();
	}
 
	public void loadDictionary(String filename) {		
		try 
    	{			
			
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			String line=new String(); 
			
			while((line = br.readLine()) != null) {
				if(line.length()>0) {
					dict.add(line.toLowerCase());
					trie.addWord(line.toLowerCase());
				}
			} 
			fr.close();
		} 
    	catch (FileNotFoundException e) {
    		System.out.println("File could not be found.");
			return;
		} 
		catch (IOException e) {
			System.out.println("File could not be read.");
			return;
		}
		catch (NullPointerException e) {
			System.out.println("File cannot be null.");
			return;
		}
	}

	public boolean isPrefix(String prefix) {
		if(prefix==null) {
			return false;
		}
		return trie.isPrefix(prefix.toLowerCase());
	}

	public boolean contains(String word) {
		if(word==null) {
			return false;
		}
		if(dict.isEmpty()) {
			System.out.println("Dictionary is empty.");
			return false;
		}
		return trie.contains(word.toLowerCase());
	}

}
