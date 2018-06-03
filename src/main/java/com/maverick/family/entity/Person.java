package com.maverick.family.entity;

import java.util.Objects;
/*
This class represents family members.
 */
public final class Person implements Comparable<Person> {
	private final String name;
	private final Gender gender;

	public Person(String name, Gender gender) {
		this.name = name;
		this.gender = gender;
	}


	public String getName() {
		return name;
	}

	public Gender getGender() {
		return gender;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return Objects.equals(name, person.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public String toString() {
		return "Person{" +
				"name='" + name + '\'' +
				", gender=" + gender +
				'}';
	}

	@Override
	public int compareTo(Person o) {
		return name.compareTo(o.name);
	}
}