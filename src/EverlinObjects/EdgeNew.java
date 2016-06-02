package EverlinObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class EdgeNew extends Figure {
	private Point a1;
	private Point a2;
	private Vertex leftVertex;
	private Vertex rightVertex;
	private String label;
	private boolean orientation; 
	private final static int arrowLength = 15;
	private final static double beta = 15 * (Math.PI / 180);
	private Color color = Color.BLACK;
	private int countUsedEdge = 0;
	
	
	public EdgeNew(String  label, boolean isMin)
	{
		a1 = new Point(0,0);
		a2 = new Point(0,0);
		this.label = label;
		this.orientation = isMin;
	}
	public EdgeNew(EdgeNew e)
	{
		a1 = new Point(0,0);
		a2 = new Point(0,0);
		this.label = e.label;
		this.orientation = e.orientation;
		this.color = Color.BLACK;
		this.countUsedEdge = e.countUsedEdge;
	}
	
	public EdgeNew(EdgeNew e,Color c)
	{
		a1 = new Point(0,0);
		a2 = new Point(0,0);
		this.label = e.label;
		this.orientation = e.orientation;
		this.color = c;
	}
	
	public EdgeNew(String  label, boolean isMin,Color color)
	{
		a1 = new Point(0,0);
		a2 = new Point(0,0);
		this.label = label;
		this.orientation = isMin;
		this.color = color; 
	}
	public EdgeNew(double x1,double y1,double x2,double y2, String  label, boolean orientation)
	{
		a1 = new Point((int)x1,(int)y1);
		a2 = new Point((int)x2,(int)y2);
		this.label = label;
		this.orientation = orientation;
	}
	public EdgeNew(double x1,double y1,double x2,double y2, String  label, boolean isMin, Color color,int countUsedEdge)
	{
		a1 = new Point((int)x1,(int)y1);
		a2 = new Point((int)x2,(int)y2);
		this.label = label;
		this.orientation = isMin;
		this.color = color;
		this.countUsedEdge = countUsedEdge;
	}
	
	public int getCountUsedEdge()
	{
		return countUsedEdge;
	}
	
	/**
	 * Метод выполняет перекрашивание ребера
	 * Если 1ый раз вызывается, то присваивается оранжевый цвет
	 * Если 2ой, то красный
	 * Во всех остальных случаях зеленый
	 * Используется для визуализации ЭП "Поиск классов эквиволентности"
	 */
	public void use()
	{
		countUsedEdge++;
		if(countUsedEdge == 1)
		{
			this.color = Color.ORANGE;
		}
		else if(countUsedEdge == 2)
		{
			this.color = Color.RED;
		}
		else
		{
			this.color = Color.GREEN;
		}			
	}
	
	public void resetCountUsedEdge()
	{
		countUsedEdge = 0;
	}
	
	public boolean getOrientation()
	{
		return orientation;
	}
	
	public String getLabel()
	{		
		return label;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
		
	public String getFullLabel()
	{
		return label + ((orientation) ? "" : "-1");
	}
	
	/**
	 * Устанавливает ссылки на смежные вершины
	 * @param leftVertex - левая(относительно центра многоугольника) смежная вершина
	 * @param rightVertex - правая(относительно центра многоугольника) смежная вершина
	 */
	public void setVertexes(Vertex leftVertex,Vertex rightVertex)
	{
		this.leftVertex = leftVertex;
		this.rightVertex = rightVertex;
	}
	
	/**
	 * Функция возващает вершину с которой начинается ребро (в зависимости от ориентации)
	 * и прописывает для вершины, c какой стороны у нас ребро(isLeftLastEdge)
	 * @return Vertex - начало ребра(вершина)
	 */
	public Vertex getBeginVertex()
	{
		return (orientation) ? leftVertex.setIsLeftLastEdge(false) : rightVertex.setIsLeftLastEdge(true); // прописываем для вершины, c какой стороны у нас ребро
	}
	
	/**
	 * Функция возващает вершину на которой закачивается ребро (в зависимости от ориентации)
	 * и прописывает для вершины, c какой стороны у нас ребро(isLeftLastEdge)
	 * @return Vertex - конец ребра(вершина)
	 */
	public Vertex getEndVertex()
	{
		return (orientation) ? rightVertex.setIsLeftLastEdge(true) : leftVertex.setIsLeftLastEdge(false) ; // прописываем для вершины, c какой стороны у нас ребро
	}
	
	public void drawLabel(Graphics g,Point center)
	{
		g.drawString(getLabel()+ ((orientation) ? "" : "-1"),a1.x + a2.x - center.x,a1.y + a2.y - center.y);
	}
	
	@Override
	protected String generatedHeaderToString() {
		return "";
	}
	
	@Override
	public String toString() {
		return "		" + "Point(" + a1.x + "," + a1.y+ ")" + " " + "Point(" + a1.x + "," + a1.y+ ")" + " " + label + " " + orientation;
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
		EdgeNew tmp = (EdgeNew)anObject;
		return (tmp.label.equals(this.label) && (this.orientation == tmp.orientation));		
	}
	
	public Point getFirstPoint()
	{
		return a1;
	}
	
	public Point getEndPoint()
	{
		return a2;
	}

	
	@Override
	public void draw(Graphics g) {
		{	
			g.setColor(color);
			g.drawLine(a1.x,a1.y,a2.x,a2.y);
					
			Point arrowPoint1,arrowPoint2;
			double angle;
			if(!orientation)
			{
				arrowPoint1 = new Point(a1);
				arrowPoint2 = new Point(a2);
			}
			else
			{
				arrowPoint1 = new Point(a2);
				arrowPoint2 = new Point(a1);
			}
			angle = Math.atan2(arrowPoint1.y - arrowPoint2.y,arrowPoint1.x - arrowPoint2.x);
			g.drawLine(arrowPoint1.x,arrowPoint1.y,(int)(arrowPoint1.x - arrowLength * Math.cos( angle - beta)),(int)(arrowPoint1.y - arrowLength * Math.sin( angle - beta)));
			g.drawLine(arrowPoint1.x,arrowPoint1.y,(int)(arrowPoint1.x - arrowLength * Math.cos( angle + beta)),(int)(arrowPoint1.y - arrowLength * Math.sin( angle + beta)));
		}

	}

}
