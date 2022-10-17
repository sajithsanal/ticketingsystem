package com.oracle.product.test.controller;

import com.oracle.product.controller.ProductController;
import com.oracle.product.dto.ProductDetailsDTO;
import com.oracle.ticketing.dto.common.TransRespDTO;
import com.oracle.product.entity.ProductDetailsEntity;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.product.service.ProductService;
import com.oracle.ticketing.util.TicketingConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class ProductControllerTest {

    @TestConfiguration
    static class ProductControllerTestContextConfiguration {

        @Bean
        public ProductController productController() {
            return new ProductController();
        }


    }


    @MockBean
    private ProductService productService;

    @Autowired
    private ProductController productController;

    @Test
    public void contextLoads() {
    }


    @Test
    public void testController() {

        Assert.assertNotNull(productController.listProducts(null));

    }

    @Test
    public void testControllerListProducts() {

        List<ProductDetailsEntity> productDetailsEntityList = (List<ProductDetailsEntity>) productController.listProducts("Ad");
        Assert.assertNotNull(productDetailsEntityList);

    }


    @Test
    public void testControllerListProducts_Exception() {

        Mockito.when(productService.getAllProducts(null)).thenThrow(new RuntimeException("Error"));
        TransRespDTO respDTO = (TransRespDTO) productController.listProducts(null);
        Assert.assertNotNull(respDTO);

    }


    @Test
    public void testControllerAddProducts() {
        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productDetailsDTO.setName("Test Prod");
        TransRespDTO resp = productController.addProduct(productDetailsDTO);
        Assert.assertNotNull(resp);
        Assert.assertTrue(TicketingConstants.SUCCESS_STATUS.equals(resp.getStatus()));
    }


    @Test
    public void testControllerAddProducts_ProductException() throws TicketingException {

        Mockito.doThrow(new TicketingException("Error", "")).when(productService).saveProduct(null);
        TransRespDTO resp = productController.addProduct(null);
        Assert.assertNotNull(resp);
        Assert.assertTrue(TicketingConstants.ERROR_STATUS.equals(resp.getStatus()));

    }

    @Test
    public void testControllerAddProducts_Exception() throws TicketingException {

        Mockito.doThrow(new RuntimeException("Error")).when(productService).saveProduct(null);
        TransRespDTO resp = productController.addProduct(null);
        Assert.assertNotNull(resp);
        Assert.assertTrue(TicketingConstants.ERROR_STATUS.equals(resp.getStatus()));

    }
}
