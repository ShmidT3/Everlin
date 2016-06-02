package EvetlinCasts;

import java.util.Vector;

import EverlinObjects.EdgeNew;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;
import EverlinObjects.Vertex;

/**
 * Класс FindEquivalentClasses описывает алгоритм поиска классов эквивалентности
 * Строит деверо обхода
 * Выполняет обход с 2ух сторон
 */
public class FindEquivalentClasses extends ElementaryCast {
	private PolyNew polygon; // ссылка на многоугольник,введена для удобства обращения, т.к. на данном этапе алгоритма многоугольник в схеме один 
	private final int startIndex = 0; // индекс вершины с которой начинается обход
	private Vertex vertex; // ссылка на вершину(узла дерева) 
	private boolean isLeftBeginEdge = false; // показывает с какой стороны был начат обход 
	private Vector<Vertex> myEquivalentClass;
	
	public FindEquivalentClasses(Scheme scheme)
	{
		super(scheme);
		nameEC = "Поиск классов эквивалентности";
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
			descAction = startFind(); 
			break;
		case 1:
			descAction = findEquivalentClasses(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0;
	}
	
	/**
	 * Метод вызывается при начале работы алгоритма поиска классов экв.
	 * @return String - описание действия
	 */
	private String startFind()
	{
		polygon = (PolyNew) scheme.getFigure(0);
		
		addLog("Начальная вершина с номером: " + (startIndex));
		addLog("Начинаем с " + ((!isLeftBeginEdge) ?  "левого" : "правого") + " ребра!");
		vertex = polygon.getVertex(startIndex).setIsLeftLastEdge(isLeftBeginEdge);
		state = States.Action;
		++action;
		return "Работа начата: " + getNodeDescription(true);
	}
	
	/**
	 * Метод описывает действие одного шага для поиска классов экв. и вызывает функцию use() для текущей вершины дерева
	 * @param boolean isNeedFullDesc - нужно ли давать расшифровку ребру по которому мы попали в эту вершину 
	 * @return String - описание действия 
	 */
	private String getNodeDescription(boolean isNeedFullDesc)
	{
		
		String retVal = "" + vertex.getIndex();
		if (isNeedFullDesc)
		{
			vertex.use();
			if (vertex.getIsNeedTakeBeginPositionEdge())
				retVal += " '" + vertex.getAdjEdge().getLabel() + "  ";
			else
				retVal += "  " + vertex.getAdjEdge().getLabel() + "' ";
		}		
		return retVal;
	}
	
	/**
	 * Метод является управляющим для всего алгоритма поиска классов экв.
	 * Вызывает метод getNextPosition и анализирует сложившуюся ситуацию 
	 * @return
	 */
	private String findEquivalentClasses()
	{		
		vertex = getNextPosition();		
		if (vertex.getIndex() != startIndex)
		{
			state = States.Action;
		}
		else
		{
			if (!isLeftBeginEdge)
			{
				lastAction();
				isLeftBeginEdge = true;
				action = 0;				
				state = States.Action;
				addLog(getNodeDescription(false));
				return "Обход завершен!";
			}
			++action;
			state = States.Finish;
			createMyEquivalentClass();
			addLog(myEquivalentClass.toString());
			return getNodeDescription(false);
		}
		return getNodeDescription(true);
	}
	
	/**
	 * Метод выполняет шаг поиска вершины для которой есть дубликат с общим ребром для vertex 
	 * и выполняет различные операции для определения следующей вершины в дереве
	 * @return Vertex - следующую вершина дерева
	 */
	private Vertex getNextPosition()
	{
		EdgeNew goodEdge = null;
		int indexVertex = vertex.getIndex();
		for (int i = 1; i < polygon.getCountFigure(); i++)// поиск вершины у которой есть смежное ребро такое же по названию как и у vertex.getAdjEdge()
		{
			if (i == 1 && polygon.getVertex(indexVertex).getIsLeftLastEdge()) continue; // проскакиваем самого себя 
			
			if(polygon.getVertex(indexVertex + i ).getLeftEdge().getLabel().equals(vertex.getAdjEdge().getLabel()))    
			{
				goodEdge = polygon.getVertex(indexVertex + i).getLeftEdge();
			}
			else if(polygon.getVertex(indexVertex + i).getRightEdge().getLabel().equals(vertex.getAdjEdge().getLabel()))
			{
				goodEdge = polygon.getVertex(indexVertex + i).getRightEdge();				
			}
			if (goodEdge != null) // если нашли, то для этого ребра, просим вернуть вершину начала или конца ребра, эти данные берем от предыдущей вершины, только она знает, какая вершина нам нужна
			{	
				goodEdge.use();
				return (vertex.getIsNeedTakeBeginPositionEdge()) ? goodEdge.getBeginVertex() : goodEdge.getEndVertex();
			}
		}
		return null;		
	}
	
	/**
	 * Метод createMyEquivalentClass выполняет поиск максимального непрывного подкласса экв. для текущего класса экв.
	 * Заполняет поле объекта myEquivalentClass
	 */
	@SuppressWarnings("unchecked")
	private void createMyEquivalentClass()  
	{
		int currIndex = 0; // текущий индекс подкласса 
		int indexMaxSizeClass = 0; // индекс максимального подкласса 
		int maxSizeClass = -1; // размер максимального подкласса 
		
		Vector<Vertex> currClass = new Vector<Vertex>(); // текущий подкласс, представляется в виде массива вершин
		currClass.add(polygon.getVertex(0)); // кладём 1ую вершину в текущий подкласс
		
		boolean isFindClass; // переменная хранит значение о том,принадлежит ли текущая вершина найденому классу экв. 
		boolean isPrevFindClass = polygon.getVertex(0).getCountUsedVertex() > 0; // // переменная хранит значение о том,принадлежит ли предыдущая вершина найденому классу экв. 
		
		Vector<Vector<Vertex>> allClasses = new Vector<Vector<Vertex>>();  // массив всех подклассов эквивол. все вершины которые не принадлежат найденому классу экв. считаются отдельным подклассом
		allClasses.add(new Vector<Vertex>()); // инициализируем 0-ой элемент  
		for(int i = 1; i < polygon.getCountFigure();i++) // начинаем с 1, потому что 0-ую вершину мы уже учитывали
		{
			isFindClass = polygon.getVertex(i).getCountUsedVertex() > 0; // принадлежит ли i-ая вершина классу экв.
			if(isFindClass)
			{
				if(isPrevFindClass)
				{					
					currClass.add(polygon.getVertex(i)); // добавляем 
					if(i == polygon.getCountFigure() - 1) // если это последняя вершина, то нужно добалять currClass к 1ому подклассу 
					{
						int firstIndex = 0;
						for(int j = 0;j < allClasses.size(); j++) //определение позиции 1ого подкласса
						{
							if(allClasses.get(j).size() > 0)
							{
								firstIndex = j;
								break;
							}
						}
						
						allClasses.get(firstIndex).addAll(0, currClass);
					}
				}
				else
				{
					allClasses.add((Vector<Vertex>) currClass.clone());
					currClass.removeAllElements();
					currIndex++;
					currClass.add(polygon.getVertex(i));
					if(i == polygon.getCountFigure() - 1) // если это последняя вершина, то нужно добалять currClass к 1ому подклассу 
					{
						int firstIndex = 0;
						for(int j = 0;j < allClasses.size(); j++) //определение позиции 1ого подкласса
						{
							if(allClasses.get(j).size() > 0)
							{
								firstIndex = j;
								break;
							}
						}
						
						allClasses.get(firstIndex).addAll(0, currClass);
					}
				}
			}
			else
			{
				
				allClasses.add((Vector<Vertex>) currClass.clone());
				currClass.removeAllElements();
				currIndex++;	
				currClass.add(polygon.getVertex(i));
			}
			if (allClasses.get(currIndex).size() > maxSizeClass ) // во время поиска подклассов одновременно определяем максимальный из них 
			{
				maxSizeClass = allClasses.get(currIndex).size();
				indexMaxSizeClass = currIndex;
			}
			isPrevFindClass = isFindClass;
		}
		myEquivalentClass = (Vector<Vertex>) allClasses.get(indexMaxSizeClass).clone(); // клонируем максимальный подкласс
		
		if (myEquivalentClass.size() == polygon.getCountFigure()) // если мощность максимального подкласса равна мощности многоугольника, то 3 шаг алгоритма выполнен!
		{
			++action;
			state = States.Exit;
		}
	}
	
	public Vector<Vertex> getMyEquivalentClass()
	{
		return myEquivalentClass;
	}
	

}
