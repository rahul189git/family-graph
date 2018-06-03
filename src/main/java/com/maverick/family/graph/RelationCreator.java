package com.maverick.family.graph;

import com.maverick.family.db.JDBCUtils;
import com.maverick.family.entity.FamilyGraph;
import com.maverick.family.entity.Gender;
import com.maverick.family.entity.Person;
import com.maverick.family.entity.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
This class has all the utility method to add person`s relatives.
All these will be mutators. It persists added data into DB and update the cache afterwards.
 */
public final class RelationCreator {
	private final static Logger logger = LoggerFactory.getLogger(RelationCreator.class);
	private static final RelationCreator CREATOR = new RelationCreator();
	private static final RelationProvider PROVIDER = RelationProvider.getInstance();
	private static final FamilyGraph GRAPH = FamilyGraph.getInstance();


	public static RelationCreator getInstance() {
		return CREATOR;
	}

	private void createRelation(String fromPerson, String toPerson, Relation relation) {
		Person from = PROVIDER.getPerson(fromPerson);
		Person to = PROVIDER.getPerson(toPerson);
		if (relation == Relation.Spouse && PROVIDER.getSpouse(from) != null) {
			throw new RuntimeException(fromPerson + " is already married.");

		}

		if (from != null && to != null) {
			createRelation(from, to, relation);
		}
	}


	private void createRelation(Person fromPerson, Person toPerson, Relation relation) {
		//Need assertion on gender. Many relationship like father, mother, brother , sisters are derived using gender.
		assert fromPerson.getGender() != null;
		assert toPerson.getGender() != null;

		JDBCUtils.createRelation(fromPerson.getName(), toPerson.getName(), Relation.Child.name());
		createInMemoryGraphRelation(fromPerson, toPerson, relation);


	}

	public void createInMemoryGraphRelation(String fromPerson, String toPerson, Relation relation) {
		createInMemoryGraphRelation(JDBCUtils.getPerson(fromPerson), JDBCUtils.getPerson(toPerson), relation);
	}


	private void createInMemoryGraphRelation(Person fromPerson, Person toPerson, Relation relation) {
		logger.info("Adding " + fromPerson.getName() + " " + relation.name() + " as " + toPerson.getName());
		//Populate In Memory Graph.
		GRAPH.createRelation(fromPerson, toPerson, relation);

		/*
		If toPerson is child of fromPerson, toPerson will also be child of the spouse of fromPerson.
		 */
		if (relation == Relation.Child) {
			Person spouse = PROVIDER.getSpouse(fromPerson);
			if (spouse != null) {
				GRAPH.createRelation(spouse, toPerson, relation);
			}
		}

		/*
		If toPerson is parent of fromPerson, spouse of toPerson will also be parent of the fromPerson.
		 */
		if (relation == Relation.Parents) {
			Person spouse = PROVIDER.getSpouse(toPerson);
			if (spouse != null) {
				GRAPH.createRelation(fromPerson, spouse, relation);
			}
		}
	}

	private void addPerson(Person person) {
		if (PROVIDER.memberExists(person.getName())) {
			throw new RuntimeException(person.getName() + " already exists.");
		}
		logger.info("Adding person " + person.getName() + " to family.");
		JDBCUtils.addPerson(person.getName(), person.getGender().name());
		GRAPH.addPerson(person);

	}

	public void addSon(String personName, String son) {
		if (!PROVIDER.memberExists(personName)) {
			throw new RuntimeException(personName + " does not exist in family.");
		}

		logger.info("Adding son " + son + " to family.");
		addPerson(new Person(son, Gender.Male));
		createRelation(personName, son, Relation.Child);

	}

	public void addWife(String personName, String wife) {
		if (!PROVIDER.memberExists(personName)) {
			throw new RuntimeException(personName + " does not exist in family.");
		}
		logger.info("Adding wife " + wife + " to family.");
		addPerson(new Person(wife, Gender.Female));
		createRelation(personName, wife, Relation.Spouse);

	}

	public void removeRelation(Person fromPerson, Person toPerson, Relation relation) {
		JDBCUtils.removeRelation(fromPerson.getName(), toPerson.getName());
		GRAPH.removeRelation(fromPerson, toPerson, relation);

	}

	public void removePerson(Person person) {
		JDBCUtils.removePerson(person.getName());
		GRAPH.removePerson(person);
	}


}
