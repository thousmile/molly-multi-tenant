package com.xaaef.molly.perms.repository;

import com.xaaef.molly.perms.entity.PmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface PmsUserRepository extends JpaRepository<PmsUser, Long> {


    boolean existsByUsername(String username);


    @Modifying
    @Query(
            value = """
                        insert into pms_user_role(user_id,role_id) value ( ?1 , ?2)
                    """,
            nativeQuery = true
    )
    int updateUserRoles(Long userId, Long roleId);


    @Modifying
    @Query(
            value = """
                       delete from pms_user_role where user_id = ?1
                    """,
            nativeQuery = true
    )
    int deleteUserRoles(Long userId);


}