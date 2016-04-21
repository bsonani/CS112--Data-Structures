package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
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
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		Scanner sc = new Scanner(new File(docFile));
		String word = "";
		int front = 0;
		char letter ;
		
		HashMap<String,Occurrence> returnHash = new HashMap<String,Occurrence>(1000,2.0f);
		while(sc.hasNext()){
			word=sc.next();
			word = getKeyWord(word);
			if (word==null){
				word = "";
			} else{
				if (returnHash.containsKey(word)){
					Occurrence oc = returnHash.get(word);
					oc.frequency++;
					returnHash.put(word, oc);
				} else{
					Occurrence oc = new Occurrence(docFile,1);
					returnHash.put(word, oc);
				}
			}
				
			
			
			/*while(front<line.length()){
				letter = line.charAt(front);
				
				if (letter!=32){
					word = word + letter;
				} 
				
				if (letter==32 && (word!="" || word!=null)){
					word = getKeyWord(word);
					if (word!=""||word!=null){
						if (returnHash.containsKey(word)){
							oc = returnHash.get(word);
							oc.frequency = oc.frequency+1;
						} else{
							oc.document = docFile;
							oc.frequency = 1;
							returnHash.put(word, oc);
						}
						word = "";
					}
				}*/
				
			/*	if ((letter>=65 && letter<=90)||(letter>=97 && letter<=122)){
					word = word + letter;
				} else if(letter==39&&word!=""){
					if(front<line.length()-1){
						if ((line.charAt(front+1)>=65 && line.charAt(front+1)<=90)||(line.charAt(front+1)>=97 && line.charAt(front+1)<=122)){
							word = word+letter;
						}
					}else{
						word = getKeyWord(word);
						if (word!=""){
							if (returnHash.containsKey(word)){
								oc = returnHash.get(word);
								oc.frequency = oc.frequency+1;
							} else{
								oc.document = docFile;
								oc.frequency = 1;
								returnHash.put(word, oc);
							}
							word = "";
						}
						
					}
				} else if(word!=""){
					word = getKeyWord(word);
					if (word!=""){
						if (returnHash.containsKey(word)){
							oc = returnHash.get(word);
							oc.frequency = oc.frequency+1;
						} else{
							oc.document = docFile;
							oc.frequency = 1;
							returnHash.put(word, oc);
						}
						word = "";
					}
				}
				*/
				//front++;
			}
		
			
		
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return returnHash;
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
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		// COMPLETE THIS METHOD
		Set<String>keySet = kws.keySet();
		int num = kws.size();
		String [] k =  keySet.toArray(new String[num]);
		for (int i=0;i<num;i++){
			ArrayList<Occurrence> ocs = new ArrayList<Occurrence>();
			if (keywordsIndex.containsKey(k[i])){
				ocs.addAll(keywordsIndex.get(k[i]));
				ocs.add(kws.get(k[i]));
				insertLastOccurrence(ocs);
			}else{
				ocs.add(kws.get(k[i]));
			}
			keywordsIndex.put(k[i],ocs);
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		if (word==null){
			return null;
		}
		int length = word.length();
		int pos = length-1;
		char letter = 0;
		
		word = word.toLowerCase();
		letter = word.charAt(pos);
		while ((letter==46 || letter==44 || letter==63 || letter==58 || letter==59 || letter==33)&&pos>=1){
				word = word.substring(0,pos);
				pos=pos-1;
				letter = word.charAt(pos);
		}
		pos=0;
		letter = word.charAt(0);
		while (pos<word.length()){
			letter = word.charAt(pos);	
			if ((letter>=65 && letter<=90)||(letter>=97 && letter<=122)){
				pos++;
			}else{
				return null;
			}
		}
		
		
		if (noiseWords.containsKey(word)){
			return null;
		} else {
			return word;
		}
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		int low = 0;
		int high = occs.size()-1;
		Occurrence oc = occs.get(high);
		ArrayList<Integer> returnList = new ArrayList<Integer>();
		int frequency = oc.frequency;
		int middle = (low+high)/2;
		if (high==0){
			middle = (low+high)/2;
			returnList.add(middle);
			return returnList;
		}
		occs.remove(high);
		high=occs.size()-1;
		while (low <= high) {
			middle = (low+high)/2;
			returnList.add(middle);
			if (occs.get(middle).frequency == frequency) {
				occs.add(middle+1,oc);
				return returnList;
			} else {
				if (occs.get(middle).frequency < frequency) {
					high = middle - 1;
				} else {
					low = middle + 1;
					middle = middle+1;
				}
			}
		}
		occs.add(middle,oc);
		return returnList;
	}
	
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		ArrayList<String> returnList = new ArrayList<String>();
		ArrayList<String> kw1String = new ArrayList<String>();
		ArrayList<Integer> kw1freq = new ArrayList<Integer>();
		ArrayList<String> kw2String = new ArrayList<String>();
		ArrayList<Integer> kw2freq = new ArrayList<Integer>();
		if(keywordsIndex.containsKey(kw1)){
			for (int i=0;i<5;i++){
				if (i>keywordsIndex.get(kw1).size()-1){
					break;
				}else{
					kw1String.add(keywordsIndex.get(kw1).get(i).document);
					kw1freq.add(keywordsIndex.get(kw1).get(i).frequency);
				}
			}
		}
		
		if(keywordsIndex.containsKey(kw2)){
			for (int i=0;i<5;i++){
				if (i>keywordsIndex.get(kw2).size()-1){
					break;
				}else{
					kw2String.add(keywordsIndex.get(kw2).get(i).document);
					kw2freq.add(keywordsIndex.get(kw2).get(i).frequency);
				}
			}	
		}
	
		if(kw1freq.isEmpty()==true && kw2freq.isEmpty()==true){
			return null;
		}
		
		int counter =0;
		int pos1 = 0;
		int pos2 = 0;
		while (pos1<=kw1freq.size()-1 || pos2<=kw2freq.size()-1){
			if (counter>=5){
				break;
			}
			if (kw1freq.size()-1<pos1 && kw2freq.size()-1<pos2){
				break;
			}else{
				if (kw1freq.size()-1<pos1 || kw2freq.size()-1<pos2){
					if (kw1freq.size()-1>=pos1 && kw2freq.size()-1<pos2){
						if (!returnList.contains(kw1String.get(pos1))){
							returnList.add(kw1String.get(pos1));
							counter++;
						}
						pos1++;
					} else if (kw1freq.size()-1<pos1 && kw2freq.size()-1>=pos2){
						if (!returnList.contains(kw2String.get(pos2))){
							returnList.add(kw2String.get(pos2));
							counter++;
						}
						pos2++;
					}
				} else{
					if (kw1freq.get(pos1)==kw2freq.get(pos2)){
						if (!(kw1String.get(pos1).equals(kw2String.get(pos2)))){
							if (!returnList.contains(kw1String.get(pos1))){
								returnList.add(kw1String.get(pos1));
								counter++;	
							}
							pos1++;
						} else{
							if (!returnList.contains(kw1String.get(pos1))){
								returnList.add(kw1String.get(pos1));
								counter++;
							}
							pos1++;	
							pos2++;
						}
						
					} else if(kw1freq.get(pos1)>kw2freq.get(pos2)){
						if (!returnList.contains(kw1String.get(pos1))){
							returnList.add(kw1String.get(pos1));
							counter++;
						}
						pos1++;
					} else if(kw2freq.get(pos2)>kw1freq.get(pos1)){
						if (!returnList.contains(kw2String.get(pos2))){
							returnList.add(kw2String.get(pos2));
							counter++;
						}
						pos2++;
					}
				}
			}	 
		}
		return returnList;
	}
}
