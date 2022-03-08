package com.fortis.MiniProject.repository;

import com.fortis.MiniProject.entity.UnitEntity;
import com.fortis.MiniProject.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;


public interface UnitRepository extends JpaRepository<UnitEntity,Long> {
    Page<UnitEntity> findAllByUserEntity(UserEntity userEntity,Pageable pageable);
    UnitEntity findUserByUnitId(String id);
    UnitEntity findByUnitId (String id);
    Page<UnitEntity> findByUnitName (String unitName, Pageable pageable);
    Page<UnitEntity> findByCategory (String category, Pageable pageable);
    Page<UnitEntity> findAll(Pageable pageable);
    List<UnitEntity> findAll();
    List<UnitEntity> findByUnitName(String name);
}
