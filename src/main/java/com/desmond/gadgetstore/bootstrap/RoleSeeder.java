package com.desmond.gadgetstore.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.desmond.gadgetstore.entities.RoleEntity;
import com.desmond.gadgetstore.entities.RoleEnum;
import com.desmond.gadgetstore.repositories.RoleRepository;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;


    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.OWNER };
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.USER, "can access the authenticated user details for Default user",
                RoleEnum.ADMIN, "can access authenticated user details and list all users for administrator",
                RoleEnum.OWNER, "has access to all endpoints, including creating an administrator for owner"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<RoleEntity> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {

                var newRole = RoleEntity.builder()
                		.name(roleName)
                		.description(roleDescriptionMap.get(roleName))
                		.build();

                roleRepository.save(newRole);
            });
        });
    }
}
