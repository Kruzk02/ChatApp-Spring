package com.Blog.Repository;

import com.Blog.Model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege,Long> {
    @Query("SELECT u FROM Privilege u WHERE u.name = :name")
    Privilege findByName(@Param("name") String name);
}
