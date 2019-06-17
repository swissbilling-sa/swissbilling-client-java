package ch.swissbilling.commercial.client.graphQL;

import io.aexp.nodes.graphql.annotations.GraphQLVariable;
import io.aexp.nodes.graphql.annotations.GraphQLVariables;

public class ConfirmTransactionNotGuaranteedRequest {

	
	@GraphQLVariables(value = { @GraphQLVariable(name = "id", scalar = "String!") })
	public ConfirmTransactionNotGuaranteedResponse confirmTransactionNotGuaranteed;
}
