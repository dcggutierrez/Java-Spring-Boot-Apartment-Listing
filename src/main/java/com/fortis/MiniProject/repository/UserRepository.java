package com.fortis.MiniProject.repository;

import com.fortis.MiniProject.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity,Long> {
    UserEntity findUserByEmail(String email);

    UserEntity findUserByUserId(String id);

    UserEntity findUserById(long id);
}
