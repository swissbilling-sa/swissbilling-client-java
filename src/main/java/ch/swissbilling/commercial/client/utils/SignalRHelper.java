package ch.swissbilling.commercial.client.utils;

import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.swissbilling.commercial.client.endpoints.IEnvironmentEndpoints;
import ch.swissbilling.commercial.client.exceptions.SwissbillingCommercialClientException;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.swissbilling.commercial.client.models.SignalRMessage;

public class SignalRHelper {

    /***
     * The logger
     */
    private static final Logger LOGGER = Logger.getLogger(SignalRHelper.class.getName());

    /***
     * The lock object
     */
    private static final Object lock = new Object();

    /***
     * The environment specific endpoints
     */
    private final IEnvironmentEndpoints _endpoints;

    /***
     * Initializes a new instance of the SignalRHelper class
     * @param endpoints
     */
    public SignalRHelper(IEnvironmentEndpoints endpoints) {
        this._endpoints = endpoints;
    }

    /***
     * Negotiate a new connection id and open websocket listening to
     * TransactionBatchDocumentStatus changes
     *
     * @param transactionBatchDocumentId
     * @param token
     */
    public void ConnectAndWait(String transactionBatchDocumentId, String token) throws SwissbillingCommercialClientException {
        try {
            // Negotiate SignalR connection ID
            var url = this._endpoints.getTransactionBatchDocumentHubNegotiate();
            LOGGER.log(Level.FINE,"Negotiating signalR connection id...");
            LOGGER.log(Level.FINE,">> " + url);

            var request = new HttpPost(url);
            request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            if (token != null) {
                request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            }

            var httpClient = HttpClientBuilder.create().build();
            var response = httpClient.execute(request);
            var rawResponse = HttpHelper.readResponse(response);
            var responseObj = new JSONObject(rawResponse);
            var connectionId = responseObj.getString("connectionId");
            LOGGER.log(Level.FINE,"SignalR connection id negotiated: " + connectionId);

            // Create SignalR websocket
            CreateWebsocket(connectionId, transactionBatchDocumentId);

            // Waiting
            LOGGER.log(Level.FINE,"Waiting for PDF processing...");
            synchronized (lock) {
                lock.wait();
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            throw new SwissbillingCommercialClientException("SignalR connection error");
        }
    }

    /***
     * Creating the websocket client
     *
     * @param connectionId
     * @param transactionBatchDocumentId
     */
    private void CreateWebsocket(String connectionId, final String transactionBatchDocumentId) {
        try {
            var url = this._endpoints.getTransactionBatchDocumentHubWebSocket(connectionId);
            LOGGER.log(Level.INFO, "Connecting to " + url);

            var mWs = new WebSocketClient(new URI(url)) {
                @Override
                public void onMessage(String message) {
                    LOGGER.log(Level.INFO, "<< " + message);

                    try {
                        var messages = message.split("\u001e");

                        for (String splittedMessage : messages) {
                            var msg = new ObjectMapper().readValue(splittedMessage, SignalRMessage.class);

                            if (msg.target != null) {
                                if (msg.target.equals("TransactionBatchDocumentStatus")
                                        && msg.arguments[0].contentEquals("Processed")) {
                                    // close waiter;
                                    LOGGER.log(Level.INFO, "Document processed");
                                }

                                if (msg.target.contentEquals("Close")) {
                                    this.close();
                                }
                            }
                        }

                    } catch (Exception e) {
                        LOGGER.log(Level.SEVERE, e.getMessage());
                    }
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    LOGGER.log(Level.INFO, "SignalR connection opened");

                    var protocolRequest = "{\"protocol\":\"json\",\"version\":1}";
                    var connectRequest = "{\"arguments\":[\"" + transactionBatchDocumentId
                            + "\"],\"invocationId\":\"0\",\"target\":\"Connect\",\"type\":1}";

                    LOGGER.log(Level.INFO, ">> " + protocolRequest);
                    this.send(protocolRequest);

                    LOGGER.log(Level.INFO, ">> " + connectRequest);
                    this.send(connectRequest);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    LOGGER.log(Level.INFO, "SignalR connection closed");

                    // Notify waiter
                    synchronized (lock) {
                        lock.notify();
                    }
                }

                @Override
                public void onError(Exception ex) {
                    LOGGER.log(Level.SEVERE, ex.getMessage());
                }

            };

            // Connect to websocket, it will be closed when the document is processed
            mWs.connect();

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }
}