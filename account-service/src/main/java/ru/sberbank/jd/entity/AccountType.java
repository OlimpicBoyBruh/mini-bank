package ru.sberbank.jd.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import ru.sberbank.api.account.service.Type;

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
    @Enumerated(EnumType.STRING)
    private Type type;
    @JsonIgnore
    @OneToMany(mappedBy = "accountType")
    private List<AccountClient> accountClients;
}
