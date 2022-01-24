package io.jobial.scase.example.greeting.lambda

import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, Context, LambdaLogger}
import io.circe.syntax._
import org.apache.commons.io.IOUtils
import org.apache.commons.io.output.ByteArrayOutputStream
import io.circe.generic.auto._

object Test extends App {
  
//  println(Hello("world").asJson)
//  
//  println(new GreetingServiceLambdaRequestHandler().serviceConfiguration.requestMarshaller.marshalToText(Hello("world")))
//
  println(new GreetingServiceLambdaRequestHandler().serviceConfiguration.requestUnmarshaller.unmarshalFromText("""{
  "Hello" : {
    "person" : "world"
  }
}"""))
  
  val out = new ByteArrayOutputStream()
  
  new GreetingServiceLambdaRequestHandler().handleRequest(
    IOUtils.toInputStream("""{
  "Hello" : {
    "person" : "world"
  }
}""", "utf-8"),
    out,
    new Context{
      override def getAwsRequestId: String = ""

      override def getLogGroupName: String = ???

      override def getLogStreamName: String = ???

      override def getFunctionName: String = ???

      override def getFunctionVersion: String = ???

      override def getInvokedFunctionArn: String = ???

      override def getIdentity: CognitoIdentity = ???

      override def getClientContext: ClientContext = ???

      override def getRemainingTimeInMillis: Int = ???

      override def getMemoryLimitInMB: Int = ???

      override def getLogger: LambdaLogger = ???
    }
  )
  
  println(out.toString("utf-8"))
}
