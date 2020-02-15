package com.bcp.falcon.reto.backendservices.security.repository;

import com.bcp.falcon.reto.backendservices.security.repository.model.UserDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kei Takayama
 * Created on 2/13/20.
 */

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsModel, Integer> {

    UserDetailsModel findByUsernameAndPassword(String username, String password);

    UserDetailsModel findByUsername(String username);

}
