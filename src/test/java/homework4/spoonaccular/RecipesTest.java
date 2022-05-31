package homework4.spoonaccular;

import homework3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
@Disabled
public class RecipesTest extends SpoonaccularTest {
    @Test
    void testAutocompleteSearch() throws Exception {

        String actually = given()
                .param("query", "burger")
                .param("number", "10")
                .expect()
                .when()
                .get("recipes/autocomplete")
                .body()
                .prettyPrint();

        String expected = getResource("expected.json");
        assertJson(expected, actually);
    }

    @Test
    void testNutritionById() {
        given()
                .pathParams("id", 1003464)
                .expect()
                .body("calories", is("316k"))
                .body("carbs", is("49g"))
                .body("fat", is("12g"))
                .body("protein", is("3g"))
                .when()
                .get("recipes/{id}/nutritionWidget.json");
    }

    @Test
    void testSummarizeRecipe() {
        SummarizeRecipe response = given()
                .pathParams("id", 4632)
                .expect()
                .when()
                .get("recipes/{id}/summary")
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
                .param("q", "salmon with fusilli and no nuts")
                .expect()
                .when()
                .get("recipes/queries/analyze")
                .as(AnalyzeResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getIngredients());
        Assertions.assertNotNull(response.getDishes());
        Assertions.assertEquals(1, response.getIngredients().size());
        Assertions.assertEquals(1, response.getDishes().size());

        System.out.println("Ingredients: " + response.getIngredients());

        Ingredient ingredient = response.getIngredients().get(0);

        Assertions.assertEquals("nuts mixed", ingredient.getName());
        Assertions.assertEquals(false, ingredient.getInclude());
        Assertions.assertEquals("nuts-mixed.jpg", ingredient.getImage());

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
                .param("ingredientName", "flour")
                .param("sourceAmount", sourceAmount)
                .param("sourceUnit", sourceUnit)
                .param("targetUnit", targetUnit)
                .expect()
                .when()
                .get("recipes/convert")
                .as(ConvertResponse.class);

        Assertions.assertNotNull(convertResponse);
        Assertions.assertEquals(sourceAmount, convertResponse.getSourceAmount());
        Assertions.assertEquals(sourceUnit, convertResponse.getSourceUnit());
        Assertions.assertEquals(312.5, convertResponse.getTargetAmount());
        Assertions.assertEquals(targetUnit, convertResponse.getTargetUnit());
        Assertions.assertTrue(convertResponse.getAnswer().contains("2.5 cups flour"));
        Assertions.assertTrue(convertResponse.getAnswer().contains("312.5 grams"));
        System.out.println("Hello world");
    }
}
