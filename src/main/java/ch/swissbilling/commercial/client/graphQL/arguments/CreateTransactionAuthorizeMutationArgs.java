package ch.swissbilling.commercial.client.graphQL.arguments;

import ch.swissbilling.commercial.client.enums.DebtorTypeEnum;
import ch.swissbilling.commercial.client.enums.GuaranteeTypeEnum;
import ch.swissbilling.commercial.client.enums.TransactionSourceEnum;
import ch.swissbilling.commercial.client.exceptions.SwissbillingCommercialClientException;
import ch.swissbilling.framework.einvoice.models.Envelope;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/***
 * The TransactionCreateModel class
 */
public class CreateTransactionAuthorizeMutationArgs {
    public final int source = TransactionSourceEnum.COMMERCIAL.getValue() + TransactionSourceEnum.COMMERCIAL_CLIENT_API.getValue();
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
    public String serviceIdentifier;
    public String serviceIdentifierInfo;
    public String merchantRef;
    public String date;
    public String description;
    public int guaranteeType;
    public String reservationId;
    public int installmentPlan;

    /***
     * Initializes a new instance of the CreateTransactionAuthorizeMutationArgs class
     */
    public CreateTransactionAuthorizeMutationArgs() {
        this.vatRate = 0;
        this.vatAmount = 0;
        this.serviceIdentifier = "";
        this.serviceIdentifierInfo = "";
        this.description = "";
    }

    /***
     * Initializes a new instance of the CreateTransactionAuthorizeMutationArgs class from the Envelope class
     * @param envelope
     * @throws SwissbillingCommercialClientException
     */
    public CreateTransactionAuthorizeMutationArgs(Envelope envelope) throws SwissbillingCommercialClientException {
        var currency = envelope.getBody().getBill().getHeader().getCurrency();
        if (!currency.equals("CHF")) {
            throw new SwissbillingCommercialClientException(
                    SwissbillingCommercialClientException.CURRENCY_NOT_SUPPORTED);
        }

        this.amountInclVat = envelope.getBody().getBill().getSummary().getTotalAmountDue().doubleValue();
        this.language = envelope.getBody().getBill().getHeader().getLanguage();
        this.merchantRef = envelope.getBody().getDeliveryInfo().getTransactionID();
        this.guaranteeType = GuaranteeTypeEnum.FULL_AMOUNT.getValue();
        this.installmentPlan = 1;
        this.description = "";

        // debtor data
        var receiverAddress = envelope.getBody().getBill().getHeader().getReceiverParty().getPartyType().getAddress();
        this.company = receiverAddress.getCompanyName();
        this.debtorType = (this.company != null && !this.company.isEmpty())
                ? DebtorTypeEnum.BUSINESS.getValue()
                : DebtorTypeEnum.INDIVIDUAL.getValue();
        this.firstName = receiverAddress.getGivenName();
        this.lastName = receiverAddress.getFamilyName();
        this.city = receiverAddress.getCity();
        this.line1 = receiverAddress.getAddress1();
        this.line2 = receiverAddress.getAddress2();
        this.postalCode = receiverAddress.getZIP();
        this.email = receiverAddress.getEmail();
        this.phoneNumber = "";

        var date = envelope.getBody().getBill().getHeader().getDocumentDate().toGregorianCalendar().getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        this.date = sdf.format(date);
    }
}

