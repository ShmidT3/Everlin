package EvetlinCasts;

import java.awt.Color;
import java.util.ArrayList;

import EverlinObjects.Alphabet;
import EverlinObjects.EdgeNew;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;

public class ElementarySecondStep extends ElementaryCast {
	private PolyNew sourcePoly;
	private PolyNew doublePoly;
	private int adjacentIndex = -1;
	private PolyNew adjacentPoly;
	
	public ElementarySecondStep(Scheme scheme)
	{
		super(scheme);
		nameEC = "Поиск смежных ребер";
		countAction = 2;
		state = States.Init;
	}
	
	@Override	
	public int step()
	{
		String descAction = "";
		switch (action) {
		case -1:
			return 0;
		case 0:
			descAction = searchAdjacentEdges(); // подсветка найденых смежных ребер
			break;
		case 1:
			descAction = deleteAdjacentEdges(); // удаление смежных ребер
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0;
	}
	
	private String searchAdjacentEdges()
	{
		int retVal = -1;
		sourcePoly = (PolyNew) scheme.getFigure(0);
		doublePoly = new PolyNew((PolyNew) scheme.getFigure(0));
		doublePoly.doublePoly();
		if (sourcePoly.getCountFigure() == 2)
		{
			action = -1;
			state = States.Exit;
			return "";
		}
		adjacentPoly = new PolyNew();
		String edgeName = Alphabet.getNextLetter();
		while(!"".equals(edgeName))
		{
			adjacentPoly = new PolyNew();
			adjacentPoly.addFigure(new EdgeNew(edgeName,true));
			adjacentPoly.addFigure(new EdgeNew(edgeName,false));
			adjacentIndex = doublePoly.contains(adjacentPoly);
			if (adjacentIndex >= 0) 
			{		
				for(int i = adjacentIndex; i < adjacentIndex + adjacentPoly.getCountFigure(); i++)
					sourcePoly.getEdge(i).setColor(Color.RED);
				retVal = 1;							
				break;
			}
			adjacentPoly = new PolyNew();
			adjacentPoly.addFigure(new EdgeNew(edgeName,false));
			adjacentPoly.addFigure(new EdgeNew(edgeName,true));
			adjacentIndex = doublePoly.contains(adjacentPoly);
			if (adjacentIndex >= 0) 
			{		
				for(int i = adjacentIndex; i < adjacentIndex + adjacentPoly.getCountFigure(); i++)
					sourcePoly.getEdge(i).setColor(Color.RED);
				retVal = 1;							
				break;
			}
			edgeName = Alphabet.getNextLetter();
		}
	
		if (retVal == -1)
		{
			action = -1;
			state = States.BadAction;
			return "Шаг 0 из " + countAction + " В данном многоугольнике нет смежных пар." ;
		}
		state = States.Action;
		return "Шаг " + (++action) + " из " + countAction + " Была найдена пара: '" + adjacentPoly.getScheme() + "'.)" ;
	}
	
	private String deleteAdjacentEdges()
	{
		((PolyNew) scheme.getFigure(0)).subEdges(adjacentIndex,2,new ArrayList<EdgeNew>());		
		action = 0;
		state = States.Action;
		return "Шаг " + countAction + " из " + countAction + " Была удалена пара смежных: '" + adjacentPoly.getScheme() + "'.)" ;
	}
}
