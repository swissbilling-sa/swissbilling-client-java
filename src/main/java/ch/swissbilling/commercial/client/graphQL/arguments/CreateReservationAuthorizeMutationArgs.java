package ch.swissbilling.commercial.client.graphQL.arguments;

/***
 * The request reservation model
 */
public class CreateReservationAuthorizeMutationArgs {
    public double amount;
    public int debtorType;
    public String company;
    public String firstName;
    public String lastName;
    public String line1;
    public String line2;
    public String postalCode;
    public String city;
    public String email;
    public String phoneNumber;
    public String birthDate;
    public String preferredLanguage;
    public String serviceIdentifier;
    public String serviceIdentifierInfo;


    /***
     * Initializes a new instance of the CreateReservationAuthorizeMutationArgs
     */
    public CreateReservationAuthorizeMutationArgs() {
        this.serviceIdentifier = "";
        this.serviceIdentifierInfo = "";
        this.line2 = "";
        this.company = "";
        this.phoneNumber = "";
    }
}
