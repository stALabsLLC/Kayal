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

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;


public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{

     /* defined outside handler for Execution context reuse */
    private static final AWSSimpleSystemsManagement ssm = AWSSimpleSystemsManagementClientBuilder.defaultClient();
    private static final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();


    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context) {
        GetItemResult wardData = readWardData(apiGatewayProxyRequestEvent);
        return generateResponse(wardData);
    }

    protected GetItemResult readWardData(APIGatewayProxyRequestEvent event){
        Map<String, String> queryParams = event.getQueryStringParameters();
        /* get tableName from parameter store */
        String tableName = getTableName();
        /* get key which contains {"id": id_number from query} */
        Map<String, AttributeValue> key = getKey(queryParams);
        GetItemRequest getItemRequest = new GetItemRequest().withTableName(tableName).withKey(key);

        return dynamoDB.getItem(getItemRequest);


    }

    private static String getTableName(){
        GetParameterRequest tableParameterRequest = new GetParameterRequest().withName("ward-data").withWithDecryption(false);
        GetParameterResult tableResult = ssm.getParameter(tableParameterRequest);
        return tableResult.getParameter().getValue();
    }

    private static Map<String, AttributeValue> getKey(Map<String, String> queryParams) {
        if(queryParams.containsKey("ward_id")) {

            Map<String, AttributeValue> key = new HashMap<>();
            /* withN() converts AttributeValue to number */
            key.put("id", new AttributeValue().withN(queryParams.get("ward_id")));

            return key;

        }

        else throw new IllegalArgumentException("param : ward_id is missing");
    }

    private static APIGatewayProxyResponseEvent generateResponse(GetItemResult wardData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(200);
        /* convert wardData object to json*/
        response.setBody(gson.toJson(wardData));

        return response;
    }




}