

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

Tree(){
	
}
//ONLY NEED TO GEN ONE LEVEL
    Tree(String D_key,String start_word,String end_word,int todos){
    	this.D_key= D_key;
    	this.start_word = start_word;
    	this.end_word =end_word;
    	 index_tree = new HashMap<String,Group>(); // the key is the string index
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
          
    }
  
    
    //depth > 1
    // generates all combos of length depth
private void Gen_tree(String Orig,String temp,int depth,int init)
    {

   if(temp.length() == depth)
   {
      /*System.out.println(temp);*/ index_tree.put(temp, new Group(D_key,start_word,end_word,temp));
     
      index_tree.get(temp).addWords(Main.makeDictionary());
     /*
      System.out.println(index_tree.get(temp).get_ID());
      System.out.println("PARENTS:" + Arrays.toString(index_tree.get(temp).parents.toArray()));
      
      System.out.println("CHILDREN:" + Arrays.toString(index_tree.get(temp).children.toArray()));
     
      System.out.println(Arrays.toString(index_tree.get(temp).words.toArray()));
      */
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
}
