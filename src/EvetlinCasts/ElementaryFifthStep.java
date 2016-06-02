package EvetlinCasts;

import java.awt.Color;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;

public class ElementaryFifthStep extends ElementaryCast {
	private ElementaryCast worker;
	private int indexFirstEdge = -1;
	private int indexSecondEdge = -1;
	private int indexFirstEdge2 = -1;
	private int indexSecondEdge2 = -1;
	private PolyNew polygon; // ссылка на многоугольник,введена для удобства обращения, т.к. на данном этапе алгоритма многоугольник в схеме один 
	public ElementaryFifthStep(Scheme scheme)
	{
		super(scheme);
		nameEC = "Выделение ручек";
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
			descAction = searchHandle(0); 
			break;
		case 1:
			descAction = cutHandle(); 
			break;
		case 2:
			descAction = mergeHandle(); 
			break;
		default:
			break;
		}
		addLog(descAction);
		return 0;
	}
	
	/**
	 * Метод searchHandle выполняет поиск невыделенной ручки с позиции startVal
	 * и записывает индексы ребер в indexFirstEdge,indexFirstEdge2,indexSecondEdge,indexSecondEdge2
	 * @param startVal - индекс начала обхода многоугольника(нужен для рекурсии)
	 */
	private String searchHandle(int startVal)
	{
		indexFirstEdge = -1;
		indexSecondEdge = -1;
		indexFirstEdge2 = -1;
		indexSecondEdge2 = -1;
		String prevName,currName,nextName;
		polygon = (PolyNew) scheme.getFigure(0);
		
		// сначала ищем первое ребро ручки
		for(int i = startVal; i < polygon.getCountFigure(); i++)
		{
			prevName = polygon.getEdge(i - 1).getLabel();
			currName = polygon.getEdge(i	).getLabel();
			nextName = polygon.getEdge(i + 1).getLabel();
			// смотрим слева и справа  
			if( indexFirstEdge == -1 && !((currName.equals(prevName) || currName.equals(nextName))))
			{
				// нашли!
				indexFirstEdge = i;
				continue;
			}
			if (indexFirstEdge != -1)
			{
				boolean condition1,condition2;
				// условие на поиск пары для 1ого элемента 
				condition1 = polygon.getEdge(indexFirstEdge).getLabel().equals(currName); 
				// на всякий случай, проверка на правильности выоплнения алгоритма
				// если condition2 == false, тогда выполнение ВСЕГО!!! алгоритма невозможно, запускайте заного Шаг 1,2,3=) 
				condition2 = polygon.getEdge(indexFirstEdge).getOrientation() == polygon.getEdge(i).getOrientation();
				if (condition1)
				{
					if(condition2)
					{ 
						this.state = States.Finish;;
						return "Шаг 0 из " + countAction + ". Произошла критическая ошибка! Дальнейшее работа алгоритма невозможна. Попробуйте запустить алгоритм заного.";						
					}
					// если мы тут, то всё хорошо
					indexSecondEdge = i;					
					break;
				}
			}
			
		}
		// если ничего не нашли, значит шаг выполнен.
		if (indexSecondEdge == -1)
		{
			this.state = States.Finish;
			return "Шаг 0 из " + countAction + ". Все ручки выделены!";	
		}
		//b c i-1 q-1 i y c-1 q y-1 b  
		// если нашли, то ищем 2ую пару ребер ручки
		for(int i = indexFirstEdge + 1; i < polygon.getCountFigure(); i++)
		{
			indexFirstEdge2 = indexFirstEdge + 1;
			prevName = polygon.getEdge(i - 1).getLabel();
			currName = polygon.getEdge(i	).getLabel();
			nextName = polygon.getEdge(i + 1).getLabel();
			// смотрим слева и справа  
			if( !((currName.equals(prevName) || currName.equals(nextName))))
			{
				// нашли!
				indexFirstEdge2 = i;
			}
			else
			{
				continue;
			}
			// ищем 2ое ребро для 2ой пары между indexFirstEdge2 и indexSecondEdge 
			for(int j = indexFirstEdge2 + 1; j < indexSecondEdge; j++)
			{
				if(polygon.getEdge(indexFirstEdge2).getLabel().equals(polygon.getEdge(j).getLabel()))
				{
					//если есть то, ищем заного 1ую вершину
					indexFirstEdge2 = -1;
					break;
				}
			}
			
			if(indexFirstEdge2 == -1) continue;
			else				
			{
				// осталось найти только 2ое ребро для 2ой пары во всём оставшемся 
				for(int j = indexSecondEdge + 1; j < polygon.getCountFigure(); j++)
				{
					boolean condition1,condition2;
					// условие на поиск пары для 1ого элемента 
					condition1 = polygon.getEdge(indexFirstEdge2).getLabel().equals(polygon.getEdge(j).getLabel());
					// на всякий случай, проверка на правильности выоплнения алгоритма
					// если condition2 == false, тогда выполнение ВСЕГО!!! алгоритма невозможно, запускайте заного Шаг 1,2,3=) 
					condition2 = polygon.getEdge(indexFirstEdge2).getOrientation() == polygon.getEdge(j).getOrientation();
					if (condition1)
					{
						if(condition2)
						{ 
							this.state = States.Finish;;
							return "Шаг 0 из " + countAction + ". Произошла критическая ошибка! Дальнейшее работа алгоритма невозможна. Попробуйте запустить алгоритм заного.";						
						}
						// если мы тут, то всё хорошо
						indexSecondEdge2 = j;				
						break;
					}
				}	
			}
						
			if (indexSecondEdge2 != -1)
			{
				//мы нашли ручку
				//теперь проверим, не является ли найденая ручка выделенной 
				int nextPosition = findGoodHandle();
				if (nextPosition != -1)
				{
					// начинаем всё заного с позиции nextPosition + 1, чтобы не найти опять нашу ручку
					return searchHandle(nextPosition + 1);
				}
				// раскрашиваем и выходим
				this.state = States.Action;
				polygon.getEdge(indexFirstEdge ).setColor(Color.RED);
				polygon.getEdge(indexSecondEdge).setColor(Color.RED);
				polygon.getEdge(indexFirstEdge2).setColor(Color.RED);
				polygon.getEdge(indexSecondEdge2).setColor(Color.RED);
				action++;
				return "Шаг 0 из " + countAction + ". Было найдена две пары ребер(" + polygon.getEdge(indexFirstEdge).getLabel() + "," + polygon.getEdge(indexFirstEdge2).getLabel() + ") похожая на ручку." ;			
			}
			
		}			
		
		this.state = States.Finish;;
		return "Шаг 0 из " + countAction + ". Все ручки Мебиуса уже выделены";
	}
	/**
	 * Метод findGoodHandle говорит, является ли найденая ручка правильной(имеет вид aba-1b-1)
	 * @return если является правильной, то возвращает индекс слудующей вершины от края обрабатываемой ручки.
	 * В противном случае результатом будет -1.
	 */
	private int findGoodHandle()
	{
		Vector<Integer> vectr = new Vector<Integer>();
		vectr.add(indexFirstEdge);
		vectr.add(indexFirstEdge2);
		vectr.add(indexSecondEdge);
		vectr.add(indexSecondEdge2);
		vectr.add(indexFirstEdge + scheme.getFigure(0).getCountFigure() );
		vectr.add(indexFirstEdge2 + scheme.getFigure(0).getCountFigure());
		vectr.add(indexSecondEdge + scheme.getFigure(0).getCountFigure());
		vectr.add(indexSecondEdge2 + scheme.getFigure(0).getCountFigure());
		
		for(int i = 0; i < vectr.size() - 3; i++)
		{
			if(vectr.get(i + 1) - vectr.get(i) == 1 && vectr.get(i + 2) - vectr.get(i + 1) == 1 && vectr.get(i + 3) - vectr.get(i + 2) == 1)
			{
				return (vectr.get(i + 3) >= scheme.getFigure(0).getCountFigure()) ? vectr.get(i + 3) - scheme.getFigure(0).getCountFigure() : vectr.get(i + 3);
			}
		}
		return -1;
	}
	/**
	 * Метод isNearlyGoodHandle говорит, является ли найденая ручка вида 3+1(aba-1...b-1),
	 * т.е. 3 веришины подряд, а одна отдельно от них
	 * @param begin инициализируется значением индекс начала тройки вершин
	 * @param end инициализируется значением индексом вершины которая отдельна от тройки
	 * @return true - если это 3+1; false - любого другого вида
	 */
	private boolean isNearlyGoodHandle(AtomicInteger begin,AtomicInteger end)
	{
		Vector<Integer> vectr = new Vector<Integer>();
		vectr.add(indexFirstEdge);
		vectr.add(indexFirstEdge2);
		vectr.add(indexSecondEdge);
		vectr.add(indexSecondEdge2);
		vectr.add(indexFirstEdge + scheme.getFigure(0).getCountFigure() );
		vectr.add(indexFirstEdge2 + scheme.getFigure(0).getCountFigure());
		vectr.add(indexSecondEdge + scheme.getFigure(0).getCountFigure());
		vectr.add(indexSecondEdge2 + scheme.getFigure(0).getCountFigure());
		
		for(int i = 0; i < vectr.size() - 2; i++)
		{
			if(vectr.get(i + 1) - vectr.get(i) == 1 && vectr.get(i + 2) - vectr.get(i + 1) == 1)
			{
				begin.set(vectr.get(i ));
				end.set( (vectr.get(i + 3) >= scheme.getFigure(0).getCountFigure()) ? vectr.get(i + 3) - scheme.getFigure(0).getCountFigure() : vectr.get(i + 3) );
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Метод cutHandle делает разрез на 2 многоугольника между невыделнной ручкой
	 * @return
	 */
	private String cutHandle()
	{		
		polygon = (PolyNew) scheme.getFigure(0);
		PolyNew newPolygon = new PolyNew();	
		AtomicInteger  beginPos = new AtomicInteger(); // см. метод isNearlyGoodHandle
		AtomicInteger  endPos = new AtomicInteger(); // см. метод isNearlyGoodHandle
		int beg = 0,end = 0; // переменные начала и конца нового многоугольника
		int correction; // корректирующий параметр

		//если не 3+1, то мы можем резакать как хотим(разрезаем по 1ым вершинам)
		if (!isNearlyGoodHandle(beginPos,endPos))
		{
			beg = indexFirstEdge;
			end = indexSecondEdge;
		}
		else 
		{
			// случай с 3+1
			// делаем сдвиг по beginPos и заменяем нашу схему, чтобы началом многоугольника было начало ручки
			PolyNew shiftedPolygon = ((PolyNew)this.scheme.getFigure(0)).shiftPoly(beginPos.get(), true);
			scheme.removeFigure(0);
			scheme.addFigure(0, shiftedPolygon);
			polygon = (PolyNew) scheme.getFigure(0);
			
			beg = 1; // резать должны 2ое ребро в ручке
			end = endPos.get() + polygon.getCountFigure() - beginPos.get(); // определяется позиция второго ребра
			if( end >= polygon.getCountFigure() ) end -= polygon.getCountFigure();			
		}
		
		if(polygon.getEdge(beg).getOrientation()) // резать на конец 2ого ребра
		{
			correction = 1;
		}
		else
		{
			correction = 0;
		}
		for(int i = beg + correction ; i <= end - correction; i++) // собираем новый многоугольник
		{			
			newPolygon.addFigure(polygon.getEdge(i));
		}		
		
		worker = new TwoDimensionalUnit(this.scheme, newPolygon); 		
		worker.run(); // разрезаем!
		action++;
		state = States.Action;
		return "";
	}
	
	private String mergeHandle() 
	{				
		worker = new TwoDimensionalConsolidation(this.scheme, 0,1);
		worker.run();
		action = 0;
		state = States.Action;
		return "";
	}
	
}
