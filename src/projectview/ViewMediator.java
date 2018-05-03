package projectview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;

import project.CodeAccessException;
import project.MachineModel;
import project.Memory;

public class ViewMediator extends Observable {
	
	private MachineModel model;
	private CodeViewPanel codeViewPanel;
	private MemoryViewPanel memoryViewPanel1;
	private MemoryViewPanel memoryViewPanel2;
	private MemoryViewPanel memoryViewPanel3;
	//private ControlPanel controlPanel;
	//private ProcessorViewPanel processorPanel;
	//private MenuBarBuilder menuBuilder;
	private JFrame frame;
	private FilesManager filesManager;
	private Animator animator;
	
	public void step() {
		try {
			int ip = model.getInstructionPointer();
			if (!(model.getCurrentJob().getStartcodeIndex() <= ip) &&
					!(ip < model.getCurrentJob().getStartcodeIndex()+model.getCurrentJob().getCodeSize())) {
				throw new CodeAccessException("Instruction Pointer not between currentJob's starting code index and starting code index + code size");
			}
			
			int opcode = model.getOp(ip);
			int arg = model.getArg(ip);
			model.get(opcode).execute(arg);
		} catch (Exception e) {
			model.get(0x1F).execute(model.getArg(model.getInstructionPointer()));
			throw e;
		}
	}
	
	public MachineModel getModel() {
		return model;
	}
	public void setModel(MachineModel model) {
		this.model = model;
	}
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public void clearJob() {
		
	}
	
	public void makeReady(String s) {
		
	}
	
	private void createAndShowGUI() {
		this.animator = new Animator(this);
		this.filesManager = new FilesManager(this);
		filesManager.initialize();
		this.codeViewPanel = new CodeViewPanel(this, model);
		this.memoryViewPanel1 = new MemoryViewPanel(this, model, 0, 240);
		this.memoryViewPanel2 = new MemoryViewPanel(this, model, 240, Memory.DATA_SIZE/2);
		this.memoryViewPanel3 = new MemoryViewPanel(this, model, Memory.DATA_SIZE/2, Memory.DATA_SIZE);
		//this.controlPanel = new ControlPanel(this);
		//this.processorPanel = new ProcessorPanel(this);
		//this.menuBuilder = new MenuBarBuilder(this);
		this.frame = new JFrame("Simulator");
		
		Container content = frame.getContentPane();
		
		content.setLayout(new BorderLayout(1, 1));
		content.setBackground(Color.BLACK);
		content.setSize(1200, 600);
		
		JPanel center = new JPanel(new GridLayout(1, 3));
		frame.add(codeViewPanel.createCodeDisplay(), BorderLayout.LINE_START);
		
		center.add(memoryViewPanel1.createMemoryDisplay());
		center.add(memoryViewPanel2.createMemoryDisplay());
		center.add(memoryViewPanel3.createMemoryDisplay());
		
		frame.add(center, BorderLayout.CENTER);
		//return HERE for the other GUI components
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// return HERE for other setup details
		frame.setVisible(true);
	}
	
	// CORRECTED LATER IN THE EVENING
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ViewMediator mediator = new ViewMediator();
				MachineModel model = new MachineModel(
				//true,  
				//() -> mediator.setCurrentState(States.PROGRAM_HALTED)
				);
				mediator.setModel(model);
				mediator.createAndShowGUI();
			}
		});
	}
	
}
