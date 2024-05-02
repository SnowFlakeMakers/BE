package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    Optional<Stamp> findById(Long stampId);
}
