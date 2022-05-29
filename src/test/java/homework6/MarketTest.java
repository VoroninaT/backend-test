package homework6;

import homework4.spoonaccular.AbstractTest;
import homework6.db.dao.ProductsMapper;
import homework6.db.model.Products;
import homework6.model.ProductDto;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;


public class MarketTest extends AbstractTest {

    private static MarketService marketService;

    @BeforeAll
    static void beforeAll() {
        marketService = new MarketService();
    }

    @Test
    @DisplayName("Создание/Удаление продукта и проверка данных в БД")
    void testCreateProductAndDeleteAndCheckInDb() throws Exception {
        ProductDto product = new ProductDto();
        product.setTitle("BredForCreateAndDelete");
        product.setPrice(100);
        product.setCategoryTitle("Food");
        Response<ProductDto> response = marketService.createProduct(product);
        Assertions.assertEquals(201, response.code());

        ProductDto created = response.body();
        Assertions.assertNotNull(created);

        var idProduct = created.getId();

        Products productFromDb = getProductFromDbById(idProduct);

        Assertions.assertEquals(Long.valueOf(created.getId()), productFromDb.getId());
        Assertions.assertEquals(created.getTitle(), productFromDb.getTitle());
        Assertions.assertEquals(created.getPrice(), productFromDb.getPrice());

        marketService.deleteProduct(idProduct);

        var productAfterDeletion = marketService.getProduct(idProduct);
        Assertions.assertNull(productAfterDeletion);

        Products productFromDbAfterDeletion = getProductFromDbById(idProduct);
        Assertions.assertNull(productFromDbAfterDeletion);
    }


    @Test
    @DisplayName("Создание/Изменение продукта и проверка данных в БД")
    void testCreateProductAndUpdateAndCheckInDb() throws Exception {
        ProductDto product = new ProductDto();
        product.setTitle("BredForUpdate");
        product.setPrice(1000);
        product.setCategoryTitle("Food");
        Response<ProductDto> response = marketService.createProduct(product);
        Assertions.assertEquals(201, response.code());

        ProductDto created = response.body();
        Assertions.assertNotNull(created);

        var idProduct = created.getId();

        Products productFromDb = getProductFromDbById(idProduct);

        Assertions.assertEquals(Long.valueOf(created.getId()), productFromDb.getId());
        Assertions.assertEquals(created.getTitle(), productFromDb.getTitle());
        Assertions.assertEquals(created.getPrice(), productFromDb.getPrice());


        created.setPrice(4376453);
        created.setTitle("BreadAfterUpdateNewTitle123");
        marketService.modifyProduct(created);

        var productAfterUpdate = marketService.getProduct(idProduct);
        Assertions.assertNotNull(productAfterUpdate);
        Assertions.assertEquals(4376453, productAfterUpdate.getPrice());
        Assertions.assertEquals("BreadAfterUpdateNewTitle123", productAfterUpdate.getTitle());

        Products productFromDbAfterUpdate = getProductFromDbById(idProduct);
        Assertions.assertNotNull(productFromDbAfterUpdate);
        Assertions.assertEquals(4376453, productFromDbAfterUpdate.getPrice());
        Assertions.assertEquals("BreadAfterUpdateNewTitle123", productFromDbAfterUpdate.getTitle());

        marketService.deleteProduct(idProduct);

        var productAfterDeletion = marketService.getProduct(idProduct);
        Assertions.assertNull(productAfterDeletion);

        Products productFromDbAfterDeletion = getProductFromDbById(idProduct);
        Assertions.assertNull(productFromDbAfterDeletion);
    }


    private Products getProductFromDbById(Integer idProduct) throws IOException {
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()
                .build(Resources.getResourceAsStream("homework6/myBatisConfig.xml"));

        try (SqlSession session = sessionFactory.openSession()) {
            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            return productsMapper.selectByPrimaryKey(Long.valueOf(idProduct));
        }
    }


}





