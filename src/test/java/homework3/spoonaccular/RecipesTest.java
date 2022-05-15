package homework3.spoonaccular;

import homework3.*;
import io.restassured.RestAssured;
import net.javacrumbs.jsonunit.JsonAssert;
import net.javacrumbs.jsonunit.core.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class RecipesTest {

    private static final String API_KEY = "74a729b59b874544b218c6218a46b1c5";

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://api.spoonacular.com/recipes/";
    }

    @Test
    void testAutocompleteSearch() throws IOException {

        String actually = given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .param("query", "burger")
                .param("number", "10")
                .expect()
                .log()
                .all()
                .when()
                .get("autocomplete")
                .body()
                .prettyPrint();

        String expected = getResourceAsString("testAutocomplete/expected.json");

        JsonAssert.assertJsonEquals(
                expected,
                actually,
                JsonAssert.when(Option.IGNORING_ARRAY_ORDER)
        );
    }

    @Test
    void testNutritionById() {
        given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .pathParams("id", 1003464)
                .expect()
                .log()
                .body()
                .body("calories", is("316k"))
                .body("carbs", is("49g"))
                .body("fat", is("12g"))
                .body("protein", is("3g"))
                .when()
                .get("{id}/nutritionWidget.json");

    }

    @Test
    void testSummarizeRecipe() {
        SummarizeRecipe response = given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .pathParams("id", 4632)
                .expect()
                .log()
                .body()
                .when()
                .get("{id}/summary")
                .as(SummarizeRecipe.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getId());
        Assertions.assertNotNull(response.getTitle());
        Assertions.assertNotNull(response.getSummary());
        Assertions.assertEquals(4632L, response.getId());
        Assertions.assertEquals("Soy-and-Ginger-Glazed Salmon with Udon Noodles", response.getTitle());
        Assertions.assertTrue(response.getTitle().startsWith("Soy-and-Ginger-Glazed Salmon with Udon Noodles"));
    }

    @Test
    void testAnalyzeRecipeSearchQuery() {
        Dish targetDish = new Dish("https://spoonacular.com/cdn/ingredients_100x100/salmon.png", "salmon");

        AnalyzeResponse response = given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .param("q", "salmon with fusilli and no nuts")
                .expect()
                .log()
                .body()
                .when()
                .get("queries/analyze")
                .as(AnalyzeResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getIngredients());
        Assertions.assertNotNull(response.getDishes());
        Assertions.assertEquals(2, response.getIngredients().size());
        Assertions.assertEquals(1, response.getDishes().size());

        Ingredient ingredient1 = response.getIngredients().get(0);
        Ingredient ingredient2 = response.getIngredients().get(1);

        Assertions.assertEquals("fusilli", ingredient1.getName());
        Assertions.assertEquals(true, ingredient1.getInclude());
        Assertions.assertEquals("fusilli.jpg", ingredient1.getImage());

        Assertions.assertEquals("nuts mixed", ingredient2.getName());
        Assertions.assertEquals(false, ingredient2.getInclude());
        Assertions.assertEquals("nuts-mixed.jpg", ingredient2.getImage());

        response.getDishes()
                .stream()
                .filter(dish -> dish.getName().equals("salmon"))
                .peek(dish -> Assertions.assertEquals(targetDish, dish))
                .findAny()
                .orElseThrow();
    }

    @Test
    void testConvertAmounts() {
        String sourceUnit = "cups";
        String targetUnit = "grams";
        Double sourceAmount = 2.5;
        ConvertResponse convertResponse = given()
                .log()
                .all()
                .param("apiKey", API_KEY)
                .param("ingredientName", "flour")
                .param("sourceAmount", sourceAmount)
                .param("sourceUnit", sourceUnit)
                .param("targetUnit", targetUnit)
                .expect()
                .log()
                .body()
                .when()
                .get("convert")
                .as(ConvertResponse.class);

        Assertions.assertNotNull(convertResponse);
        Assertions.assertEquals(sourceAmount, convertResponse.getSourceAmount());
        Assertions.assertEquals(sourceUnit, convertResponse.getSourceUnit());
        Assertions.assertEquals(312.5, convertResponse.getTargetAmount());
        Assertions.assertEquals(targetUnit, convertResponse.getTargetUnit());
        Assertions.assertTrue(convertResponse.getAnswer().contains("2.5 cups flour"));
        Assertions.assertTrue(convertResponse.getAnswer().contains("312.5 grams"));
    }

    public String getResourceAsString(String resource) throws IOException {
        InputStream stream = getClass().getResourceAsStream(resource);
        assert stream != null;
        byte[] bytes = stream.readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
