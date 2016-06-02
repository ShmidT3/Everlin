package EvetlinCasts;

import javax.swing.JOptionPane;

import EverlinFrame.MainFrame;
import EverlinObjects.Scheme;

/**
 * Класс описывает логику 6 шага алгоритма
 *
 */
public class SixthStep extends ElementaryCast {
	private ElementaryCast ess;
	public SixthStep(Scheme scheme) {
		super(scheme);
		ess = new ElementarySixthStep(scheme);
		nameEC = "Шаг 6";
		isStep = true;
		state = States.Init;
		addLog("Начало работы.");
	}

	public int step()
	{
		ess.step();
		switch (ess.getState()) {
		case BadAction:
			ess = new FourthStep(scheme);
			ess.run();
			ess = new ElementarySixthStep(scheme);
			break;
		case Finish:	
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
			return 1;				
		default:
			state = States.Action;
			break;
		}
		return 0;
	}
}
