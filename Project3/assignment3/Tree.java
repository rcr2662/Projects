
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

public class Tree{

	private String D_key; // this value is what gens the tree
	private String start_word;
	private String end_word;
private Map<String,Group> index_tree;
private ArrayList<String> All_Words;
//private Set<String> makeDictionary = Main.makeDick; 
Tree(){
	
}
//ONLY NEED TO GEN ONE LEVEL
    Tree(String D_key,String start_word,String end_word,int todos){
    	this.D_key= D_key;
    	this.start_word = start_word;
    	this.end_word =end_word;
    	 index_tree = new HashMap<String,Group>(); // the key is the string index
    	 All_Words = new ArrayList<String>();
    	//both trees should have the same values in the same order
         
         index_tree.put("start",new Group(D_key,start_word,end_word,"start"));
         int length = D_key.length();
             //System.out.println("START WORD:" + start_word);
             for(int depth =0; depth <= todos;depth++ ) 
                  {
                  for(int x = 0; x <length; x++ )
          { 
                      if(depth>=1)
                     { Gen_tree(D_key,D_key.substring(x,x+1),depth,x);}
                      else {index_tree.get("start").words.add(start_word);}
                      
                  }
          }
             
             Sort_All_Words(1); 
    }
  
    
    //depth > 1
    // generates all combos of length depth
private void Gen_tree(String Orig,String temp,int depth,int init)
    {

   if(temp.length() == depth)
   {
    index_tree.put(temp, new Group(D_key,start_word,end_word,temp));
     
      index_tree.get(temp).addWords(Main.makeDick); //reduces time complexity to O(N)
      if(depth == 1)
      {All_Words.addAll(  index_tree.get(temp).words);}
      return;  }
   
   int o = Orig.length();
   
   if(init == o -1)
   {return;}
   
   String consto= temp;
   for(int x = init+1; x < o; x++)
    {
 
    temp += Orig.charAt(x);
    int y =  temp.length()-1; 
    int new_init = Orig.indexOf(temp.charAt(y));

        
        
    if(temp.length() <= depth)
        { Gen_tree(Orig, temp,depth,new_init);}
        temp =  consto;   
   
   }
   
    }
    
    public Group Get_Group(String key) //creates group if its null
    {
    if(index_tree.get(key) == null)
    { index_tree.put(key,new Group(D_key,start_word,end_word,key));}
    return index_tree.get(key);
    
    }
   //one = parent
   //two = child of parent
 
   public String D_key()
   {return D_key;}
   public String start_word()
   {return start_word;}
   public String end_word()
   {return end_word;}
 
   private void Sort_All_Words(int depth)
   {
	   if(depth != 1) {return;}
	   int bro_len = All_Words.size();
	   int word_len = end_word.length();
	   for(int q = 0; q < bro_len; q++)
		{
			//ORDER WORDS FROM LOW TO HI
			for(int t = q; t < bro_len;t++)
			{	
				int word_diffq = Main.Difference(All_Words.get(q),end_word,word_len).length();
				int word_difft = Main.Difference(All_Words.get(t),end_word,word_len).length();
				if (word_difft < word_diffq) {
			        String temp = All_Words.get(t);
			        All_Words.set(t,All_Words.get(q));
			        All_Words.set(q,temp);
			      }
			}
   }
	   
   }
   
   public ArrayList<String> Get_All_Words(int depth)
   {
	   if(depth!=1) {return null;}
	   return All_Words;}
   
}
