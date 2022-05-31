package homework6.model;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String title;
    private Integer price;
    private String categoryTitle;
}
