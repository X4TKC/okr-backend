package com.upb.okrbackend.repositories;

import com.upb.okrbackend.models.Objective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ObjectiveRepository extends JpaRepository <Objective, Long> {
}
