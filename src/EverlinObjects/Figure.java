package EverlinObjects;

import java.awt.Graphics;
import java.util.Vector;


/**
 * Класс Figure является базовым классом для описания всех элементов схемы многоугольников.
 * Содержит в себе массив объектов Figure.
 * Методы get,add,remove помимо своего смыслового значения выполняют роль закольцованности для элементов.
 */
public abstract class Figure {
	protected Vector<Figure> v_Figure = new Vector<Figure>();
	protected int countFigure;
	protected int index;
	
	public Figure()
	{
		index = countFigure = 0;	
	}
	
	public Figure(Figure figure)
	{
		this.v_Figure = new Vector<Figure>(figure.v_Figure);
		this.index = figure.index;
		countFigure = v_Figure.size();
	}
		
	public void addFigure(int index, Figure figure)
	{
		if( index >= countFigure) 
		{
			addFigure(figure);
		}
		else
		{
			countFigure++;
			v_Figure.add(index, figure);
		}
	}
	
	public void addFigure(Figure figure)
	{
		countFigure++;
		v_Figure.add(figure);
	}
	
	public void removeFigure(int index)
	{
		if (index >= countFigure) index = 0; 
		countFigure--;
		v_Figure.remove(index);
		refreshIndexes();
	}

	public final int getCountFigure()
	{
		return countFigure;	
	}
	
	public void draw(Graphics g)
	{		
		for (int i = 0; i < countFigure; i++)
		{	
			v_Figure.get(i).draw(g);
		}
	}
	
	protected abstract String generatedHeaderToString();
	
	public final int getIndex()
	{
		return index;
	}
	
	public final void setIndex(int index)
	{
		this.index = index;
	}
	
	public final Figure getFigure(int i)
	{
		if( i >= countFigure ) i-=countFigure;
		if( i < 0 ) i+=countFigure;
		return v_Figure.get(i);
	}
	
	protected void refreshIndexes()
	{
		for(int i = 0; i < countFigure; i++)
		{
			v_Figure.get(i).setIndex(i);
		}
	}
	
	protected void reset()
	{
		v_Figure.removeAllElements();
		countFigure = 0;
	}
	
	@Override
	public String toString()
	{
		String retVal = generatedHeaderToString();
		for (int i = 0; i < countFigure; i++)
		{	
			retVal += v_Figure.get(i) + "\r\n";
		}
		return retVal;
	}
}
