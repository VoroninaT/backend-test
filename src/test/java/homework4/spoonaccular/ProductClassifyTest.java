package homework4.spoonaccular;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
@Disabled
public class ProductClassifyTest extends SpoonaccularTest {

    @Test
    void testClassifyGroceryProduct() throws Exception {
        String requestBody = "{ \n" +
                "    \"title\": \"300% Milk\", \n" +
                "    \"upc\": \"1231\", \n" +
                "    \"plu_code\": \"2323\"\n" +
                "}";

        String response = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("food/products/classify")
                .prettyPrint();


        String expected = getResource("expected.json");
        assertJson(expected, response);
    }

}
