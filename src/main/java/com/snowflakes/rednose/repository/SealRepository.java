package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Seal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SealRepository extends JpaRepository<Seal, Long> {

}
