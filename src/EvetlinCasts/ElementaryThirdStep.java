package EvetlinCasts;

import java.util.Vector;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;
import EverlinObjects.Vertex;

public class ElementaryThirdStep extends ElementaryCast {
	private Vector<Vertex> myEquivClass;
	private ElementaryCast worker;
	
	public ElementaryThirdStep(Scheme scheme,Vector<Vertex> EquivClass)
	{
		super(scheme);
		myEquivClass = EquivClass;
		nameEC = "Объединение классов эквиволентности";
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
			descAction = cut(); 
			break;
		case 1:
			descAction = merge(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0;
	}
	
	private String cut()
	{
		PolyNew newPolygon = new PolyNew();
		int index = myEquivClass.get(myEquivClass.size() - 1).getIndex() + 1; // берём крайнюю вершину класса экв, смотрим на его индекс и делаем + 1, чтобы получить след. точку 
		Vertex v = ((PolyNew)this.scheme.getFigure(0)).getVertex(index);

		newPolygon.addFigure(v.getLeftEdge());
		newPolygon.addFigure(v.getRightEdge());
		worker = new TwoDimensionalUnit(this.scheme, newPolygon);
		
		worker.run();
		action++;
		state = States.Action;
		return "";
	}

	
	private String merge()
	{
		PolyNew pn = ((PolyNew)this.scheme.getFigure(0)).shiftPoly(1, true);
		scheme.removeFigure(0);
		scheme.addFigure(0, pn);
		
		worker = new TwoDimensionalConsolidation(this.scheme, 0,1);
		worker.run();
		action++;
		state = States.Finish;
		return "";
	}

}
