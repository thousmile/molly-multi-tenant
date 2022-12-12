package com.xaaef.molly.system.repository;

import com.xaaef.molly.system.entity.SysConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SysConfigRepository extends JpaRepository<SysConfig, Long> {


    @Query("""
            select configValue from SysConfig where configKey = ?1
            """)
    String findOneByConfigKey(String configKey);


}