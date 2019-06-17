package ch.swissbilling.commercial.client.graphQL.models;

import java.util.List;

public class GraphQLError {
	public String message;
	public List<String> path;
	public List<String> locations;
	public List<String> extensions;
}
