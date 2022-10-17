package com.oracle.product.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.product.ProductApplication;
import com.oracle.product.dto.ProductDetailsDTO;
import com.oracle.ticketing.dto.common.TransRespDTO;
import com.oracle.product.entity.ProductDetailsEntity;
import com.oracle.product.service.ProductService;
import com.oracle.ticketing.util.TicketingConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProductApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductIntegrationTest {


    @TestConfiguration
    static class ProductIntegrationTestContextConfiguration {

        @Bean
        public ProductService productService() {
            return new ProductService();
        }


    }


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSaveProduct() throws IOException {

        ProductDetailsDTO productDetailsDTO = new ProductDetailsDTO();
        productDetailsDTO.setName("Testing Product");
        HttpEntity<ProductDetailsDTO> request = new HttpEntity<>(productDetailsDTO);

        ResponseEntity<String> resp = restTemplate.postForEntity("http://localhost:" + port + "/product/addProduct", request, String.class);
        Assert.assertNotNull(resp);
        Assert.assertEquals(200, resp.getStatusCodeValue());
        ObjectMapper objectMapper = new ObjectMapper();
        TransRespDTO finalResponse = objectMapper.readValue(resp.getBody(), TransRespDTO.class);
        Assert.assertTrue(TicketingConstants.SUCCESS_STATUS.equals(finalResponse.getStatus()));
    }

    @Test
    public void testListProduct() throws IOException {
        String response = restTemplate.getForObject("http://localhost:" + port + "/product/listProducts?searchVal=", String.class);
        Assert.assertNotNull(response);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductDetailsEntity> resp = objectMapper.readValue(response, List.class);
        Assert.assertTrue(resp.size() > 0);
    }

    @Test
    public void testListProduct_testcase2() throws IOException {
        String response = restTemplate.getForObject("http://localhost:" + port + "/product/listProducts?searchVal=ad", String.class);
        Assert.assertNotNull(response);
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProductDetailsEntity> resp = objectMapper.readValue(response, List.class);
        Assert.assertTrue(resp.size() > 0);
    }


}
