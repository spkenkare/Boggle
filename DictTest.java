package assignment;

import static org.junit.Assert.*;

import org.junit.Test;

public class DictTest {
	@Test 
	public void testPrefix() {
		GameDictionary test1 = new GameDictionary();
		test1.loadDictionary("words.txt"); //lowercase words test
		assertTrue(test1.isPrefix("home")); //normal prefix
		assertTrue(test1.isPrefix("homework")); //full word
		assertTrue(test1.isPrefix("h")); //single letter
		assertTrue(test1.isPrefix("H")); //checking cases
		assertTrue(test1.isPrefix("hOmE")); //checking cases
		assertTrue(test1.isPrefix("")); //empty string
		
		GameDictionary test2 = new GameDictionary();
		test2.loadDictionary("numbers.txt"); //numbers test
		assertTrue(test2.isPrefix("00")); //general prefix
		assertTrue(test2.isPrefix("512")); //general prefix
		assertTrue(test2.isPrefix("000")); //full word
		assertTrue(test2.isPrefix("")); //empty string
		
		GameDictionary test3 = new GameDictionary();
		test3.loadDictionary("symbols.txt"); //symbols test
		assertTrue(test3.isPrefix("&>#$*!@"));  
		assertTrue(test3.isPrefix("&>#$*!"));
		assertTrue(test3.isPrefix("-"));
		assertTrue(test3.isPrefix("*-(")); 
		assertFalse(test3.isPrefix(".")); //not a prefix
		
		GameDictionary test4 = new GameDictionary();
		test4.loadDictionary("uppercase.txt"); //case checking
		assertTrue(test4.isPrefix("home"));
		assertTrue(test4.isPrefix("homework"));
		assertTrue(test4.isPrefix("HOME"));
		assertTrue(test4.isPrefix("h"));
		assertTrue(test4.isPrefix("H"));
		assertTrue(test4.isPrefix("hOmE"));
		assertTrue(test4.isPrefix(""));
		
		GameDictionary test5 = new GameDictionary();
		test5.loadDictionary("blank.txt"); //no errors
		
		
	}
	
	@Test 
	public void testContains() {
		GameDictionary test1 = new GameDictionary();
		test1.loadDictionary("words.txt");
		assertTrue(test1.contains("home")); //when there is a longer version
		assertTrue(test1.contains("homework"));
		assertTrue(test1.contains("HOMEWORK")); //cases
		assertTrue(test1.contains("homEwOrk")); //cases
		assertFalse(test1.contains("h")); //just a prefix
		assertFalse(test1.contains(""));
		
		GameDictionary test2 = new GameDictionary();
		test2.loadDictionary("numbers.txt");
		assertTrue(test2.contains("000"));
		assertTrue(test2.contains("5125746935"));
		assertFalse(test2.contains("0"));
		assertFalse(test2.contains(""));
		
		GameDictionary test3 = new GameDictionary();
		test3.loadDictionary("symbols.txt");
		assertTrue(test3.contains("&>#$*!@"));
		assertFalse(test3.contains("&>#$*!"));
		assertTrue(test3.contains("-':@^"));
		assertTrue(test3.contains("*-(.\"")); //backslash test
		assertFalse(test3.contains("a"));
		
		GameDictionary test4 = new GameDictionary();
		test4.loadDictionary("uppercase.txt"); //should work for all cases of word
		assertTrue(test4.contains("homework"));
		assertTrue(test4.contains("HOMEWORK"));
		assertTrue(test4.contains("homEwOrk"));
		assertFalse(test4.contains("h"));
		assertFalse(test4.contains(""));
		
		GameDictionary test5 = new GameDictionary();
		test5.loadDictionary("blank.txt"); //no errors
		
		GameDictionary test6 = new GameDictionary();
		test6.loadDictionary("uppercase.txt");
		test6.loadDictionary("uppercase2.txt");
		assertTrue(test6.contains("APPLE"));
		assertTrue(test6.contains("HOMEWORK"));
		
		GameDictionary test7 = new GameDictionary();
		test7.loadDictionary(null);
	}
}
