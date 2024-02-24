package ru.sberbank.jd.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import ru.sberbank.api.account.service.Status;
import ru.sberbank.api.account.service.Type;

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
    @Enumerated(EnumType.STRING)
    private Status status;
    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne(fetch = FetchType.EAGER)
    private AccountType accountType;
}
