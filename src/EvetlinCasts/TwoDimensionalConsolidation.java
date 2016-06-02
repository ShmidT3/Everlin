package EvetlinCasts;

import java.awt.Color;

import EverlinObjects.EdgeNew;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;

/**
 * Класс описывает поведение ЭП "Двуменое укрупнение"
 * При выполнении ЭП, находится общее ребро среде выбраных многоугольников 
 * Далле происходит склейка многоугольников по общему ребру
 * @param scheme - исходная схема 
 * @param firstPoly - индекс 1ого многоугольника в схеме
 * @param secondPoly - индекс 2ого многоугольника в схеме
 */
public class TwoDimensionalConsolidation extends ElementaryCast {
	private int firstPoly;
	private int secondPoly;
	private PolyNew p1;
	private PolyNew p2;
	private boolean orient = false;
	private boolean isFind = false;
	private int retVal = -1;
	
	public TwoDimensionalConsolidation(Scheme scheme,int firstPoly,int secondPoly)
	{
		super(scheme);
		nameEC = "Двумерное укрупнение";
		this.firstPoly = firstPoly;
		this.secondPoly = secondPoly;
		countAction = 3;
	}
	
	public void init(int firstPoly,int secondPoly)
	{
		this.firstPoly = firstPoly;
		this.secondPoly = secondPoly;
	}
	
	@Override	
	public int step()
	{
		String descAction = "";
		switch (action) {
		case -1:	
		case 0:
			action = 0;
			addLog("Параметры(" + firstPoly + "," + secondPoly + ")");
			descAction += searchEdges(); // подсветка найденых ребер
			break;
		case 1:
			descAction = merge(); // объединение многоугольников
			break;
		case 2:
			descAction = lastAction(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return retVal;
	}
	
	private String searchEdges()
	{
		isFind = false;
		int i;
		p1 = (PolyNew) scheme.getFigure(firstPoly);
		if (secondPoly < scheme.getCountFigure()) 
		{
			p2 = (PolyNew) scheme.getFigure(secondPoly);
			
			myCycle:
			for (i = 0; i < p1.getCountFigure(); i++)
			{
				for( int j = 0; j < p2.getCountFigure(); j++ )
				{
					if(p1.getEdge(i).getLabel().equals(p2.getEdge(j).getLabel()))
					{
						isFind = true;
						p1.getEdge(i).setColor(Color.RED);
						p2.getEdge(j).setColor(Color.RED);			
						break myCycle;
					}
				}
			}
			if (isFind)
			{
				state = States.Action;
				action++;
				retVal = 1;
				return "Шаг " + action + " из " + countAction + " (Найдено общее ребро '" + p1.getEdge(i).getLabel() + "').";
			}
			else
			{
				state = States.BadAction;
				action = -1;
				retVal = 0;
				return "Шаг 0 из " + countAction + " (У выбранных многоугольников нет общих ребер!)" ;
			}
		}
		else
		{
			state = States.Unknown;
			action = -1;
			retVal = -1;
			return "Шаг 0 из " + countAction + " (Многоугольника " + secondPoly + " не существует!)" ;
		}
	}
	
	private String merge()
	{
		p1 = (PolyNew) scheme.getFigure(firstPoly);
		p2 = (PolyNew) scheme.getFigure(secondPoly);
		myCycle:
		for (int i = 0; i < p1.getCountFigure(); i++)
		{
			for( int j = 0; j < p2.getCountFigure(); j++ )
			{
				if(p1.getEdge(i).getLabel().equals(p2.getEdge(j).getLabel()))
				{
					orient = p1.getEdge(i).getOrientation() == p2.getEdge(j).getOrientation();
					p1.removeFigure(i);
					PolyNew shifyPoly =  p2.shiftPoly(j,!orient);
					for(int k = 1; k < shifyPoly.getCountFigure(); k++ )
					{
						p1.addFigure(i + k - 1,new EdgeNew(shifyPoly.getEdge(k),Color.BLUE));
					}
					scheme.removeFigure(secondPoly);						
					break myCycle;
				}
			}
		}
		action++;
		state = States.Action;
		return "Шаг " + action+ " из " + countAction + " (Выполнение элементарного преобразования!')." ;
	}
	

}

