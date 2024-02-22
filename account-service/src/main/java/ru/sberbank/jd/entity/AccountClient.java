package ru.sberbank.jd.entity;



import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import ru.sberbank.jd.model.Status;
import ru.sberbank.jd.model.Type;

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
