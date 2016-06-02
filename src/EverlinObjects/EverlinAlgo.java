package EverlinObjects;

import java.awt.Graphics;
import java.lang.reflect.Constructor;
import java.util.Vector;
import javax.swing.JOptionPane;
import EverlinFrame.MainFrame;
import EverlinFrame.TableItemDomain;
import EvetlinCasts.ElementaryCast;
import EvetlinCasts.FirstStep;
import EvetlinCasts.IStepByStep;



public class EverlinAlgo implements IStepByStep {
	private int step = 0;
	boolean isFirst = true;
	private Graphics graph;
	private final Scheme scheme = new Scheme();
	private ElementaryCast ec; 
	public int step()
	{
		try
		{
			if (!ec.getNextClass().getName().equals(ec.getClass().getName()))
			{
				Class<?> cls = Class.forName(ec.getNextClass().getName());
	            Constructor<?> constructor = cls.getConstructor(Scheme.class);
				ec = (ElementaryCast) constructor.newInstance(scheme);					
			}   
			step += ec.step();			
			scheme.isValid();
			if(ec.isFinished()) return -1;
			return step;
		} 
		catch(Error e)
		{
			JOptionPane.showMessageDialog(null,
				e.getMessage(),
				MainFrame.titleWindow + ": Ошибка!",
				JOptionPane.ERROR_MESSAGE);
			return -1;
		}
		catch (Throwable e) {
            e.printStackTrace();
            return -1;
        } 
	}
	
	@Override
	public int run()
	{				
		while(step() != 6 && !ec.isFinished())
		{
			draw(graph);
		}
		return -1;
	}
	
	public int init(Vector<String> polygons)
	{
		reset();
		ec = new FirstStep(scheme);
		return scheme.create(polygons);
	}
	
	public Vector<TableItemDomain> getSchmemeText()
	{
		Vector<TableItemDomain> retVal = new Vector<TableItemDomain>();
		for(int i = 0; i < scheme.getCountFigure(); i++)
		{
			TableItemDomain item = new TableItemDomain();
			item.setRow(i);
			item.setPolyString(((PolyNew)scheme.getFigure(i)).getScheme());
			retVal.add(item);
		}	
			
		return retVal;
	}
	
	public void draw(Graphics g)
	{
		graph = g;
		System.out.println(scheme.toString());
		scheme.draw(graph);		
	}
	
	private void reset()
	{
		step = 0;
		scheme.reset();
		ec = null;
	}
}
