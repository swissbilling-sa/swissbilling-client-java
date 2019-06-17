package ch.swissbilling.commercial.client.graphQL;

import io.aexp.nodes.graphql.annotations.GraphQLVariable;
import io.aexp.nodes.graphql.annotations.GraphQLVariables;

import java.util.List;

public class TransactionsByBatchIdRequest {
	@GraphQLVariables(value = {
	        @GraphQLVariable(name = "transactionBatchDocumentId", scalar = "String!") })
	public List<TransactionsByBatchIdResponse> transactionsByBatchId;
}
