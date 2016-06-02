package EverlinFrame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.ComponentOrientation;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.table.AbstractTableModel;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.ListSelectionModel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.DropMode;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import EverlinObjects.EverlinAlgo;
import EverlinObjects.Scheme;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	public static String titleWindow;
	private InteractiveTable interactiveTable;
	private JMenuBar menuBar = new JMenuBar();
	private JPanel panel = new JPanel();
	private JButton btnBuild = new JButton("Построить");
	private Canvas canvas;
	private Color colorBackGround = Color.LIGHT_GRAY;
	private JMenu fileMenu = new JMenu("Файл");
	private JMenuItem ExitMenuItem = new JMenuItem("Выход");
	private JMenu editMenu;
	private JMenu helpMenu;
	private final static int countOverrideSteps = 6;
	private int retValInit = 0;
	private EverlinAlgo sys = new EverlinAlgo();
	private final JButton btnStep = new JButton("Далее");
	private final JToolBar toolBar = new JToolBar();
	private final JToolBar toolBar_1 = new JToolBar();
	private final static JPanel panel_1 = new JPanel();
	private final static JTextArea actionLog = new JTextArea();
	private final JPanel panel_3 = new JPanel();
	private final JScrollPane scrollPane = new JScrollPane();
	private final JButton btnRun = new JButton("Выполнить");
	public MainFrame(String titleWindow1)
	{		
		super();
		titleWindow = titleWindow1;
		addWindowListener(new WindowAdapter() {
			@Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showOptionDialog(null, "Вы действительно хотите закрыть приложение?",titleWindow, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION)
                	System.exit(1);
                else
                	return;
            }
        });
		setBounds(new Rectangle(0, 0, 1024, 768));
		setTitle(titleWindow);
		setJMenuBar(menuBar);		
		menuBar.add(fileMenu);		
		ExitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
                int confirm = JOptionPane.showOptionDialog(null, "Вы действительно хотите закрыть приложение?",titleWindow, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == JOptionPane.YES_OPTION)
                	System.exit(1);
                else
                	return;
			}
		});
		fileMenu.add(ExitMenuItem);
		editMenu = new JMenu("Правка");
		menuBar.add(editMenu);		
		helpMenu = new JMenu("Справка");
		menuBar.add(helpMenu);
		toolBar_1.setOrientation(SwingConstants.VERTICAL);
		toolBar_1.setRollover(true);
		
		getContentPane().add(toolBar_1, BorderLayout.EAST);
		toolBar_1.add(panel);
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setLayout(new MigLayout("", "[230px,grow]", "[64px][][][][][grow][][][][][][][][][][][][][][][][grow][]"));
		interactiveTable = new InteractiveTable();
		interactiveTable.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		interactiveTable.getTable().setFillsViewportHeight(true);

		panel.add(interactiveTable, "cell 0 0 1 21,growx,aligny top");
		
	//	interactiveTable.getModel().addRow("bbacca-1");
		
	//	interactiveTable.getModel().addRow("a-1cddcb-1a-1b");
		
	//	interactiveTable.getModel().addRow("e-1cc-1e-1bda-1b-1");
	//	interactiveTable.getModel().addRow("adtii-1t-1");
		
	//	interactiveTable.getModel().addRow("c-1abb-1a-1ckk-1");
	//	interactiveTable.getModel().addRow("b-1cca-1brra");
	//	interactiveTable.getModel().addRow("bci-1q-1ihqyc-1h-1y-1b");
	//	interactiveTable.getModel().addRow("bci-1q-1hqyc-1h-1y-1ib");
	//	a-1bbh-1ehe-1d-1ad	
	//	interactiveTable.getModel().addRow("idi-1ad-1bbca-1c-1");
	
	//	interactiveTable.getModel().addRow("de-1d-1aaxyx-1y-1e");	
	//	interactiveTable.getModel().addRow("xyx-1y-1aa");
		
		interactiveTable.getModel().addRow("achyq-1");
		interactiveTable.getModel().addRow("bk-1kt-1hd");
	 	interactiveTable.getModel().addRow("a-1cy-1b-1d-1");
		interactiveTable.getModel().addRow("qiti-1");
		interactiveTable.getModel().addEmptyRow();
	//	b c i-1 q-1 i h q y c-1 h-1 y-1 b
	//	bci-1q-1ihqyc-1h-1y-1b
	//  q-1 a-1 q d-1 a c-1 b b d c
		getContentPane().add(toolBar, BorderLayout.NORTH);
		toolBar.add(panel_1);
		FlowLayout fl_panel_1 = new FlowLayout(FlowLayout.LEFT, 5, 5);
		panel_1.setLayout(fl_panel_1);
		btnBuild.setToolTipText("Запуск");
		btnBuild.setIcon(new ImageIcon(MainFrame.class.getResource("/javax/swing/plaf/metal/icons/ocean/computer.gif")));
		panel_1.add(btnBuild);
		btnBuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionLog.setText("");;
				Vector<String> schema = new Vector<String>();
				String tmp = "";
				((AbstractTableModel)interactiveTable.getModel()).fireTableDataChanged();
				for(int i = 0; i < interactiveTable.getModel().getRowCount(); i++)
				{
					tmp = (String)interactiveTable.getModel().getValueAt(i, 1);
					if(!tmp.equals(""))	schema.add(tmp);
				}				
				retValInit = sys.init(schema);
				if (retValInit != -1)
				{
					interactiveTable.getTable().setEnabled(false);
					btnBuild.setEnabled(false);
					btnStep.setEnabled(true);
					btnRun.setEnabled(true);
					canvas.repaint();
					interactiveTable.getModel().init(sys.getSchmemeText());
				}
				else
				{
					btnStep.setEnabled(false);
					btnRun.setEnabled(false);
				}
			}
		});
		btnStep.setEnabled(false);
		panel_1.add(btnStep);
		btnRun.setIcon(new ImageIcon(MainFrame.class.getResource("/javax/swing/plaf/metal/icons/ocean/iconify.gif")));
		btnRun.setToolTipText("Привести систему к каноническому виду");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (retValInit != -1)
				{
					sys.run();
					interactiveTable.getTable().setEnabled(true);
					btnBuild.setEnabled(true);
					btnStep.setEnabled(false);
					btnRun.setEnabled(false);
					canvas.repaint();
					interactiveTable.getModel().init(sys.getSchmemeText());
				}			
			}
		});
		btnRun.setEnabled(false);
		
		panel_1.add(btnRun);
		btnStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (retValInit != -1)
				{
					switch (sys.step()) {
					case countOverrideSteps:
					case -1:
						interactiveTable.getTable().setEnabled(true);
						btnBuild.setEnabled(true);
						btnStep.setEnabled(false);
						btnRun.setEnabled(false);
						break;
					default:
						break;
					}
					canvas.repaint();
					interactiveTable.getModel().init(sys.getSchmemeText());
				}			
			}
		});
		
		getContentPane().add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		canvas = new Canvas()
		{
			public void paint(Graphics g) {
				canvas.setBackground(colorBackGround);
				Scheme.panelHeight = getHeight();
				Scheme.panelWidth = getWidth();
				Graphics2D g2 = (Graphics2D) g;
				g2.setStroke(new BasicStroke( 1.5F));
				if (retValInit != -1) 
					sys.draw(g2);
			}
		};
		panel_3.add(canvas);
		canvas.setBackground(colorBackGround);
		scrollPane.setViewportView(actionLog);
		actionLog.setSize(new Dimension(5, 9));
		actionLog.setMaximumSize(new Dimension(4, 16));
		actionLog.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		actionLog.setWrapStyleWord(true);
		actionLog.setFont(new Font("MS Reference Sans Serif", Font.BOLD, 11));
		actionLog.setRows(11);
		actionLog.setLineWrap(true);
		actionLog.setEditable(false);
		actionLog.setDropMode(DropMode.INSERT);
		panel_3.add(scrollPane, BorderLayout.SOUTH);	
	}
	
	public static void addRowLog(String string)
	{
		actionLog.append(string + "\r\n");
		actionLog.setCaretPosition(actionLog.getDocument().getLength());		
	}
}
