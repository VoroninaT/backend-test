package homework6;

import homework6.model.MarketCategoriesResult;
import homework6.model.ProductDto;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class MarketService {

    private MarketApi marketApi;

    public MarketService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(System.out::println);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        this.marketApi = new Retrofit.Builder()
                .baseUrl("https://minimarket1.herokuapp.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MarketApi.class);
    }


    public MarketCategoriesResult getCategory() {
        Call<MarketCategoriesResult> categoryCall = marketApi.category(1);
        return getCallBody(categoryCall);
    }

    public List<ProductDto> getProducts() {
        Call<List<ProductDto>> productsCall = marketApi.getProducts();
        return getCallBody(productsCall);
    }

    public ProductDto getProduct(Integer id) {
        Call<ProductDto> productCall = marketApi.getProduct(id);
        return getCallBody(productCall);
    }

    public Response<ProductDto> createProduct(ProductDto body) throws IOException {
        Call<ProductDto> productsCall = marketApi.createProduct(body);
        return productsCall.execute();
    }

    public Response<Void> modifyProduct(ProductDto body) throws IOException {
        Call<Void> productsCall = marketApi.modifyProduct(body);
        return productsCall.execute();
    }

    public Response<Void> deleteProduct(Integer id) throws IOException {
        Call<Void> deleteCall = marketApi.deleteProduct(id);
        return deleteCall.execute();
    }


    public <T> T getCallBody(Call<T> call) {
        try {
            Response<T> response = call.execute();
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
