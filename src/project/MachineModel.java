package project;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel {
	private class CPU{
		private int accumulator;
		private int instructionPointer;
		private int memoryBase;
		public void incrementIP(int val) {
			instructionPointer += val;
		}
		public int getAccumulator() {
			return accumulator;
		}
		public void setAccumulator (int arg) {
			cpu.accumulator = arg;
		}
		public int getInstructionPointer() {
			return instructionPointer;
		}
		public void setInstructionPointer(int arg) {
			cpu.instructionPointer = arg;
		}
		public int getMemoryBase() {
			return memoryBase;
		}
		public void setMemoryBase (int arg) {
			cpu.memoryBase = arg;
		}
	}
	public Map<Integer, Instruction> INSTRUCTIONS = new TreeMap<Integer, Instruction>();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;
	private boolean withGUI;
	public MachineModel() {
		this(false, null);
	}
	public MachineModel (boolean withGUI, HaltCallback callback) {
		//INSTRUCTION_MAP entry for "ADDI"
        INSTRUCTIONS.put(0xC, arg -> {
            cpu.accumulator += arg;
            cpu.incrementIP(1);
        });

        //INSTRUCTION_MAP entry for "ADD"
        INSTRUCTIONS.put(0xD, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            cpu.accumulator += arg1;
            cpu.incrementIP(1);
        });

        //INSTRUCTION_MAP entry for "ADDN"
        INSTRUCTIONS.put(0xE, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator += arg2;
            cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "SUBI"
        INSTRUCTIONS.put(0xF, arg ->{
        	cpu.accumulator -= arg;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "SUB"
        INSTRUCTIONS.put (0x10, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
        	cpu.accumulator -= arg1;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "SUBN"
        INSTRUCTIONS.put(0x11, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
        	int arg2 = memory.getData(cpu.memoryBase +arg1);
        	cpu.accumulator -= arg2;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "MULI"
        INSTRUCTIONS.put(0x12, arg -> {
        	cpu.accumulator *= arg;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "MUL"
        INSTRUCTIONS.put(0x13, arg ->{
        	int arg1 = memory.getData(cpu.memoryBase+arg);
        	cpu.accumulator *= arg1;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "MULN"
        INSTRUCTIONS.put(0x14, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
        	int arg2 = memory.getData(cpu.memoryBase+arg1);
        	cpu.accumulator *= arg2;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "DIVI"
        INSTRUCTIONS.put(0x15, arg -> {
        	if(arg == 0) {throw new DivideByZeroException();}
        	cpu.accumulator /= arg;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "DIV"
        INSTRUCTIONS.put(0x16, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
        	if(arg1 == 0) {throw new DivideByZeroException();}
        	cpu.accumulator /=arg1;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for"DIVN"
        INSTRUCTIONS.put(0x17,  arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
        	int arg2 = memory.getData(cpu.memoryBase +arg1);
        	if(arg2 == 0) {throw new DivideByZeroException();}
        	cpu.accumulator /= arg2;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "NOP"
        INSTRUCTIONS.put(0, arg ->{
        	cpu.incrementIP(1);
        });
        //INSRUCTION_MAP entry for "LODI"
        INSTRUCTIONS.put(1, arg -> {
        	cpu.setAccumulator(arg);
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "LOD"
        INSTRUCTIONS.put(2 , arg -> {
        	int val = memory.getData(cpu.memoryBase + arg);
        	cpu.setAccumulator(val);
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "LODN"
        INSTRUCTIONS.put(3, arg -> {
        	int val = memory.getData(cpu.memoryBase +arg);
        	int val1 = memory.getData(cpu.memoryBase + val);
        	cpu.setAccumulator(cpu.memoryBase+val1);
        	cpu.incrementIP(1);
        });
        //INSTURCTION_MAP entry for "STO"
        INSTRUCTIONS.put(4, arg -> {
        	memory.getData()[cpu.memoryBase +arg] = cpu.accumulator;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "STON"
        INSTRUCTIONS.put(5, arg -> {
        	int val = memory.getData(cpu.memoryBase + arg);
        	memory.getData()[cpu.memoryBase + val] = cpu.accumulator;
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "JMPR"
        INSTRUCTIONS.put(6, arg -> {
        	cpu.incrementIP(arg);
        });
        //INSTRUCTION_MAP entry for "JUMP"
        INSTRUCTIONS.put(7, arg -> {
        	int val = memory.getData(cpu.memoryBase +arg);
        	cpu.incrementIP(val);
        });
        //INSTRUCTION_MAP entry for "JUMPI"
        INSTRUCTIONS.put(8,  arg ->{
        	cpu.setInstructionPointer(arg);
        });
        //INSTRUCTION_MAP entry for "JMPZR"
        INSTRUCTIONS.put(9, arg ->{
        	if (cpu.accumulator == 0){cpu.incrementIP(arg);}
        	else {cpu.incrementIP(1);}
        });
        //INSTRUCTION_MAP entry for "JMPZ"
        INSTRUCTIONS.put(0xA, arg -> {
        	if (cpu.accumulator == 0) {
        		int val = memory.getData(cpu.memoryBase +arg);
            	cpu.incrementIP(val);
        	}
        	else {cpu.incrementIP(1);}
        });
        //INSTRUCTION_MAP for "JMPZI"
        INSTRUCTIONS.put(0xB, arg -> {
        	if (cpu.accumulator ==0) {cpu.setInstructionPointer(arg);}
        	else {cpu.incrementIP(1);}
        });
        //INSTRUCTION_MAP for "ANDI"
        INSTRUCTIONS.put(0x18, arg -> {
        	if (cpu.accumulator != 0 && arg != 0) {cpu.setAccumulator(1);}
        	else {cpu.setAccumulator(0);}
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP for "AND"
        INSTRUCTIONS.put(0x19, arg -> {
        	if (cpu.accumulator != 0 && memory.getData()[cpu.memoryBase + arg] !=0) {cpu.setAccumulator(1);}
        	else {cpu.setAccumulator(0);}
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP for "NOT"
        INSTRUCTIONS.put(0x1A, arg ->{
        	if (cpu.accumulator != 0) {cpu.setAccumulator(0);}
        	else if (cpu.accumulator == 0) {cpu.setAccumulator(1);}
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP for "CMPL"
        INSTRUCTIONS.put(0x1B, arg -> {
        	if (memory.getData()[cpu.memoryBase +arg] < 0) {cpu.setAccumulator(1);}
        	else {cpu.setAccumulator(0);}
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP for "CMPZ"
        INSTRUCTIONS.put(0x1C, arg -> {
        	if (memory.getData()[cpu.memoryBase +arg] == 0) {cpu.setAccumulator(1);}
        	else {cpu.setAccumulator(0);}
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP for "HALT"
        INSTRUCTIONS.put(0x1F, arg -> {
        	callback.halt();
        });
	}
	public int[] getData() {
		return memory.getData();
	}
	public int getData(int index) {
		return memory.getData(index);
	}
	public void setData(int index, int value) {
		memory.setData(index, value);
	}
	public Instruction get (int index) {
		return INSTRUCTIONS.get(index);
	}
	public void incrementIP(int val) {
		cpu.incrementIP(val);
	}
	public int getAccumulator() {
		return cpu.getAccumulator();
	}
	public void setAccumulator(int arg) {
		cpu.setAccumulator(arg);
	}
	public int getInstructionPointer() {
		return cpu.getInstructionPointer();
	}
	public void setInstructionPointer(int arg) {
		cpu.setInstructionPointer(arg);
	}
	public int getMemoryBase() {
		return cpu.getMemoryBase();
	}
	public void setMemoryBase(int arg) {
		cpu.setMemoryBase(arg);
	}
	
}
