package com.oracle.ticketing.service;

import com.oracle.ticketing.dao.TicketAttachmentsRepo;
import com.oracle.ticketing.dao.TicketDetailsRepository;
import com.oracle.ticketing.dao.TicketTransDetailsRepo;
import com.oracle.ticketing.dto.CreateTicketResponseDTO;
import com.oracle.ticketing.dto.TicketAssignRequest;
import com.oracle.ticketing.dto.TicketDetailsDTO;
import com.oracle.ticketing.dto.TicketTransactionDTO;
import com.oracle.ticketing.entity.TicketDetailsEntity;
import com.oracle.ticketing.entity.TicketTransDetailsEntity;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.ticketing.util.RestTemplateUtil;
import com.oracle.ticketing.util.TicketingConstants;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TicketingService {

    Logger logger = LoggerFactory.getLogger(TicketingService.class);


    @Autowired
    private TicketDetailsRepository ticketDetailsRepository;

    @Autowired
    private TicketTransDetailsRepo ticketTransDetailsRepo;

    @Autowired
    private TicketAttachmentsRepo ticketAttachmentsRepo;

    @Autowired
    private RestTemplateUtil restTemplateUtil;

    @Value("${agent.service.api.url}")
    private String agentApiUrl;

    @Value("${customer.service.api.url}")
    private String customerApiUrl;

    @Autowired
    private ModelMapper modelMapper;


    @Transactional
    public CreateTicketResponseDTO createTicket(TicketDetailsDTO request) throws TicketingException {
        validateRequest(request);
        logger.info("Received request for create request." + request.getCustomerId());

        request.setTicketId(null);
        TicketDetailsEntity entity = modelMapper.map(request, TicketDetailsEntity.class);
        Timestamp createdTime = new Timestamp(System.currentTimeMillis());
        entity.setCreatedDate(createdTime);
        entity.setLastUpdatedDate(createdTime);
        entity.setStatus(TicketingConstants.OPEN);

        //Saving Parent entity
        entity = ticketDetailsRepository.save(entity);

        //Setting the child transaction entity details
        TicketTransDetailsEntity ticketTransDetailsEntity = new TicketTransDetailsEntity();
        ticketTransDetailsEntity.setLastUpdatedById(request.getLastUpdatedById());
        ticketTransDetailsEntity.setStatus(TicketingConstants.OPEN);
        ticketTransDetailsEntity.setLastUpdatedDate(createdTime);
        ticketTransDetailsEntity.setNotes(request.getComplaintDetails());
        ticketTransDetailsEntity.setTicketId(entity.getTicketId());

        //Saving child entuty
        ticketTransDetailsRepo.save(ticketTransDetailsEntity);

        CreateTicketResponseDTO createTicketResponseDTO = new CreateTicketResponseDTO();
        createTicketResponseDTO.setStatus(TicketingConstants.SUCCESS_STATUS);
        createTicketResponseDTO.setTicketId(entity.getTicketId());
        logger.info("Completed persisting ticket details");
        return createTicketResponseDTO;


    }

    public List<TicketDetailsDTO> listAllTickets() throws TicketingException {

        logger.info("Received request to get all tickets");
        List<TicketDetailsEntity> entityList = ticketDetailsRepository.findAll(new Sort(new Sort.Order(Sort.Direction.DESC, "lastUpdatedDate")));
        List<TicketDetailsDTO> response = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            entityList.forEach((item) -> {
                TicketDetailsDTO dto = modelMapper.map(item, TicketDetailsDTO.class);
                response.add(dto);
            });
            mapNames(response);
        }


        return response;
    }



    public void updateTicket(TicketTransactionDTO request) throws TicketingException {
        validateUpdateRequest(request);
        logger.info("Received request for updating the ticket");
        request.setTransId(null);
        TicketDetailsEntity ticketDetailsEntity = ticketDetailsRepository.findByTicketId(request.getTicketId());

        if (ticketDetailsEntity == null) {
            logger.error("Unable to find the ticket with ticket id - > " + request.getTicketId());
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Unable to find the ticket with ticket id - > " + request.getTicketId());
        }
        if (TicketingConstants.CLOSED.equals(ticketDetailsEntity.getStatus())) {
            logger.error("Ticket Status is already closed. Unable to update the ticket. Please create a new ticket.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Ticket Status is already closed. Unable to update the ticket. Please create a new ticket.");
        }


        TicketTransDetailsEntity ticketTransDetailsEntity = modelMapper.map(request,TicketTransDetailsEntity.class);
        ticketTransDetailsEntity.setLastUpdatedDate(new Timestamp(System.currentTimeMillis()));
        ticketDetailsEntity.setStatus(ticketTransDetailsEntity.getStatus());

        ticketDetailsRepository.save(ticketDetailsEntity);

        ticketTransDetailsRepo.save(ticketTransDetailsEntity);


        logger.info("Successfully updated the ticket details");


    }

    public void assignTicket(TicketAssignRequest request) throws TicketingException {
        validateAssignTicketRequest(request);
        validateAssignee(request.getAssignee());
        logger.info("Received request for assigning the ticket");;
        TicketDetailsEntity ticketDetailsEntity = ticketDetailsRepository.findByTicketId(request.getTicketId());

        if (ticketDetailsEntity == null) {
            logger.error("Unable to find the ticket with ticket id - > " + request.getTicketId());
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Unable to find the ticket with ticket id - > " + request.getTicketId());
        }
        if (TicketingConstants.CLOSED.equals(ticketDetailsEntity.getStatus())) {
            logger.error("Ticket Status is already closed. Unable to assign the ticket.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Ticket Status is already closed. Unable to assign the ticket.");
        }
        ticketDetailsEntity.setStatus(TicketingConstants.ASSIGNED);
        ticketDetailsEntity.setAssignedTo(request.getAssignee());

        ticketDetailsRepository.save(ticketDetailsEntity);

        logger.info("Successfully assigned the ticket details");


    }

    public List<TicketTransactionDTO> getTicketTransactions(Long ticket) throws TicketingException {
        if (ticket == null) {

            logger.error("Ticket Id is empty- > ");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Ticket Id is empty");

        }
        logger.info("Received request for assigning the ticket");;
        TicketDetailsEntity ticketDetailsEntity = ticketDetailsRepository.findByTicketId(ticket);

        if (ticketDetailsEntity == null) {
            logger.error("Unable to find the ticket with ticket id - > " + ticket);
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Unable to find the ticket with ticket id - > " + ticket);
        }

        List<TicketTransDetailsEntity> ticketTransDetailsEntityList = ticketTransDetailsRepo.findAllByTicketIdOrderByLastUpdatedDateDesc(ticket);
        List<TicketTransactionDTO> response = new ArrayList<>();

        if(!CollectionUtils.isEmpty(ticketTransDetailsEntityList)){



            ticketTransDetailsEntityList.forEach((trans) -> {
                response.add(modelMapper.map(trans,TicketTransactionDTO.class)) ;
            });
        }
        mapTransNames(response)   ;


        logger.info("Ticket Trans details of size --> " + response.size());

        return response;

    }

    public List<TicketDetailsDTO> listCustomerTickets(Long customerId) throws TicketingException {

        if (customerId == null) {
            logger.error("Customer Id is empty- > ");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Customer Id");

        }

        validateCustomer(customerId);
        logger.info("Received request to get all tickets for " + customerId);
        List<TicketDetailsEntity> entityList = ticketDetailsRepository.findAllByCustomerIdOrderByLastUpdatedDateDesc(customerId);
        List<TicketDetailsDTO> response = new ArrayList<>();
        if (!CollectionUtils.isEmpty(entityList)) {
            entityList.forEach((item) -> {
                TicketDetailsDTO dto = modelMapper.map(item, TicketDetailsDTO.class);
                response.add(dto);
            });
            mapNames(response);
        }


        return response;
    }

    private void mapNames(List<TicketDetailsDTO> response) throws TicketingException {

        //Logic for mapping customer/agent ids with their names

        Map<Long, String> agentMap = restTemplateUtil.getAllAgents(agentApiUrl);
        Map<Long, String> customerMap = restTemplateUtil.getAllCustomers(customerApiUrl);
        response.forEach((ticket) -> {
            if (TicketingConstants.AGENT.equals(ticket.getCreatorType())) {

                ticket.setCreatedBy(agentMap.get(ticket.getCreatedById()));
            } else {
                ticket.setCreatedBy(customerMap.get(ticket.getCreatedById()));
            }

            if (!StringUtils.isEmpty(agentMap.get(ticket.getLastUpdatedById()))) {
                ticket.setLastUpdatedBy(agentMap.get(ticket.getLastUpdatedById()));
            } else {
                ticket.setLastUpdatedBy(customerMap.get(ticket.getLastUpdatedById()));
            }

        });


    }

    private void mapTransNames(List<TicketTransactionDTO> response) throws TicketingException {

        //Logic for mapping customer/agent ids with their names

        Map<Long, String> agentMap = restTemplateUtil.getAllAgents(agentApiUrl);
        Map<Long, String> customerMap = restTemplateUtil.getAllCustomers(customerApiUrl);
        response.forEach((trans) -> {

            if (!StringUtils.isEmpty(agentMap.get(Long.valueOf(trans.getLastUpdatedById())))) {
                trans.setLastUpdatedBy(agentMap.get(Long.valueOf(trans.getLastUpdatedById())));
            } else {
                trans.setLastUpdatedBy(customerMap.get(Long.valueOf(trans.getLastUpdatedById())));
            }

        });


    }

    private void validateAssignee(String assignee) throws TicketingException {
        Map<Long, String> agentMap = restTemplateUtil.getAllAgents(agentApiUrl);
        //Check for valid agent id
        if(agentMap.get(Long.valueOf(assignee)) ==  null){
            logger.error("Unable to find the agent with id  " + assignee);
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Unable to find the agent with id  " + assignee);
        }


    }

    private void validateCustomer(Long customerId) throws TicketingException {
        Map<Long, String> customerMap = restTemplateUtil.getAllCustomers(customerApiUrl);
        //Check for valid agent id
        if(customerMap.get(customerId) ==  null){
            logger.error("Unable to find the customer with id  " + customerId);
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Unable to find the customer with id  " + customerId);
        }


    }


    private void validateAssignTicketRequest(TicketAssignRequest request) throws TicketingException {

        if (request == null) {

            logger.error("Validation failed. Empty Request Received.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Empty Request Received.");
        }

        if (StringUtils.isEmpty(request.getAssignee())) {

            logger.error("Assignee details are empty.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Assignee details are empty.");
        }

        if (StringUtils.isEmpty(request.getTicketId())) {

            logger.error("Ticket Id is empty");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Ticket Id is empty");
        }



    }



    private void validateUpdateRequest(TicketTransactionDTO request) throws TicketingException {

        if (request == null) {

            logger.error("Validation failed. Empty Request Received.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Empty Request Received.");
        }

        if (StringUtils.isEmpty(request.getLastUpdatedById())) {

            logger.error("Last Updated by is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Last Updated by is empty in the request.");
        }

        if (StringUtils.isEmpty(request.getNotes())) {

            logger.error("Notes empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Notes is empty in the request.");
        }

        if (StringUtils.isEmpty(request.getStatus())) {

            logger.error("Status is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Status is empty in the request.");
        }


        if (StringUtils.isEmpty(request.getTicketId())) {

            logger.error("Ticket Id is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Ticket Id is empty in the request.");
        }


    }


    private void validateRequest(TicketDetailsDTO request) throws TicketingException {

        if (request == null) {

            logger.error("Validation failed. Empty Request Received.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Empty Request Received.");
        }

        if (StringUtils.isEmpty(request.getProductId())) {

            logger.error("Product Id is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Product Id is empty in the request.");
        }

        if (StringUtils.isEmpty(request.getCustomerId())) {

            logger.error("Customer Id is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Customer Id is empty in the request.");
        }

        if (StringUtils.isEmpty(request.getComplaintDetails())) {

            logger.error("Complaint details is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Complaint details is empty in the request.");
        }


        if (StringUtils.isEmpty(request.getCreatedById())) {

            logger.error("Created Id is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Created Id is empty in the request.");
        }

        if (StringUtils.isEmpty(request.getLastUpdatedById())) {

            logger.error("Last Updated Id is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Last Updated is empty in the request.");
        }

        if (StringUtils.isEmpty(request.getCreatorType())) {

            logger.error("Creator type is empty in the request.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Creator type is empty in the request.");
        }


    }


}
