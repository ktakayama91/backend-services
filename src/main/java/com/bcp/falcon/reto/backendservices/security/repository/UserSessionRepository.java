package com.bcp.falcon.reto.backendservices.security.repository;

import com.bcp.falcon.reto.backendservices.security.repository.model.UserSessionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kei Takayama
 * Created on 2/13/20.
 */

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessionModel, Long> {

    UserSessionModel findByUsername(String username);
}
