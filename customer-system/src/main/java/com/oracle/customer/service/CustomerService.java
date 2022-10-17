package com.oracle.customer.service;

import com.oracle.customer.dao.CustomerDetailsRepository;
import com.oracle.customer.entity.CustomerDetailsEntity;
import com.oracle.ticketing.dto.customer.CustomerDTO;
import com.oracle.ticketing.dto.customer.CustomerDetailsDTO;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.ticketing.util.TicketingConstants;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

    Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    public void createCustomer(CustomerDetailsDTO customerDetailsDTO) throws TicketingException {
        validateRequest(customerDetailsDTO);
        logger.info("Received request to create customer --> " + customerDetailsDTO.getFullName());
        ModelMapper modelMapper = new ModelMapper();
        customerDetailsDTO.setId(null);
        CustomerDetailsEntity existCustomer = customerDetailsRepository.findByEmail(customerDetailsDTO.getEmail());
        if (existCustomer != null) {
            logger.error("Unable to create customer. Customer with email " + existCustomer.getEmail() + " already exists in the system");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Unable to create customer. Customer with email " + customerDetailsDTO.getEmail() + " already exists in the system");

        }
        validateDobFormat(customerDetailsDTO.getDob());
        customerDetailsDTO.setPassword(passwordEncoder.encode(customerDetailsDTO.getPassword()));
        CustomerDetailsEntity customerDetailsEntity = modelMapper.map(customerDetailsDTO, CustomerDetailsEntity.class);

        customerDetailsRepository.save(customerDetailsEntity);

    }


    public void customerLogin(CustomerDetailsDTO customerDetailsDTO) throws TicketingException {

        validateEmptyRequest(customerDetailsDTO);
        validateEmailId(customerDetailsDTO.getEmail());
        validatePassword(customerDetailsDTO.getPassword());
        logger.info("Customer login request received --> " + customerDetailsDTO.getEmail());
        CustomerDetailsEntity existCustomer = customerDetailsRepository.findByEmail(customerDetailsDTO.getEmail());

        if (existCustomer == null) {

            logger.error("Username/Email id is wrong");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Username/Email id is wrong");

        }


        if (existCustomer != null && !passwordEncoder.matches(customerDetailsDTO.getPassword(), existCustomer.getPassword())) {

            logger.error("Password is wrong");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Password is wrong");

        }
    }

    public List<CustomerDTO> getAllCustomerNames(){
        logger.info("Get all getAllCustomerNames --> ");
        List<CustomerDetailsEntity> customerDetailsEntityList = customerDetailsRepository.findAll();
        List<CustomerDTO> resp = new ArrayList<>();
        if(!CollectionUtils.isEmpty(customerDetailsEntityList)){

            customerDetailsEntityList.forEach((customer) -> {
                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setId(customer.getId());
                customerDTO.setFullName(customer.getFullName());
                resp.add(customerDTO);
            });


        }
        logger.info("Get all response  --> " + resp);
        return resp;

    }


    private void validateRequest(CustomerDetailsDTO customerDetailsDTO) throws TicketingException {


        validateEmptyRequest(customerDetailsDTO);

        if (StringUtils.isEmpty(customerDetailsDTO.getFullName())) {

            logger.error("Validation failed. Full Name is empty.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Full Name is empty.");
        }

        validateEmailId(customerDetailsDTO.getEmail());

        validatePassword(customerDetailsDTO.getPassword());


    }


    private void validateDobFormat(String dob) throws TicketingException {

        if (!StringUtils.isEmpty(dob)) {


            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                simpleDateFormat.setLenient(false);
                Date date = simpleDateFormat.parse(dob);
            } catch (ParseException e) {
                logger.error(dob + " is a wrong date of birth format. Please enter dob as yyyy-MM-dd, ex: 2002-01-30");
                throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, dob + " is a wrong date of birth format. Please enter dob as yyyy-MM-dd, ex: 2002-01-30");
            }


        }


    }


    private void validateEmptyRequest(CustomerDetailsDTO customerDetailsDTO) throws TicketingException {

        if (customerDetailsDTO == null) {

            logger.error("Validation failed. Empty Request Received.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Empty Request Received.");
        }

    }


    private void validateEmailId(String emailId) throws TicketingException {

        if (StringUtils.isEmpty(emailId)) {

            logger.error("Validation failed. Email Id is empty.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Email id is empty.");
        }

    }

    private void validatePassword(String password) throws TicketingException {

        if (StringUtils.isEmpty(password)) {

            logger.error("Validation failed. Password is empty.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Password is empty.");
        }

    }

}
