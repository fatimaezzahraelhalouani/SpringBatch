package com.example.springbatchtp;

import com.example.springbatchtp.entities.Compte;
import com.example.springbatchtp.entities.Transaction;
import com.example.springbatchtp.repositories.CompteRepository;
import com.example.springbatchtp.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.springbatchtp.configuration","com.example.springbatchtp.controller","com.example.springbatchtp.dto","com.example.springbatchtp.entities","com.example.springbatchtp.repositories"})
public class SpringBatchTpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchTpApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CompteRepository compteRepository, TransactionRepository transactionRepository) {
        return (args) -> {
            // save a couple of accounts
            compteRepository.save(new Compte(Long.valueOf(1),Long.valueOf(4000)));
            compteRepository.save(new Compte(Long.valueOf(2),Long.valueOf(4000)));
            compteRepository.save(new Compte(Long.valueOf(3),Long.valueOf(4000)));
            compteRepository.save(new Compte(Long.valueOf(4),Long.valueOf(4000)));
            compteRepository.save(new Compte(Long.valueOf(5),Long.valueOf(4000)));

          /*  //save transactions

            transactionRepository.save(new Transaction(Long.valueOf(1),Long.valueOf(100),new Date(2022,02,1),new Date()));
            transactionRepository.save(new Transaction(Long.valueOf(2),Long.valueOf(200),new Date(2022,01,2),new Date()));
            transactionRepository.save(new Transaction(Long.valueOf(3),Long.valueOf(300),new Date(2022,12,3),new Date()));
            transactionRepository.save(new Transaction(Long.valueOf(4),Long.valueOf(400),new Date(2022,11,4),new Date()));


           */
        };
}
}
