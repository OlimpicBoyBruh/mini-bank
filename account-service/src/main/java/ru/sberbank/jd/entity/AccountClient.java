package ru.sberbank.jd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity объект Бд account_client.
 */
@Entity
@Setter
@Getter
public class AccountClient {
    @Id
    private String numberAccount;
    private String idClient;
    private double balance;
    private LocalDateTime openingDate;
    private LocalDateTime closedDate;
    private String status;
    @ManyToOne(fetch = FetchType.EAGER)
    private AccountType accountType;
}
