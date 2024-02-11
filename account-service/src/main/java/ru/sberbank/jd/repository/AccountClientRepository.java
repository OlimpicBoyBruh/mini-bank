package ru.sberbank.jd.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
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
     * Класс для взаимодействия с БД AccountClient.
     *
     * @param clientId идентификатор клиента
     * @return все счета клиента.
     */
    @Query(value = "select * from account_client where id_client = :clientId",
            nativeQuery = true)
    List<AccountClient> getClientAccount(@Param("clientId") String clientId);
}
