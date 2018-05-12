package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FullAssembler implements Assembler {
	private boolean readingCode = true;
	private boolean blankLine = false;
	private int blankLineNumber = 0;
	private List<String> code = new ArrayList<>();
	private List<String> data = new ArrayList<>();
	private int lineNumber;

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		lineNumber = 0;
		int retVal = 0;
		try (Scanner s = new Scanner(new File(inputFileName))) { // pass 1
			while (s.hasNextLine()) {
				lineNumber++;
				String line = s.nextLine();
				System.out.println("line " + lineNumber + ": " + line);

				if (line.trim().length() == 0) { // error 1
					if (!blankLine) {
						blankLineNumber = lineNumber;
					}
					blankLine = true;
					continue;
				}

				if (blankLine && line.trim().length() > 0) {
					error.append("\nError on line " + (blankLineNumber) + ": illegal blank line");
					retVal = blankLineNumber;
					blankLine = false;

				}

				if (line.length() != 0 && (line.charAt(0) == ' ' || line.charAt(0) == '\t')) { // error 2
					error.append("\nError on line " + (lineNumber) + ": illegal white space");
					retVal = lineNumber;
				}

				if (line.trim().toUpperCase().equals("DATA")) { // error 3
					if (!line.trim().equals("DATA")) {
						error.append("\nError on line " + (lineNumber) + ": DATA is not all uppercase");
						retVal = lineNumber;
					}
					if (readingCode == false) {
						error.append("\nError on line " + (lineNumber) + ": illegal duplicate DATA delimeter");
						retVal = lineNumber;
					} else {
						readingCode = false;
					}
				}
				if (readingCode) {
					code.add(line.trim());
				} else {
					data.add(line.trim());
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		lineNumber = 0;
		// System.out.println(code);
		// System.out.println(data);
		for (String line : code) { // pass 2
			lineNumber++;
			if (line.length() == 0)
				continue;
			String[] parts = line.trim().split("\\s+");

			if (!InstrMap.toCode.keySet().contains(parts[0].toUpperCase())) { // error 4
				error.append("\nError on line " + (lineNumber) + ": illegal mnemonic");
				retVal = lineNumber;
			}

			if (InstrMap.toCode.keySet().contains(parts[0].toUpperCase())) {
				if (!InstrMap.toCode.keySet().contains(parts[0])) {
					error.append("\nError on line " + (lineNumber) + ": mnemonic is not all uppercase");
					retVal = lineNumber;
				}
			}
			if (Assembler.noArgument.contains(parts[0].toUpperCase()) && parts.length != 1) { // error 5
				error.append(
						"\nError on line " + (lineNumber) + ": " + parts[0].toUpperCase() + " cannot have an argument");
				retVal = lineNumber;
			}
			if (!Assembler.noArgument.contains(parts[0].toUpperCase()) && parts.length != 2) {
				if (parts.length < 2) {
					error.append("\nError on line " + (lineNumber) + ": mnemonic is missing an argument");
					retVal = lineNumber;
				} else {
					error.append("\nError on line " + (lineNumber) + ": mnemonic has too many arguments");
					retVal = lineNumber;
				}

			}

			if (parts.length == 2) { // error 6
				try {
					Integer.parseInt(parts[1], 16);
				} catch (NumberFormatException e) {
					error.append("\nError on line " + (lineNumber) + ": argument is not a hex number");
					retVal = lineNumber;
				}

			}
		}

		for (String line : data) {
			lineNumber++;
			if (line.length() == 0 || line.toUpperCase().equals("DATA"))
				continue;
			String[] parts = line.split("\\s+");

			if (parts.length < 2) {
				error.append("\nError on line " + (lineNumber) + ": data line has too few values");
				retVal = lineNumber;
			}
			if (parts.length > 2) {
				error.append("\nError on line " + (lineNumber) + ": data line has too many values");
				retVal = lineNumber;
			}
			if (parts.length > 1) {
				try {
					Integer.parseInt(parts[0], 16);
				} catch (NumberFormatException e) {
					error.append("\nError on line " + (lineNumber) + ": data has non-numeric memory address");
					retVal = lineNumber;
				}

				try {
					Integer.parseInt(parts[1], 16);
				} catch (NumberFormatException e) {
					error.append("\nError on line " + (lineNumber) + ": data has non-numeric memory value");
					retVal = lineNumber;
				}
			}
		}

		// if we found no errors, now we can assemble the pasm file to a pexe file
		if (error.length() == 0) {
			new SimpleAssembler().assemble(inputFileName, outputFileName, error);
		} else {
			System.out.println(error.toString());
		}
		return retVal;
	}

	public static void main(String[] args) {
		StringBuilder error = new StringBuilder();
		System.out.println("Enter the name of the file without extension: ");
		try (Scanner keyboard = new Scanner(System.in)) {
			String filename = keyboard.nextLine();
			int i = new FullAssembler().assemble(filename + ".pasm", filename + ".pexe", error);
			System.out.println("result = " + i);
		}
	}
}
