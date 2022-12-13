package com.xaaef.molly.system.repository;

import com.xaaef.molly.system.entity.CommConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CommConfigRepository extends JpaRepository<CommConfig, Long> {


    @Query("""
            select configValue from CommConfig where configKey = ?1
            """)
    String findOneByConfigKey(String configKey);


}