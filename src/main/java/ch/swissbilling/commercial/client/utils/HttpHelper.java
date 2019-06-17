package ch.swissbilling.commercial.client.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import ch.swissbilling.commercial.client.endpoints.IEnvironmentEndpoints;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import ch.swissbilling.commercial.client.graphQL.models.GraphQLQuery;

public class HttpHelper {
	/***
	 * The logger
	 */
	private static final Logger LOGGER = Logger.getLogger(HttpHelper.class.getName());

	/***
	 * The environment specific endpoints
	 */
	private final IEnvironmentEndpoints _endpoints;

	/***
	 * Initializes a new instance of the HttpHelper class
	 * @param endpoints
	 */
	public HttpHelper(IEnvironmentEndpoints endpoints) {
		this._endpoints = endpoints;
	}

	/***
	 * Method used to post graphQL queries
	 * 
	 * @param query The graphQL query object
	 * @param token The authorization token
	 * @return The raw httpResponse string
	 */
	public String sendQuery(GraphQLQuery query, String token) {
		
		try {
			var jsonQuery = query.getJsonQuery();
			LOGGER.log(Level.FINE,">> " + jsonQuery);

			var request = new HttpPost(query.getUrl());
			var params = new StringEntity(jsonQuery, "UTF-8");

			request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");

			if (token != null) {
				request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
			}

			request.setEntity(params);

			var httpClient = HttpClientBuilder.create().build();
			var response = httpClient.execute(request);

			return readResponse(response);

		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
			return "";
		}
	}

	/**
	 * Method used to upload PDF invoice
	 * 
	 * @param file   The invoice file
	 * @param shopId The merchant's shop id
	 * @param token  The authorization token
	 * @return The TransactionBatchDocument's identifier
	 */
	public String uploadInvoice(File file, String shopId, String token) {
		try {
			var url = _endpoints.getTransactionDocument(shopId);
			LOGGER.log(Level.FINE,">> uploading file: " + file.getPath() + " to: " + url);

			var entity = MultipartEntityBuilder.create()
					.addPart("file", new FileBody(file, ContentType.create("application/pdf"))).build();

			var request = new HttpPost(url);
			request.setEntity(entity);

			if (token != null) {
				request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
			}

			var httpClient = HttpClientBuilder.create().build();
			var response = httpClient.execute(request);
			var responseString = readResponse(response);

			return responseString.replaceAll("\"", "");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
			return "";
		}
	}

	/***
	 * Method used for reading HTTP response
	 * 
	 * @param response
	 * @return
	 */
	public static String readResponse(HttpResponse response) {
		try {
			var rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			var result = new StringBuffer();
			var line = "";

			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			var rawResult = result.toString();
			LOGGER.log(Level.FINE,"<< " + rawResult);

			return rawResult;

		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		}
		
		return new String("");
	}
}
