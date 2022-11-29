package com.example.springbatchtp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDto implements Serializable {
    private Long idTransaction;
    private Long idCompte;
    private Long montant ;
    private Date dateTransaction;
}
