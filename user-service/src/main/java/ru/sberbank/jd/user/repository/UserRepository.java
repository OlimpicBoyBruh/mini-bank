package ru.sberbank.jd.user.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.sberbank.jd.user.model.UserInfo;

/**
 * Репозиторий данных пользователей.
 */
public interface UserRepository extends JpaRepository<UserInfo, UUID> {

    UserInfo findByEmail(String email);

    UserInfo findByPhoneNormalized(String phone);
}
