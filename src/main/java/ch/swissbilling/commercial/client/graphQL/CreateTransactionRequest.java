package ch.swissbilling.commercial.client.graphQL;

import io.aexp.nodes.graphql.annotations.GraphQLVariable;
import io.aexp.nodes.graphql.annotations.GraphQLVariables;

public class CreateTransactionRequest {
	@GraphQLVariables(value = {
			@GraphQLVariable(name = "company", scalar = "String!"), 
			@GraphQLVariable(name = "firstName", scalar = "String!"), 
			@GraphQLVariable(name = "lastName", scalar = "String!"), 
			@GraphQLVariable(name = "line1", scalar = "String!"), 
			@GraphQLVariable(name = "line2", scalar = "String"), 
			@GraphQLVariable(name = "postalCode", scalar = "String!"), 
			@GraphQLVariable(name = "city", scalar = "String!"), 
			@GraphQLVariable(name = "email", scalar = "String"), 
			@GraphQLVariable(name = "phoneNumber", scalar = "String"), 
			@GraphQLVariable(name = "birthdate", scalar = "Date"),
			@GraphQLVariable(name = "debtorType", scalar = "Int!"), 
			@GraphQLVariable(name = "serviceIdentifier", scalar = "String"), 
			@GraphQLVariable(name = "serviceIdentifierInfo", scalar = "String"), 
			@GraphQLVariable(name = "amountInclVat", scalar = "Decimal!"), 
			@GraphQLVariable(name = "vatRate", scalar = "Decimal!"), 
			@GraphQLVariable(name = "vatAmount", scalar = "Decimal!"), 
			@GraphQLVariable(name = "merchantRef", scalar = "String!"),
			@GraphQLVariable(name = "date", scalar = "DateTimeOffset"),
			@GraphQLVariable(name = "description", scalar = "String!"), 
			@GraphQLVariable(name = "language", scalar = "String!"),
			@GraphQLVariable(name = "guaranteeType", scalar = "Int!"), 
			@GraphQLVariable(name = "reservationId", scalar = "String"), 
			@GraphQLVariable(name = "installmentPlan", scalar = "Int!"),
			@GraphQLVariable(name = "source", scalar = "Int!")
			})
	public CreateTransactionResponse createTransaction;
}
