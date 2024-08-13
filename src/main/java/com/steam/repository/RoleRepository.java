package com.steam.repository;

import com.steam.entity.Role;

public interface RoleRepository {
    Role findByName(String name);

    Role save(Role role);
}
