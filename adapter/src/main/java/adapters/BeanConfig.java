package adapters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ports.ProductOptionsPort;
import service.ProductService;
import ports.ProductServicePort;

@Configuration
public class BeanConfig {

    @Bean
    ProductService productService(ProductServicePort productServicePort, ProductOptionsPort productOptionsPort){
        return new ProductService(productServicePort, productOptionsPort);
    }
}
