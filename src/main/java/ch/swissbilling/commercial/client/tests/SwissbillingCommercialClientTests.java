package ch.swissbilling.commercial.client.tests;

import ch.swissbilling.commercial.client.ISwissbillingCommercialClient;
import ch.swissbilling.commercial.client.SwissbillingCommercialClient;
import ch.swissbilling.commercial.client.enums.DebtorTypeEnum;
import ch.swissbilling.commercial.client.enums.GuaranteeTypeEnum;
import ch.swissbilling.commercial.client.enums.SwissbillingEnvironment;
import ch.swissbilling.commercial.client.enums.TransactionGuaranteeBehavior;
import ch.swissbilling.commercial.client.exceptions.SwissbillingCommercialClientException;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateReservationAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateTransactionAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.settings.SwissbillingClientSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class SwissbillingCommercialClientTests {
    static ISwissbillingCommercialClient _client;

    @BeforeAll
    static void beforeAll() {
        var username = "";
        var password = "";
        var settings = new SwissbillingClientSettings(username, password, SwissbillingEnvironment.DEV);

        try {
            _client = new SwissbillingCommercialClient(settings);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void requestReservationTest() {
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

        try {
            var reservationId = _client.requestReservation(reservation);
            assertTrue(reservationId != null && !reservationId.isEmpty());
        } catch (SwissbillingCommercialClientException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void createTransactionWithoutPdfTest() {
        var transaction = new CreateTransactionAuthorizeMutationArgs();

        // transaction data
        transaction.amountInclVat = 40;
        transaction.vatAmount = 10.0;
        transaction.vatRate = 0.4;
        transaction.language = "en";
        transaction.merchantRef = "CH04";
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

        try {
            var createTransactionResponse = _client.createTransactionWithoutPdf(transaction,
                    TransactionGuaranteeBehavior.CONFIRM_NO_GUARANTEE_IF_NO_AVAILABLE);
            assertNotNull(createTransactionResponse);
        } catch (SwissbillingCommercialClientException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void createTransactionWithPdfTest() {
        var transaction = new CreateTransactionAuthorizeMutationArgs();

        // transaction data
        transaction.amountInclVat = 55.8;
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

        var pdfInvoicePath = getFullPath("Invoice-458055.pdf");
        var pdfInvoiceFile = new File(pdfInvoicePath);

        try {
            var createTransactionResponse = _client.createTransactionWithPdf(transaction, pdfInvoiceFile,
                    TransactionGuaranteeBehavior.CONFIRM_NO_GUARANTEE_IF_NO_AVAILABLE);
            assertNotNull(createTransactionResponse);
        } catch (SwissbillingCommercialClientException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void createTransactionFromYellowbillTest() {
        var yellowbillPath = getFullPath("Yellowbill.xml");
        var xmlFile = new File(yellowbillPath);

        try {
            var createTransactionResponse = _client.createTransactionFromYellowbillInvoice(xmlFile,
                    TransactionGuaranteeBehavior.CONFIRM_NO_GUARANTEE_IF_NO_AVAILABLE);
            assertNotNull(createTransactionResponse);
        } catch (SwissbillingCommercialClientException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void createTransactionFromYellowbillWithPdfTest() {
        var yellowbillPath = getFullPath("Yellowbill.xml");
        var pdfInvoicePath = getFullPath("Invoice-458055.pdf");
        var xmlFile = new File(yellowbillPath);
        var pdfInvoiceFile = new File(pdfInvoicePath);

        try {
            var createTransactionResponse = _client.createTransactionFromYellowbillInvoice(xmlFile, pdfInvoiceFile,
                    TransactionGuaranteeBehavior.CONFIRM_NO_GUARANTEE_IF_NO_AVAILABLE);
            assertNotNull(createTransactionResponse);
        } catch (SwissbillingCommercialClientException e) {
            e.printStackTrace();
            fail();
        }
    }

    private static String getFullPath(String resourceFile) {
        ClassLoader classLoader = SwissbillingCommercialClientTests.class.getClassLoader();
        return classLoader.getResource(resourceFile).getPath();
    }
}
