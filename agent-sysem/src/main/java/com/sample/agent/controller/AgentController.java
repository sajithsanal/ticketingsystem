package com.sample.agent.controller;


import com.sample.agent.entity.AgentDetailsEntity;
import com.sample.agent.service.AgentService;
import com.oracle.ticketing.dto.agent.AgentDetailsDTO;
import com.oracle.ticketing.dto.common.TransRespDTO;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.ticketing.util.TicketingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Autowired
    private AgentService agentService;

    @PostMapping(value = "/addAgent", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransRespDTO addAgent(@RequestBody AgentDetailsDTO agentDetailsDTO) {

        long startTime = System.currentTimeMillis();
        TransRespDTO transRespDTO = new TransRespDTO();
        try {
            agentService.createAgent(agentDetailsDTO);
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

        logger.info("Total Time taken to add an agent is --> " + (System.currentTimeMillis() - startTime) + " ms");

        return transRespDTO;

    }


    @GetMapping(value = "/listAgents", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Object listAgents() {

        long startTime = System.currentTimeMillis();
        List<AgentDetailsEntity> response = null;
        try {
            response = agentService.getAllAgents();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransRespDTO transRespDTO = new TransRespDTO();
            transRespDTO.setErrorCode(TicketingConstants.TECHNICAL_ERROR_CODE);
            transRespDTO.setErrorDesc(TicketingConstants.TECH_ERROR_MESSAGE);
            transRespDTO.setStatus(TicketingConstants.ERROR_STATUS);
            return transRespDTO;

        }
        logger.info("Total Time taken to fetch all the agents --> " + (System.currentTimeMillis() - startTime) + " ms");

        return response;

    }


    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    TransRespDTO agentLogin(@RequestBody AgentDetailsDTO agentDetailsDTO) {

        long startTime = System.currentTimeMillis();
        TransRespDTO transRespDTO = new TransRespDTO();
        try {
            agentService.agentLogin(agentDetailsDTO);
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

        logger.info("Total Time taken for agent login --> " + (System.currentTimeMillis() - startTime) + " ms");

        return transRespDTO;

    }


}
