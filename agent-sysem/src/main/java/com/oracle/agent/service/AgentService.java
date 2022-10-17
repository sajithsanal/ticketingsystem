package com.oracle.agent.service;

import com.oracle.agent.dao.AgentDetailsRepository;
import com.oracle.agent.entity.AgentDetailsEntity;
import com.oracle.ticketing.dto.agent.AgentDetailsDTO;
import com.oracle.ticketing.dto.common.TransRespDTO;
import com.oracle.ticketing.exception.TicketingException;
import com.oracle.ticketing.util.TicketingConstants;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AgentService {


    Logger logger = LoggerFactory.getLogger(AgentService.class);

    @Autowired
    private AgentDetailsRepository agentDetailsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createAgent(AgentDetailsDTO agentDetailsDTO) throws TicketingException {
        validateRequest(agentDetailsDTO);
        logger.info("Received request create agent --> " + agentDetailsDTO.getFullName());
        ModelMapper modelMapper = new ModelMapper();
        agentDetailsDTO.setId(null);
        AgentDetailsEntity existAgent = agentDetailsRepository.findByUserName(agentDetailsDTO.getUserName());
        if(existAgent != null){
            logger.error("Unable to create agent. Agent with username " + agentDetailsDTO.getUserName() + " already exists in the system" );
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Unable to create agent. Agent with username " + agentDetailsDTO.getUserName() + " already exists in the system" );

        }
        agentDetailsDTO.setPassword(passwordEncoder.encode(agentDetailsDTO.getPassword()));
        AgentDetailsEntity agentDetailsEntity = modelMapper.map(agentDetailsDTO, AgentDetailsEntity.class);
        agentDetailsRepository.save(agentDetailsEntity);

    }


    public List<AgentDetailsEntity> getAllAgents() {

        logger.info(" Executing get all agents method");

        return agentDetailsRepository.findAll();

    }


    public void agentLogin(AgentDetailsDTO agentDetailsDTO) throws TicketingException {

        validateEmptyRequest(agentDetailsDTO);
        validateUsername(agentDetailsDTO.getUserName());
        validatePassword(agentDetailsDTO.getPassword());
        logger.info("Agent login request received --> " + agentDetailsDTO.getUserName());
        AgentDetailsEntity agentDetailsEntity = agentDetailsRepository.findByUserName(agentDetailsDTO.getUserName());

        if (agentDetailsEntity == null) {

            logger.error("username is wrong" );
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "UserName is wrong");

        }


        if (agentDetailsEntity != null && !passwordEncoder.matches(agentDetailsDTO.getPassword(), agentDetailsEntity.getPassword())) {

            logger.error("Password is wrong" );
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Password is wrong");

        }
    }

    private void validateRequest(AgentDetailsDTO agentDetailsDTO) throws TicketingException {


        validateEmptyRequest(agentDetailsDTO);

        if (StringUtils.isEmpty(agentDetailsDTO.getFullName())) {

            logger.error("Validation failed. Full Name is empty.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Full Name is empty.");
        }

        validateUsername(agentDetailsDTO.getUserName());

        validatePassword(agentDetailsDTO.getPassword());


    }


    private void validateEmptyRequest(AgentDetailsDTO agentDetailsDTO) throws TicketingException {

        if (agentDetailsDTO == null) {

            logger.error("Validation failed. Empty Request Received.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Empty Request Received.");
        }

    }


    private void validateUsername(String userName) throws TicketingException {

        if (StringUtils.isEmpty(userName)) {

            logger.error("Validation failed. Username is empty.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Username is empty.");
        }

    }

    private void validatePassword(String password) throws TicketingException {

        if (StringUtils.isEmpty(password)) {

            logger.error("Validation failed. Password is empty.");
            throw new TicketingException(TicketingConstants.BUSINESS_ERROR_CODE, "Password is empty.");
        }

    }
}







