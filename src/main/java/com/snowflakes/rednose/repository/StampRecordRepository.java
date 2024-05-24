package com.snowflakes.rednose.repository;

import com.snowflakes.rednose.entity.Stamp;
import com.snowflakes.rednose.entity.StampRecord;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRecordRepository extends JpaRepository<StampRecord, Long> {
    List<StampRecord> findAllByStamp(Stamp stamp);

}
