package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.StampRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRecordRepository extends JpaRepository<StampRecord, Long> {
}
