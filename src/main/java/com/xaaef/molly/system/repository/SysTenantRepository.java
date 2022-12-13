package com.xaaef.molly.system.repository;

import com.xaaef.molly.system.entity.SysTenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;


public interface SysTenantRepository extends JpaRepository<SysTenant, String> {


    @Query("select s.tenantId from SysTenant s")
    Page<String> findAllByIncludeTenantId(Pageable pageable);



}