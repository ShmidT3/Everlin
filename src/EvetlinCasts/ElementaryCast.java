package EvetlinCasts;

import java.awt.Color;

import EverlinFrame.MainFrame;
import EverlinObjects.PolyNew;
import EverlinObjects.Scheme;

public abstract class ElementaryCast implements IStepByStep {
	protected States state = States.Unknown;
	protected int action = 0;
	protected int countAction;
	protected String nameEC;
	protected Class<? extends ElementaryCast> nextClass;
	protected final Scheme scheme;
	protected boolean isStep = false;
	private final static String tab = "\t";
	
	public ElementaryCast(Scheme scheme)
	{
		state = States.Init;
		this.scheme = scheme;
		nextClass = this.getClass();
		countAction = 0;
	}
	
	public abstract int step();
	
	@Override
	public int run()
	{		
		action = 0;
		state = States.Init;
		while(!(action == -1 || state == States.Finish || state == States.Exit) ) step();
		return 0;
	}
	
	protected final States getState()
	{
		return state;
	}
	
	public Class<? extends ElementaryCast> getNextClass()
	{
		return nextClass;
	}
	
	/**
	 * Является завершающей функцией для всех ЭП
	 * Функция подкрашивает все ребра в исходный цвет(черный) и устанавливает action = -1 и state = States.Finish;
	*/
	protected final String lastAction() 
	{
		for(int i = 0; i < scheme.getCountFigure(); i++)
		{
			for( int j = 0; j < scheme.getFigure(i).getCountFigure(); j++ )
			{
				((PolyNew) scheme.getFigure(i)).getVertex(j).resetCountUsedVertex();
				((PolyNew) scheme.getFigure(i)).getEdge(j).resetCountUsedEdge();
				((PolyNew) scheme.getFigure(i)).getEdge(j).setColor(Color.BLACK);
				((PolyNew) scheme.getFigure(i)).getVertex(j).setColor(Color.BLACK);
			}
		}
		action = -1;
		state = States.Finish;
		return "Шаг " + countAction + " из " + countAction + " (Завершение работы)";
	}
	
	public boolean isFinished()
	{
		return this.state == States.Exit;
	}
	protected final void addLog(String text)
	{
		if("".equals(text)) return;
		String resultString = "";
		resultString += ((isStep) ? "" : tab);
		resultString += nameEC + ": " + text;
		MainFrame.addRowLog(resultString);
	}
}
