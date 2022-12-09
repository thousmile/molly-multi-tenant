package com.xaaef.molly.perms.repository;

import com.xaaef.molly.perms.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SysUserRepository extends JpaRepository<SysUser, Long> {
}