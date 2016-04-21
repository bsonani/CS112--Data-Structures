package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file. The root of the 
	 * tree is stored in the root field.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		String line = sc.nextLine();
		TagNode changing = null;
		Stack<TagNode> tags = new Stack<TagNode>();
		Stack<String> types = new Stack<String>();
		Stack<TagNode> nodes = new Stack<TagNode>();
		String type;
		String end = "";
		int index = 0;
		if (line.substring(0,1).equals("<")){
			while(!end.equals(">")){
				index++;
				end = line.substring(index,index+1);
			}
			TagNode first = new TagNode(line.substring(1,index),null,null);
			root=first;
			tags.push(root);
			types.push("Otag");
			nodes.push(root);
		}		
	while(tags.isEmpty()==false){
		line = sc.nextLine();
		if (line.substring(0,1).equals("<") && !line.substring(1,2).equals("/")){  //check for tag open
			type = "Otag";
		}else if (line.substring(0,1).equals("<")){
			type = "Ctag";
		}else{
			type = "text";
		}
		
		if (type.equals("Otag")){
			end="";
			index=0;
			while(!end.equals(">")){
				index++;
				end = line.substring(index,index+1);
			}
			TagNode create = new TagNode(line.substring(1,index),null,null);
			changing=nodes.peek();
			if (types.peek().equals("Otag")){
				changing.firstChild=create;;
			} else if (types.peek().equals("Ctag")){
				changing.sibling=create;	
			} else {
				changing.sibling=create;
			}
			nodes.push(create);
			tags.push(create);
			types.push("Otag");
		}else if (type.equals("Ctag")){
			while(!nodes.peek().equals(tags.peek())){
				nodes.pop();
			}
			tags.pop();
			types.push("Ctag");
		}else{
			changing=nodes.peek();
			TagNode create = new TagNode(line,null,null);
			if (types.peek().equals("Otag")){
				changing.firstChild=create;;
			} else { //close tag before
				changing.sibling=create;	
			} 
			types.push("text");
			nodes.push(create);
		}
	}
}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		recursive(root, oldTag,newTag);
	}
	private void recursive(TagNode T, String oldTag, String newTag){
		if(T==null){
			return;
		}
		else if (T.tag.equals(oldTag)){
			T.tag=newTag;
		} else{
			recursive(T.sibling,oldTag,newTag);
			recursive(T.firstChild,oldTag,newTag);
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		search(root,row);
	}
	
	private void search(TagNode pos,int row){
		if(pos==null){
			return;
		}
		else if (pos.tag.equals("table")){
			TagNode rows=pos.firstChild;
			for(int i=1;i<row;i++){
				rows=rows.sibling;
			}
			TagNode ptr=rows.firstChild;
			while(ptr!=null){
				TagNode b = new TagNode("b",ptr.firstChild,null);
				ptr.firstChild=b;
				ptr=ptr.sibling;
			}
		} else{
			search(pos.sibling, row);
			search(pos.firstChild, row);
		}
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		Removerecursive(root,tag);
	}
	
	private void Removerecursive(TagNode T, String tag){
		TagNode last;
		TagNode connect;
		TagNode delete;
		if(T==null){
			return;
		}else if (T.sibling!=null&&T.sibling.tag.equals(tag)){
			delete=T.sibling;
			connect=delete.sibling;
			T.sibling=delete.firstChild;
			last=delete.firstChild;
			if (tag.equals("ol")||tag.equals("ul")){
				while(last.sibling!=null){
					last.tag="p";
					last=last.sibling;
				}
				last.tag="p";
			}else{
				while(last.sibling!=null){
					last=last.sibling;
				}
			}
			last.sibling=connect;
		}else if (T.firstChild!=null&&T.firstChild.tag.equals(tag)){
			delete=T.firstChild;
			connect=delete.sibling;
			last=delete.firstChild;
			if (tag.equals("ol")||tag.equals("ul")){
				while(last.sibling!=null){
					last.tag="p";
					last=last.sibling;
				}
				last.tag="p";
			}else{
				while(last.sibling!=null){
					last=last.sibling;
				}
			}
			last.sibling=connect;
			T.firstChild=delete.firstChild;
		}
		Removerecursive(T.sibling,tag);
		Removerecursive(T.firstChild,tag);
		
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		Addrecursive(root,word,tag);
	}
	
	private void Addrecursive(TagNode T, String word, String tag){
		if(T==null){
			return;
		
		}else if (T.sibling!=null&&T.sibling.tag.toLowerCase().contains(word.toLowerCase())){ //check if contains same word("string") in any form
			if (T.sibling.tag.equalsIgnoreCase(word)){	//exact word, nothing else
				TagNode target = T.sibling;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(0,word.length()),null, null);
				TagNode insertTag = new TagNode(tag,text,connect);
				T.sibling=insertTag;
			} else if (T.sibling.tag.substring(0,word.length()-1).equalsIgnoreCase(word)&&(T.sibling.tag.charAt(word.length())==33||T.sibling.tag.charAt(word.length())==63||T.sibling.tag.charAt(word.length())==46||T.sibling.tag.charAt(word.length())==59||T.sibling.tag.charAt(word.length())==58)){
				TagNode target = T.sibling;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(target.tag.toLowerCase().indexOf(word.toLowerCase()),target.tag.toLowerCase().indexOf(word.toLowerCase())+word.length()+1),null, null);
				TagNode insertTag = new TagNode(tag,text,connect);
				T.sibling=insertTag;
			} else if (T.sibling.tag.contains(word)&&((T.sibling.tag.indexOf(word)!=0&&T.sibling.tag.charAt((T.sibling.tag.indexOf(word)-1))==32)&&(T.sibling.tag.charAt((T.sibling.tag.indexOf(word))+word.length()-1))==32)){
				TagNode target = T.sibling;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(target.tag.toLowerCase().indexOf(word.toLowerCase()),target.tag.toLowerCase().indexOf(word.toLowerCase())+word.length()),null, null);
				TagNode after = new TagNode(target.tag.substring(target.tag.indexOf(word)+word.length(),target.tag.length()),null,connect);
				TagNode insertTag = new TagNode(tag,text,after);
				TagNode before = new TagNode(target.tag.substring(0,target.tag.indexOf(word)),null,insertTag);
				T.sibling=before;
			} else if (T.sibling.tag.indexOf(word)==0&&T.sibling.tag.charAt(T.sibling.tag.indexOf(word)+word.length())==32){
				TagNode target = T.sibling;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(target.tag.toLowerCase().indexOf(word.toLowerCase()),target.tag.toLowerCase().indexOf(word.toLowerCase())+word.length()),null, null);
				TagNode after = new TagNode(target.tag.substring(target.tag.indexOf(word)+word.length(),word.length()),null,connect);
				TagNode insertTag = new TagNode(tag,text,after);
				T.sibling=insertTag;
			} else if (T.sibling.tag.indexOf(word)==T.sibling.tag.length()-word.length()&&T.sibling.tag.charAt(T.sibling.tag.indexOf(word)-1)==32){
				TagNode target = T.sibling;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(target.tag.toLowerCase().indexOf(word.toLowerCase()),target.tag.toLowerCase().indexOf(word.toLowerCase())+word.length()),null, null);
				TagNode insertTag = new TagNode(tag,text,connect);
				TagNode before = new TagNode(target.tag.substring(0,target.tag.indexOf(word)),null,insertTag);
				T.sibling=before;
			}
		}else if (T.firstChild!=null&&T.firstChild.tag.toLowerCase().contains(word.toLowerCase())){ 
			if (T.firstChild.tag.equalsIgnoreCase(word)){	//exact word, nothing else
				TagNode target = T.firstChild;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(0,word.length()),null, null);
				TagNode insertTag = new TagNode(tag,text,connect);
				T.firstChild=insertTag;
			} else if (T.firstChild.tag.substring(0,word.length()-1).equalsIgnoreCase(word)&&(T.firstChild.tag.charAt(word.length())==33||T.firstChild.tag.charAt(word.length())==63||T.firstChild.tag.charAt(word.length())==46||T.firstChild.tag.charAt(word.length())==59||T.firstChild.tag.charAt(word.length())==58)){
				TagNode target = T.firstChild;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(target.tag.toLowerCase().indexOf(word.toLowerCase()),target.tag.toLowerCase().indexOf(word.toLowerCase())+word.length()+1),null, null);
				TagNode insertTag = new TagNode(tag,text,connect);
				T.firstChild=insertTag;
			} else if (((T.firstChild.tag.indexOf(word)!=0&&T.firstChild.tag.charAt((T.firstChild.tag.indexOf(word)-1))==32)&&(T.firstChild.tag.charAt((T.firstChild.tag.indexOf(word))+word.length()-1))==32)){
				TagNode target = T.firstChild;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(target.tag.toLowerCase().indexOf(word.toLowerCase()),target.tag.toLowerCase().indexOf(word.toLowerCase())+word.length()),null, null);
				TagNode after = new TagNode(target.tag.substring(target.tag.indexOf(word)+word.length(),target.tag.length()),null,connect);
				TagNode insertTag = new TagNode(tag,text,after);
				TagNode before = new TagNode(target.tag.substring(0,target.tag.indexOf(word)),null,insertTag);
				T.firstChild=before;
			} else if (T.firstChild.tag.indexOf(word)==0&&T.firstChild.tag.charAt(T.firstChild.tag.indexOf(word)+word.length())==32){
				TagNode target = T.firstChild;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(target.tag.toLowerCase().indexOf(word.toLowerCase()),target.tag.toLowerCase().indexOf(word.toLowerCase())+word.length()),null, null);
				TagNode after = new TagNode(target.tag.substring(target.tag.indexOf(word)+word.length(),word.length()),null,connect);
				TagNode insertTag = new TagNode(tag,text,after);
				T.firstChild=insertTag;
			} else if (T.firstChild.tag.indexOf(word)==T.firstChild.tag.length()-word.length()&&T.firstChild.tag.charAt(T.firstChild.tag.indexOf(word)-1)==32){
				TagNode target = T.firstChild;
				TagNode connect = target.sibling;
				TagNode text = new TagNode(target.tag.substring(target.tag.toLowerCase().indexOf(word.toLowerCase()),target.tag.toLowerCase().indexOf(word.toLowerCase())+word.length()),null, null);
				TagNode insertTag = new TagNode(tag,text,connect);
				TagNode before = new TagNode(target.tag.substring(0,target.tag.indexOf(word)),null,insertTag);
				T.firstChild=before;
			}
			
			
			/**if (T.tag.substring(0,word.length()-1).equalsIgnoreCase(word)){	
				
			}**/
		}else{
			Addrecursive(T.sibling,word,tag);
			Addrecursive(T.firstChild,word,tag);
		
		}
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
}
