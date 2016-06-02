package EverlinObjects;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import EverlinFrame.MainFrame;

/**
 * Класс Scheme описывает поведение всех полигонов(PolyNew) в системе.
 */
public class Scheme extends Figure {
	private final static String regExp1 = "[a-z](-1)?";
	public static int panelWidth;
	public static int panelHeight;

	/**
	 * 
	 * Метод isValid() делает проверку системы на корректность, а так же обновляет алфавит,
	 * должен выполняется каждый раз после любой манипуляцией(измением) над системой(схемой). 
	 * @throws Error бросается при неправильной манипуляцией на системой или некорректной исходной схемой.
	 */
	@SuppressWarnings("unchecked")
	public void isValid() throws Error
	{
		String errorString = "";
		TreeMap<String,Integer> map = new TreeMap<String,Integer>(); 
		countFigure = v_Figure.size();
		for(int i = 0; i < countFigure; i++)
		{
			((PolyNew) v_Figure.get(i)).setCountPolygons(countFigure);
			for( int j = 0; j < v_Figure.get(i).getCountFigure(); j++ )
			{
				if (map.containsKey(((EdgeNew)v_Figure.get(i).getFigure(j)).getLabel()))
				{
					Integer tmp = map.get(((EdgeNew)((PolyNew) v_Figure.get(i)).getEdge(j)).getLabel());
					map.put(((EdgeNew) ((PolyNew) v_Figure.get(i)).getFigure(j)).getLabel(),++tmp);
				}
				else
				{
					map.put(((EdgeNew)((PolyNew) v_Figure.get(i)).getEdge(j)).getLabel(),1);
				}
			}
		}
		Iterator<?> it = map.entrySet().iterator();
	    while( it.hasNext() )
	    {
	      Entry<String,Integer> me = (Entry<String,Integer>)it.next();
	      if( me.getValue() != 2 )
	      {
	    	  errorString += "Количество ребер с именем '" + me.getKey() + "' отличино от 2!\r\n" ;
	      }
	    }
	    Alphabet.init(map);
	    if(errorString.length() > 0)
	    {
	    	throw new Error(errorString);
	    }
	}

	/**
	 * Метод parsePoly преобразует входную строку polyString в набор токенов,
	 * удовлетворяющих регулярному выражению regExp1 = "[a-z](-1)?",вызывается при построении многоугольника.
	 * @throws Error
	 */
	private Vector<String> parsePoly(String polyString) throws Error
	{
		String stringEdge;
		Vector<String> scheme = new Vector<String>();

		Pattern p = Pattern.compile(regExp1);
		Matcher m = p.matcher(polyString); 
		boolean isFind = m.find();
		int countChars = 0;
		while(isFind){
			stringEdge = m.group();
			countChars += stringEdge.length();
			scheme.add(stringEdge);
			isFind = m.find();
		}
		if(polyString.length() != countChars)
		{		
			throw new Error("При создании многоугольника произошла ошибка!\r\nВ таблицу введено значение, которое не соответствует шаблону!\r\nОшибочная строка: " + polyString + ".");
		}
		return scheme;	
	}
	
	/**
	 * Метод create выполняет процесс создания(парсинг,генерация многоугольников) схемы.
	 * @param polygons - многоугольники в виде массива строк.
	 * @return  1 - все прошло успешно.
	 * 		   (-1) - при построении выявилась ошибка.
	 */
	public int create(Vector<String> polygons)
	{
		try
		{
			reset();
			countFigure  = polygons.size();
			if (countFigure == 0) throw new Error("Введите систему многоугольников");
			
			for(int i = 0; i < countFigure; i++)
			{			
				v_Figure.add(new PolyNew(parsePoly(polygons.get(i)),i,countFigure));				
			}
			isValid();
			MainFrame.addRowLog("Процесс инициализации системы прошел успешно!");
			return 1;
		}
		catch(Error e)
		{
			JOptionPane.showMessageDialog(null,
					e.getMessage(),
					MainFrame.titleWindow + ": Ошибка!",
					JOptionPane.ERROR_MESSAGE);
			return -1;
		}
	}
	
	@Override
	protected String generatedHeaderToString() {
		return "Холст("+panelWidth+ ","+panelHeight+"); Количество полигонов: " + countFigure + ";\r\n";
	};
}
