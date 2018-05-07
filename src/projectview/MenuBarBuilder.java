package projectview;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenuItem;

public class MenuBarBuilder implements Observer {
	private JMenuItem assemble = new JMenuItem("Assemble Source...");
	private JMenuItem load = new JMenuItem("Load Program...");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem go = new JMenuItem("Go");
	private JMenuItem job0 = new JMenuItem("Job 0");
	private JMenuItem job1 = new JMenuItem("Job 1");
	private ViewMediator view;

	public MenuBarBuilder(ViewMediator gui) { 
	     view = gui; gui.addObserver(this); 
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
