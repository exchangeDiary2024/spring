package com.exchangediary.group.domain;

import com.exchangediary.group.domain.dtop.GroupName;
import com.exchangediary.group.domain.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByCode(String code);
    Optional<GroupName> findNameById(Long groupId);
}
