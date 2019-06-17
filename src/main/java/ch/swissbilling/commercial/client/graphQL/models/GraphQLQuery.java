package ch.swissbilling.commercial.client.graphQL.models;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.swissbilling.commercial.client.enums.GraphQLQueryTypesEnum;
import io.aexp.nodes.graphql.GraphQLRequestEntity;

public class GraphQLQuery {
	private static final Logger LOGGER = Logger.getLogger(GraphQLQuery.class.getName());
	
	public String operationName;
	public String namedQuery;
	public String query;
	public String variables;
	private String url;
	private String queryType;

	public GraphQLQuery(GraphQLQueryTypesEnum queryType, String url, Class<?> classType) {

		GraphQLRequestEntity requestEntity;
		try {
			requestEntity = GraphQLRequestEntity.Builder().url(url).request(classType).build();

			this.query = requestEntity.getRequest();
			this.url = url;
			this.queryType = queryType.getValue();
		} catch (IllegalStateException | MalformedURLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	public void mapArguments(Object arguments) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			if (arguments.getClass().equals(JSONObject.class)) {
				this.variables = arguments.toString();
			} else {
				this.variables = mapper.writeValueAsString(arguments);
			}
		} catch (JsonProcessingException e) {
			LOGGER.log(Level.SEVERE,e.getMessage());
		}
	}

	public String getJsonQuery() {
		ObjectMapper mapper = new ObjectMapper();

		if (this.queryType == GraphQLQueryTypesEnum.MUTATION.value) {
			this.query = this.query.replaceFirst("query", "mutation");
		}

		try {
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("operationName", this.operationName);
			queryMap.put("namedQuery", this.namedQuery);
			queryMap.put("query", this.query);
			queryMap.put("variables", this.variables);

			return mapper.writeValueAsString(queryMap);
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
