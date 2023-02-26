package com.roms;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class AuthenticationMicroserviceApplication {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		SpringApplication.run(AuthenticationMicroserviceApplication.class, args);
	}
	
	
	@PostConstruct
    private void initDb() {
        String[] sqlStatements = {
        "insert into login(id,username,password,roles) values ('1','abc','$2a$10$WXgq.mz4ipsvjCfnmjoy1eTd0X3zk6I5FQvNQMVWSHzu0nxXCe226','ROLE_USER')",
        "insert into login(id,username,password,roles) values ('2','cde','$2a$10$WXgq.mz4ipsvjCfnmjoy1eTd0X3zk6I5FQvNQMVWSHzu0nxXCe226','ROLE_USER')"
        
        };
        Arrays.asList(sqlStatements).forEach(sql -> jdbcTemplate.execute(sql) );
	}
	
}
