package com.oracle.product.controller;


import com.oracle.product.dto.ProductDetailsDTO;
import com.oracle.ticketing.dto.common.TransRespDTO;
import com.oracle.product.entity.ProductDetailsEntity;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.product.service.ProductService;
import com.oracle.ticketing.util.TicketingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/addProduct", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransRespDTO addProduct(@RequestBody ProductDetailsDTO productDetailsDTO) {

        long startTime = System.currentTimeMillis();
        TransRespDTO transRespDTO = new TransRespDTO();
        try {
            productService.saveProduct(productDetailsDTO);
            transRespDTO.setErrorCode(TicketingConstants.SUCCESS_CODE);
            transRespDTO.setStatus(TicketingConstants.SUCCESS_STATUS);
        } catch (TicketingException e) {
            logger.error(e.getMessage(), e);
            transRespDTO.setErrorCode(e.getErrorCode());
            transRespDTO.setErrorDesc(e.getErrorMessage());
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            transRespDTO.setErrorCode(TicketingConstants.TECHNICAL_ERROR_CODE);
            transRespDTO.setErrorDesc(TicketingConstants.TECH_ERROR_MESSAGE);
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
        }

        logger.info("Total Time taken to add a product is --> " + (System.currentTimeMillis() - startTime) + " ms");

        return transRespDTO;

    }


    @GetMapping(value = "/listProducts", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object listProducts(@RequestParam(name = "searchVal") String searchVal) {

        long startTime = System.currentTimeMillis();
        List<ProductDetailsEntity> response = null;
        try {
            response = productService.getAllProducts(searchVal);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransRespDTO transRespDTO = new TransRespDTO();
            transRespDTO.setErrorCode(TicketingConstants.TECHNICAL_ERROR_CODE);
            transRespDTO.setErrorDesc(TicketingConstants.TECH_ERROR_MESSAGE);
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
            return transRespDTO;

        }
        logger.info("Total Time taken to fetch the products --> " + (System.currentTimeMillis() - startTime) + " ms");

        return response;


    }


}
