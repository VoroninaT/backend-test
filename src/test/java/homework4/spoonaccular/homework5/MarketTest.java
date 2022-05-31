package homework4.spoonaccular.homework5;

import homework4.spoonaccular.AbstractTest;
import homework4.spoonaccular.homework5.model.MarketCategoriesResult;
import homework4.spoonaccular.homework5.model.ProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.util.List;


public class MarketTest extends AbstractTest {

    private static MarketService service;

    @BeforeAll
    static void beforeAll() {
        service = new MarketService();
    }

    @Test
    void testGetCategories() throws Exception {
        MarketCategoriesResult categories = service.getCategory();
        assertJson(getResource("categories.json"), categories);
    }

    @Test
    void testGetProducts() throws Exception {
        List<ProductDto> products = service.getProducts();
        assertJson(getResource("products.json"), products);
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDto product = new ProductDto();
        product.setTitle("Bred22222");
        product.setPrice(1);
        product.setCategoryTitle("Food");
        Response<ProductDto> response = service.createProduct(product);
        Assertions.assertEquals(201, response.code());

        ProductDto created = response.body();
        Assertions.assertNotNull(created);
    }

    @Test
    void testModifyProduct() throws Exception {
        ProductDto product = new ProductDto();
        product.setId(399);
        product.setTitle("Bred");
        product.setPrice(100);
        product.setCategoryTitle("Food");
        Response<Void> response = service.modifyProduct(product);
        var productUpdated = service.getProduct(399);

    }

    @Test
    void testDeleteProduct() throws Exception {
        Response<Void> response = service.deleteProduct(399);
        Assertions.assertEquals(200, response.code());
        System.out.println("Hello world");
    }
}





