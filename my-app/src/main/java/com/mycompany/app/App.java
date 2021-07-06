package com.mycompany.app;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
    private static final AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.defaultClient();
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        GetItemResult wardData = readWardData(apiGatewayProxyRequestEvent);
        return generateResponse(wardData);
    }

    protected GetItemResult readWardData(APIGatewayProxyRequestEvent event){
        Map<String, String> queryParams = event.getQueryStringParameters();
        String tableName = getTableName();
        Map<String, AttributeValue> key = getKey(queryParams);
        GetItemRequest getItemRequest = new GetItemRequest().withTableName(tableName).withKey(key);

        return dynamoDB.getItem(getItemRequest);


    }

    private static String getTableName(){
        GetParameterRequest tableParameterRequest = new GetParameterRequest().withName("Keepr-ward-data").withWithDecryption(false);
        GetParameterResult tableResult = ssm.getParameter(tableParameterRequest);
        return tableResult.getParameter().getValue();
    }

    private static Map<String, AttributeValue> getKey(Map<String, String> queryParams) {
        if(queryParams.containsKey("ward_id")) {

            Map<String, AttributeValue> key = new HashMap<>();
            key.put("id", new AttributeValue(queryParams.get("ward_id")));

            return key;

        }
        return null;
    }

    private static APIGatewayProxyResponseEvent generateResponse(GetItemResult wardData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        response.setBody(gson.toJson(wardData));

        return response;
    }






}