package com.sample.agent.dao;

import com.sample.agent.entity.AgentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentDetailsRepository extends JpaRepository<AgentDetailsEntity, Long> {

    AgentDetailsEntity findByUserNameAndPassword(String userName, String password);

    AgentDetailsEntity findByUserName(String userName);


}
