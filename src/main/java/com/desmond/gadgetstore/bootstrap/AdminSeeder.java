package com.desmond.gadgetstore.bootstrap;


import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.desmond.gadgetstore.entities.RoleEntity;
import com.desmond.gadgetstore.entities.RoleEnum;
import com.desmond.gadgetstore.entities.UserEntity;
import com.desmond.gadgetstore.repositories.RoleRepository;
import com.desmond.gadgetstore.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public AdminSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createOwnerAdministrator();
    }

    private void createOwnerAdministrator() {
       var userDto = UserEntity.builder()
    		   .firstName("company")
    		   .lastName("owner")
    		   .email("owner@gmail.com")
    		   .password(passwordEncoder.encode("P@ss123"))
    		   .build();

        Optional<RoleEntity> optionalRole = roleRepository.findByName(RoleEnum.OWNER);
        Optional<UserEntity> optionalUser = userRepository.findByEmail(userDto.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        userRepository.save(userDto);
    }
}