package EvetlinCasts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Vector;

import EverlinObjects.Alphabet;
import EverlinObjects.EdgeNew;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;


/**
 * Класс описывает поведение ЭП "Одномерное укрупнение".
 * При выполнении ЭП, находится ребро edge в схеме.
 * Далее выполняется замена edge на 2 новых ребра с учетом ориентации исходных.
 * @param scheme - исходная схема 
 * @param edge - индекс 1ого многоугольника в схеме
 */
public class OneDimensionalConsolidation extends ElementaryCast{
	private EdgeNew edge1;
	private EdgeNew edge2;
	private Vector<Integer> polyIndexes;
	private Vector<Integer> adjacentIndexes;

	public OneDimensionalConsolidation(Scheme scheme,EdgeNew edge1,EdgeNew edge2)
	{
		super(scheme);
		nameEC = "Одномерное укрупнение";
		this.edge1 = edge1;
		this.edge2 = edge2;
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
			descAction = searchAdjacentEdges(); // подсветка найденых ребер
			break;
		case 1:
			descAction = changeMyEdgesToNew(); // замена ребер на новые
			break;
		case 2:
			descAction = lastAction(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0; //action;
	}
	
	private String searchAdjacentEdges()
	{
		int adjacentIndex;
		PolyNew myPolygon = new PolyNew();
		polyIndexes = new Vector<Integer>();
		adjacentIndexes = new Vector<Integer>();
		myPolygon.addFigure(edge1);
		myPolygon.addFigure(edge2);
		
		for(int i = 0; i < scheme.getCountFigure(); i++) // в расширенном многоугольнике ищем ребра
		{
			PolyNew doublePoly = new PolyNew((PolyNew) scheme.getFigure(i));
			doublePoly.doublePoly();
			adjacentIndex = doublePoly.contains(myPolygon);
			if (adjacentIndex >= 0) 
			{		
				polyIndexes.add(i);
				adjacentIndexes.add(adjacentIndex);
			}
		}
		if (polyIndexes.size() == 2) // если их 2, то подсвечиваем их! 
		{
			for(int j = 0; j < polyIndexes.size(); j++)
			{
				PolyNew doublePoly = new PolyNew((PolyNew) scheme.getFigure(polyIndexes.get(j)));
				doublePoly.doublePoly();
				adjacentIndex = doublePoly.contains(myPolygon);
				for(int k = adjacentIndex; k < adjacentIndex + myPolygon.getCountFigure(); k++)
					((PolyNew)scheme.getFigure(polyIndexes.get(j))).getEdge(k).setColor(Color.RED);				
			}
			state = States.Action;
		}
		else
		{
			action = -1;
			state = States.BadAction;
			return "Шаг 0 из " + countAction + " В данном многоугольнике нет смежных пар." ;
		}
		return "Шаг " + (++action) + " из " + countAction + " Были найдены пары " + edge1.getFullLabel()+ edge2.getFullLabel() + ": в многоугольниках " + polyIndexes.get(0) + " и " + polyIndexes.get(1) + "." ;
	}
	
	private String changeMyEdgesToNew()
	{
		boolean orientation = false,isWas = false;
		int adjacentIndex,polyIndex;
		String word1 = Alphabet.getNewNextLetter();
		ArrayList<EdgeNew> a_Edges = new ArrayList<EdgeNew>();
		
		for(int j = 0; j < polyIndexes.size(); j++)
		{
			a_Edges.clear();
			polyIndex = polyIndexes.get(j);
			adjacentIndex = adjacentIndexes.get(j);
			if(isWas)
			{
				if(orientation == ((PolyNew) scheme.getFigure(polyIndex)).getEdge(adjacentIndex).getOrientation())
				{
					a_Edges.add(new EdgeNew(word1,orientation,Color.BLUE));
				}
				else
				{
					a_Edges.add(new EdgeNew(word1,!orientation,Color.BLUE));						
				}
			}
			else
			{
				orientation = ((EdgeNew)((PolyNew) scheme.getFigure(j)).getFigure(adjacentIndex)).getOrientation();
				a_Edges.add(new EdgeNew(word1,orientation,Color.BLUE));
				isWas = true;
			}
			((PolyNew) scheme.getFigure(polyIndex)).subEdges(adjacentIndex,2, a_Edges); 
		}
		state = States.Action;
		action++;
		return "Шаг " + action + " из " + countAction + " Пары " + edge1.getFullLabel()+ edge2.getFullLabel() + ": в многоугольниках " + polyIndexes.get(0) + " и " + polyIndexes.get(1) + " были заменены на ребро: " + word1 +  " ." ;
	}

}
