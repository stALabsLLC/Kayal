# Kayal

the lambda function getWardData **my-app/src/main/java/com/mycompany/app/App.java** returns data from a table in dynamoDB based on the query parameter "ward_id". You can use this function by uploading my-app/target/my-app-1.0-SNAPSHOT.jar to lambda and setting the runtime to Java cornetto 11. **To test in Lambda use the test event**: 
 
 {
  "queryStringParameters": {
    "ward_id": 1234
  }
}


The function is triggered only if the correct Authorization token is sent. The User recieves the token after sign-in through the callback url. Ive copied the id_token and used it for the authorization header.

Using maven to build the project.
refer my-app/pom.xml for all the dependencies and maven configurations. Make sure the java version used by maven is the same as the Lambda runtime.


**input http request format:**

GET/resourcename/?query_paramter=1234 
Host: API gateway invoke url
Authorization: Bearer id_token issued by cognito


**input example : http request used in Postman:**

GET /test2/ward?ward_id=1234 HTTP/1.1
Host: tj9zpzpn57.execute-api.ap-south-1.amazonaws.com
Authorization: Bearer eyJraWQiOiJJb0tvZ2JpQ0ZCSXA1aUxnNzhUcjRvQXZGNHRweDFocklESU9sdEZvTU44PSIsImFsZyI6IlJTMjU2In0.eyJhdF9oYXNoIjoiZ2lMb2xqZC02TkRHVkc0T2xaemNuUSIsInN1YiI6IjhlZGFhZGEwLTI4MWUtNGNjNi05ZGYyLThjYTRhNjExZDNmZCIsImNvZ25pdG86Z3JvdXBzIjpbIkdyYXBoUUwtQVBJIl0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuYXAtc291dGgtMS5hbWF6b25hd3MuY29tXC9hcC1zb3V0aC0xXzNsQ0RQUlVOSCIsInBob25lX251bWJlcl92ZXJpZmllZCI6dHJ1ZSwiY29nbml0bzp1c2VybmFtZSI6ImtheWFsIiwiY29nbml0bzpyb2xlcyI6WyJhcm46YXdzOmlhbTo6OTg3OTM2NDU0ODc5OnJvbGVcL3NlcnZpY2Utcm9sZVwvYXBwc3luYy1kcy1kZGItYjN1ZnNwLVVzZXJUYWJsZSJdLCJhdWQiOiI1ZXR0YmxuN2lnYmFlNWdkY2NnNGRsbmVociIsImV2ZW50X2lkIjoiNGRmOTkyNzItYTMxOS00ZTVmLTgyYTItNTkxZmI5ZDdhNTIwIiwidG9rZW5fdXNlIjoiaWQiLCJhdXRoX3RpbWUiOjE2MjU2MjUxMTMsInBob25lX251bWJlciI6Iis5MTYzODI3MzI3MTAiLCJleHAiOjE2MjU2Mjg3MTMsImlhdCI6MTYyNTYyNTExMywianRpIjoiMTliNGRiMmItZTQ0MS00NDBlLTk4YmUtNjExZTM0NzA2ZDc2IiwiZW1haWwiOiJrYXlhbC50LnZpemhpQGdtYWlsLmNvbSJ9.fdlQUuLXAlmPuaRZwY8eKc026MEhK0fezHHlgvQchyQsJo14HQtVuO2HAgz051bPDQWpWlRau44dndMNr-IqnpdPadf74ioK2AdFDJXtptVe9ekx4ucNzmGQEppYHlqNzJeyCp6NoAj9rD6acH3dvGP7H-Nf2p-Ob4SIiGbCf1487CLEQD5GP_plgi_aNKwQYyLPlqneYXSTq7CBHsA36sZ0mQ-oBKqBu-I4wQjxD6178NNtKAh6ZKs8A4aCqg1YQ_B5PIVZAuNe4S_xfwu8mAq07oHeGPshIIl0S4-foi97W5cRjRvO7PHe_W_8dZUezbGhbt-ZSSaPVi47IojIug

output: 

{
    "item": {
        "last name": {
            "s": "Potter"
        },
        "first name": {
            "s": "Harry"
        },
        "id": {
            "n": "1234"
        }
    },
    "sdkResponseMetadata": {
        "metadata": {
            "AWS_REQUEST_ID": "101KC9Q9T0IOJUNCRH3J49F3VFVV4KQNSO5AEMVJF66Q9ASUAAJG"
        }
    },
    "sdkHttpMetadata": {
        "allHeaders": {
            "Server": [
                "Server"
            ],
            "Connection": [
                "keep-alive"
            ],
            "x-amzn-RequestId": [
                "101KC9Q9T0IOJUNCRH3J49F3VFVV4KQNSO5AEMVJF66Q9ASUAAJG"
            ],
            "x-amz-crc32": [
                "3349414711"
            ],
            "Content-Length": [
                "82"
            ],
            "Date": [
                "Sat, 10 Jul 2021 05:57:09 GMT"
            ],
            "Content-Type": [
                "application/x-amz-json-1.0"
            ]
        },
        "httpHeaders": {
            "Connection": "keep-alive",
            "Content-Length": "82",
            "Content-Type": "application/x-amz-json-1.0",
            "Date": "Sat, 10 Jul 2021 05:57:09 GMT",
            "Server": "Server",
            "x-amz-crc32": "3349414711",
            "x-amzn-RequestId": "101KC9Q9T0IOJUNCRH3J49F3VFVV4KQNSO5AEMVJF66Q9ASUAAJG"
        },
        "httpStatusCode": 200
    }
}

output without the authorization header:

{
    "message": "Unauthorized"
}






