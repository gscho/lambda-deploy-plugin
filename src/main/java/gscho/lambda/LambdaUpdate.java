package gscho.lambda;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.UpdateFunctionCodeRequest;

/**
 * This goal will update a lambda.
 * 
 */
@Mojo(name = "update")
public class LambdaUpdate extends AbstractMojo {

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

	public void execute() throws MojoExecutionException {
		Regions region = Regions.fromName(awsRegion);
		BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		AWSLambda lambdaClient = AWSLambdaClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();
		try {
			lambdaClient.updateFunctionCode(new UpdateFunctionCodeRequest() //
					.withFunctionName(functionName) //
					.withPublish(true).withZipFile(ByteBuffer.wrap(Files.readAllBytes(Paths.get(zipFile)))));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}