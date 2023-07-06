package com.project.security.model.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
          select t from Token t 
          inner join User u on t.user.id = u.id
          where u.id = :userId and t.revoked = false
          """
    )
    List<Token> findAllValidTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);

}
