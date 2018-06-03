package com.maverick.family.db;

import com.maverick.family.data.FamilyMembersData;
import com.maverick.family.entity.Edge;
import com.maverick.family.entity.Gender;
import com.maverick.family.entity.Person;
import com.maverick.family.entity.Relation;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class JDBCUtilsTest {
	@BeforeClass
	public static void setUp() {
		List<Person> familyMembers = FamilyMembersData.getFamilyMembers();
		List<Edge> edges = FamilyMembersData.getRelationData();

		JDBCUtils.createTables();
		JDBCUtils.initSchema(familyMembers, edges);
	}


	@Test
	public void should_be_able_to_add_and_remove_person() {
		JDBCUtils.addPerson("Rahul", Gender.Male.name());
		Assert.assertTrue(JDBCUtils.memberExists("Rahul"));

		JDBCUtils.removePerson("Rahul");
		Assert.assertFalse(JDBCUtils.memberExists("Rahul"));
	}


	@Test
	public void should_be_able_to_create_and_delete_relation() {
		JDBCUtils.createRelation("Rahul", "Amit", Relation.Child.name());

		Relation relation = JDBCUtils.getRelation("Rahul", "Amit");
		Assert.assertEquals(Relation.Child, relation);

		JDBCUtils.removeRelation("Rahul", "Amit");
		relation = JDBCUtils.getRelation("Rahul", "Amit");
		Assert.assertNull(relation);
	}

	@Test
	public void should_be_to_get_relation() {
		Relation relation = JDBCUtils.getRelation("Alex", "Nancy");
		Assert.assertEquals(Relation.Spouse, relation);
	}

	@Test
	public void should_be_to_get_person() {
		Person alex = JDBCUtils.getPerson("Alex");
		Assert.assertEquals("Alex", alex.getName());
		Assert.assertEquals(Gender.Male, alex.getGender());
	}

	@Test
	public void should_return_all_members() {
		List<Person> members = JDBCUtils.getAllMembers();
		Assert.assertEquals(29, members.size());
	}

	@Test
	public void should_return_all_relation() {
		List<Edge> relations = JDBCUtils.getRelations();
		Assert.assertEquals(28, relations.size());
	}

	@Test
	public void should_return_relation_for_given_member() {
		List<Edge> relations = JDBCUtils.getRelations("Nancy");
		Assert.assertEquals(2, relations.size());
	}

	@Test
	public void should_return_true_when_person_exists() {
		Assert.assertTrue(JDBCUtils.memberExists("Alex"));
	}
}