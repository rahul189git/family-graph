package com.maverick.family.entity;

import com.maverick.family.db.JDBCUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
This class is used to model family member and their relation.
Family members are nodes in graph and each person is linked to other through PersonConnection in adjacency list.

This class also works as cache to avoid DB hit for all get operations.
Class properties are populated at JVM-Start from DB.

Any add operation first happen in DB and this in-memory cache is updated with the returned value.
 */
public final class FamilyGraph {
	private static final FamilyGraph instance = new FamilyGraph();
	private final Map<String, Person> personMap;
	private final Map<Person, Set<PersonConnection>> adjList;

	private FamilyGraph() {
		personMap = new HashMap<>();
		adjList = new HashMap<>();
	}

	public static FamilyGraph getInstance() {
		return instance;
	}

	private void addPerson(String personName, Gender gender) {
		personMap.put(personName, new Person(personName, gender));
	}

	public final void addPerson(Person person) {
		addPerson(person.getName(), person.getGender());
	}

	public final void addPersons(Collection<Person> persons) {
		for (Person person : persons) {
			addPerson(person);
		}
	}

	public final void removePerson(Person person) {
		personMap.remove(person.getName());
	}

	public final void createRelation(Person fromPerson, Person toPerson, Relation relation) {
		adjList.putIfAbsent(fromPerson, new HashSet<>());
		adjList.putIfAbsent(toPerson, new HashSet<>());

		adjList.get(fromPerson).add(new PersonConnection(toPerson, relation));
		adjList.get(toPerson).add(new PersonConnection(fromPerson, relation.reverse()));
	}

	public final void removeRelation(Person fromPerson, Person toPerson, Relation relation) {
		Set<PersonConnection> edgesOfV1 = adjList.get(fromPerson);
		Set<PersonConnection> edgesOfV2 = adjList.get(toPerson);

		if (edgesOfV1 != null) edgesOfV1.remove(new PersonConnection(toPerson, relation));
		if (edgesOfV2 != null) edgesOfV2.remove(new PersonConnection(toPerson, relation.reverse()));

	}

	public final Set<PersonConnection> getAdjConnections(Person person) {
		return Collections.unmodifiableSet(adjList.get(person));
	}

	public final Set<Person> getAdjPersons(Person person) {
		return Collections.unmodifiableSet(
				getAdjConnections(person).stream()
						.map(PersonConnection::getPerson)
						.collect(Collectors.toSet()));
	}

	public final Map<Person, Set<PersonConnection>> getAdjList() {
		return Collections.unmodifiableMap(adjList);
	}

	public final Map<String, Person> getPersonMap() {
		return Collections.unmodifiableMap(personMap);
	}






}