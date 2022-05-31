package homework4.spoonaccular.homework5.model;

import lombok.Data;

import java.util.List;

@Data
public class MarketCategoriesResult {

    private Integer id;
    private String title;
    private List<ProductDto> products;
}
