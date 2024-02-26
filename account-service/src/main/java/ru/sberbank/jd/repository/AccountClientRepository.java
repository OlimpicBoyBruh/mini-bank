package ru.sberbank.jd.repository;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.jd.entity.AccountClient;

/**
 * Класс для взаимодействия с БД AccountClient.
 */
@Repository
public interface AccountClientRepository extends JpaRepository<AccountClient, String> {

    /**
     * Возвращает все счета клиента.
     *
     * @param clientId идентификатор клиента
     * @return все счета клиента.
     */
    @Query(value = "select * from account_client where id_client = :clientId and type = 'ACCOUNT' and status= 'ACTIVE'",
            nativeQuery = true)
    List<AccountClient> getClientAccounts(@Param("clientId") String clientId);

    @Query(value = "select * from account_client where number_account = :accountNumber",
    nativeQuery = true)
    AccountClient getAccountForId(@Param("accountNumber") String accountNumber);
    /**
     * Возвращает все вклады клиента.
     *
     * @param clientId идентификатор клиента
     * @return все счета клиента.
     */
    @Query(value = "select * from account_client where id_client = :clientId and type = 'DEPOSIT' and status= 'ACTIVE'",
            nativeQuery = true)
    List<AccountClient> getClientDeposits(@Param("clientId") String clientId);
    @Query(value = "select * from account_client where number_account = '400000000000000000000'", nativeQuery = true)
    AccountClient getBankAccount();
    List<AccountClient> findByNumberAccountIn(List<String> accountNumbers);

    @Transactional
    @Modifying
    @Query(value = "update account_client set status ='CLOSED', closed_date= :closedDate " +
            "where number_account = :numberAccount", nativeQuery = true)
    void closeAccount(@Param("closedDate") LocalDateTime closedDate, @Param("numberAccount") String numberAccount);

    @Transactional
    @Modifying
    @Query(value = "update account_client set balance = balance + :change "
            + "where number_account = :numberAccount", nativeQuery = true)
    void changeBalance(@Param("change") double change, @Param("numberAccount") String numberAccount);
}
