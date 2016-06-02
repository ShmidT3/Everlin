package EvetlinCasts;

import java.awt.Color;
import java.util.Vector;

import EverlinObjects.Alphabet;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;

public class ElementaryFourthStep extends ElementaryCast {
	private ElementaryCast worker;
	private int indexFirstElem = -1;
	private int indexSecondElem = -1;
	private PolyNew polygon; // ссылка на многоугольник,введена для удобства обращения, т.к. на данном этапе алгоритма многоугольник в схеме один 
	public ElementaryFourthStep(Scheme scheme)
	{
		super(scheme);
		nameEC = "Поиск пленок Мебиуса";
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
			descAction = searchCrossCap(); 
			break;
		case 1:
			descAction = cutCrossCap(); 
			break;
		case 2:
			descAction = mergeCrossCap(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0;
	}
	
	private String searchCrossCap()
	{
		boolean orientationFirstElem = false;
		polygon = (PolyNew) scheme.getFigure(0);
		Vector<String> allLetters = Alphabet.getAllLetters();
		for ( int i = 0; i < allLetters.size(); i++) // перебираем все использованные буквы
		{
			indexFirstElem  = -1; 
			indexSecondElem = -1;
			
			for(int j = 0; j < polygon.getCountFigure(); j++)
			{				
				if (polygon.getEdge(j).getLabel().equals( allLetters.get(i) )) // если нашли ребро с нужным именем 
				{
					if (indexFirstElem == -1) 
					{ 
						//  мы нашли только 1ое ребро
						indexFirstElem = j;
						orientationFirstElem = polygon.getEdge(j).getOrientation();
					}
					else
					{
						if (orientationFirstElem == polygon.getEdge(j).getOrientation()) // ребра имеют одинаковую ориентацию
						{
							indexSecondElem = j;
							
							if (Math.abs(indexFirstElem - ( (polygon.getCountFigure() - indexSecondElem == 1) ? 1 : indexSecondElem)) == 1)
							{								
								break; // это уже пленка, смысла нет смотреть дальше ребра с этим именем
							}
							else
							{ // наш случай! есть ребра с одинаковым именем, ориентацией и между ними есть разделяющие их ребра
								this.state = States.Action;
								polygon.getEdge(indexFirstElem ).setColor(Color.RED);
								polygon.getEdge(indexSecondElem).setColor(Color.RED);
								action++;
								return "Шаг 0 из " + countAction + ". Была найдена пара ребер(" + polygon.getEdge(indexFirstElem).getFullLabel() + ") похожая на пленку Мебиуса." ;
							}
							
						}
					}
				}
			}
		}
		this.state = States.Finish;;
		return "Шаг 0 из " + countAction + ". Все пленки Мебиуса уже выделены";
	}

	
	private String cutCrossCap()
	{		
		polygon = (PolyNew) scheme.getFigure(0);
		PolyNew newPolygon = new PolyNew();	
		
		for(int i = indexFirstElem; i < indexSecondElem; i++)
		{			
			newPolygon.addFigure(polygon.getEdge(i));
		}
		
		worker = new TwoDimensionalUnit(this.scheme, newPolygon);
		
		worker.run();
		action++;
		state = States.Action;
		return "";
	}
	
	private String mergeCrossCap()
	{		
		
		worker = new TwoDimensionalConsolidation(this.scheme, 0,1);
		worker.run();
		action = 0;
		state = States.Action;
		return "";
	}
	
}
