package com.maverick.family.graph;

import com.maverick.family.entity.FamilyGraph;
import com.maverick.family.entity.Gender;
import com.maverick.family.entity.Person;
import com.maverick.family.entity.PersonConnection;
import com.maverick.family.entity.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/*
This class has all the utility method to return person`s relatives.
All these only need accessors and can use in-memory cache of FamilyGraph object.
This will avoid DB hit for improved performance.
 */

public final class RelationProvider {
	private final static Logger logger = LoggerFactory.getLogger(RelationProvider.class);

	private static final RelationProvider RELATION_PROVIDER = new RelationProvider();
	private static final FamilyGraph GRAPH;
	private static final Map<Person, Set<PersonConnection>> ADJ_LIST;

	static {
		GRAPH = FamilyGraph.getInstance();
		ADJ_LIST = GRAPH.getAdjList();
	}

	public static RelationProvider getInstance() {
		return RELATION_PROVIDER;
	}

	private static <T> Collector<T, ?, T> toSingleton() {
		return Collectors.collectingAndThen(
				Collectors.toList(),
				list -> {
					if (list.size() == 0) {
						return null;
					}
					return list.get(0);
				}
		);
	}

	public Person getFather(Person person) {
		logger.info("Getting father of " + person.getName());
		return Optional.ofNullable(ADJ_LIST.get(person))
				.orElseGet(Collections::emptySet)
				.stream()
				.filter(c -> c.getRelation() == Relation.Parents && c.getPerson().getGender() == Gender.Male)
				.map(PersonConnection::getPerson)
				.collect(toSingleton());
	}

	public Person getMother(Person person) {
		logger.info("Getting mother of " + person.getName());
		Person father = getFather(person);
		if (father == null) {
			return null;
		}
		return getSpouse(father);

	}

	private List<Person> getChildren(Person person) {
		logger.info("Getting children of " + person.getName());
		return Optional.ofNullable(ADJ_LIST.get(person))
				.orElseGet(Collections::emptySet).stream()
				.filter(c -> c.getRelation() == Relation.Child)
				.map(PersonConnection::getPerson)
				.sorted()
				.collect(Collectors.toList());
	}

	public List<Person> getSons(Person person) {
		logger.info("Getting sons of " + person.getName());
		return Optional.ofNullable(ADJ_LIST.get(person))
				.orElseGet(Collections::emptySet).stream()
				.filter(c -> c.getRelation() == Relation.Child && c.getPerson().getGender() == Gender.Male)
				.map(PersonConnection::getPerson)
				.sorted()
				.collect(Collectors.toList());

	}

	public List<Person> getDaughters(Person person) {
		logger.info("Getting daughters of " + person.getName());
		return Optional.ofNullable(ADJ_LIST.get(person))
				.orElseGet(Collections::emptySet)
				.stream()
				.filter(c -> c.getRelation() == Relation.Child && c.getPerson().getGender() == Gender.Female)
				.map(PersonConnection::getPerson)
				.sorted()
				.collect(Collectors.toList());

	}

	public Person getGrandFather(Person person) {
		logger.info("Getting grandfather of " + person.getName());
		Person grandFather = null;
		Person father = getFather(person);
		if (father != null) {
			grandFather = getFather(father);
		}
		if (grandFather == null) {
			Person mother = getMother(person);
			if (mother != null) {
				grandFather = getFather(mother);
			}
		}
		return grandFather;
	}

	public Person getGrandMother(Person person) {
		logger.info("Getting grandmother of " + person.getName());
		Person grandFather = getGrandFather(person);
		if (grandFather != null) {
			return getSpouse(grandFather);
		}
		return null;

	}

	public Person getSpouse(Person person) {
		logger.info("Getting spouse of " + person.getName());
		return Optional.ofNullable(ADJ_LIST.get(person))
				.orElseGet(Collections::emptySet)
				.stream()
				.filter(c -> c.getRelation() == Relation.Spouse)
				.map(PersonConnection::getPerson)
				.collect(toSingleton());
	}

	public List<Person> getGrandSons(Person person) {
		logger.info("Getting grandsons of " + person.getName());
		List<Person> grandSons = new ArrayList<>();
		for (Person child : getSons(person)) {
			grandSons.addAll(getSons(child));
		}
		for (Person child : getDaughters(person)) {
			grandSons.addAll(getSons(child));
		}
		Collections.sort(grandSons);
		return grandSons;
	}

	public ArrayList<Person> getGrandDaughters(Person person) {
		logger.info("Getting grandfathers of " + person.getName());
		ArrayList<Person> grandDaughters = new ArrayList<>();
		for (Person child : getSons(person)) {
			grandDaughters.addAll(getDaughters(child));
		}
		for (Person child : getDaughters(person)) {
			grandDaughters.addAll(getDaughters(child));
		}
		Collections.sort(grandDaughters);
		return grandDaughters;
	}

	public List<Person> getBrothers(Person person) {
		logger.info("Getting brothers of " + person.getName());
		List<Person> brothers = new ArrayList<>();
		Person father = getFather(person);
		if (father == null) {
			return Collections.emptyList();
		}
		for (Person child : getSons(father)) {
			if (!child.equals(person)) {
				brothers.add(child);
			}
		}
		Collections.sort(brothers);
		return brothers;
	}

	public List<Person> getSisters(Person person) {
		logger.info("Getting sisters of " + person.getName());
		List<Person> sisters = new ArrayList<>();
		Person father = getFather(person);
		if (father == null) {
			return Collections.emptyList();
		}
		for (Person child : getDaughters(father)) {
			if (!child.equals(person)) {
				sisters.add(child);
			}
		}
		Collections.sort(sisters);
		return sisters;
	}

	public List<Person> getUncles(Person person) {
		logger.info("Getting uncles of " + person.getName());
		List<Person> uncles = new ArrayList<>();
		Person father = getFather(person);
		if (father == null) {
			return Collections.emptyList();
		}
		Person mother = getMother(person);


		if (getBrothers(father) != null) {
			uncles.addAll(getBrothers(father));
		}
		if (getBrothers(Objects.requireNonNull(mother)) != null) {
			uncles.addAll(getBrothers(mother));
		}

		Optional.ofNullable(getSisters(father)).ifPresent(l -> l.forEach(u -> {
			Person spouse = getSpouse(u);
			if (spouse != null) {
				uncles.add(spouse);
			}

		}));

		Optional.ofNullable(getSisters(mother)).ifPresent(l -> l.forEach(u -> {
			Person spouse = getSpouse(u);
			if (spouse != null) {
				uncles.add(spouse);
			}

		}));
		Collections.sort(uncles);
		return uncles;
	}

	public List<Person> getAunts(Person person) {
		logger.info("Getting aunts of " + person.getName());
		List<Person> aunts = new ArrayList<>();

		Optional.ofNullable(getUncles(person)).ifPresent(l -> l.forEach(u -> {
			Person spouse = getSpouse(u);
			if (spouse != null) {
				aunts.add(spouse);
			}

		}));
		Collections.sort(aunts);
		return aunts;
	}

	public List<Person> getCousins(Person person) {
		logger.info("Getting cousins of " + person.getName());
		List<Person> cousins = new ArrayList<>();

		Optional.ofNullable(getUncles(person)).ifPresent(l -> l.forEach(u -> {
			List<Person> children = getChildren(u);
			if (children != null && !children.isEmpty()) {
				cousins.addAll(children);
			}

		}));

		Collections.sort(cousins);
		return cousins;
	}

	public final boolean memberExists(String personName) {
		return GRAPH.getPersonMap().containsKey(personName);
	}

	public Person getPerson(String personName) {
		return GRAPH.getPersonMap().get(personName);
	}
}
