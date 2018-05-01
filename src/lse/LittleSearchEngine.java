package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		HashMap<String,Occurrence> words = new HashMap<String, Occurrence>();
		Scanner sc= new Scanner(new File(docFile));
		while(sc.hasNext()) {
			String line = sc.nextLine();
			String[] chunks = line.split("\\s+");
			for(String s: chunks) {//what is this python?!
				String w = getKeyword(s);
				if(w == null) continue;
				if(words.containsKey(w)) {
					words.get(w).frequency++;
				} else {
					Occurrence newOc = new Occurrence(docFile, 1);
					words.put(w, newOc);
				}
			}
		}
		sc.close();
		return words;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		//TODO do unit testing
		
		for(String key : kws.keySet()) {
			//HashMap<String,ArrayList<Occurrence>> 
			if(!keywordsIndex.containsKey(key)) {
				keywordsIndex.put(key, new ArrayList<Occurrence>());
			} 
			keywordsIndex.get(key).add(kws.get(key));
			insertLastOccurrence(keywordsIndex.get(key));//sorts list into correct order
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {	
		word = word.toLowerCase();
		word = cleanWord(word);
		if(word.equals("")) return null;
		if(noiseWords.contains(word)) return null;
		return word;
	}
	
	
	/**
	 * Function: cleanWord, returns word stripped of punctuation in lower case, 
	 * returns empty string if not valid case
	 * @param s String to be cleaned
	 * @return result of string cleaning, if not a valid input returns empty string
	 */
	private String cleanWord(String s) {
		//Ideally this could be a global constant but i don't think thats allowed
		final Set<Character> punctuation = new HashSet<>(Arrays.asList('.', ',', '?', ':', ';' , '!'));	 
		if( s == null ) return "";
		s = s.toLowerCase();
		if( s.length() <1 ) return "";
		if( !Character.isAlphabetic( s.charAt(0) ) ) return "";
		int lastCharI = 0;
		while(lastCharI+1<s.length() && Character.isAlphabetic( s.charAt(lastCharI+1) )) {
			lastCharI++;
		}
		if(lastCharI == s.length()-1) return s;
		for(int i =lastCharI+1 ; i< s.length(); i++ ) {
			if(!punctuation.contains(s.charAt(i))) return "";
		}
		
		return s.substring(0, lastCharI+1 );
	}
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) { 
		if(occs.size()<2) return null;
		ArrayList<Integer> midPoints = new ArrayList<Integer>();
		int lh= 0;
		int rh = occs.size()-2;
		int mid = 0;
		while(lh<=rh) {
			mid = (lh+rh)/2;
			midPoints.add(mid);
			if (occs.get(occs.size()-1).frequency==occs.get(mid).frequency) {
				occs.add(mid,occs.get(occs.size()-1));
				occs.remove(occs.size()-1);
				return midPoints;
			}if(occs.get(occs.size()-1).frequency>occs.get(mid).frequency) {
				rh = mid-1;
			} else {
				lh = mid+1;
			}
		}
		if(occs.get(occs.size()-1).frequency>occs.get(mid).frequency){
			occs.add(mid,occs.get(occs.size()-1));
		}else {
			occs.add(mid+1,occs.get(occs.size()-1));
		}
		occs.remove(occs.size()-1);
		return midPoints;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		//TODO unit test this 
		ArrayList<Occurrence> l1 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> l2 = new ArrayList<Occurrence>();
		ArrayList<String> strings =  new ArrayList<String>();
		
		if(keywordsIndex.containsKey(kw1)) l1 = keywordsIndex.get(kw1);
		if(keywordsIndex.containsKey(kw2)) l2 = keywordsIndex.get(kw2);
		
		int l1_i=0;
		int l2_i = 0;
		
		while(strings.size()<5) {
			if(l1_i >= l1.size() && l2_i >= l2.size()) break;//exhausted both lists 
			Occurrence temp = null;// i hope to god this get set in the following block 
			if(l1_i >= l1.size() ) {//l1 exhausted
				temp= l2.get(l2_i++);
			}else if(l2_i >= l2.size()) {//l2 exhausted
				temp = l1.get(l1_i++);//l1 exhausted (check out that sick post increment action)	
			}else if(l1.get(l1_i).frequency>= l2.get(l2_i).frequency) {//l2 high value
				temp =  l1.get(l1_i++);
			} else {
				temp = l2.get(l2_i++);
			}
			
			if(strings.contains(temp.document)) {
				
			}else {
				strings.add(temp.document);
			}
			
		}
		
		
		return strings;
	
	}
}
