package com.upb.okrbackend.repositories;

import com.upb.okrbackend.models.KeyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyResultRepository extends JpaRepository<KeyResult, Long> {
}
