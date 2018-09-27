# lambda-deploy-plugin


Create or updates an AWS Lambda function from your Java8 jar.

## Command Line Usage

Create Goal:

```
 mvn gscho.lambda:lambda-deploy:create -DaccessKey=<accessKey> -DsecretKey=<secretKey> -DzipFile=/path/to/my-lambda-0.0.1-SNAPSHOT.jar -DfunctionName=my-lambda -Dhandler=com.MyLambda::handleRequest -DroleArn=<roleArn>
```

Update Goal:

```
 mvn gscho.lambda:lambda-deploy:update -DaccessKey=<accessKey> -DsecretKey=<secretKey> -DzipFile=/path/to/my-lambda-0.0.1-SNAPSHOT.jar -DfunctionName=my-lambda
```

## pom.xml Usage
TBD..

## Configuration parameters

| Parameter | Description | Required | Default |
|-----------|-------------|----------|---------|
|accessKey|AWS Access Key| yes | none |
|secretKey|AWS Secret Key| yes | none |
|region|AWS Region| yes | us-east-1 |
|zipFile|Path to the jar file| yes | none |
|functionName|Name of the Lambda | yes | none |
|handler|S3 secret key | no | **required when creating** |
|roleArn| AWS ARN for the Lambda Role| no | **required when creating** |
|environmentVars|Environment Variables| no | none |
