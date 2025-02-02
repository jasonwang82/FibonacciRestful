package com.emc.test.repository;

import com.emc.test.entity.UserOperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOperationLogRepository extends JpaRepository<UserOperationLog, Long> {
}
