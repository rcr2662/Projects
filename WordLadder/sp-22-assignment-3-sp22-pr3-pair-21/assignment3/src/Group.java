/* WORD LADDER Group.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Roberto Reyes
 * rcr2662
 * 17360
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Spring 2022
 */


package assignment3;
import java.util.*;

public class Group {
    private String ID;
   protected List<String> words;
   protected List<String> children;
    protected List<String> parents;
    private String D_key; // this value is what gens the tree
	private String start_word;
	private String end_word;
	private int depth;
	
//protected List<String> Black_List; //prevents BFS/DFS from checking the same shit twice
//protected Map<String,String> Link_word;

    Group(){}
    
    Group(String D_key,String start_word,String end_word,String ID){
    	this.D_key = D_key;
    	this.start_word = start_word;
    	this.end_word = end_word;
    	//this.depth = depth;
        this.ID = ID;
        words = new ArrayList<String>();
        children = new ArrayList<String>(); // contains indexes to immediate children
       parents = new ArrayList<String>(); //contains indexes to immediate parents
        //Black_List  = new ArrayList<String>(); //List that shouldnt be checked
	    //Link_word = new HashMap<String,String>(); //List of Link words
	   
        addChildren();
        addParents();
        
    }



    //this fills group with words;
    //fills after object is called to avoid time complexity/stack overflow
    public void addWords(Set<String> dictionary){
        if(ID.equals("start")){
            words.add(start_word); 
            return;
        }

        //If dictionary word has diff chars and same chars in correct indices, add to words
        for (String word : dictionary){
            if(diffInd(word, start_word)){
            	if(sameInd(word,start_word))
            		{words.add(word);}
            }
        }
    }

    

    /**
	 * @param word current word from the dictionary
     * @param start_word start word of ladder
	 * @return boolean determined by if the dictionary word
     * has different characters as the start word at indices
     * determined by the group index. 
	 */
    private boolean diffInd(String word, String start_word){
        char[] g_index = ID.toCharArray();
        char[] start = start_word.toCharArray();
        char[] d_word = word.toCharArray();
        int diff = 0;
        int index = 0;
        
        //compare dictionary word with start word
        for (int i = 0; i < g_index.length; i++){
            index = Character.getNumericValue(g_index[i]);
            if(start[index] != d_word[index]){
                diff++;
            }
        }
        //checks to see if dictionary has right amount of diff chars.
        return diff == g_index.length;
    }



    /**
	 * @param word current word from the dictionary
     * @param start_word start word of ladder
	 * @return boolean determined by if the dictionary word
     * has same characters as the start word at specific indices 
	 */
    private boolean sameInd(String word, String start_word){
       // char[] g_index = ID.toCharArray();
        char[] start = start_word.toCharArray();
        char[] d_word = word.toCharArray();
        //int[] same_ind = new int[start.length - g_index.length];
        //int index = 0;
        //int same = 0;
/*
        //make array of indices of start word chars that need to remain the same
        for(int i = 0; i < start.length; i++){
            for(int j = 0; j < g_index.length; j++){
                if (i == Character.getNumericValue(g_index[j])){
                    break;
                }
            }
            same_ind[index] = i;
            index++;
        }

        //compare start word and dictionary word
        for (int i = 0; i < same_ind.length; i++){
            if(start[same_ind[i]] == d_word[same_ind[i]]){
                same++;
            }
        }
        */
        //gets indeces where dword and start_word should be equal
        String same_ind_s = "";
       int ID_len = ID.length();
        for(int i = 0; i < start.length;i++) 
        {
        	if(ID.indexOf( (char) (i+'0')) == -1) 
        	{same_ind_s+= (char) (i+'0');}
        }
        //compares d_word and start_word at those indices
        int same_len = same_ind_s.length();
        for(int x = 0; x < same_len;x++)
        {
        	int same_index = same_ind_s.charAt(x)-'0';
        	if(d_word[same_index] != start[same_index]) {return false;}
        }
        //checks if dictionary word has the right amount of same chars
       // return same == same_ind.length;
        
        	return true;
    }



    //these methods fill the group
    //automatically does this everytime Group Object is made
    private void addChildren(){
	    if(ID.equals("start"))
	    {
		   int D_len = D_key.length();
		    for(int x = 0; x < D_len; x++)
		    	{
			    String item = String.valueOf(D_key.charAt(x));
			    children.add(item);
		    	}
		    return;
	    }
	   
	    int OG_len = start_word.length();
	   
        for(int x = 0; x <OG_len; x++){
        	 String temp_ID = "";
        	if(ID.indexOf((char) (x+'0')) == -1) {
        		temp_ID = ID+(char)(x+'0');
        		char s[] = temp_ID.toCharArray();
        		Arrays.sort(s);
        		temp_ID = "";
        		for(int y = 0; y < s.length;y++) {temp_ID+=s[y];}
        		children.add(temp_ID);
        			
        	}
        }
        

    }
    
    private void addParents(){
	    if(ID.equals("start")){return;}
	    else if(ID.length() == 1) {parents.add("start");return;}
        int len = ID.length();
        for(int x = 0; x <len; x++){
            String new_ID = ID.replace(String.valueOf(ID.charAt(x)),"");
            parents.add(new_ID);
         
        }
    }
    
    public String get_ID()
    {//System.out.println("PARENTS:" + Arrays.toString(parents.toArray()));
    //System.out.println("CHILDREN:" + Arrays.toString(children.toArray()));
   /* System.out.println(Arrays.toString(words.toArray()));*/return ID;}
	public String get_end()
	{return end_word;}
	public String get_start()
	{return start_word;}
	public int get_depth()
	{return depth;}
	public boolean is_Empty()
	{return words.isEmpty();}
	
}
