package client.example;

import ch.swissbilling.commercial.client.ISwissbillingCommercialClient;
import ch.swissbilling.commercial.client.SwissbillingCommercialClient;
import ch.swissbilling.commercial.client.enums.DebtorTypeEnum;
import ch.swissbilling.commercial.client.enums.GuaranteeTypeEnum;
import ch.swissbilling.commercial.client.enums.SwissbillingEnvironment;
import ch.swissbilling.commercial.client.enums.TransactionGuaranteeBehavior;
import ch.swissbilling.commercial.client.exceptions.SwissbillingCommercialClientException;
import ch.swissbilling.commercial.client.graphQL.CreateTransactionResponse;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateReservationAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateTransactionAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.settings.SwissbillingClientSettings;

import java.io.File;
import java.util.logging.Logger;

public class Main {
    /***
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static ISwissbillingCommercialClient _client;

    /***
     * client.example.Main method
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Setup Swissbilling client
        var username = "";
        var password = "";
        var settings = new SwissbillingClientSettings(username, password, SwissbillingEnvironment.DEV);
        _client = new SwissbillingCommercialClient(settings);

        var transactionGuaranteeBehavior = TransactionGuaranteeBehavior.CONFIRM_NO_GUARANTEE_IF_NO_AVAILABLE;

        var reservationId = requestReservationExample();
        var transactionWithoutPdf = createTransactionWithoutPdfExample(transactionGuaranteeBehavior);
        var pdfTransaction = createTransactionWithPdfExample(transactionGuaranteeBehavior);
        var yellowbillTransaction = createTransactionFromYellowbillExample(transactionGuaranteeBehavior);
        var yellowbillTransactionWithPdf = createTransactionFromYellowbillWithPdfExample(transactionGuaranteeBehavior);
    }

    private static String requestReservationExample() throws SwissbillingCommercialClientException {
        var reservation = new CreateReservationAuthorizeMutationArgs();
        reservation.debtorType = DebtorTypeEnum.INDIVIDUAL.getValue();
        reservation.amount = 30.00;
        reservation.firstName = "Henri";
        reservation.lastName = "Combes";
        reservation.city = "Le Vaud";
        reservation.line1 = "Ch. du Mottaz 2";
        reservation.postalCode = "1261";
        reservation.email = "henri.combes@swissbilling.ch";
        reservation.preferredLanguage = "fr";

        return _client.requestReservation(reservation);
    }

    private static CreateTransactionResponse createTransactionWithoutPdfExample(TransactionGuaranteeBehavior transactionGuaranteeBehavior) throws SwissbillingCommercialClientException {
        var transaction = new CreateTransactionAuthorizeMutationArgs();

        // transaction data
        transaction.amountInclVat = 500.1;
        transaction.vatAmount = 10.0;
        transaction.vatRate = 0.4;
        transaction.language = "en";
        transaction.merchantRef = "CH03";
        transaction.guaranteeType = GuaranteeTypeEnum.FULL_AMOUNT.getValue();
        transaction.reservationId = null;
        transaction.installmentPlan = 1;

        // transaction debtor data
        transaction.debtorType = DebtorTypeEnum.INDIVIDUAL.getValue();
        transaction.company = "";
        transaction.firstName = "Henri";
        transaction.lastName = "Combes";
        transaction.city = "Le Vaud";
        transaction.line1 = "Ch. du Mottaz 2";
        transaction.line2 = "";
        transaction.postalCode = "1261";
        transaction.email = "henri.combes@swissbilling.ch";

        return _client.createTransactionWithoutPdf(transaction, transactionGuaranteeBehavior);
    }

    private static CreateTransactionResponse createTransactionWithPdfExample(TransactionGuaranteeBehavior transactionGuaranteeBehavior) throws SwissbillingCommercialClientException {
        var transaction = new CreateTransactionAuthorizeMutationArgs();

        // transaction data
        transaction.amountInclVat = 55.8;
        transaction.vatAmount = 10.0;
        transaction.vatRate = 0.4;
        transaction.language = "en";
        transaction.merchantRef = "CH03";
        transaction.guaranteeType = GuaranteeTypeEnum.NONE.getValue();
        transaction.reservationId = null;
        transaction.installmentPlan = 1;

        // transaction debtor data
        transaction.debtorType = DebtorTypeEnum.INDIVIDUAL.getValue();
        transaction.company = "";
        transaction.firstName = "Henri";
        transaction.lastName = "Combes";
        transaction.city = "Le Vaud";
        transaction.line1 = "Ch. du Mottaz 2";
        transaction.line2 = "";
        transaction.postalCode = "1261";
        transaction.email = "henri.combes@swissbilling.ch";

        var pdfInvoicePath = getFullPath("Invoice-458055.pdf");
        var pdfInvoiceFile = new File(pdfInvoicePath);

        return _client.createTransactionWithPdf(transaction, pdfInvoiceFile, transactionGuaranteeBehavior);
    }

    public static CreateTransactionResponse createTransactionFromYellowbillExample(TransactionGuaranteeBehavior transactionGuaranteeBehavior) throws SwissbillingCommercialClientException {
        var yellowbillPath = getFullPath("Yellowbill.xml");
        var xmlFile = new File(yellowbillPath);

        return _client.createTransactionFromYellowbillInvoice(xmlFile, transactionGuaranteeBehavior);
    }

    private static CreateTransactionResponse createTransactionFromYellowbillWithPdfExample(TransactionGuaranteeBehavior transactionGuaranteeBehavior) throws SwissbillingCommercialClientException {
        var yellowbillPath = getFullPath("Yellowbill.xml");
        var pdfInvoicePath = getFullPath("Invoice-458055.pdf");
        var xmlFile = new File(yellowbillPath);
        var pdfInvoiceFile = new File(pdfInvoicePath);

        return _client.createTransactionFromYellowbillInvoice(xmlFile, pdfInvoiceFile, transactionGuaranteeBehavior);
    }

    private static String getFullPath(String resourceFile) {
        ClassLoader classLoader = Main.class.getClassLoader();
        return classLoader.getResource(resourceFile).getPath();
    }

}
