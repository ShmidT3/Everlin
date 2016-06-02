package EvetlinCasts;

import EverlinObjects.EdgeNew;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;

public class Classifier extends ElementaryCast {
	private int countHandles = 0;
	private int countCrossCup = 0;
	private String classfier = "";
	private PolyNew polygon; // ссылка на многоугольник,введена для удобства обращения, т.к. на данном этапе алгоритма многоугольник в схеме один 
	public Classifier(Scheme scheme)
	{
		super(scheme);
		nameEC = "Классификация";
		countAction = 1;
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
			descAction = startClassification(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0;
	}
	private String startClassification()
	{
		EdgeNew a1 = null;
		EdgeNew a2 = null;
		EdgeNew b1 = null;
		EdgeNew b2 = null;
		polygon = (PolyNew) scheme.getFigure(0);
		if(polygon.getCountFigure() == 2)
		{
			a1 = polygon.getEdge(0);
			a2 = polygon.getEdge(1);
			if(a1.getOrientation() == a2.getOrientation())
			{
				classfier += "Данная схема относится к канонческой развертке 2 типа(вид аа)";
			}
			else 
			{
				classfier += "Данная схема относится к канонческой развертке 1 типа(вид аа-1)";
			}			
		}
		else
		{
			PolyNew doublePoly = new PolyNew((PolyNew) scheme.getFigure(0));
			doublePoly.doublePoly();
			// ищем шаблоны
			for(int i = 0; i < polygon.getCountFigure() + 3; i++)
			{
				a1 = doublePoly.getEdge(i);
				a2 = doublePoly.getEdge(i + 1);
				b1 = doublePoly.getEdge(i + 2);
				b2 = doublePoly.getEdge(i + 3);
				if(a1.getLabel().equals(b1.getLabel()) && a1.getOrientation() != b1.getOrientation() && 
						a2.getLabel().equals(b2.getLabel()) && a2.getOrientation() != b2.getOrientation())
				{
					countHandles++;
					if(i + 4  > polygon.getCountFigure() - 1 )
						break;
					else
						i += 3;

				}
				
				if( i < (polygon.getCountFigure() + 1) && (a1.getLabel().equals(a2.getLabel()) && a1.getOrientation() == a2.getOrientation()))
				{
					countCrossCup++;
					if(i + 2 >= polygon.getCountFigure())
						break;
					else
						i += 1;				
				}
			}
			if(countHandles > 0)
			{
				classfier += "Данная схема относится к канонческой развертке 1 типа(в схеме " + countHandles + " ручек)";
			}
			else if(countCrossCup > 0)
			{
				classfier += "Данная схема относится к канонческой развертке 2 типа(в схеме " + countCrossCup + " пленок Мебиуса)";
			}
			else
				classfier += "ОШИБКА! Попробуйте запустить алгоритм заного!";
		}
		
		state = States.Exit;
		return classfier;
	}
	
	public String getClassfier()
	{
		return classfier;
	}
}
