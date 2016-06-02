package EvetlinCasts;

import java.awt.Color;
import java.util.ArrayList;

import EverlinObjects.Alphabet;
import EverlinObjects.EdgeNew;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;


/**
 * Класс описывает поведение ЭП "Одномерное подразделение".
 * При выполнении ЭП, находится ребро edge в схеме.
 * Далее выполняется замена edge на 2 новых ребра с учетом ориентации исходных.
 * @param scheme - исходная схема 
 * @param edge - индекс 1ого многоугольника в схеме
 */
public class OneDimensionalUnit extends ElementaryCast{
	private EdgeNew myEdge;
	
	public OneDimensionalUnit(Scheme scheme,EdgeNew edge)
	{
		super(scheme);
		nameEC = "Одномерное подразделение";
		this.myEdge = edge;
		countAction = 3;
		state = States.Init;
	}
	
	@Override	
	public int step()
	{
		String descAction = "";
		switch (action) {
		case -1:	
		case 0:
			action = 0;
			descAction = searchEdges(); // подсветка найденых ребер
			break;
		case 1:
			descAction = changeMyEdgeToNew(); // замена ребер на новые
			break;
		case 2:
			descAction = lastAction(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return action;
	}
	
	private String searchEdges()
	{
		int count = 0;
		for(int i = 0; i < scheme.getCountFigure(); i++)
		{
			for( int j = 0; j < scheme.getFigure(i).getCountFigure(); j++ )
			{
				if(myEdge.getLabel().equals(((EdgeNew)(scheme.getFigure(i).getFigure(j))).getLabel()))
				{
					((EdgeNew)(scheme.getFigure(i).getFigure(j))).setColor(Color.RED);
					count++;
				}
			}
		}
		if(count == 2)
		{
			state = States.Action;
			action++;
			return "Шаг " + action + " из " + countAction + " (Найдено 2 ребра с именем '" + myEdge.getLabel() + "')." ;
		}
		else 
		{
			state = States.BadAction;
			action = countAction;
			return "Шаг 0 из " + countAction + " (Не нашли ребра с именем '" + myEdge.getLabel() + "'. Запуск элементарного преобразования отменен!)" ;
			
		}
	}
	
	private String changeMyEdgeToNew()
	{
		boolean orientation = false,isWas = false;
		String word1 = Alphabet.getNewNextLetter();
		String word2 = Alphabet.getNewNextLetter();
		ArrayList<EdgeNew> a_Edges = new ArrayList<EdgeNew>();
		for(int i = 0; i < scheme.getCountFigure(); i++)
		{
			for( int j = 0; j < scheme.getFigure(i).getCountFigure(); j++ )
			{
				if(myEdge.getLabel().equals(((EdgeNew)(scheme.getFigure(i).getFigure(j))).getLabel()))
				{
					a_Edges.clear();
					if(isWas)
					{						
						if(orientation == ((PolyNew) scheme.getFigure(i)).getEdge(j).getOrientation())
						{
							a_Edges.add(new EdgeNew(word1,orientation,Color.BLUE));
							a_Edges.add(new EdgeNew(word2,orientation,Color.BLUE));
						}
						else
						{
							a_Edges.add(new EdgeNew(word2,!orientation,Color.BLUE));
							a_Edges.add(new EdgeNew(word1,!orientation,Color.BLUE));						
						}
					}
					else
					{
						isWas = true;
						orientation = ((PolyNew) scheme.getFigure(i)).getEdge(j).getOrientation();
						a_Edges.add(new EdgeNew(word1,orientation,Color.BLUE));
						a_Edges.add(new EdgeNew(word2,orientation,Color.BLUE));
					}
					((PolyNew) scheme.getFigure(i)).subEdges(j, a_Edges);
				}
			}
		}
		state = States.Action;
		action++;
		return "Шаг " + action + " из " + countAction + " (Ребра с именем '" + myEdge.getLabel() + "' были заменены на ребра '" + word1 +"' и '" + word2 +")." ;
	}
}
