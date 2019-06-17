package ch.swissbilling.commercial.client.graphQL;

import io.aexp.nodes.graphql.annotations.GraphQLVariable;
import io.aexp.nodes.graphql.annotations.GraphQLVariables;

public class CreateReservationRequest {
	@GraphQLVariables(value = {
			@GraphQLVariable(name = "company", scalar = "String!"),
			@GraphQLVariable(name = "firstName", scalar = "String!"),
			@GraphQLVariable(name = "lastName", scalar = "String!"), 
			@GraphQLVariable(name = "line1", scalar = "String!"), 
			@GraphQLVariable(name = "line2", scalar = "String!"), 
			@GraphQLVariable(name = "postalCode", scalar = "String!"), 
			@GraphQLVariable(name = "city", scalar = "String!"), 
			@GraphQLVariable(name = "email", scalar = "String"), 
			@GraphQLVariable(name = "phoneNumber", scalar = "String"), 
			@GraphQLVariable(name = "birthdate", scalar = "Date"), 
			@GraphQLVariable(name = "preferredLanguage", scalar = "String"), 
			@GraphQLVariable(name = "debtorType", scalar = "Int!"), 
			@GraphQLVariable(name = "serviceIdentifier", scalar = "String"), 
			@GraphQLVariable(name = "serviceIdentifierInfo", scalar = "String"), 
			@GraphQLVariable(name = "amount", scalar = "Decimal!")
	})
	public CreateReservationResponse createReservation;
}
