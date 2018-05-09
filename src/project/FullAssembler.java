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
		try(Scanner s = new Scanner(inputFileName)) { //pass 1
			while (s.hasNextLine()) {
				lineNumber ++;
				String line = s.nextLine();

				if (line.trim().length() == 0) { //error 1
					blankLine = true;
					blankLineNumber = lineNumber;
				}
				
				if (blankLine && line.trim().length() > 0) {
					return blankLineNumber;
				}
				
				if (line.charAt(0) == ' ' || line.charAt(0) == '\t') { //error 2
					return lineNumber;
				}
				
				if (line.trim().toUpperCase().equals("DATA")) { //error 3
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
		try(Scanner s = new Scanner(inputFileName)) { // pass 2
			
		}
		return 0;
	}

}
