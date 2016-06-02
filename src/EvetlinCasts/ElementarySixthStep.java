package EvetlinCasts;

import java.awt.Color;
import EverlinObjects.EdgeNew;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;

public class ElementarySixthStep extends ElementaryCast {
	private ElementaryCast worker;
	private int positionCrossCap = -1;
	private int positionHandle = -1;
	private PolyNew polygon; // ссылка на многоугольник,введена для удобства обращения, т.к. на данном этапе алгоритма многоугольник в схеме один 
	public ElementarySixthStep(Scheme scheme)
	{
		super(scheme);
		nameEC = "Выделение пленок из пленки и ручки";
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
			descAction = searchTemplates(); 
			break;
		case 1:
			descAction = cutTemplates(); 
			break;
		case 2:
			descAction = mergeTemplates(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0;
	}
	private String searchTemplates()
	{
		EdgeNew a1 = null;
		EdgeNew a2 = null;
		EdgeNew b1 = null;
		EdgeNew b2 = null;
		polygon = (PolyNew) scheme.getFigure(0);
		if(polygon.getCountFigure() < 6)
		{
			state = States.Finish;
			return "Длина последовательности меньше 6! Шаг выполнен!";
		}
		PolyNew doublePoly = new PolyNew((PolyNew) scheme.getFigure(0));
		doublePoly.doublePoly();
		// ищем шаблоны
		for(int i = 0; i < doublePoly.getCountFigure() - 4; i++)
		{
			a1 = doublePoly.getEdge(i);
			a2 = doublePoly.getEdge(i + 1);
			b1 = doublePoly.getEdge(i + 2);
			b2 = doublePoly.getEdge(i + 3);
			if(positionHandle == -1 && (a1.getLabel().equals(b1.getLabel()) && a1.getOrientation() != b1.getOrientation() && 
					a2.getLabel().equals(b2.getLabel()) && a2.getOrientation() != b2.getOrientation()))
			{
				positionHandle = i;
			}
			if(positionCrossCap == -1 && (a1.getLabel().equals(a2.getLabel()) && a1.getOrientation() == a2.getOrientation()))
			{
				positionCrossCap  = i;				
			}
		}
		
		if(positionCrossCap != -1 && positionHandle != -1)
		{	
			for(int i = positionCrossCap; i < positionCrossCap + 2; i++)
			{
				polygon.getEdge(i).setColor(Color.RED);
			}
			for(int i = positionHandle; i < positionHandle + 4; i++)
			{
				polygon.getEdge(i).setColor(Color.BLUE);
			}
			this.state = States.Action;
			action++;
			return "В текущем многоугольнике найдены ручка и пленка Мебиуса.";
		}
		state = States.Finish;
		return "В текущем многоугольнике не найдены ручка и пленка Мебиуса. Шаг выполнен!";
	}

	private String cutTemplates()
	{
		polygon = (PolyNew) scheme.getFigure(0);
		PolyNew newPolygon = new PolyNew();	
		int indexBegin = positionCrossCap + 1;
		int indexEnd = positionHandle + 2;
		
		if(positionCrossCap > positionHandle + 1)
		{
			indexBegin = positionHandle + 2;
			indexEnd = positionCrossCap + 1;		
		}
		
		for(int i = indexBegin; i < indexEnd; i++)
		{			
			newPolygon.addFigure(polygon.getEdge(i));
		}
		
		worker = new TwoDimensionalUnit(this.scheme, newPolygon);
		((TwoDimensionalUnit)worker).setIsNeedReverseTakeWord(true);
		worker.run();
		action++;
		state = States.Action;
		return "";
	}

	private String mergeTemplates() 
	{				
		worker = new TwoDimensionalConsolidation(this.scheme, 0,1);
		worker.run();
		action = 0;
		state = States.BadAction;
		return "";
	}	
}
