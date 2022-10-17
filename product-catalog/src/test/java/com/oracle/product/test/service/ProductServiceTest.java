package com.oracle.product.test.service;


import com.oracle.product.dao.ProductRepository;
import com.oracle.product.dto.ProductDetailsDTO;
import com.oracle.product.entity.ProductDetailsEntity;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.product.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @TestConfiguration
    static class ProductServiceTestContextConfiguration {

        @Bean
        public ProductService productService() {
            return new ProductService();
        }


    }

    @Autowired
    private ProductService productService;


    @MockBean
    private ProductRepository productRepository;

    @Test
    public void testGetAllProducts_test1() {

        List<ProductDetailsEntity> productDetailsEntityList = new ArrayList<>();
        productDetailsEntityList.add(new ProductDetailsEntity());
        productDetailsEntityList.add(new ProductDetailsEntity());

        Mockito.when(productRepository.findAll(new Sort(new Sort.Order(Sort.Direction.ASC, "name")))).thenReturn(productDetailsEntityList);
        List<ProductDetailsEntity> returnEntity = productService.getAllProducts(null);
        Assert.assertNotNull(returnEntity);


    }

    @Test
    public void testGetAllProducts_test2() {

        List<ProductDetailsEntity> productDetailsEntityList = new ArrayList<>();
        productDetailsEntityList.add(new ProductDetailsEntity());
        productDetailsEntityList.add(new ProductDetailsEntity());

        Mockito.when(productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(Mockito.anyString())).thenReturn(productDetailsEntityList);
        List<ProductDetailsEntity> returnEntity = productService.getAllProducts("Ad");
        Assert.assertNotNull(returnEntity);


    }

    @Test
    public void testSaveProduct_test1() throws TicketingException {
        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productDetailsDTO.setName("Test Prod");
        productService.saveProduct(productDetailsDTO);


    }

    @Test(expected = TicketingException.class)
    public void testSaveProduct_test2() throws TicketingException {
        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productService.saveProduct(productDetailsDTO);
    }


}
