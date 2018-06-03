package com.maverick.family.entity;

import java.util.Objects;

/*
This class is used to model DB relation table each row.
 */
public class Edge {
	private final String fromPerson;
	private final String toPerson;
	private final Relation relation;

	public Edge(String fromPerson, String toPerson, Relation relation) {
		this.fromPerson = fromPerson;
		this.toPerson = toPerson;
		this.relation = relation;
	}

	public String getFromPerson() {
		return fromPerson;
	}

	public String getToPerson() {
		return toPerson;
	}

	public Relation getRelation() {
		return relation;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return Objects.equals(fromPerson, edge.fromPerson) &&
				Objects.equals(toPerson, edge.toPerson) &&
				relation == edge.relation;
	}

	@Override
	public int hashCode() {

		return Objects.hash(fromPerson, toPerson, relation);
	}
}
