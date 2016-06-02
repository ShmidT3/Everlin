package EvetlinCasts;

import javax.swing.JOptionPane;

import EverlinFrame.MainFrame;
import EverlinObjects.Scheme;

/**
 * Класс описывает логику 4 шага алгоритма
 *
 */
public class FourthStep extends ElementaryCast {
	private ElementaryCast ess;
	public FourthStep(Scheme scheme) {
		super(scheme);
		ess = new ElementaryFourthStep(scheme);
		nameEC = "Шаг 4";
		isStep = true;
		state = States.Init;
		addLog("Начало работы.");
	}

	public int step()
	{
		ess.step();
		switch (ess.getState()) {
		case BadAction:
		case Finish:
			nextClass = FifthStep.class;
			state = States.Finish;
			addLog("Выполнен!");
			return 1;
		case Exit:
			state = States.Exit;
			ess = new Classifier(scheme);
			ess.run();
			String cls = ((Classifier) ess).getClassfier();
			addLog("Выполнен! Система приведена к каноническому виду!");
			JOptionPane.showMessageDialog(null,
					"Алгоритм выполненен! Система приведена к каноническому виду!\r\n" + cls,
					MainFrame.titleWindow + ": Сообщение",
					JOptionPane.INFORMATION_MESSAGE);
			return -4;				
		default:
			state = States.Action;
			break;
		}
		return 0;
	}
}
