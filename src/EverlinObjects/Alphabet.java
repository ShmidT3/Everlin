package EverlinObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Vector;

public final class Alphabet {
	private final static ArrayList<Boolean> letters = new ArrayList<Boolean>();
	private final static int size = 26;
	private static int indexNextLetter = 0;

	
	public static void init(TreeMap<String,Integer> map)
	{
		indexNextLetter = 0;
		letters.clear();
		for(int i = 0; i < size; i++)
		{
			letters.add(false);
		}
		Iterator<?> it = map.entrySet().iterator();
		while( it.hasNext() )
		{
			@SuppressWarnings("unchecked")
			Entry<String,Integer> me = (Entry<String, Integer>) it.next();
			int a = (int)((char) (me.getKey().charAt(0) - 'a'));
			letters.set(a,true);
		}
	}
	
	public static String getNextLetter()
	{
		for(;indexNextLetter < size; indexNextLetter++)
		{
			if(letters.get(indexNextLetter))
			{
				return "" + (char)((char)(indexNextLetter++) + 'a');
			}
		}
		return "";
	}
	
	public static Vector<String> getAllLetters()
	{
		indexNextLetter = 0;
		Vector<String> retVal = new Vector<String>();
		for(;indexNextLetter < size; indexNextLetter++)
		{
			if(letters.get(indexNextLetter))
			{
				retVal.add("" + (char)((char)(indexNextLetter) + 'a'));
			}
		}
		return retVal;
	}
	
	public static String getNewNextLetter()
	{
		for(int i = 0; i < size; i++)
		{
			if(!letters.get(i))
			{
				letters.set(i, true);
				return "" + (char)((char)i + 'a');
			}
		}
		return "";
	}
	
	public static String getNewNextLetterReverse()
	{
		for(int i = size - 1; i >= 0; i--)
		{
			if(!letters.get(i))
			{
				letters.set(i, true);
				return "" + (char)((char)i + 'a');
			}
		}
		return "";
	}

}
