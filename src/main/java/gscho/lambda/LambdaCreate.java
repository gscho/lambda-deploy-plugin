package gscho.lambda;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.CreateFunctionRequest;
import com.amazonaws.services.lambda.model.CreateFunctionResult;
import com.amazonaws.services.lambda.model.Environment;
import com.amazonaws.services.lambda.model.FunctionCode;
import com.amazonaws.services.lambda.model.ResourceConflictException;

/**
 * This goal will create a lambda.
 *
 */
@Mojo(name = "create")
public class LambdaCreate extends AbstractMojo {

	@Parameter(property = "accessKey", required = true)
	private String awsAccessKey;

	@Parameter(property = "secretKey", required = true)
	private String awsSecretKey;

	@Parameter(defaultValue = "us-east-1", property = "region", required = true)
	private String awsRegion;

	@Parameter(property = "zipFile", required = true)
	private String zipFile;

	@Parameter(property = "functionName", required = true)
	private String functionName;

	@Parameter(property = "handler", required = true)
	private String handler;

	@Parameter(property = "roleArn", required = true)
	private String roleArn;

	@Parameter(required = false)
	private Map<String, String> environmentVars;

	public void execute() throws MojoExecutionException {
		Regions region = Regions.fromName(awsRegion);
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		AWSLambda lambdaClient = AWSLambdaClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
		try {
			CreateFunctionResult result = lambdaClient.createFunction(new CreateFunctionRequest().withRole(roleArn) //
					.withFunctionName(functionName) //
					.withHandler(handler) //
					.withEnvironment(new Environment()
							.withVariables(Optional.ofNullable(environmentVars).orElse(new HashMap<String, String>()))) //
					.withRuntime("java8") //
					.withCode(new FunctionCode().withZipFile(ByteBuffer.wrap(Files.readAllBytes(Paths.get(zipFile))))));
			getLog().info("Function ARN = " + result.getFunctionArn());
		} catch (ResourceConflictException resourceException) {
			getLog().info("Lambda Function already exists!");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
