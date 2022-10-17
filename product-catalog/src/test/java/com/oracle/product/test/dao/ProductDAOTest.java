package com.oracle.product.test.dao;

import com.oracle.product.dao.ProductRepository;
import com.oracle.product.entity.ProductDetailsEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ProductDAOTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;


    @Test
    public void testFindByNameContainingIgnoreCaseOrderByNameAsc(){

        ProductDetailsEntity productDetailsEntity = new ProductDetailsEntity();
        productDetailsEntity.setName("Addidas34");
        entityManager.persist(productDetailsEntity);
        entityManager.flush();

        List<ProductDetailsEntity> fetchedEntity = productRepository.findByNameContainingIgnoreCaseOrderByNameAsc("Ad");

        Assert.assertNotNull(fetchedEntity);
        Assert.assertTrue(fetchedEntity.size() >0);




    }




}
