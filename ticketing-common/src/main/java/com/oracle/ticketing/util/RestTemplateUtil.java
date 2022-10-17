package com.oracle.ticketing.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.ticketing.dto.agent.AgentDetailsDTO;
import com.oracle.ticketing.dto.customer.CustomerDTO;
import com.oracle.ticketing.exception.TicketingException;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RestTemplateUtil {

    Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

    @Autowired
    private RestTemplate restTemplate;

    public Map<Long, String> getAllAgents(String apiUrl) throws TicketingException {

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        if (response.getStatusCodeValue() == 200 && response.getBody().contains(TicketingConstants.ERROR_STATUS)) {
            logger.error("Error in getting the response from Agent service." + response.getBody());
            throw new TicketingException(TicketingConstants.TECHNICAL_ERROR_CODE, "Error in getting the response from Agent service." + response.getBody());

        }

        String body = response.getBody();
        logger.info("Response from Agent service. -> " + response.getBody());
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                ;
        try {
            List<AgentDetailsDTO> agentDetailsDTOList = mapper.readValue(body, new TypeReference<List<AgentDetailsDTO>>(){});
            Map<Long, String> agentMap = new HashMap<>();
            agentDetailsDTOList.forEach((agent) -> agentMap.put(agent.getId(), agent.getFullName()));
            return agentMap;

        } catch (JsonProcessingException e) {
            logger.error("Error in parsing response from agent service. ", e);
            throw new TicketingException(TicketingConstants.TECHNICAL_ERROR_CODE, e.getMessage(), e);

        }



    }

    public Map<Long, String> getAllCustomers(String apiUrl) throws TicketingException {

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        if (response.getStatusCodeValue() == 200 && response.getBody().contains(TicketingConstants.ERROR_STATUS)) {
            logger.error("Error in getting the response from Agent service." + response.getBody());
            throw new TicketingException(TicketingConstants.TECHNICAL_ERROR_CODE, "Error in getting the response from Agent service." + response.getBody());

        }

        String body = response.getBody();
        logger.info("Response from Customer service. -> " + response.getBody());
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            List<CustomerDTO> customerDTOList = mapper.readValue(body, new TypeReference<List<CustomerDTO>>(){});
            Map<Long, String> customerMap = new HashMap<>();
            customerDTOList.forEach((customer) -> customerMap.put(customer.getId(), customer.getFullName()));
            return customerMap;

        } catch (JsonProcessingException e) {
            logger.error("Error in parsing response from customer service. ", e);
            throw new TicketingException(TicketingConstants.TECHNICAL_ERROR_CODE, e.getMessage(), e);

        }



    }


}
