package com.maverick.family.graph;

import com.maverick.family.data.FamilyMembersData;
import com.maverick.family.db.JDBCUtils;
import com.maverick.family.entity.Edge;
import com.maverick.family.entity.FamilyGraph;
import com.maverick.family.entity.Person;

import java.util.List;

/*
This class is created to populate database and graph based cache at JVM-Start.
 */
public final class DataPopulator {

	public static void populateInitialData() {
		List<Person> familyMembers = FamilyMembersData.getFamilyMembers();
		List<Edge> edges = FamilyMembersData.getRelationData();

		//Create records in DB.
		JDBCUtils.createTables();
		JDBCUtils.initSchema(familyMembers, edges);

		//Create im-memory cache.
		FamilyGraph graph = FamilyGraph.getInstance();
		graph.addPersons(familyMembers);
		edges.forEach(edge -> RelationCreator.getInstance().createInMemoryGraphRelation(
				edge.getFromPerson(),
				edge.getToPerson(),
				edge.getRelation()));

	}

}
