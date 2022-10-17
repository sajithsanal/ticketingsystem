package com.oracle.ticketing.controller;


import com.oracle.ticketing.dto.CreateTicketResponseDTO;
import com.oracle.ticketing.dto.TicketAssignRequest;
import com.oracle.ticketing.dto.TicketDetailsDTO;
import com.oracle.ticketing.dto.TicketTransactionDTO;
import com.oracle.ticketing.dto.common.TransRespDTO;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.ticketing.service.TicketingService;
import com.oracle.ticketing.util.TicketingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ticketing")
public class TicketingController {


    Logger logger = LoggerFactory.getLogger(TicketingController.class);

    @Autowired
    private TicketingService ticketingService;

    @PostMapping(value = "/createTicket", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object createTicket(@RequestBody TicketDetailsDTO ticketDetailsDTO) {

        long startTime = System.currentTimeMillis();
        CreateTicketResponseDTO response = null;
        try {
            response = ticketingService.createTicket(ticketDetailsDTO);
        } catch (TicketingException e) {
            logger.error(e.getMessage(), e);
            TransRespDTO transRespDTO = new TransRespDTO();
            transRespDTO.setErrorCode(e.getErrorCode());
            transRespDTO.setErrorDesc(e.getErrorMessage());
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
            return transRespDTO;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransRespDTO transRespDTO = new TransRespDTO();
            transRespDTO.setErrorCode(TicketingConstants.TECHNICAL_ERROR_CODE);
            transRespDTO.setErrorDesc(TicketingConstants.TECH_ERROR_MESSAGE);
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
            return transRespDTO;

        }
        logger.info("Total Time taken to create ticket in system --> " + (System.currentTimeMillis() - startTime) + " ms");

        return response;


    }


    @GetMapping(value = "/listTickets", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object listTickets() {

        long startTime = System.currentTimeMillis();
        List<TicketDetailsDTO> response = null;
        try {
            response = ticketingService.listAllTickets();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransRespDTO transRespDTO = new TransRespDTO();
            transRespDTO.setErrorCode(TicketingConstants.TECHNICAL_ERROR_CODE);
            transRespDTO.setErrorDesc(TicketingConstants.TECH_ERROR_MESSAGE);
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
            return transRespDTO;

        }
        logger.info("Total Time taken to get all ticket from system --> " + (System.currentTimeMillis() - startTime) + " ms");

        return response;


    }

    @PostMapping(value = "/updateTicket", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransRespDTO updateTicket(@RequestBody TicketTransactionDTO ticketTransactionDTO) {

        long startTime = System.currentTimeMillis();
        TransRespDTO transRespDTO = new TransRespDTO();
        try {
            ticketingService.updateTicket(ticketTransactionDTO);
            logger.info("Ticket details updated successfully");
            transRespDTO.setStatus(TicketingConstants.SUCCESS_STATUS);
            transRespDTO.setErrorCode(TicketingConstants.SUCCESS_CODE);
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
        logger.info("Total Time taken to update ticket in system --> " + (System.currentTimeMillis() - startTime) + " ms");

        return transRespDTO;


    }

    @PatchMapping(value = "/assignTicket", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransRespDTO assignTicket(@RequestBody TicketAssignRequest request) {

        long startTime = System.currentTimeMillis();
        TransRespDTO transRespDTO = new TransRespDTO();
        try {
            ticketingService.assignTicket(request);
            logger.info("Ticket assigned successfully");
            transRespDTO.setStatus(TicketingConstants.SUCCESS_STATUS);
            transRespDTO.setErrorCode(TicketingConstants.SUCCESS_CODE);
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
        logger.info("Total Time taken to assign ticket--> " + (System.currentTimeMillis() - startTime) + " ms");

        return transRespDTO;


    }


    @GetMapping(value = "/getTicketUpdates/{ticketId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object getTicketUpdates(@PathVariable("ticketId") Long ticket) {

        long startTime = System.currentTimeMillis();
        List<TicketTransactionDTO> response = null;
        try {
            response = ticketingService.getTicketTransactions(ticket);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransRespDTO transRespDTO = new TransRespDTO();
            transRespDTO.setErrorCode(TicketingConstants.TECHNICAL_ERROR_CODE);
            transRespDTO.setErrorDesc(TicketingConstants.TECH_ERROR_MESSAGE);
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
            return transRespDTO;

        }
        logger.info("Total Time taken to get all ticket transactions --> " + (System.currentTimeMillis() - startTime) + " ms");

        return response;


    }


    @GetMapping(value = "/listCustomerTickets/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object getCustomerTickets(@PathVariable("customerId") Long customerId) {

        long startTime = System.currentTimeMillis();
        List<TicketDetailsDTO> response = null;
        try {
            response = ticketingService.listCustomerTickets(customerId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransRespDTO transRespDTO = new TransRespDTO();
            transRespDTO.setErrorCode(TicketingConstants.TECHNICAL_ERROR_CODE);
            transRespDTO.setErrorDesc(TicketingConstants.TECH_ERROR_MESSAGE);
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
            return transRespDTO;

        }
        logger.info("Total Time taken to list all customer tickets --> " + (System.currentTimeMillis() - startTime) + " ms");

        return response;


    }

}
