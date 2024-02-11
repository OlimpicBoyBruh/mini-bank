package ru.sberbank.jd.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private String depositId;
    private String accountName;
    private double interestRate;
    private boolean replenishmentOption;
    private boolean withdrawalOption;
    private String type;
    @OneToMany(mappedBy = "accountType")
    private List<AccountClient> depositsClients;
}