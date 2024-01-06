package domain;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import conf.Settings;

// Singleton design pattern
public class WordSet {
	private static WordSet instance;
	private Set<String> words;

	private WordSet() {
		// The words loaded from the text file are stored in a hashset
		words = new HashSet<>();
		// This file has been downloaded from the following source:
		// https://github.com/dwyl/english-words/blob/master/words.txt
		loadWords(Settings.WORDS_FILE + "\\words.txt");
	}

	public static WordSet getInstance() {
		if (instance == null) {
			instance = new WordSet();
		}
		return instance;
	}
	
	// We load words from the words.txt file and add them to the words set
	private void loadWords(String filePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				words.add(line.toLowerCase());
			}
			// debug
			System.out.println("Loaded " + words.size() + " words from file " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean contains(String word) {
		return words.contains(word.toLowerCase());
	}
}