package com.maverick.family.graph;

import com.maverick.family.data.FamilyMembersData;
import com.maverick.family.db.JDBCUtils;
import com.maverick.family.entity.Edge;
import com.maverick.family.entity.Gender;
import com.maverick.family.entity.Person;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class RelationProviderTest {
	@BeforeClass
	public static void setUp() {
		DataPopulator.populateInitialData();
	}

	@Test
	public void getInstance() {
		Assert.assertTrue(RelationProvider.getInstance() == RelationProvider.getInstance());
	}

	@Test
	public void getFather() {
		Assert.assertEquals(new Person("Evan", Gender.Male),
				RelationProvider.getInstance().getFather(new Person("Alex", Gender.Male)));
	}

	@Test
	public void getFather_when_father_is_not_present_in_family_graph() {
		Assert.assertNull(
				RelationProvider.getInstance().getFather(new Person("Evan", Gender.Male)));
	}

	@Test
	public void getMother() {
		Assert.assertEquals(new Person("Diana", Gender.Female),
				RelationProvider.getInstance().getMother(new Person("Joe", Gender.Male)));
	}

	@Test
	public void getSons() {
		List<Person> sons = RelationProvider.getInstance().getSons(new Person("Evan", Gender.Male));
		Assert.assertEquals(3, sons.size());
		Assert.assertEquals("Alex", sons.get(0).getName());
		Assert.assertEquals("Joe", sons.get(1).getName());
		Assert.assertEquals("John", sons.get(2).getName());
	}

	@Test
	public void getDaughters() {
		List<Person> daughters = RelationProvider.getInstance().getDaughters(new Person("Evan", Gender.Male));
		Assert.assertEquals(1, daughters.size());
		Assert.assertEquals("Nisha", daughters.get(0).getName());
	}

	@Test
	public void getGrandFather() {
		Assert.assertEquals(new Person("Evan", Gender.Male),
				RelationProvider.getInstance().getGrandFather(new Person("Shaun", Gender.Male)));
	}

	@Test
	public void getGrandMother() {
		Assert.assertEquals(new Person("Diana", Gender.Female),
				RelationProvider.getInstance().getGrandMother(new Person("Shaun", Gender.Male)));
	}

	@Test
	public void getSpouse() {
		Assert.assertEquals(new Person("Diana", Gender.Female),
				RelationProvider.getInstance().getGrandMother(new Person("Shaun", Gender.Male)));

	}

	@Test
	public void getGrandSons() {
		List<Person> grandSons = RelationProvider.getInstance().getGrandSons(new Person("Evan", Gender.Male));
		Assert.assertEquals(5, grandSons.size());
		Assert.assertEquals("Jacob", grandSons.get(0).getName());
		Assert.assertEquals("Paul", grandSons.get(1).getName());
		Assert.assertEquals("Piers", grandSons.get(2).getName());
		Assert.assertEquals("Shaun", grandSons.get(3).getName());
		Assert.assertEquals("William", grandSons.get(4).getName());
	}

	@Test
	public void getGrandDaughters() {
		List<Person> grandDaughters = RelationProvider.getInstance().getGrandDaughters(new Person("Evan", Gender.Male));
		Assert.assertEquals(2, grandDaughters.size());
		Assert.assertEquals("Ruth", grandDaughters.get(0).getName());
		Assert.assertEquals("Sally", grandDaughters.get(1).getName());
	}

	@Test
	public void getBrothers() {

		List<Person> brothers = RelationProvider.getInstance().getBrothers(new Person("Alex", Gender.Male));
		Assert.assertEquals(2, brothers.size());
		Assert.assertEquals("Joe", brothers.get(0).getName());
		Assert.assertEquals("John", brothers.get(1).getName());
	}

	@Test
	public void getSisters() {
		List<Person> sisters = RelationProvider.getInstance().getSisters(new Person("Alex", Gender.Male));
		Assert.assertEquals(1, sisters.size());
		Assert.assertEquals("Nisha", sisters.get(0).getName());
	}

	@Test
	public void getUncles() {
		List<Person> uncles = RelationProvider.getInstance().getUncles(new Person("Piers", Gender.Male));
		Assert.assertEquals(3, uncles.size());
		Assert.assertEquals("Adam", uncles.get(0).getName());
		Assert.assertEquals("Alex", uncles.get(1).getName());
		Assert.assertEquals("John", uncles.get(2).getName());
	}

	@Test
	public void getAunts() {
		List<Person> aunts = RelationProvider.getInstance().getAunts(new Person("Piers", Gender.Male));
		Assert.assertEquals(2, aunts.size());
		Assert.assertEquals("Nancy", aunts.get(0).getName());
		Assert.assertEquals("Nisha", aunts.get(1).getName());
	}

	@Test
	public void getCousins() {
		List<Person> cousins = RelationProvider.getInstance().getCousins(new Person("Piers", Gender.Male));
		Assert.assertEquals(5, cousins.size());
		Assert.assertEquals("Jacob", cousins.get(0).getName());
		Assert.assertEquals("Paul", cousins.get(1).getName());
		Assert.assertEquals("Ruth", cousins.get(2).getName());
		Assert.assertEquals("Shaun", cousins.get(3).getName());
		Assert.assertEquals("William", cousins.get(4).getName());
	}
}