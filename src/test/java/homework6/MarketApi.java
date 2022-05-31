package homework6;

import homework6.model.MarketCategoriesResult;
import homework6.model.ProductDto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface MarketApi {

    @GET("/market/api/v1/categories/{id}")
    Call<MarketCategoriesResult> category(@Path("id") Integer id);

    @GET("/market/api/v1/products")
    Call<List<ProductDto>> getProducts();

    @GET("/market/api/v1/products/{id}")
    Call<ProductDto> getProduct(@Path("id") Integer id);

    @POST("/market/api/v1/products")
    Call<ProductDto> createProduct(@Body ProductDto body);

    @PUT("/market/api/v1/products")
    Call<Void> modifyProduct(@Body ProductDto body);

    @DELETE("/market/api/v1/products/{id}")
    Call<Void> deleteProduct(@Path("id") Integer id);
}

