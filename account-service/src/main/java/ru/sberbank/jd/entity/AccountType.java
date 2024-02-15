package ru.sberbank.jd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity объект Бд account_type.
 */
@Entity
@Getter
@Setter
public class AccountType {
    @Id
    private String accountId;
    private String accountName;
    private double interestRate;
    private boolean replenishmentOption;
    private boolean withdrawalOption;
    private String type;
    @JsonIgnore
    @ManyToMany(mappedBy = "accountType")
    private List<AccountClient> accountClients;
}