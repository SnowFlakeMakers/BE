package com.snowflakes.rednose.repository.stamp;

import com.snowflakes.rednose.entity.Stamp;
import java.awt.print.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {

    Slice<Stamp> findAll(Pageable pageable);
}
