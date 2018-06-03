package com.maverick.family.entity;

import java.util.Objects;

/*
This class is used to model each member connection in graph.
If Diana child is Nisha, then this class will model Nisha connection with Diana in Graph adjacency list.

Diana --> MemberConnection(New Person("Nisha",Relation.Child));

Since Diana will be stored as key, this class is just storing Nisha and its relation with Diana.
 */
public final class PersonConnection {

	private final Person person;
	private final Relation relation;

	public PersonConnection(Person person, Relation relation) {
		this.person = person;
		this.relation = relation;
	}


	public Person getPerson() {
		return person;
	}

	public Relation getRelation() {
		return relation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PersonConnection that = (PersonConnection) o;
		return Objects.equals(person, that.person) &&
				relation == that.relation;
	}

	@Override
	public int hashCode() {

		return Objects.hash(person, relation);
	}

	@Override
	public String toString() {
		return "PersonConnection{" +
				"person=" + person +
				", relation=" + relation +
				'}';
	}
}




