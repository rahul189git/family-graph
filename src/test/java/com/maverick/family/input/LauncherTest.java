package com.maverick.family.input;

import com.maverick.family.graph.DataPopulator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public final class LauncherTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();


	@BeforeClass
	public static void setUp() {
		DataPopulator.populateInitialData();
	}


	@Test
	public void show_relative_with_empty_input() {
		String inputs = "";
		String result = Launcher.process(inputs);
		assertEquals("Empty input.", result);
	}

	@Test
	public void show_relative_with_wrong_input() {
		String inputs = "Person";
		String result = Launcher.process(inputs);
		assertEquals("Invalid input: Person", result);
	}

	@Test
	public void show_relative_with_wrong_input1() {
		String inputs = "Person=aa Relation=bb";
		String res = Launcher.process(inputs);
		assertEquals("The person \"aa\" is not a family member.", res);
	}

	@Test
	public void show_relative_with_wrong_relation() {
		String inputs = "Person=Alex Relation=ABC";
		String res = Launcher.process(inputs);
		assertEquals("Unsupported relation ABC", res);
	}
}