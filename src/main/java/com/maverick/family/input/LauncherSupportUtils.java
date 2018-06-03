package com.maverick.family.input;

import com.maverick.family.entity.Gender;
import com.maverick.family.entity.Person;
import com.maverick.family.entity.Relation;
import com.maverick.family.graph.DataPopulator;
import com.maverick.family.graph.RelationCreator;
import com.maverick.family.graph.RelationProvider;

import java.util.List;
import java.util.stream.Collectors;

final class LauncherSupportUtils {
	private static final RelationProvider RELATION_PROVIDER = RelationProvider.getInstance();
	private static final RelationCreator RELATION_CREATOR = RelationCreator.getInstance();

	static {
		DataPopulator.populateInitialData();
	}

	static void addSon(String personName, String sonName) {
		RELATION_CREATOR.addSon(personName, sonName);

	}

	static void addWife(String personName, String wife) {
		RELATION_CREATOR.addWife(personName, wife);
	}

	static void removeRelation(Person fromPerson, Person toPerson, Relation relation) {
		RELATION_CREATOR.removeRelation(fromPerson, toPerson, relation);

	}

	static void removePerson(Person person) {
		RELATION_CREATOR.removePerson(person);
	}


	static String getConnections(String personName, String relationName) {
		Person person = RELATION_PROVIDER.getPerson(personName);

		if (person == null) {
			throw new RuntimeException("The person \"" + personName + "\" is not a family member.");
		}

		switch (relationName.toUpperCase()) {

			case "FATHER":
				Person father = RELATION_PROVIDER.getFather(person);
				if (father == null) {
					return personName + " has no father added.";
				}
				return "Father=" + father.getName();
			case "MOTHER":
				Person mother = RELATION_PROVIDER.getMother(person);
				if (mother == null) {
					return personName + " has no mother added.";
				}
				return "Mother=" + mother.getName();

			case "HUSBAND":
				//For Husband - Input person gender should be female.
				if (person.getGender() == Gender.Male) {
					return personName + " is Male.";
				}
				Person husband = RELATION_PROVIDER.getSpouse(person);
				if (husband == null) {
					return personName + " is single.";
				}
				return "Husband=" + husband.getName();

			case "WIFE":
				//For Husband - Input person gender should be male.
				if (person.getGender() == Gender.Female) {
					return personName + " is Female.";
				}
				Person wife = RELATION_PROVIDER.getSpouse(person);
				if (wife == null) {
					return personName + " is single.";
				}
				return "Wife=" + wife.getName();

			case "BROTHER":
			case "BROTHERS":
				List<Person> brothers = RELATION_PROVIDER.getBrothers(person);
				if (brothers == null || brothers.isEmpty()) {
					return personName + " has no brothers.";
				}
				return "Brothers=" + brothers.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));
			case "SISTER":
			case "SISTERS":
				List<Person> sisters = RELATION_PROVIDER.getSisters(person);
				if (sisters == null || sisters.isEmpty()) {
					return personName + " has no sisters.";
				}
				return "Sisters=" + sisters.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));

			case "SON":
			case "SONS":
				List<Person> sons = RELATION_PROVIDER.getSons(person);
				if (sons.isEmpty()) {
					return personName + " has no sons.";
				}
				return "Sons=" + sons.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));

			case "DAUGHTER":
			case "DAUGHTERS":
				List<Person> daughters = RELATION_PROVIDER.getDaughters(person);
				if (daughters.isEmpty()) {
					return personName + " has no daughters.";
				}
				return "Daughters=" + daughters.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));

			case "COUSIN":
			case "COUSINS":
				List<Person> cousins = RELATION_PROVIDER.getCousins(person);
				if (cousins.isEmpty()) {
					return personName + " has no cousins.";
				}
				return "Cousins=" + cousins.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));

			case "GRANDMOTHER":
				Person grandMother = RELATION_PROVIDER.getGrandMother(person);
				if (grandMother == null) {
					return personName + " has no grandmother added.";
				}
				return "Grandmother=" + grandMother.getName();
			case "GRANDFATHER":
				Person grandFather = RELATION_PROVIDER.getGrandFather(person);
				if (grandFather == null) {
					return personName + " has no grandfather added.";
				}
				return "Grandfather=" + grandFather.getName();
			case "GRANDSON":
			case "GRANDSONS":
				List<Person> grandsons = RELATION_PROVIDER.getGrandSons(person);
				if (grandsons.isEmpty()) {
					return personName + " has no grandsons.";
				}
				return "Grandsons=" + grandsons.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));

			case "GRANDDAUGHTER":
			case "GRANDDAUGHTERS":
				List<Person> grandDaughters = RELATION_PROVIDER.getGrandDaughters(person);
				if (grandDaughters.isEmpty()) {
					return personName + " has no granddaughters.";
				}
				return "Granddaughters=" + grandDaughters.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));

			case "AUNT":
			case "AUNTS":
				List<Person> aunts = RELATION_PROVIDER.getAunts(person);
				if (aunts.isEmpty()) {
					return personName + " has no aunts.";
				}
				return "Aunts=" + aunts.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));


			case "UNCLE":
			case "UNCLES":
				List<Person> uncles = RELATION_PROVIDER.getUncles(person);
				if (uncles.isEmpty()) {
					return personName + " has no uncles.";
				}
				return "Uncles=" + uncles.stream()
						.map(Person::getName)
						.collect(Collectors.joining(","));


			default:
				return "Unsupported relation " + relationName;

		}

	}


}
