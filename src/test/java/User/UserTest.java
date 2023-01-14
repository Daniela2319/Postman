package User;

import Entities.User;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.config.LogConfig.logConfig;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // ordem dos testes
public class UserTest {

    private static User user;
    public static Faker faker;
    public static RequestSpecification request;

    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "https://petstore.swagger.io/v2";

        faker = new Faker();

        user = new User(faker.name().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().safeEmailAddress(),
                faker.internet().password(8,10),
                faker.phoneNumber().toString());

    }

    @BeforeEach
    public void setRequest(){
        request = given()
                .header("api-key","special-key")
                .contentType(ContentType.JSON);
    }

    @Test
    public void CreateNewUser_WithValidData_ReturnOk(){ // inicia o teste fazendo o que vai validar

      //  given().header().when().get.then.assertThat() estrutura basica para restAssured
        request
                .body(user) // passa entitade user
                .when()
                .post("/user") // pega url passa a rota
                .then()
                .assertThat().statusCode(200).and()
                .body("code", equalTo(200))
                .body("type", equalTo("unknown"))
                .body("message", isA(String.class))
                .body("size()", equalTo(3));


    }
}
