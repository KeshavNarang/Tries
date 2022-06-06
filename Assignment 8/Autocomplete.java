import java.io.*;
import java.lang.*;
import java.util.*;
public class Autocomplete 
{
	static Trie <String> dictionary = new Trie <String> ();
	
	public static void main (String [] args) throws IOException
	{
		Scanner fileInput = new Scanner (new File ("words.txt"));
		while (fileInput.hasNextLine())
		{
			dictionary.put(fileInput.next(), "valid");
		}
		
		Scanner userInput = new Scanner (System.in);
		
		System.out.println("Start typing your word. Press enter after each letter. When five or less words remain, the program will suggest all possible option: ");
		
		String prefix = "";
		int wordsLeft = dictionary.autocomplete(prefix).size();
		
		while (wordsLeft > 1)
		{
			prefix += userInput.next().toLowerCase();
			wordsLeft = dictionary.autocomplete(prefix).size();
			
			if (wordsLeft <= 5)
			{
				System.out.println("There are " + wordsLeft + " words remaining: " + dictionary.autocomplete(prefix));
			}
		}
		
		if (wordsLeft == 0)
		{
			System.out.println("Sorry, no words exist in the dictionary that start with: " + prefix);
		}
		else
		{
			System.out.println("Your word was: " + dictionary.autocomplete(prefix));
		}
	}
}

class Trie <Value>
{
	private static int length = 256; 
	private Node root;
	
	private static class Node
	{
		private Object value;
		private Node [] children = new Node [length];
	}
	
	@SuppressWarnings("unchecked")
	public Value get (String key)
	{
		Node find = get(root, key, 0);
		if (find == null)
		{
			return null;
		}
		return (Value) find.value;
	}
	
	private Node get (Node find, String key, int depth)
	{ 
		if (find == null)
		{			
			return null;
		}
		
		if (depth == key.length())
		{
			return find;
		}
		
		char current = key.charAt(depth); 
		return get(find.children[current], key, depth+1);
	}
	
	public void put (String key, Value value)
	{ 
		root = put(root, key, value, 0);
	}
	
	private Node put (Node find, String key, Value value, int depth)
	{ 
		if (find == null)
		{
			find = new Node();
		}
		
		if (depth == key.length())
		{ 
			find.value = value; 
			return find; 
		}
		
		char current = key.charAt(depth); 
		find.children[current] = put(find.children[current], key, value, depth+1);
		return find;
	}
	
	private void collect(Node find, String prefix, ArrayList <String> list)
	{
		 if (find == null)
		 {
			 return;
		 }
		 
		 if (find.value != null)
		 {			 
			list.add(prefix);
		 }
		 
		 for (char current = 0; current < length; current++)
		 {
			collect(find.children[current], prefix + current, list);
		 }
	}
	
	public ArrayList <String> autocomplete (String prefix)
	{
		ArrayList <String> list = new ArrayList <String> ();
		collect(get(root, prefix, 0), prefix, list);
		return list;
	}
}