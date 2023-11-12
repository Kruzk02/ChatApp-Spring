package com.Blog.Repository;

import com.Blog.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    @Query("SELECT u FROM Role u WHERE u.name = :name")
    Role findByName(@Param("name") String name);
}
