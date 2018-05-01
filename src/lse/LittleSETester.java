package lse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class LittleSETester {
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		
		LittleSearchEngine lse = new LittleSearchEngine();
		print("__testing loadKeyWords___");
		
		String docs ="TestFiles.txt";
		String noiseWordsFile = "noisewords.txt";
		lse.makeIndex(docs, noiseWordsFile);
		testGetKeyWord(lse);
		testLoadKeyWords(lse);
		testInsert(lse);
		lse = testMergeKeyWords();//decided not to pass to have fresh object
		testTopFive(lse);
	}
	
	private static void testTopFive(LittleSearchEngine lse) throws FileNotFoundException {
		// TODO Auto-generated method stub
		print("");
		print("\t::testing top five");
		print("roberto "+ "divian: "+lse.top5search("roberto", "divian").toString());
		print("poo "+ "divian: "+lse.top5search("poo", "divian").toString());
		print("china "+ "poo: "+lse.top5search("china", "poo").toString());		
		LittleSearchEngine lse2 = new LittleSearchEngine();
		
		lse2.makeIndex("mergeManifest2.txt", "noiseWords.txt");
		print("gary "+ "phill: "+lse2.top5search("gary", "phill").toString());		
		
		
		
		
		print("\t::finished testing top five");
	}

	private static LittleSearchEngine testMergeKeyWords() throws FileNotFoundException {
		//TODO this seems to mostly work.  think of more cases 
		
		print("testing merge");
		LittleSearchEngine lse = new LittleSearchEngine();
		lse.makeIndex("mergeManifest.txt", "noiseWords.txt");
		
		assert(lse.keywordsIndex.get("table").size()== 2);
		assert(lse.keywordsIndex.get("table").get(0).document.equals("testMerge1.txt"));
		assert(lse.keywordsIndex.get("table").get(0).frequency== 3);
		assert(lse.keywordsIndex.get("table").get(1).document.equals("testMerge2.txt"));
		assert(lse.keywordsIndex.get("table").get(1).frequency== 1);
		
		
		assert(lse.keywordsIndex.get("billy").size()== 2);
		print(lse.keywordsIndex.get("billy"));
		assert(lse.keywordsIndex.get("billy").get(0).document.equals("testMerge2.txt"));
		assert(lse.keywordsIndex.get("billy").get(0).frequency== 2);
		assert(lse.keywordsIndex.get("billy").get(1).document.equals("testMerge1.txt"));
		assert(lse.keywordsIndex.get("billy").get(1).frequency== 1);
		
		
		assert(lse.keywordsIndex.get("china").size()== 3);
		print(lse.keywordsIndex.get("china"));
		assert(lse.keywordsIndex.get("china").get(0).document.equals("testMerge3.txt"));
		assert(lse.keywordsIndex.get("china").get(0).frequency== 3);
		assert(lse.keywordsIndex.get("china").get(1).document.equals("testMerge1.txt"));
		assert(lse.keywordsIndex.get("china").get(1).frequency== 2);
		assert(lse.keywordsIndex.get("china").get(2).document.equals("testMerge2.txt"));
		assert(lse.keywordsIndex.get("china").get(2).frequency== 1);

		return lse;
	}
	
	
	
	
	
	
	
	private static void testInsert(LittleSearchEngine lse) {
		assert(reversedOrder(buildOccs(new int[]{9,8,7,6,5})));
		ArrayList<Occurrence> occs1 =buildOccs(new int[]{9,8,7,6,5,20}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{20}); 
		assert(reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{20, 19}); 
		assert(reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{19,20}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{2,1,3}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		
		occs1 =buildOccs(new int[]{8,7,6,5,4,3,2,1,9}); 
		assert(!reversedOrder(occs1));
		ArrayList<Integer> b = lse.insertLastOccurrence(occs1);
		print(b);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{9,7,6,5,4,3,2,1,8}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{9,8,6,5,4,3,2,1,7}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{9,8,7,5,4,3,2,1,6}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{9,8,7,6,4,3,2,1,5}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{9,8,7,6,5,3,2,1,4}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{9,8,7,6,5,4,2,1,3}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{9,8,7,6,5,4,3,1,2}); 
		assert(!reversedOrder(occs1));
		lse.insertLastOccurrence(occs1);
		assert(reversedOrder(occs1));
		
		occs1 =buildOccs(new int[]{9,8,7,6,5,4,3,2,1}); 
		assert(reversedOrder(occs1));
		print(lse.insertLastOccurrence(occs1));
		assert(reversedOrder(occs1));
	}
	
	
	private static boolean reversedOrder(ArrayList<Occurrence> occs) {
		for(int i = 0 ; i < occs.size()-1; i++) {
			if(occs.get(i).frequency<occs.get(i+1).frequency) return false;
		}
		
		
		return true;
	}

	private static ArrayList<Occurrence> buildOccs(int[] vals){
		ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
		for(int i = 0; i< vals.length; i++) {
			Occurrence temp = new Occurrence("", vals[i]);
			occs.add(temp);
		}
		return occs;
	}
	
	
	private static void testLoadKeyWords(LittleSearchEngine lse) throws FileNotFoundException {
		print("Testing loadKeywords");
		
		String docFile = "";
		HashMap<String, Occurrence> wordSet =lse.loadKeywordsFromDocument("pohlx.txt");
		wordSet = lse.loadKeywordsFromDocument("Tyger.txt");
		wordSet = lse.loadKeywordsFromDocument("jude.txt");
		
		docFile = "testFile.txt";
		wordSet =lse.loadKeywordsFromDocument(docFile);
		assert(!wordSet.containsKey("the"));
		assert(!wordSet.containsKey("just"));
		assert(!wordSet.containsKey("ma'am"));
		assert(wordSet.get("bob").frequency ==5);
		assert(wordSet.get("bobs").frequency ==1);
		assert(wordSet.get("carrot").frequency ==4);
		assert(wordSet.get("randall").frequency ==4);
		assert(wordSet.size() == 10);
		
		//reloading wordSet
		
		docFile = "WowCh1.txt";
		wordSet =lse.loadKeywordsFromDocument(docFile);
		assert(wordSet.get("grey").frequency == 2);
		assert(wordSet.get("planet").frequency == 13);
		
		String max = "grey";
		
		for(String s: wordSet.keySet()) {
			if(wordSet.get(s).frequency > wordSet.get(max).frequency) {
				max = s;
			}
		}
		print("max is :"+max+": "+ wordSet.get(max).frequency +" in wowch1");
		print("FinishedTesting loadKeywords");
		
		
		
		print("FinishedTesting loadKeywords");
		
		
	}
	
	private static void testPrivateHelpers(LittleSearchEngine lse) {
		//testCleanWord();
		testGetKeyWord(lse);
		
	}
	
	private static void testCleanWord() {
		//Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
		/*
		print("testing cleanWords");
		LittleSearchEngine lse = new LittleSearchEngine();
		assert(lse.cleanWord("the").equals("the"));
		assert(lse.cleanWord("The").equals("the"));
		assert(lse.cleanWord("tHe").equals("the"));
		assert(lse.cleanWord("THE").equals("the"));
		assert(lse.cleanWord("bob-assurace").equals(""));
		assert(lse.cleanWord("THE.?!").equals("the"));
		assert(lse.cleanWord(".THE.?!").equals(""));
		assert(lse.cleanWord("THE.,?!:;").equals("the"));
		assert(lse.cleanWord("RutgersIsWeird").equals("rutgersisweird"));
		assert(lse.cleanWord("Ru.tgersIsWeird").equals(""));
		assert(lse.cleanWord("RutgersIsWeird_").equals(""));
		assert(lse.cleanWord("?RutgersIsWeird").equals(""));
		assert(lse.cleanWord("reticulating splines").equals(""));
		*/
	}
	
	private static void testGetKeyWord(LittleSearchEngine lse) {
		print("testing getKeyword");
		assert(lse.getKeyword("the")==null);
		assert(lse.getKeyword("bob9")==null);
		assert(lse.getKeyword("bob@")==null);
		assert(lse.getKeyword("b@ob")==null);
		assert(lse.getKeyword(".the")==null);
		assert(lse.getKeyword("the.?")==null);
		assert(lse.getKeyword("th?e-")==null);
		assert(lse.getKeyword("equi-distant")==null);
		assert(lse.getKeyword("we're")==null);
		assert(lse.getKeyword("th?e-")==null);
		assert(lse.getKeyword("What,ever")==null);
		assert(lse.getKeyword("bob").equals("bob"));
		assert(lse.getKeyword("Bob").equals("bob"));
		assert(lse.getKeyword("BOB").equals("bob"));
		assert(lse.getKeyword("bob-")==null);
		assert(lse.getKeyword("bo,b")==null);
		assert(lse.getKeyword(".bob")==null);
		assert(lse.getKeyword("betWEEN.?!")==null);
		assert(lse.getKeyword("just")==null);
		assert(lse.getKeyword("jusT,.,.,.!!!!!")==null);
		assert(lse.getKeyword("bOB,.,.,.!!!!!").equals("bob") );
		print("finished getKeyword test");
		
	}
	
	
	private static<T> void print(T t) {
		System.out.println(t);
	}

	
	
}
