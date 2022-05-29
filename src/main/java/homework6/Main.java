package homework6;

import homework6.db.dao.ProductsMapper;
import homework6.db.model.Products;
import homework6.db.model.ProductsExample;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder()
                .build(Resources.getResourceAsStream("homework6/myBatisConfig.xml"));

        try (SqlSession session = sessionFactory.openSession()) {
            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            Products product = productsMapper.selectByPrimaryKey(473L);
            System.out.println("Select: " + product);

            ProductsExample example = new ProductsExample();
            example.createCriteria()
                    .andTitleLike("Bread")
                    .andPriceGreaterThan(4);

            List<Products> products = productsMapper.selectByExample(example);
            System.out.println("Select by example1: " + products);

            example.clear();
            example.createCriteria()
                    .andCategoryIdEqualTo(2L);

            products = productsMapper.selectByExample(example);
            System.out.println("Select by example2: " + products);

            productsMapper.deleteByPrimaryKey(444L);
            example.clear();

            example.createCriteria()
                    .andTitleLike("Apple")
                    .andPriceGreaterThan(5);

            products = productsMapper.selectByExample(example);
            System.out.println("Select by example3: " + products);
        }
    }
}
