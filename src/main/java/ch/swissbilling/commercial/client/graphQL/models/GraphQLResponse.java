package ch.swissbilling.commercial.client.graphQL.models;

import java.util.List;

public class GraphQLResponse<T> {
	public T data;
	public List<GraphQLError> errors;
}
