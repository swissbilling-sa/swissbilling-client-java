package ch.swissbilling.commercial.client;

import ch.swissbilling.commercial.client.endpoints.*;
import ch.swissbilling.commercial.client.enums.GraphQLQueryTypesEnum;
import ch.swissbilling.commercial.client.enums.TransactionGuaranteeBehavior;
import ch.swissbilling.commercial.client.enums.TransactionStatusEnum;
import ch.swissbilling.commercial.client.exceptions.SwissbillingCommercialClientException;
import ch.swissbilling.commercial.client.graphQL.*;
import ch.swissbilling.commercial.client.graphQL.arguments.ConfirmTransactionNotGuaranteedAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateReservationAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.graphQL.arguments.CreateTransactionAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.graphQL.arguments.TransactionsByBatchIdAuthorizeQueryArgs;
import ch.swissbilling.commercial.client.graphQL.models.GraphQLError;
import ch.swissbilling.commercial.client.graphQL.models.GraphQLQuery;
import ch.swissbilling.commercial.client.graphQL.models.GraphQLResponse;
import ch.swissbilling.commercial.client.models.LoginAnonymousQueryArgs;
import ch.swissbilling.commercial.client.graphQL.arguments.UpdateTransactionAuthorizeMutationArgs;
import ch.swissbilling.commercial.client.settings.SwissbillingClientSettings;
import ch.swissbilling.commercial.client.utils.HttpHelper;
import ch.swissbilling.commercial.client.utils.SignalRHelper;
import ch.swissbilling.framework.einvoice.services.IYellowbillInvoice2Service;
import ch.swissbilling.framework.einvoice.services.YellowbillInvoice2Service;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * The public class SwissbillingCommercialClient class
 */
public class SwissbillingCommercialClient implements ISwissbillingCommercialClient {

    /***
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(SwissbillingCommercialClient.class.getName());

    /***
     * Client settings
     */
    private final SwissbillingClientSettings _settings;
    /***
     * The http helper
     */
    private final HttpHelper _httpHelper;
    /***
     * The signalR helper
     */
    private final SignalRHelper _signalRHelper;
    /***
     * The IYellowbillInvoice2 service
     */
    private final IYellowbillInvoice2Service _yellowbillInvoice2Service;
    /***
     * Http bearer token
     */
    private String _token;
    /***
     * The merchant's shop id
     */
    private String _shopId;
    /***
     * The environment specific endpoints
     */
    private IEnvironmentEndpoints _endpoints;

    /***
     * Initializes a new instance of the SwissbillingCommercialClient
     * @param settings
     * @throws Exception
     */
    public SwissbillingCommercialClient(SwissbillingClientSettings settings) throws Exception {
        this._settings = settings;

        this.configureEndpoints();
        if (this._endpoints == null) {
            LOGGER.log(Level.SEVERE, SwissbillingCommercialClientException.FAIL_TO_CONFIGURE_ENDPOINTS);
            throw new SwissbillingCommercialClientException(
                    SwissbillingCommercialClientException.FAIL_TO_CONFIGURE_ENDPOINTS);
        }

        this._httpHelper = new HttpHelper(this._endpoints);
        this._signalRHelper = new SignalRHelper(this._endpoints);

        var isLoggedIn = this.login();
        if (!isLoggedIn) {
            LOGGER.log(Level.SEVERE, SwissbillingCommercialClientException.LOGIN_FAILED);
            throw new SwissbillingCommercialClientException(SwissbillingCommercialClientException.LOGIN_FAILED);
        }

        var hasShop = this.getShopId();
        if (!hasShop) {
            LOGGER.log(Level.SEVERE, SwissbillingCommercialClientException.NO_SHOP);
            throw new SwissbillingCommercialClientException(SwissbillingCommercialClientException.NO_SHOP);
        }

        this._yellowbillInvoice2Service = new YellowbillInvoice2Service();

        LOGGER.log(Level.INFO, "The SwissbillingCommercialClient is successfully initialized.");
    }

    //<editor-fold desc="Public methods">

    /**
     * Request reservation
     * @param reservation
     * @return
     * @throws SwissbillingCommercialClientException
     */
    public String requestReservation(CreateReservationAuthorizeMutationArgs reservation) throws SwissbillingCommercialClientException {
        LOGGER.log(Level.INFO, String.format(
                "Request a new reservation for %s %s %s for %s", reservation.firstName, reservation.lastName,
                reservation.company, Double.toString(reservation.amount)));

        var createReservationMutation = new GraphQLQuery(GraphQLQueryTypesEnum.MUTATION, this._endpoints.getGraphQlAuthorize(),
                CreateReservationRequest.class);
        createReservationMutation.mapArguments(reservation);

        var createReservationResponseRaw = this._httpHelper.sendQuery(createReservationMutation, this._token);
        var createReservationRequest = this.mapJson(createReservationResponseRaw, new TypeReference<GraphQLResponse<CreateReservationRequest>>() {
        });

        // Verify reservation created
        if (createReservationRequest.data == null || createReservationRequest.data.createReservation == null) {
            LOGGER.log(Level.SEVERE, "Failed to create reservation.");
            throw new SwissbillingCommercialClientException("Failed to create reservation.");
        }

        var reservationId = createReservationRequest.data.createReservation.id;
        LOGGER.log(Level.INFO, String.format("Reservation created: ", reservationId));

        return reservationId;

    }

    /***
     * Creates a transaction without pdf invoice
     * @param transaction
     * @return
     */
    public CreateTransactionResponse createTransactionWithoutPdf(CreateTransactionAuthorizeMutationArgs transaction, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException {
        LOGGER.log(Level.INFO, "Creating transaction without pdf...");

        var createTransactionMutation = new GraphQLQuery(GraphQLQueryTypesEnum.MUTATION, this._endpoints.getGraphQlAuthorize(),
                CreateTransactionRequest.class);
        createTransactionMutation.mapArguments(transaction);

        var createTransactionResponseRaw = this._httpHelper.sendQuery(createTransactionMutation, this._token);
        var createTransactionRequest = this.mapJson(createTransactionResponseRaw, new TypeReference<GraphQLResponse<CreateTransactionRequest>>() {
        });

        // Verify if it has errors
        if (createTransactionRequest.errors != null) {
            LOGGER.log(Level.SEVERE, createTransactionRequest.errors.get(0).message);
            throw new SwissbillingCommercialClientException("Fail to create transaction.");
        }

        // Verify transaction created
        if (createTransactionRequest.data == null || createTransactionRequest.data.createTransaction == null) {
            LOGGER.log(Level.SEVERE, "Fail to create transaction.");
        }

        LOGGER.log(Level.INFO,
                String.format("Transaction created: %s", createTransactionRequest.data.createTransaction.id));

        // Check rejected by prescreening
        if (guaranteeBehavior == TransactionGuaranteeBehavior.CONFIRM_NO_GUARANTEE_IF_NO_AVAILABLE
                && createTransactionRequest.data.createTransaction.status.value == TransactionStatusEnum.REJECTED_BY_PRESCREENING.getValue()) {
            this.confirmNoGuarantee(createTransactionRequest.data.createTransaction.id);
        }

        return createTransactionRequest.data.createTransaction;
    }

    /***
     * Creates a transaction with a pdf invoice
     * @param transaction
     * @param pdfInvoiceFile
     * @return
     */
    public CreateTransactionResponse createTransactionWithPdf(CreateTransactionAuthorizeMutationArgs transaction, File pdfInvoiceFile, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException {
        LOGGER.log(Level.INFO, "Creating transaction with pdf...");

        // Upload PDF invoice
        var pdfTransaction = this.uploadPdfInvoice(pdfInvoiceFile);

        // Update transaction
        var transactionUpdateModel = new UpdateTransactionAuthorizeMutationArgs(pdfTransaction.id, transaction);
        transactionUpdateModel.date = pdfTransaction.date;
        var updatedTransaction = this.updateTransaction(transactionUpdateModel, guaranteeBehavior);

        LOGGER.log(Level.INFO, String.format("Transaction created: %s", updatedTransaction.id));
        return new CreateTransactionResponse(updatedTransaction);
    }

    /***
     * Creates transaction form yellowbill xml file
     * @param xmlFile
     * @return
     */
    public CreateTransactionResponse createTransactionFromYellowbillInvoice(File xmlFile, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException {
        LOGGER.log(Level.INFO, "Creating transaction from yellowbill xml...");

        var yellowBillEnvelope = this._yellowbillInvoice2Service.Deserialize(xmlFile);
        var createTransactionArgs = new CreateTransactionAuthorizeMutationArgs(yellowBillEnvelope);
        var transactionCreateResponse = this.createTransactionWithoutPdf(createTransactionArgs, guaranteeBehavior);

        LOGGER.log(Level.INFO, String.format("Transaction created: %s", transactionCreateResponse.id));
        return transactionCreateResponse;
    }

    /***
     * Creates transaction form yellowbill xml file and assigns the pdf invoice to it
     * @param xmlFile
     * @param pdfInvoiceFile
     * @return
     * @throws SwissbillingCommercialClientException
     */
    public CreateTransactionResponse createTransactionFromYellowbillInvoice(File xmlFile, File pdfInvoiceFile, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException {
        LOGGER.log(Level.INFO, "Creating transaction from yellowbill xml with pdf invoice...");

        var yellowBillEnvelope = this._yellowbillInvoice2Service.Deserialize(xmlFile);
        var createTransactionArgs = new CreateTransactionAuthorizeMutationArgs(yellowBillEnvelope);
        var transactionCreateResponse = this.createTransactionWithPdf(createTransactionArgs, pdfInvoiceFile, guaranteeBehavior);

        LOGGER.log(Level.INFO, String.format("Transaction created: %s", transactionCreateResponse.id));
        return transactionCreateResponse;
    }

    //</editor-fold>

    //<editor-fold desc="Private methods">

    /***
    /***
     * Upload PDF invoice
     *
     * @param pdfInvoiceFile
     * @return
     * @throws IllegalStateException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    private TransactionsByBatchIdResponse uploadPdfInvoice(File pdfInvoiceFile) throws SwissbillingCommercialClientException {
        LOGGER.log(Level.INFO, "Uploading pdf invoice...");

        var transactionBatchDocumentId = this._httpHelper.uploadInvoice(pdfInvoiceFile, this._shopId, this._token);
        LOGGER.log(Level.FINE, "Batch document id recieved: " + transactionBatchDocumentId);

        // Connecting to SignalR and waiting for PDF processing
        LOGGER.log(Level.FINE, "Connecting to SignalR and waiting for PDF processing...");
        this._signalRHelper.ConnectAndWait(transactionBatchDocumentId, this._token);

        // Get transactions by batch document id
        LOGGER.log(Level.FINE, "Get transactions by batch document id...");
        var trasactionsQuery = new GraphQLQuery(GraphQLQueryTypesEnum.QUERY, this._endpoints.getGraphQlAuthorize(),
                TransactionsByBatchIdRequest.class);
        trasactionsQuery.mapArguments(new TransactionsByBatchIdAuthorizeQueryArgs(transactionBatchDocumentId));

        var getTransactionsRequestRaw = this._httpHelper.sendQuery(trasactionsQuery, this._token);
        var transactionsRequest = this.mapJson(getTransactionsRequestRaw, new TypeReference<GraphQLResponse<TransactionsByBatchIdRequest>>() {
        });

        // Check transaction data
        if (transactionsRequest.data == null || transactionsRequest.data.transactionsByBatchId == null) {
            LOGGER.log(Level.SEVERE, "Fail to get transaction by batch document id");
            throw new SwissbillingCommercialClientException("Fail to upload pdf invoice");
        }

        var transactions = transactionsRequest.data.transactionsByBatchId;
        LOGGER.log(Level.INFO, "Found " + transactions.size() + " transaction(s)");

        return transactions.get(0);
    }

    /***
     * Update transaction
     *
     * @param transaction
     * @throws Exception
     */
    private UpdateTransactionResponse updateTransaction(UpdateTransactionAuthorizeMutationArgs transaction, TransactionGuaranteeBehavior guaranteeBehavior) throws SwissbillingCommercialClientException {
        LOGGER.log(Level.INFO, "Updating transaction...");

        var updateTransactionMutation = new GraphQLQuery(GraphQLQueryTypesEnum.MUTATION, this._endpoints.getGraphQlAuthorize(),
                UpdateTransactionRequest.class);
        updateTransactionMutation.mapArguments(transaction);
        var updateTransactionResponseRaw = this._httpHelper.sendQuery(updateTransactionMutation, this._token);
        var updateTransactionRequest = this.mapJson(updateTransactionResponseRaw, new TypeReference<GraphQLResponse<UpdateTransactionRequest>>() {
        });

        if (updateTransactionRequest.errors != null) {
            LOGGER.log(Level.SEVERE, updateTransactionRequest.errors.get(0).message);
            throw new SwissbillingCommercialClientException("Fail to update transaction");
        }

        if (updateTransactionRequest.data == null || updateTransactionRequest.data.updateTransaction == null) {
            LOGGER.log(Level.SEVERE, "Fail to update transaction");
            throw new SwissbillingCommercialClientException("Fail to update transaction");
        }

        // Check rejected by prescreening
        if (guaranteeBehavior == TransactionGuaranteeBehavior.CONFIRM_NO_GUARANTEE_IF_NO_AVAILABLE
                && updateTransactionRequest.data.updateTransaction.status.value == TransactionStatusEnum.REJECTED_BY_PRESCREENING.getValue()) {
            this.confirmNoGuarantee(updateTransactionRequest.data.updateTransaction.id);
        }

        LOGGER.log(Level.INFO, "Transaction updated.");
        return updateTransactionRequest.data.updateTransaction;
    }

    /***
     * Confirm no guarantee
     *
     * @param transactionId
     * @throws IllegalStateException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    private TransactionStatusEnum confirmNoGuarantee(String transactionId) throws SwissbillingCommercialClientException {
        LOGGER.log(Level.INFO, "Confirm no guarantee...");

        var confirmNoGuaranteeMutation = new GraphQLQuery(GraphQLQueryTypesEnum.MUTATION,
                this._endpoints.getGraphQlAuthorize(), ConfirmTransactionNotGuaranteedRequest.class);

        confirmNoGuaranteeMutation.mapArguments(new ConfirmTransactionNotGuaranteedAuthorizeMutationArgs(transactionId));
        var confirmNoGuaranteeResponseRaw = this._httpHelper.sendQuery(confirmNoGuaranteeMutation, this._token);
        var confirmNoGuaranteeRequest = this.mapJson(confirmNoGuaranteeResponseRaw, new TypeReference<GraphQLResponse<ConfirmTransactionNotGuaranteedRequest>>() {
        });

        if (confirmNoGuaranteeRequest.errors != null) {
            LOGGER.log(Level.SEVERE, confirmNoGuaranteeRequest.errors.get(0).message);
            throw new SwissbillingCommercialClientException(confirmNoGuaranteeRequest.errors.get(0).message);
        }

        if (confirmNoGuaranteeRequest.data == null || confirmNoGuaranteeRequest.data.confirmTransactionNotGuaranteed == null) {
            LOGGER.log(Level.SEVERE, "Fail to confirm guarantee");
            throw new SwissbillingCommercialClientException("Fail to confirm guarantee");
        }

        var transactionStatus = confirmNoGuaranteeRequest.data.confirmTransactionNotGuaranteed.status.value;
        var status = TransactionStatusEnum.values()[transactionStatus];

        LOGGER.log(Level.INFO, String.format("Guarantee confirmed for transaction %s", transactionId));
        return status;
    }

    /***
     * Method used to configure encironment specific endpoints
     */
    private void configureEndpoints() {
        switch (this._settings.environment) {
            case DEV:
                this._endpoints = new DevEndpoints();
                break;
            case LOCALHOST:
                this._endpoints = new LocalhostEndpoints();
                break;
            case PRE_PRODUCTION:
                this._endpoints = new PreProductionEndpoints();
                break;
            case PRODUCTION:
                this._endpoints = new ProductionEndpoints();
                break;
            case STAGING:
                this._endpoints = new StagingEndpoints();
                break;
            default:
                break;
        }
    }

    /***
     * Method used to login
     * @return
     */
    private boolean login() {
        LOGGER.log(Level.INFO, String.format("Login with %s ...", this._settings.merchantEmail));

        var loginModel = new LoginAnonymousQueryArgs(this._settings.merchantEmail, this._settings.merchantPassword);
        var loginQuery = new GraphQLQuery(GraphQLQueryTypesEnum.QUERY, this._endpoints.getGraphQlAnonymous(),
                LoginRequest.class);
        loginQuery.mapArguments(loginModel);

        var loginRequestRaw = this._httpHelper.sendQuery(loginQuery, null);
        var loginRequest = this.mapJson(loginRequestRaw, new TypeReference<GraphQLResponse<LoginRequest>>() {
        });

        // Verify login
        if (loginRequest.data == null || loginRequest.data.login == null) {
            for (GraphQLError error : loginRequest.errors)
                LOGGER.log(Level.SEVERE, String.format("Fail to log in %s .", error.message));

            return false;
        }

        // Get token
        var token = loginRequest.data.login.accessToken;
        LOGGER.log(Level.FINE, String.format("User logged in with token: %s .", token));

        this._token = token;

        return true;
    }

    /***
     * Method used to get the merchant's shop identifier
     * @return
     */
    private boolean getShopId() {
        LOGGER.log(Level.FINE, "Get merchant's shop id...");

        var shopsQuery = new GraphQLQuery(GraphQLQueryTypesEnum.QUERY, this._endpoints.getGraphQlAuthorize(), ShopsRequest.class);
        var shopsRequestRaw = this._httpHelper.sendQuery(shopsQuery, this._token);
        var shopsRequest = this.mapJson(shopsRequestRaw, new TypeReference<GraphQLResponse<ShopsRequest>>() {
        });

        // Verify shop exists
        if (shopsRequest.data == null || shopsRequest.data.shops == null || shopsRequest.data.shops.size() == 0) {
            LOGGER.log(Level.SEVERE, "Merchant has no shops.");
            return false;
        }

        var shopId = shopsRequest.data.shops.get(0).id;
        this._shopId = shopId;
        LOGGER.log(Level.FINE, String.format("Shop id found: %s", shopId));

        return true;
    }

    /***
     * Generic method used to parse GraphQL responses
     * @param jsonString
     * @param type
     * @param <T>
     * @return
     */
    private <T> GraphQLResponse<T> mapJson(String jsonString, TypeReference<GraphQLResponse<T>> type) {
        try {
            GraphQLResponse<T> output = new ObjectMapper().readValue(jsonString, type);
            return output;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fail to parse jsonString", e);
        }

        return new GraphQLResponse<>();
    }
    //</editor-fold>
}
