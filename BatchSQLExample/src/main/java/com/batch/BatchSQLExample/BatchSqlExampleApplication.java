package com.batch.BatchSQLExample;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchSqlExampleApplication {

	@Value("${total:10}")
	private int total;

	public static void main(String[] args) {
		SpringApplication.run(BatchSqlExampleApplication.class, args);
	}

}
