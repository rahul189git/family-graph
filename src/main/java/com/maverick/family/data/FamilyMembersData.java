package com.maverick.family.data;

import com.maverick.family.entity.Edge;
import com.maverick.family.entity.Gender;
import com.maverick.family.entity.Person;
import com.maverick.family.entity.Relation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
This class will be used to populateInitialData data in DB once.
 */
public final class FamilyMembersData {

	public static List<Person> getFamilyMembers() {

		Person evan = new Person("Evan", Gender.Male);
		Person diana = new Person("Diana", Gender.Female);

		Person john = new Person("John", Gender.Male);

		Person alex = new Person("Alex", Gender.Male);
		Person nancy = new Person("Nancy", Gender.Female);

		Person joe = new Person("Joe", Gender.Male);
		Person niki = new Person("Niki", Gender.Female);


		Person nisha = new Person("Nisha", Gender.Female);
		Person adam = new Person("Adam", Gender.Male);


		Person jacob = new Person("Jacob", Gender.Male);
		Person rufi = new Person("Rufi", Gender.Female);

		Person shaun = new Person("Shaun", Gender.Male);

		Person piers = new Person("Piers", Gender.Male);
		Person pippa = new Person("Pippa", Gender.Female);


		Person owen = new Person("Owen", Gender.Male);
		Person sally = new Person("Sally", Gender.Female);

		Person niel = new Person("Niel", Gender.Male);
		Person ruth = new Person("Ruth", Gender.Female);


		Person william = new Person("William", Gender.Male);
		Person rose = new Person("Rose", Gender.Female);

		Person bern = new Person("Bern", Gender.Male);


		Person george = new Person("George", Gender.Male);
		Person sophia = new Person("Sophia", Gender.Female);


		Person peter = new Person("Peter", Gender.Male);
		Person sarah = new Person("Sarah", Gender.Female);

		Person paul = new Person("Paul", Gender.Male);
		Person zoe = new Person("Zoe", Gender.Female);

		Person steve = new Person("Steve", Gender.Male);

		Person roger = new Person("Roger", Gender.Male);

		List<Person> personList = Arrays.asList(evan, diana, john, alex, nancy, joe, niki,
				nisha, adam, jacob, rufi, shaun, piers, pippa, owen, sally,
				niel, ruth, william, rose, bern, george, sophia, peter, sarah, paul,
				zoe, steve, roger


		);
		return personList;
	}

	public static List<Edge> getRelationData() {
		List<Edge> edgeList = new ArrayList<>();
		edgeList.add(new Edge("Evan", "Diana", Relation.Spouse));
		edgeList.add(new Edge("Diana", "John", Relation.Child));
		edgeList.add(new Edge("Diana", "Alex", Relation.Child));
		edgeList.add(new Edge("Diana", "Joe", Relation.Child));
		edgeList.add(new Edge("Diana", "Nisha", Relation.Child));

		//Create Alex and Nancy tree
		edgeList.add(new Edge("Alex", "Nancy", Relation.Spouse));
		edgeList.add(new Edge("Nancy", "Jacob", Relation.Child));
		edgeList.add(new Edge("Nancy", "Shaun", Relation.Child));

		edgeList.add(new Edge("Jacob", "Rufi", Relation.Spouse));
		edgeList.add(new Edge("Rufi", "Bern", Relation.Child));
		edgeList.add(new Edge("Rufi", "Sophia", Relation.Child));

		edgeList.add(new Edge("George", "Sophia", Relation.Spouse));

		//Create Joe and Niki tree
		edgeList.add(new Edge("Joe", "Niki", Relation.Spouse));
		edgeList.add(new Edge("Niki", "Piers", Relation.Child));
		edgeList.add(new Edge("Niki", "Sally", Relation.Child));

		edgeList.add(new Edge("Pippa", "Piers", Relation.Spouse));
		edgeList.add(new Edge("Owen", "Sally", Relation.Spouse));


		edgeList.add(new Edge("Pippa", "Sarah", Relation.Child));
		edgeList.add(new Edge("Peter", "Sally", Relation.Spouse));


		//Create Adam and Nisha tree
		edgeList.add(new Edge("Adam", "Nisha", Relation.Spouse));
		edgeList.add(new Edge("Nisha", "Ruth", Relation.Child));
		edgeList.add(new Edge("Nisha", "William", Relation.Child));
		edgeList.add(new Edge("Nisha", "Paul", Relation.Child));

		edgeList.add(new Edge("Niel", "Ruth", Relation.Spouse));
		edgeList.add(new Edge("William", "Rose", Relation.Spouse));
		edgeList.add(new Edge("Rose", "Steve", Relation.Child));

		edgeList.add(new Edge("Paul", "Zoe", Relation.Spouse));
		edgeList.add(new Edge("Zoe", "Roger", Relation.Child));

		return edgeList;


	}
}
