# Scase Lambda Function Example

To build the lambda function and deploy to AWS through CloudFormation run:

```scala
sbt proguard condense
```

To run the client and invoke the function:

```bash
sbt "runMain io.jobial.scase.example.greeting.lambda.GreetingLambdaClient"
```

You can also call the function on the AWS Dashboard or using any other client.





