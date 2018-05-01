package project;

import java.io.File;
import java.util.Scanner;

public class Loader {
	public static String load(MachineModel model, File file, int codeOffset, int memoryOffset) {
		int codeSize = 0;
		if (model == null || file == null) return null;
		try (Scanner input = new Scanner(file)){
			boolean incode = true;
			while (input.hasNextLine()) {
				String line1 = input.nextLine();
				String line2 = input.nextLine();
				Scanner parser = new Scanner(line1+""+line2);
				int firstInt = parser.nextInt();
			}
		}
	}
}
