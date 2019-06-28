package ch.swissbilling.commercial.client.graphQL.arguments;

import ch.swissbilling.commercial.client.enums.TransactionSourceEnum;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateTransactionAuthorizeMutationArgs;

public class UpdateTransactionAuthorizeMutationArgs {
    public final int source = TransactionSourceEnum.COMMERCIAL.getValue() + TransactionSourceEnum.COMMERCIAL_CLIENT_API.getValue();
    public String id;
    public String company;
    public String firstName;
    public String lastName;
    public String line1;
    public String line2;
    public String postalCode;
    public String city;
    public int debtorType;
    public double amountInclVat;
    public double vatRate;
    public double vatAmount;
    public String email;
    public String phoneNumber;
    public String birthDate;
    public String language;
    public String preferredLanguage;
    public String serviceIdentifier;
    public String serviceIdentifierInfo;
    public String merchantRef;
    public String description;
    public String date;
    public int guaranteeType;
    public String reservationId;
    public int installmentPlan;

    public UpdateTransactionAuthorizeMutationArgs(){
    }

    public UpdateTransactionAuthorizeMutationArgs(String transactionId, CreateTransactionAuthorizeMutationArgs transaction) {
        this.id=transactionId;
        this.company = transaction.company;
        this.firstName = transaction.firstName;
        this.lastName = transaction.lastName;
        this.line1 = transaction.line1;
        this.line2 = transaction.line2;
        this.postalCode = transaction.postalCode;
        this.city = transaction.city;
        this.debtorType = transaction.debtorType;
        this.amountInclVat = transaction.amountInclVat;
        this.vatRate = transaction.vatRate;
        this.vatAmount = transaction.vatAmount;
        this.email = transaction.email;
        this.phoneNumber = transaction.phoneNumber;
        this.birthDate = transaction.birthDate;
        this.language = transaction.language;
        this.preferredLanguage = transaction.language;
        this.serviceIdentifier = transaction.serviceIdentifier;
        this.serviceIdentifierInfo = transaction.serviceIdentifierInfo;
        this.merchantRef = transaction.merchantRef;
        this.description = transaction.description;
        this.date = transaction.date;
        this.guaranteeType = transaction.guaranteeType;
        this.reservationId = transaction.reservationId;
        this.installmentPlan = transaction.installmentPlan;
    }
}

