package com.maverick.family.input;

import com.maverick.family.entity.Gender;
import com.maverick.family.entity.Person;
import com.maverick.family.entity.Relation;
import com.maverick.family.graph.DataPopulator;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public final class LauncherSupportUtilsTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setUp() {
		DataPopulator.populateInitialData();
	}


	@Test
	public void should_not_add_wife_for_non_existent_member() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("The person \"abc\" is not a family member.");
		LauncherSupportUtils.getConnections("abc", "Wife");
	}


	@Test
	public void should_add_son_for_mother() {
		LauncherSupportUtils.removeRelation(new Person("Zoe", Gender.Female), new Person("Boris", Gender.Male), Relation.Child);
		LauncherSupportUtils.removePerson(new Person("Boris", Gender.Male));

		LauncherSupportUtils.addSon("Zoe", "Boris");
		String result = LauncherSupportUtils.getConnections("Paul", "Sons");


		assertEquals("Sons=Boris,Roger", result);
	}


	@Test
	public void should_add_wife_for_husband() {
		LauncherSupportUtils.removeRelation(new Person("Bern", Gender.Male), new Person("Julia", Gender.Female), Relation.Spouse);
		LauncherSupportUtils.removePerson(new Person("Julia", Gender.Female));

		LauncherSupportUtils.addWife("Bern", "Julia");
		String result = LauncherSupportUtils.getConnections("Bern", "Wife");


		assertEquals("Wife=Julia", result);
	}

	/*@Test
	public void should_add_husband_for_wife() {
		LauncherSupportUtils.removeRelation(new Person("Roger", Gender.Male), new Person("Anne", Gender.Female), Relation.Spouse);
		LauncherSupportUtils.removePerson(new Person("Anne", Gender.Female));

		String welcomeMessage = LauncherSupportUtils.addWife("Roger", "Anne");
		String result = LauncherSupportUtils.getConnections("Roge", "Wife");


		assertEquals("Welcome to the family, Anne!", welcomeMessage);
		assertEquals("Wife=Julia", result);
	}*/

	@Test
	public void should_return_correct_connection_with_son() {
		String result = LauncherSupportUtils.getConnections("Alex", "Son");
		assertEquals("Sons=Jacob,Shaun", result);
	}

	@Test
	public void should_return_correct_connection_with_no_daughter() {
		String result = LauncherSupportUtils.getConnections("Alex", "Daughter");
		assertEquals("Alex has no daughters.", result);
	}

	@Test
	public void should_return_correct_connection_with_daughter() {
		String result = LauncherSupportUtils.getConnections("Evan", "Daughter");
		assertEquals("Daughters=Nisha", result);
	}

	@Test
	public void should_return_correct_connection_with_brother() {
		String result = LauncherSupportUtils.getConnections("Alex", "Brothers");
		assertEquals("Brothers=Joe,John", result);
	}

	@Test
	public void should_return_correct_connection_with_sister() {
		String result = LauncherSupportUtils.getConnections("Alex", "Sisters");
		assertEquals("Sisters=Nisha", result);
	}

	@Test
	public void should_return_correct_connection_with_father() {
		String result = LauncherSupportUtils.getConnections("Alex", "Father");
		assertEquals("Father=Evan", result);
	}

	@Test
	public void should_return_correct_connection_with_mother() {
		String result = LauncherSupportUtils.getConnections("Alex", "Mother");
		assertEquals("Mother=Diana", result);
	}

	@Test
	public void should_return_correct_connection_with_grandfather() {
		String result = LauncherSupportUtils.getConnections("Paul", "Grandfather");
		assertEquals("Grandfather=Evan", result);
	}

	@Test
	public void should_return_correct_connection_with_grandfather1() {
		String result = LauncherSupportUtils.getConnections("Steve", "Grandfather");
		assertEquals("Grandfather=Adam", result);
	}

	@Test
	public void should_return_correct_connection_with_grandson() {
		String result = LauncherSupportUtils.getConnections("Evan", "Grandson");
		assertEquals("Grandsons=Jacob,Paul,Piers,Shaun,William", result);
	}

	@Test
	public void should_return_correct_connection_with_no_grandson() {
		String result = LauncherSupportUtils.getConnections("John", "Grandson");
		assertEquals("John has no grandsons.", result);
	}

	@Test
	public void should_return_correct_connection_with_granddaughter() {
		String result = LauncherSupportUtils.getConnections("Evan", "Granddaughter");
		assertEquals("Granddaughters=Ruth,Sally", result);
	}

	@Test
	public void should_return_correct_connection_with_grandmother() {
		String result = LauncherSupportUtils.getConnections("Paul", "Grandmother");
		assertEquals("Grandmother=Diana", result);
	}


	@Test
	public void should_return_correct_connection_with_uncles() {
		String result = LauncherSupportUtils.getConnections("Sally", "Uncles");
		assertEquals("Uncles=Adam,Alex,John", result);
	}

	@Test
	public void should_return_correct_connection_with_uncles1() {
		String result = LauncherSupportUtils.getConnections("Peter", "Uncle");
		assertEquals("Peter has no uncles.", result);
	}

	@Test
	public void should_return_correct_connection_with_aunts() {
		String result = LauncherSupportUtils.getConnections("Sally", "Aunts");
		assertEquals("Aunts=Nancy,Nisha", result);
	}

	@Test
	public void should_return_correct_connection_with_cousins() {

		String result = LauncherSupportUtils.getConnections("Sally", "Cousins");
		assertEquals("Cousins=Jacob,Paul,Ruth,Shaun,William", result);
	}

	@Test
	public void should_return_no_connection_with_wrong_family_member() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("The person \"abc\" is not a family member.");
		LauncherSupportUtils.getConnections("abc", "Cousins");
	}
}