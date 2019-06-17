package ch.swissbilling.commercial.client;

import ch.swissbilling.commercial.client.enums.TransactionGuaranteeBehavior;
import ch.swissbilling.commercial.client.exceptions.SwissbillingCommercialClientException;
import ch.swissbilling.commercial.client.graphQL.CreateTransactionResponse;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateReservationAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateTransactionAuthorizeMutationArgs;

import java.io.File;


/***
 * The public class SwissbillingCommercialClient class
 */
public interface ISwissbillingCommercialClient {
    /***
     * Request reservation
     *
     * @param reservation
     * @return
     */
    public String requestReservation(CreateReservationAuthorizeMutationArgs reservation) throws SwissbillingCommercialClientException;

    /***
     * Creates a transaction without pdf invoice
     * @param transaction
     * @return
     */
    public CreateTransactionResponse createTransactionWithoutPdf(CreateTransactionAuthorizeMutationArgs transaction, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException;

    /***
     * Creates a transaction with a pdf invoice
     * @param transaction
     * @param pdfInvoiceFile
     * @return
     */
    public CreateTransactionResponse createTransactionWithPdf(CreateTransactionAuthorizeMutationArgs transaction, File pdfInvoiceFile, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException;

    /***
     * Creates transaction form yellowbill xml file
     * @param xmlFile
     * @return
     */
    public CreateTransactionResponse createTransactionFromYellowbillInvoice(File xmlFile, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException;

    /***
     * Creates transaction form yellowbill xml file and assigns the pdf invoice to it
     * @param xmlFile
     * @param pdfInvoiceFile
     * @return
     */
    public CreateTransactionResponse createTransactionFromYellowbillInvoice(File xmlFile, File pdfInvoiceFile, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException;
}
