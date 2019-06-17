package ch.swissbilling.commercial.client.graphQL;

import io.aexp.nodes.graphql.annotations.GraphQLVariable;
import io.aexp.nodes.graphql.annotations.GraphQLVariables;

public class LoginRequest {
	@GraphQLVariables(value = {
			@GraphQLVariable(name = "email", scalar = "String!"),
			@GraphQLVariable(name = "password", scalar = "String!")
	})
	public LoginResponse login;
}