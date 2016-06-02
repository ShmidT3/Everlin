package EverlinObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Vertex extends Figure {
	private Point a1;
	private Color color;
	private boolean isNeedTakeBeginPositionEdge = false; // нужно брать начало или конец ребра, для поиска классов экв. 
	private boolean isLeftLastEdge = false; // с помощью левого или правого ребра мы попали в эту вершину, для поиска классов экв. 
	private int countUsedVertex = 0;
	
	public Vertex(EdgeNew leftEdge,EdgeNew rightEdge,int index)
	{
		addFigure(leftEdge);		
		addFigure(rightEdge);
		this.index = index;
		this.a1 = leftEdge.getEndPoint();
		this.color = Color.BLACK;
	}
	public Vertex(EdgeNew leftEdge,EdgeNew rightEdge,int index,boolean isNeedTakeBeginPositionEdge,boolean isLeftLastEdge,int countUsedVertex,Color color)
	{
		addFigure(leftEdge);		
		addFigure(rightEdge);
		this.index = index;
		this.a1 = leftEdge.getEndPoint();
		this.isNeedTakeBeginPositionEdge = isNeedTakeBeginPositionEdge;
		this.isLeftLastEdge = isLeftLastEdge;
		this.countUsedVertex = countUsedVertex;
		this.color = color;
	}
	public Vertex(Vertex vertex)
	{		
		this.isNeedTakeBeginPositionEdge = vertex.isNeedTakeBeginPositionEdge;
		this.isLeftLastEdge = vertex.isLeftLastEdge;
	}
	
	public Vertex(double x1,double y1, Color color)
	{
		a1 = new Point((int)x1,(int)y1);
		this.color = color;
	}
	
	/**
	 * Метод выполняет перекрашивание вершины и одного из смежных ребер
	 * Если 1ый раз вызывается, то присваивается оранжевый цвет
	 * Если 2ой, то красный
	 * Во всех остальных случаях зеленый
	 * Используется для визуализации ЭП "Поиск классов эквиволентности"
	 */
	public void use()
	{
		getAdjEdge().use();
		countUsedVertex++;
		if(countUsedVertex == 1)
		{
			this.color = Color.ORANGE;
		}
		else if(countUsedVertex == 2)
		{
			this.color = Color.RED;
		}
		else
		{
			this.color = Color.GREEN;
		}			
	}
	
	/**
	 * Функция возвращает ребро находящееся справо или слево от вершины,
	 * в зависимости от того, как мы попали в эту вершину(isLeftLastEdge),
	 * и определяет isNeedTakeBeginPositionEdge
	 * @return EdgeNew - смежное ребро
	 */
	public EdgeNew getAdjEdge()
	{
		if (isLeftLastEdge) 
		{
			isNeedTakeBeginPositionEdge = getRightEdge().getOrientation();
			return getRightEdge();
		}
		else
		{
			isNeedTakeBeginPositionEdge = !getLeftEdge().getOrientation();
			return getLeftEdge();
		}
	}
	
	public Vertex setIsLeftLastEdge(boolean val)
	{
		this.isLeftLastEdge = val;
		getAdjEdge(); // выполняется для правильной инициализации isNeedTakeBeginPositionEdge
		return this;
	}
	
	public boolean getIsLeftLastEdge()
	{
		return isLeftLastEdge;
	}
	
	public int getCountUsedVertex()
	{		
		return countUsedVertex;
	}
	
	public boolean getIsNeedTakeBeginPositionEdge()
	{
		return isNeedTakeBeginPositionEdge;
	}
	
	/**
	 * Возваращает ребро, которое находится слева от вершины, если смотреть из центра многоугольника
	 * @return EdgeNew - левое смежное ребро
	 */
	public EdgeNew getLeftEdge()
	{
		return (EdgeNew) getFigure(0);
	}
	
	/**
	 * Возваращает ребро, которое находится справа от вершины, если смотреть из центра многоугольника
	 * @return EdgeNew - правое смежное ребро
	 */
	public EdgeNew getRightEdge()
	{
		return (EdgeNew) getFigure(1);
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void resetCountUsedVertex()
	{
		countUsedVertex = 0;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
		
	public void drawLabel(Graphics g,Point center)
	{
		g.drawString("" + (index),a1.x + 5,a1.y - 5);
	}
	@Override
	protected String generatedHeaderToString() {
		return "";
	}
	@Override
	public String toString(){
		return "		" + index + " "+ ((EdgeNew)(v_Figure.get(0))).getFullLabel() + " " + ((EdgeNew)(v_Figure.get(1))).getFullLabel();
	}
	@Override 
	public int hashCode() 
	{
		assert false : "hashCode not designed";
		return 11;
	}
	
	@Override
	public boolean equals(Object anObject)
	{
		if (anObject == null) return false;
		Vertex tmp = (Vertex)anObject;
		return (tmp.index == this.index);		
	}
		
	@Override
	public void draw(Graphics g) {
		{	
			g.setColor(color);
			g.drawOval(a1.x,a1.y, 6, 6);
		}

	}

}
