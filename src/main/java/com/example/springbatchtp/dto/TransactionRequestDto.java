package com.example.springbatchtp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {
    private Long idTransaction;
    private Long idCompte;
    private Long montant ;
    private String dateTransaction;
}
