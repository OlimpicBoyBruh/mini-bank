package ru.sberbank.jd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.jd.entity.AccountType;

/**
 * Класс для взаимодействия с БД AccountType.
 */
@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, String> {

}
