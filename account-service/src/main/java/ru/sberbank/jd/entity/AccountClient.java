package ru.sberbank.jd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.Builder;
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
    private String type;
    @ManyToOne(fetch = FetchType.EAGER)
    private AccountType accountType;
}
