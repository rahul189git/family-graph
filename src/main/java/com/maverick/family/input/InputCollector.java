package com.maverick.family.input;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.web.RunnerData;

import java.util.function.BiConsumer;

import static com.maverick.family.input.Launcher.displayHelp;


public class InputCollector implements BiConsumer<TextIO, RunnerData> {
	public static void main(String[] args) {
		TextIO textIO = TextIoFactory.getTextIO();
		new InputCollector().accept(textIO, null);
	}

	@Override
	public void accept(TextIO textIO, RunnerData runnerData) {
		TextTerminal<?> terminal = textIO.getTextTerminal();
		terminal.getProperties().put("pane.title", "Family Graph Interactive Console");

		displayHelp();
		String exitStr;
		do {

			String inputs = textIO
					.newStringInputReader()
					.withInputTrimming(true)
					.read("Input: ");

			String output = Launcher.process(inputs);
			if (output != null && !output.isEmpty()) {
				terminal.printf("\nOutput: %s", output);
			}
			exitStr = textIO
					.newStringInputReader()
					.withMinLength(0)
					.read("\nPress Enter to continue and x to exit.\n"
					);

		} while (!"X".equalsIgnoreCase(exitStr));


		textIO.dispose("User has left.");
	}


}