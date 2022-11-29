package com.example.springbatchtp.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {
    @Id
    private Long idTransaction;
    private Long montant;
    private Date dateTransaction;
    private Date dateDebit;
}
