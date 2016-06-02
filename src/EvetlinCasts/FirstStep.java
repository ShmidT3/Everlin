package EvetlinCasts;

import EverlinObjects.Scheme;

public class FirstStep extends ElementaryCast {
	private TwoDimensionalConsolidation myCast;
	private int index1 = 0;
	private int index2 = 0;

	
	public FirstStep(Scheme scheme) {
		super(scheme);
		nameEC = "Шаг 1";
		action = 1;
		isStep = true;
		state = States.Unknown;
	}
	
	@Override
	public int step()
	{
		// реализация конечного автомата
		switch (this.state)
		{
		case Unknown:
			addLog("Начало работы.");
		case Init:
			if(scheme.getCountFigure() == 1) 
			{	
				nextClass = SecondStep.class;
				state = States.Finish;
				addLog("Выполнен!");
				return 1;
			}
			index1 = 0;
			index2 = 1;
			myCast = new TwoDimensionalConsolidation(scheme, index1,index2);
			state = States.Action;
			myCast.step();
			break;
		case Action:
			switch (myCast.getState()) 
			{
			case Init:
			case BadAction: // нет общих ребер! пробуем следующий многоугольник
				if(index2 < scheme.getCountFigure() - 1)
				{
					index2++; // смотрим следующий многоугольник
				}
				else if (index1 < scheme.getCountFigure() - 2)
				{
					index2 = ++index1;
					index2++; // для полного перебора значений достаточно смотреть многоугольники следующие за index1, т.к остальные уже просматривались
				}
				else
				{
					state = States.BadAction; // неоднозначная ситуация
					return step();
				}
				myCast.init(index1, index2);
				myCast.step();
				break;
			case Unknown:
				addLog("Чтото пошло не так! ");
				return -1;
			case Action: // нашли общие ребра, выполняем ЭП. 
				myCast.step();
				break;
			case Finish: // удачно выполнили преобразование, начинаем заного, чтобы не потерять многоугольники
				state = States.Init;
				return step();
			default:
				break;
			}
			break;
		case BadAction:
			if(scheme.getCountFigure() == 1) 
			{	
				nextClass = SecondStep.class;
				state = States.Finish;
				addLog("Выполнен!");
				return 1;
			}
			else
			{
				state = States.BadAction;
				addLog("Многоугольники не связанны!");
				return -1;	
			}
		case Finish:
			nextClass = SecondStep.class;
			addLog("Выполнен!");
			return 1;
		default:
			break;
		}
		return 0;
	}
}
