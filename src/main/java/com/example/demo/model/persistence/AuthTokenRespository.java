package com.example.demo.model.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.AuthToken;

@Repository
public interface AuthTokenRespository extends JpaRepository<AuthToken, String> {

}