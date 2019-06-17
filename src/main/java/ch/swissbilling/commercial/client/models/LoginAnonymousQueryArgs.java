package ch.swissbilling.commercial.client.models;

public class LoginAnonymousQueryArgs {
	public String email;
	public String password;

	public LoginAnonymousQueryArgs(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
