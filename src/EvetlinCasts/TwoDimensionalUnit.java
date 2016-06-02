package EvetlinCasts;


import java.awt.Color;
import java.util.ArrayList;

import EverlinObjects.Alphabet;
import EverlinObjects.EdgeNew;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;

/**
 * Класс описывает поведение ЭП "Двуменое подразделение"
 * При выполнении ЭП, находится в схеме многоугольник polygon,
 * разрезает мноугольник на 2 мноугольника: polygon, найденный без polygon
 * @param scheme - исходная схема 
 * @param polygon - многоугольника 
 */
public class TwoDimensionalUnit extends ElementaryCast {
	private PolyNew polygon;
	private int adjacentIndex;
	private boolean isNeedReverseTakeWord = false;
	private int index; // индекс многоугольника в коротом содержится polygon
	
	public TwoDimensionalUnit(Scheme scheme,PolyNew polygon)
	{
		super(scheme);
		nameEC = "Двумерное подразделение";
		this.polygon = polygon; 
		countAction = 3;
		index = -1;
		adjacentIndex = -1;
	}
	
	@Override	
	public int step()
	{
		String descAction = "";
		switch (action) {
		case -1:	
		case 0:
			action = 0;
			descAction += searchSubPolyInScheme(); // подсветка
			break;
		case 1:
			descAction = cut(); // разделяем
			break;
		case 2:
			descAction = lastAction(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0;
	}
	
	private String searchSubPolyInScheme()
	{
		boolean isFind = false;
		
		PolyNew doublePoly;
		for(index = 0 ; index < scheme.getCountFigure(); index++)
		{
			doublePoly = new PolyNew((PolyNew)scheme.getFigure(index));
			doublePoly.doublePoly();
			adjacentIndex = doublePoly.contains(polygon); 
			if (adjacentIndex >= 0) 
			{		
				isFind = true;
				for(int k = adjacentIndex; k < adjacentIndex + polygon.getCountFigure(); k++)
					((PolyNew)scheme.getFigure(index)).getEdge(k).setColor(Color.RED);
				for(int k = 0; k < scheme.getFigure(index).getCountFigure() - polygon.getCountFigure(); k++)
					((PolyNew)scheme.getFigure(index)).getEdge(adjacentIndex - k - 1).setColor(Color.WHITE);
				break;
			}
		}
		if (isFind)
		{
			state = States.Action;
			action++;
			return "Шаг " + action + " из " + countAction + " (Найден искомый многоугольник " + polygon.getScheme() + " в многоугольнике " + index + ")";
		}
		else
		{
			state = States.BadAction;
			action = -1;
			return "Шаг 0 из " + countAction + " (В схеме нет многоугольника " + polygon.getScheme() + ")" ;
		}
	}
	
	public void setIsNeedReverseTakeWord(boolean val)
	{
		isNeedReverseTakeWord = val;
	}
	
	private String cut()
	{
		if (scheme.getFigure(index).getCountFigure() - polygon.getCountFigure() < 2)
			throw new Error("При выполнении ЭП '" + nameEC + "' произошла ошибка.\r\nВ новом мнгоугольнике количество ребер не должно быть меньше 2!");
		String word1;
		if(isNeedReverseTakeWord)
			word1 = Alphabet.getNewNextLetterReverse();
		else
			word1 = Alphabet.getNewNextLetter();
		polygon.addFigure(new EdgeNew(word1,true,Color.RED));
		scheme.addFigure(polygon); // добавляем новый
		((PolyNew) scheme.getFigure(index)).subEdges(adjacentIndex,polygon.getCountFigure() - 1, new ArrayList<EdgeNew>()); // вырезаем многоугольник, для замены в subEdges подсовываем пустой массив 
		scheme.getFigure(index).addFigure(adjacentIndex,new EdgeNew(word1,false,Color.WHITE));
		scheme.addFigure((PolyNew) scheme.getFigure(index)); // добавляем измененный многоугольник
		scheme.removeFigure(index);	// удаляем старый
		
		state = States.Action;
		action++;
		return "Шаг " + action + " из " + countAction + " (Разрезали!) ";
	}
	

}

