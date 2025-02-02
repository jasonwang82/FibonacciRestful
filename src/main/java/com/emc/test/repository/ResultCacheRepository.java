package com.emc.test.repository;

import com.emc.test.entity.ResultCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ResultCacheRepository extends JpaRepository<ResultCache, Long> {
    Optional<ResultCache> findByInputValue(Integer inputValue);
}
