package EvetlinCasts;

import EverlinObjects.Scheme;

/**
 * Класс описывает логику 3 шага алгоритма
 *
 */
public class ThirdStep extends ElementaryCast {
	private ElementaryCast ec;
	
//	private PolyNew polygon;
//	private int startIndex = 0;
	
	public ThirdStep(Scheme scheme) {
		super(scheme);
		nameEC = "Шаг 3";
		ec = new FindEquivalentClasses(scheme);
		isStep = true;
		state = States.Init;
		addLog("Начало работы.");
	}
	
	public int step()
	{	
		ec.step();
		switch (ec.getState()) {
		case BadAction:
		case Finish:
			if (ec instanceof FindEquivalentClasses)
			{
				ec = new ElementaryThirdStep(scheme,((FindEquivalentClasses)ec).getMyEquivalentClass());
			}
			else if(ec instanceof ElementaryThirdStep)
			{
				ec = new SecondStep(scheme);
				ec.run();
				if (ec.getState() == States.Exit)
				{
					state = States.Exit;
					return 1;
				}
				ec = new FindEquivalentClasses(scheme);				
			}
			
			state = States.BadAction;
			return 0;

		case Exit:
			nextClass = FourthStep.class;
			lastAction();
			state = States.Finish;
			addLog("Выполнен!");
			return 1;
		default:
			state = States.Action;
			break;
		}
		return 0;
	}
}
