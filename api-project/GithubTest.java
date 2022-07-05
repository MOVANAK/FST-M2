import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import io.restassured.authentication.AuthenticationScheme;
import io.restassured.authentication.BasicAuthScheme;
import io.restassured.authentication.OAuth2Scheme;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class GithubTest {
        // Declare request specification
        RequestSpecification requestSpec;
        // Declare response specification
        ResponseSpecification responseSpec;
        String sshKey;
        int sshKeyId;
        @BeforeClass
        public void setUp() {
                // Create request specification
                requestSpec = new RequestSpecBuilder()
                        // Set content type
                        .setContentType(ContentType.JSON)
                        .addHeader("Authorization","token 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")
                        // Set base URL
                        .setBaseUri("https://api.github.com")

                        // Build request specification
                        .build();

        }
        @Test(priority=1)
        public void addKey() {
                String reqBody = "{\"title\":\"TestAPIKey\",\"key\":\"ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIFj7eh05RsDCxBWUCJCBMtod3mHuXRqHzfcQyh37IZc8 mohammad.vanak@ibm.com\"}";

                Response response = given().spec(requestSpec) // Use requestSpec
                        .body(reqBody) // Send request body
                        .when().post(" /user/keys"); // Send POST request

                response.then().statusCode(201);
                JsonPath jsnPath = response.jsonPath();
                sshKeyId=jsnPath.get("id");
                response.then().statusCode(201);

        }
        @Test(priority=2)
        public void getKey() {
                Response response = given().spec(requestSpec) // Use requestSpec
                        .pathParam("keyId", sshKeyId) // Add path parameter
                        .when().get("/user/keys/{keyId}");

                response.then().statusCode(200);
                System.out.println(response.asPrettyString());

        }
        @Test(priority=3)
        public void deleteKey() {
                Response response = given().spec(requestSpec) // Use requestSpec
                        .pathParam("keyId", sshKeyId) // Add path parameter
                        .when().delete("/user/keys/{keyId}");

                response.then().statusCode(204);
                System.out.println(response.asPrettyString());

        }
}
