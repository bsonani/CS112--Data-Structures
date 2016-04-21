package solitaire;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;
import java.util.NoSuchElementException;

/**
 * This class implements a simplified version of Bruce Schneier's Solitaire Encryption algorithm.
 * 
 * @author RU NB CS112
 */
public class Solitaire {
	
	/**
	 * Circular linked list that is the deck of cards for encryption
	 */
	CardNode deckRear;
	
	/**
	 * Makes a shuffled deck of cards for encryption. The deck is stored in a circular
	 * linked list, whose last node is pointed to by the field deckRear
	 */
	public void makeDeck() {
		// start with an array of 1..28 for easy shuffling
		int[] cardValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < cardValues.length; i++) {
			cardValues[i] = i+1;
		}
		
		// shuffle the cards
		Random randgen = new Random();
 	        for (int i = 0; i < cardValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = cardValues[i];
	            cardValues[i] = cardValues[other];
	            cardValues[other] = temp;
	        }
	     
	    // create a circular linked list from this deck and make deckRear point to its last node
	    CardNode cn = new CardNode();
	    cn.cardValue = cardValues[0];
	    cn.next = cn;
	    deckRear = cn;
	    for (int i=1; i < cardValues.length; i++) {
	    	cn = new CardNode();
	    	cn.cardValue = cardValues[i];
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
	    }
	}
	
	/**
	 * Makes a circular linked list deck out of values read from scanner.
	 */
	public void makeDeck(Scanner scanner) 
	throws IOException {
		CardNode cn = null;
		if (scanner.hasNextInt()) {
			cn = new CardNode();
		    cn.cardValue = scanner.nextInt();
		    cn.next = cn;
		    deckRear = cn;
		}
		while (scanner.hasNextInt()) {
			cn = new CardNode();
	    	cn.cardValue = scanner.nextInt();
	    	cn.next = deckRear.next;
	    	deckRear.next = cn;
	    	deckRear = cn;
		}
	}
	
	/**
	 * Implements Step 1 - Joker A - on the deck.
	 */
	void jokerA() {
		// COMPLETE THIS METHOD
		CardNode ptr = deckRear.next;
		CardNode swap= deckRear.next.next;
		while(ptr.cardValue!=27){
			ptr=ptr.next;
			swap=swap.next;
		}
		ptr.cardValue=swap.cardValue;
		swap.cardValue=27;
		
		
	}
	
	/**
	 * Implements Step 2 - Joker B - on the deck.
	 */
	void jokerB() {
	    // COMPLETE THIS METHOD
		CardNode ptr = deckRear.next;
		CardNode after1=ptr.next;
		CardNode after2= ptr.next.next;
		while(ptr.cardValue!=28){
			ptr=ptr.next;
			after1=after1.next;
			after2=after2.next;
		}
		ptr.cardValue=after1.cardValue;
		after1.cardValue=after2.cardValue;
		after2.cardValue=28;
		
	}
	
	/**
	 * Implements Step 3 - Triple Cut - on the deck.
	 */
	void tripleCut() {
		// COMPLETE THIS METHOD
		CardNode front = deckRear.next;
		CardNode joker1=front,joker2 = front;
		CardNode front1=front,back1 = null;
		CardNode front2=null,back2 = null;
		while(joker1.next!=front){
			if (joker1.cardValue==27){
				joker2=joker2.next;
				while(joker2.cardValue!=28){
					joker2=joker2.next;
				}
				break;
			} else if (joker1.cardValue==28){
				joker2=joker2.next;
				while(joker2.cardValue!=27){
					joker2=joker2.next;
				}
				break;
			} else{
				back1=joker1;
				joker1=joker1.next;
				joker2=joker2.next;
			}
		}
		
		if(joker2!=deckRear){
			back2=deckRear;
			front2=joker2.next;
		}else{
			back2=null;
		}
		
		if(back1==null){
			if(back2==null){
				return;
			}else{
				joker2.next=front2;
				back2.next=joker1;
				deckRear=joker2;
			}
		}else{
			if (back2==null){
				joker2.next=front1;
				back1.next=joker1;
				deckRear=back1;
			}else{
				back2.next=joker1;
				joker2.next=front1;
				back1.next=front2;
				deckRear=back1;
			}
		}
	}
		
	/**
	 * Implements Step 4 - Count Cut - on the deck.
	 */
	void countCut() {
		// COMPLETE THIS METHOD
		CardNode front=deckRear.next;
		CardNode after=front;
		CardNode last=front;
		while(after.next!=deckRear){
			after=after.next;
		}
		int num=deckRear.cardValue;
		if(num==28||num==27){
			num=27;
			return;
		}
		for (int i=1;i<num;i++){
			last=last.next;
		}
		deckRear.next=last.next;
		after.next=front;
		last.next=deckRear;
	}
	
	/**
	 * Gets a key. Calls the four steps - Joker A, Joker B, Triple Cut, Count Cut, then
	 * counts down based on the value of the first card and extracts the next card value 
	 * as key. But if that value is 27 or 28, repeats the whole process (Joker A through Count Cut)
	 * on the latest (current) deck, until a value less than or equal to 26 is found, which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	int getKey() {
		// COMPLETE THIS METHOD
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		int key;
	    do{
	    	jokerA();
	    	jokerB();
	    	tripleCut();
	    	countCut();
	    	int count=deckRear.next.cardValue;
	    	if (count==28){
	    		count=27;
	    	}
	    	CardNode number=deckRear.next;
	    	for(int i=0;i<count;i++){
	    		number=number.next;
	    	}
	    	key=number.cardValue;
	    }while(key==27 || key==28);
	    
		return key;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(CardNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.cardValue);
		CardNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.cardValue);
		} while (ptr != rear);
		System.out.println("\n");
	}

	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
		// COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		int[] keystream = new int[message.length()];
		int[] alphabetPos = new int[message.length()];
		int[] codeStream = new int[message.length()];
		String encrypted="";
		String msg="";
		int pos = 0;
		int count = 0;
		int code = 0;
		char encryptedLetter;
		for(int i=0;i<message.length();i++){
			char check=message.charAt(i);
			if((int)check>=65 && (int)check<=90){
				msg=msg+(char)check;
				pos=check-'A'+1;
				alphabetPos[count]=pos;
				keystream[count]=getKey();
				code = alphabetPos[count]+keystream[count];
				if (code>26){
					code=code-26;
				}
				codeStream[count]=code;
				encryptedLetter=(char)(code-1+'A');
				encrypted=encrypted+(char)encryptedLetter;
				count++;
			}
		}
	    return encrypted;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
		// COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		int[] keystream = new int[message.length()];
		int[] alphabetPos = new int[message.length()];
		int[] codeStream = new int[message.length()];
		String decrypted="";
		String msg="";
		int pos = 0;
		int count = 0;
		int code = 0;
		char decryptedLetter;
		for(int i=0;i<message.length();i++){
			char check=message.charAt(i);
			if((int)check>=65 && (int)check<=90){
				msg=msg+(char)check;
				pos=check-'A'+1;
				alphabetPos[count]=pos;
				keystream[count]=getKey();
				if(pos<=keystream[count]){
					code=(pos+26)-keystream[count];
				}else{
					code=pos-keystream[count];
				}
				codeStream[count]=code;
				decryptedLetter=(char)(code-1+'A');
				decrypted=decrypted+(char)decryptedLetter;
				count++;
			}
		}
	    return decrypted;
	}
}
