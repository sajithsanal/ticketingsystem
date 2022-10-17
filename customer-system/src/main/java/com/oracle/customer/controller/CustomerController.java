package com.oracle.customer.controller;


import com.oracle.customer.service.CustomerService;
import com.oracle.ticketing.dto.common.TransRespDTO;
import com.oracle.ticketing.dto.customer.CustomerDTO;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.ticketing.util.TicketingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.oracle.ticketing.dto.customer.CustomerDetailsDTO;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping(value = "/addCustomer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransRespDTO addAgent(@RequestBody CustomerDetailsDTO customerDetailsDTO) {

        long startTime = System.currentTimeMillis();
        TransRespDTO transRespDTO = new TransRespDTO();
        try {
            customerService.createCustomer(customerDetailsDTO);
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

        logger.info("Total Time taken to add a customer is --> " + (System.currentTimeMillis() - startTime) + " ms");

        return transRespDTO;

    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransRespDTO customerLogin(@RequestBody CustomerDetailsDTO customerDetailsDTO) {

        long startTime = System.currentTimeMillis();
        TransRespDTO transRespDTO = new TransRespDTO();
        try {
            customerService.customerLogin(customerDetailsDTO);
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

        logger.info("Total Time taken for customer login --> " + (System.currentTimeMillis() - startTime) + " ms");

        return transRespDTO;

    }

    @GetMapping(value = "/listCustomers", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object listCustomers() {

        long startTime = System.currentTimeMillis();
        List<CustomerDTO> resp = null;

        try {
            resp = customerService.getAllCustomerNames();
        } catch (Exception e) {
            TransRespDTO transRespDTO = new TransRespDTO();
            logger.error(e.getMessage(), e);
            transRespDTO.setErrorCode(TicketingConstants.TECHNICAL_ERROR_CODE);
            transRespDTO.setErrorDesc(TicketingConstants.TECH_ERROR_MESSAGE);
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
            return transRespDTO;
        }

        logger.info("Total Time taken to list all customer is --> " + (System.currentTimeMillis() - startTime) + " ms");

        return resp;

    }


}
