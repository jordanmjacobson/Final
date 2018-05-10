package project;

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
		try (Scanner s = new Scanner(inputFileName)) { // pass 1
			while (s.hasNextLine()) {
				lineNumber++;
				String line = s.nextLine();

				if (line.trim().length() == 0) { // error 1
					blankLine = true;
					blankLineNumber = lineNumber;
				}

				if (blankLine && line.trim().length() > 0) {
					return blankLineNumber;
				}

				if (line.charAt(0) == ' ' || line.charAt(0) == '\t') { // error 2
					return lineNumber;
				}

				if (line.trim().toUpperCase().equals("DATA")) { // error 3
					if (!line.trim().equals("DATA")) {
						return lineNumber;
					}
					if (readingCode == false) {
						return lineNumber;
					} else {
						readingCode = false;
					}
				}

				if (readingCode) {
					code.add(line);
				} else {
					data.add(line);
				}
			}
		}

		lineNumber = 0;
		readingCode = true;
		try (Scanner s = new Scanner(inputFileName)) { // pass 2
			while (s.hasNextLine()) {
				lineNumber++;
				String line = s.nextLine();
				String[] parts = line.trim().split("\\s+");

				if (line.trim().equals("DATA")) {
					readingCode = false;
				}

				if (!InstrMap.toCode.keySet().contains(parts[0]) && readingCode) { // error 4
					return lineNumber;
				}

				if (InstrMap.toCode.keySet().contains(parts[0].toUpperCase()) && readingCode) {
					if (!InstrMap.toCode.keySet().contains(parts[0])) {
						return lineNumber;
					}
				}
				if (Assembler.noArgument.contains(parts[0]) && parts.length != 1 && readingCode) { // error 5
					return lineNumber;
				}
				if (!Assembler.noArgument.contains(parts[0]) && parts.length != 2 && readingCode) {
					return lineNumber;
				}
				if (parts.length == 2 && readingCode) { // error 6
					try {
						Integer.parseInt(parts[1], 16);
					} catch (NumberFormatException e) {
						error.append("\nError on line " + (lineNumber) + ": argument is not a hex number");
						return lineNumber;
					}

				}

				if (parts.length == 2 && !readingCode) { // error 7
					try {
						Integer.parseInt(parts[0], 16);
						Integer.parseInt(parts[1], 16);
					} catch (NumberFormatException e) {
						error.append("\nError on line " + (lineNumber) + ": data has non-numeric memory address");
						return lineNumber;
					}

				}

			}
			return 0;
		}
	}
}
