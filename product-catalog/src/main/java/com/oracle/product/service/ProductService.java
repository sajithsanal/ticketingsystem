package com.oracle.product.service;


import com.oracle.product.dao.ProductRepository;
import com.oracle.product.dto.ProductDetailsDTO;
import com.oracle.product.entity.ProductDetailsEntity;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.ticketing.util.TicketingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {


    private Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDetailsEntity> getAllProducts(String searchString) {

        logger.info("Search string --> " + searchString);
        if (!StringUtils.isEmpty(searchString))
            return productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(searchString);
        else {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC,"name"));
            return productRepository.findAll(sort);
        }
    }

    @Transactional
    public void saveProduct(ProductDetailsDTO productDetailsDTO) throws TicketingException {
        validateRequest(productDetailsDTO);

        ProductDetailsEntity productDetailsEntity = new ProductDetailsEntity();
        productDetailsEntity.setName(productDetailsDTO.getName());
        productRepository.save(productDetailsEntity);
        logger.info("Successfully saved product details");

    }

    private void validateRequest(ProductDetailsDTO productDetailsDTO) throws TicketingException {

        if (productDetailsDTO == null || (productDetailsDTO != null && StringUtils.isEmpty(productDetailsDTO.getName()))) {

            logger.error("Empty request received...");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Empty request or Empty name field");
        }


    }

}
