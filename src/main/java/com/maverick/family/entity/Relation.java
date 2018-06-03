package com.maverick.family.entity;

/*
Enum class for modelling relation between immediate family members.
As per problem statement, an immediate relation can only be Spouse, Parents, Child.

All other relations can be deduced from these three relations and by using person gender.
 */
public enum Relation {
	Spouse,
	Parents,
	Child;

	static {
		Parents.inverse = Child;
		Child.inverse = Parents;
		Spouse.inverse = Spouse;
	}

	private Relation inverse;

	public Relation reverse() {
		return inverse;
	}
}
