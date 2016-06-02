package EvetlinCasts;

import javax.swing.JOptionPane;

import EverlinFrame.MainFrame;
import EverlinObjects.Scheme;

public class SecondStep extends ElementaryCast {
	private ElementaryCast ess;
	public SecondStep(Scheme scheme) {
		super(scheme);
		ess = new ElementarySecondStep(scheme);
		nameEC = "Шаг 2";
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
			nextClass = ThirdStep.class;
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
			return -2;				
		default:
			state = States.Action;
			break;
		}
		return 0;
	}
}
