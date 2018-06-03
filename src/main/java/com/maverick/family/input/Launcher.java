package com.maverick.family.input;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.maverick.family.input.LauncherSupportUtils.getConnections;


public final class Launcher {
	private final static Logger logger = LoggerFactory.getLogger(Launcher.class);

	public static String process(String inputs) {
		//String inputs = "Person=Alex Relation=Daughter";
		String invalid_input = "Invalid input: " + inputs;

		try {
			if (isEmpty(inputs)) {
				return "Empty input.";
			}
			//Split input string (Person=Alex Relation=Brothers) or (Husband=Bern Wife=Julia)
			String[] splittedInput = inputs.split("\\s+");

			if (splittedInput.length != 2) {
				return invalid_input;
			}

			String output;
			//Split further by = token [(Person=Alex) (Relation=Brothers)] or [(Husband=Bern) (Wife=Julia)]
			String[] keyValuePair1 = splittedInput[0].split("=");//"Person=Alex" or "Husband=Bern"
			String[] keyValuePair2 = splittedInput[1].split("=");//"Relation=Brothers" or "Wife=Julia"
			String personName = keyValuePair1[1];
			switch (keyValuePair1[0]) {
				case "Person":
					if (validateSecondInputPair(keyValuePair2, "Relation")) {
						return invalid_input;
					}
					String relationName = keyValuePair2[1];
					output = getConnections(personName, relationName);
					break;
				case "Husband":
					if (validateSecondInputPair(keyValuePair2, "Wife")) {
						return invalid_input;
					}
					String wifeName = keyValuePair2[1];
					LauncherSupportUtils.addWife(personName, wifeName);
					output = "Welcome to the family, " + wifeName + "!";
					break;
				/*case "Wife":
					if (validateSecondInputPair(keyValuePair2, "Husband")) {
						return;
					}
					String husbandName = keyValuePair2[1];
					LauncherSupportUtils.addHusband(personName, husbandName);
					break;*/
				case "Mother":
				case "Father":
					if (validateSecondInputPair(keyValuePair2, "Son")) {
						return invalid_input;
					}
					String sonName = keyValuePair2[1];
					LauncherSupportUtils.addSon(personName, sonName);
					output = "Welcome to the family, " + sonName + "!";
					break;
				default:
					output = invalid_input;
					break;

			}
			return output;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return e.getMessage();
		}


	}

	private static boolean validateSecondInputPair(String[] keyVal2, String key) {
		//Validate to check if second string key is in "Relation", "Wife", "Husband", "Son" and their value is not empty.
		if (!key.equalsIgnoreCase(keyVal2[0]) || isEmpty(keyVal2[1])) {
			return true;
		}
		return false;
	}

	private static boolean isEmpty(String inputs) {
		return inputs == null || inputs.isEmpty();
	}

	public static void displayHelp() {
		String sb = "---------Please give input in format below.---------\n" +
				"1).Show relative: Person=Alex Relation=Brothers\n" +
				"Where relations are :\n" +
				"\t\tFather\n" +
				"\t\tMother\n" +
				"\t\tWife\n" +
				"\t\tBrother(s)\n" +
				"\t\tSister(s)\n" +
				"\t\tSon(s)\n" +
				"\t\tDaughter(s)\n" +
				"\t\tCousin(s)\n" +
				"\t\tGrandmother\n" +
				"\t\tGrandfather\n" +
				"\t\tGrandson(s)\n" +
				"\t\tGranddaugter(s)\n" +
				"\t\tAunt(s)\n" +
				"\t\tUncle(s)\n" +
				"2).Add spouse: Husband=Bern Wife=Julia\n" +
				"3).Add child:  Mother=Zoe Son=Boris\n" +
				"---------------------------------------------------\n";

		System.out.println(sb);
	}


}
