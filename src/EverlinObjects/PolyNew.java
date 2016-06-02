package EverlinObjects;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;


/**
 *	Класс PolyNew описывает поведение многоугольника.
 */
public class PolyNew extends Figure {
	private Vector<Vertex> v_Vertexes = new Vector<Vertex>();
	private double r;
	private double ang;
	private double x0,y0;
	private int countPol = 0;
	private static double heightKoeff = (3) / 8.0;	
	
	public PolyNew()
	{
		super();
		x0 = y0 = ang = r = 0.0;
	}	
	
	public PolyNew(Vector<String> scheme , int ind ,int countPol)
	{
		super();
		this.index = ind;
		this.countPol = countPol;
		caclulateHeightKoeff();
		generatePoly(scheme);
	}
	
	public PolyNew(PolyNew poly)
	{
		super(poly);
		this.ang = poly.ang;
		this.r = poly.r;
		this.x0 = poly.x0;
		this.y0 = poly.y0;
		this.countPol = poly.countPol;
	}
	/**
	 * Метод создаёт вершины для текущего многоугольника
	 * и создаёт ссылки для ребер на вершины и на оборот
	 */	
	private void regenerateVertexes()
	{
		@SuppressWarnings("unchecked")
		Vector<Vertex> duplicate = (Vector<Vertex>) v_Vertexes.clone();
		v_Vertexes.removeAllElements();
		for(int i = 0; i < countFigure; i++)
		{
			v_Vertexes.add(new Vertex(getEdge(i),
							getEdge(i+1),
							i,
							duplicate.get(i).getIsNeedTakeBeginPositionEdge(),
							duplicate.get(i).getIsLeftLastEdge(),
							duplicate.get(i).getCountUsedVertex(),
							duplicate.get(i).getColor()));			
		}
		for(int i = 0; i < v_Vertexes.size(); i++)
		{
			getEdge(i).setVertexes(getVertex(i - 1), getVertex(i));		
		}
		duplicate.removeAllElements();
	}
	
	/**
	 * Метод создаёт вершины для текущего многоугольника
	 * и создаёт ссылки для ребер на вершины и на оборот
	 */		
	private void generateVertexes()
	{
		v_Vertexes.removeAllElements();
		for(int i = 0; i < countFigure; i++)
		{
			v_Vertexes.add(new Vertex(getEdge(i),getEdge(i+1),i));			
		}
		for(int i = 0; i < v_Vertexes.size(); i++)
		{
			getEdge(i).setVertexes(getVertex(i - 1), getVertex(i));		
		}
	}

	/** 
	 * Метод contains дает ответ, содержится мноугольник poly в текущем.
	 * @param poly - многоугольник
	 * @return  k - индекс на котором встретился искомый многоугольник(poly).
	 * 		   (-1) - не найден poly
	 * 		   (-2) - вводимый многоугольник не прошел проверку (poly.getCountFigure() <= countFigure && countFigure > 2)
	 */
	public int contains(PolyNew poly)
	{
		boolean isContainsPoly = true;
		boolean isContainsShiftPoly = true;
		PolyNew revPoly = poly.shiftPoly(-1, false);
		if (countFigure > 2 && poly.getCountFigure() <= countFigure)
		{			
			for(int i = 0; i < countFigure ; i++)
			{
				for (int j = 0; j < poly.getCountFigure(); j++ )
				{
					isContainsPoly 		&= this.getFigure(i + j).equals(poly.getEdge(j));
					isContainsShiftPoly &= this.getFigure(i + j).equals(revPoly.getEdge(j));
				}
				if (isContainsPoly)
				{
					return i;
				}
				if (isContainsShiftPoly)
				{
					poly.reset();
					poly.generatePoly(revPoly.getSchemeV()); 
					return i;
				}
				isContainsPoly = true;
				isContainsShiftPoly = true;
			}
		}		
		else
		{
			return -2;
		}
		return -1;
	}
	
	/**
	 * Возващает описание многоугольника в строковом виде.
	 * @return String - результирующая строка.
	 */
	public String getScheme()
	{
		String retVal = "";
		for(int i = 0; i < countFigure; i++)
		{
			retVal += ((EdgeNew)v_Figure.get(i)).getFullLabel();			
		}
		return retVal;
	}
	
	private Vector<String> getSchemeV()
	{
		Vector<String> retVal = new Vector<String>();
		for(int i = 0; i < countFigure; i++)
		{
			retVal.add(((EdgeNew)v_Figure.get(i)).getFullLabel());			
		}
		return retVal;
	}
	
	public void setCountPolygons(int count)
	{
		countPol = count;
	}
	
	/**
	 * Удваивает текущий многоугольник 
	 */
	public void doublePoly() 
	{
		int count = countFigure;
		for(int i = 0; i < count; i++)
			this.addFigure(v_Figure.get(i));
	}
	
	/**
	 * Выполняет замену ребра с индексом j на набор ребер a_Edges.
	 * @param j - индекс по которому будет удалено ребро. 
	 * @param a_Edges - Ребра на которое будет заменено ребро.
	 */	
	public void subEdges(int j,ArrayList<EdgeNew> a_Edges)
	{
		removeFigure(j);
		for(int index = 0; index < a_Edges.size(); index++)
		{
			addFigure(j + index,a_Edges.get(index) );
		}
	}
	/**
	 * Выполняет замену набор ребер с началом в j и длинной length на набор ребер a_Edges.
	 * Пример. Для "aa-1bb-1cc-1" делаем вызов subEdges(0,4,"dede" - для упрощения) после выполнениея получим "dedecc-1".
	 * Учитывает закольцованность полигона.
	 * @param j - индекс по которому будет определяться начало заменяемого набора ребер.
	 * @param length - длина заменяемого набора ребер
	 * @param a_Edges - набор ребер
	 */
	public void subEdges(int j,int length,ArrayList<EdgeNew> a_Edges)
	{
		if( countFigure - length + a_Edges.size() < 2)
			throw new Error("Количество ребер в многоугольнике не должно быть меньше 2!");
		for(int i = 0;i < length; i++) 
			removeFigure(j);
		for(int index = 0; index < a_Edges.size(); index++)
		{
			addFigure(j + index,a_Edges.get(index) );
		}
	}
	/**
	 * Создает многоугольник для которого был выполнен сдвиг на (countEdge + 1 - index) с выбранной ориентацией. 
	 * @param index - индекс с которого будет начинается многоугольник.
	 * @param orientation - направление сдвига, ориентация при сдвига. 1 - право. 0 - лево.
	 * @return newPoly
	 */
	public PolyNew shiftPoly(int index,boolean orientation)
	{
		PolyNew newPoly = new PolyNew();
		if (index >= 0 || index < v_Figure.size())
		{
			if (orientation)
			{
				for(int i = index; i < v_Figure.size(); i++)
				{
					newPoly.addFigure(v_Figure.get(i));
				}
				for(int i = 0; i < index; i++)
				{
					newPoly.addFigure(v_Figure.get(i));
				}
			}
			else
			{
				for(int i = index; i >= 0; i--)
				{
					newPoly.addFigure(new EdgeNew(((EdgeNew)v_Figure.get(i)).getLabel(),!((EdgeNew)v_Figure.get(i)).getOrientation()));
				}
				for(int i = v_Figure.size() - 1 ; i > index; i--)
				{
					newPoly.addFigure(new EdgeNew(((EdgeNew)v_Figure.get(i)).getLabel(),!((EdgeNew)v_Figure.get(i)).getOrientation()));
				}

			}
		}
		return newPoly;
	}

	/**
	 * Пересоздание содержимого многоугольника для корректного отображения ребер.
	 * @return 1
	 */
	private int regeneratePoly()
	{	
		caclulateHeightKoeff();
		x0 = Scheme.panelWidth * ((2 * index + 1)/ ( 2.0 * countPol));
		y0 = Scheme.panelHeight / 2;
		countFigure = v_Figure.size();
		r = heightKoeff * (Scheme.panelWidth) / (1.0 * countPol);
		r = (r > y0) ? y0 : r;
		ang = (2 * Math.PI ) / countFigure;
		
		for(int i = 0; i < countFigure; i++)
		{
			v_Figure.set(i,createEdge((EdgeNew)v_Figure.get(i),i));
		}
		regenerateVertexes();
		return 1;
	}
	
	/**
	 * Создание многоугольника.  
	 * @param scheme - входная строка описывающая многоугольник в строчном формате.
	 */
	private void generatePoly(Vector<String> scheme)
	{	
		x0 = Scheme.panelWidth * ((2 * index + 1)/ ( 2.0 * countPol));
		y0 = Scheme.panelHeight / 2;
		countFigure = scheme.size();
		r = heightKoeff * (Scheme.panelWidth) / ( 1.0 * countPol);
		r = (r > y0) ? y0 : r;
		ang = (2 * Math.PI ) / countFigure;
		
		for(int i = 0; i < countFigure; i++)
		{
			v_Figure.add(createEdge(scheme.get(i),i));
		}
		generateVertexes();
	}

	/**
	 * Создание нового ребра со свежими параметрами.
	 * @param edge - входное ребро.
	 * @param ind - индекс нового ребра в массиве v_Figure.
	 * @return Новое ребро. 
	 */
	private EdgeNew createEdge(EdgeNew edge,int ind)
	{
		return new EdgeNew(x0 + r * (Math.cos(ind * ang)),
						   y0 + r * (Math.sin(ind * ang)),
						   x0 + r * (Math.cos(((ind == countFigure - 1) ? 0 : ind + 1) * ang)),
						   y0 + r * (Math.sin(((ind == countFigure - 1) ? 0 : ind + 1) * ang)),
						   edge.getLabel(),
						   edge.getOrientation(),
						   edge.getColor(),
						   edge.getCountUsedEdge());
	}
	
	/**
	 * Cоздание нового ребра из строкового представления edgeString
	 * @param edgeString - строка описывающая ребро.
	 * @param ind - индекс нового ребра в массиве v_Figure.
	 * @return Новое ребро
	 */
	private EdgeNew createEdge(String edgeString,int ind)
	{
		String label = "";
		boolean orientation = true;

		if(edgeString.length() == 1)
		{				
			label = edgeString;
			orientation = true;
		} else if (edgeString.length() == 3)
		{				
			label = "" + edgeString.charAt(0);
			orientation = false;
		}
		return new  EdgeNew(x0 + r * (Math.cos(ind * ang)),
						 y0 + r * (Math.sin(ind * ang)),
						 x0 + r * (Math.cos(((ind == countFigure - 1) ? 0 : ind + 1) * ang)),
						 y0 + r * (Math.sin(((ind == countFigure - 1) ? 0 : ind + 1) * ang)),
						 label,
						 orientation);
	}
	
	@Override
	public void draw(Graphics g)
	{	
		caclulateHeightKoeff();
		regeneratePoly();
		for (int i = 0; i < countFigure; i++)
		{				
			v_Figure.get(i).draw(g);
			((EdgeNew)v_Figure.get(i)).drawLabel(g,getCenterPoly());
			v_Vertexes.get(i).draw(g);
			v_Vertexes.get(i).drawLabel(g,getCenterPoly());
			
		}
	}
	
	public Vertex getVertex(int index)
	{
		if( index >= countFigure )index-=countFigure;
		if( index < 0 ) index+=countFigure;
		return v_Vertexes.get(index);
	}
	
	private void caclulateHeightKoeff()
	{
		heightKoeff = ((countPol > 3)? 3 : countPol)/8f ;
	}
	
	public Point getCenterPoly()
	{
		return new Point((int)x0,(int)y0);
	}
	
	public EdgeNew getEdge(int i)
	{
		return (EdgeNew)getFigure(i);
	}
	
	@Override
	protected String generatedHeaderToString() {
		return "	Полигон № " + index + "; Центр(" + x0 + "," + y0 + "); Угол: " + ang + "; Радиус: " + r + " Количество ребер: " + countFigure + ";\r\n	Ребра:\r\n";
	};
	
	@Override
	public String toString()
	{
		String retVal = generatedHeaderToString();
		for (int i = 0; i < countFigure; i++)
		{	
			retVal += v_Figure.get(i) + "\r\n" + v_Vertexes.get(i) + "\r\n";
		}
		return retVal;
	}
	@Override
	protected void reset()
	{
		super.reset();
		v_Vertexes.removeAllElements();
	}
	
	@Override
	public  void addFigure(int index, Figure figure)
	{
		super.addFigure(index, figure);
		generateVertexes();
	}
	
	@Override
	public void addFigure(Figure figure)
	{
		super.addFigure(figure);
		generateVertexes();		
	}
	
	@Override
	public void removeFigure(int index)
	{
		super.removeFigure(index);
		generateVertexes();
	}
}
