package ru.selm.manager.repository;

import org.springframework.data.repository.CrudRepository;
import ru.selm.manager.entity.CUser;

import java.util.Optional;

public interface CUserRepository extends CrudRepository<CUser, Integer> {

    Optional<CUser> findByUsername(String username);

}
