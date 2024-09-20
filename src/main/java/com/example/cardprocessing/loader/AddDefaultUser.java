package com.example.cardprocessing.loader;

import com.example.cardprocessing.entity.users.Admin;
import com.example.cardprocessing.entity.users.Roles;
import com.example.cardprocessing.entity.users.Users;
import com.example.cardprocessing.repository.AdminRepository;
import com.example.cardprocessing.repository.RoleRepository;
import com.example.cardprocessing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddDefaultUser implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String initMode;

    @Override
    public void run(String... args) throws Exception {
        if (initMode.equalsIgnoreCase("create")){
            try {
                Roles roleAdmin = new Roles(1L, "ROLE_ADMIN");
                Roles roleUser = new Roles(2L, "ROLE_USER");

                Set<Roles> rolesSet = new HashSet<>(Set.of(roleAdmin, roleUser));
                roleRepository.saveAll(rolesSet);

                Users users = new Users();
                users.setFirstName("Hasan");
                users.setLastName("Baxriddinov");
                users.setUsername("user_username");
                users.setPassword(passwordEncoder.encode("userPass"));
                users.setRoles(Set.of(roleUser));
                userRepository.save(users);

                Admin admin = new Admin();
                admin.setFirstName("HasanAdmin");
                admin.setLastName("BaxriddinovAdmin");
                admin.setUsername("admin_username");
                admin.setWho("admin");
                admin.setPassword(passwordEncoder.encode("adminPass"));
                admin.setRoles(rolesSet);
                adminRepository.save(admin);

            }catch (Exception e){
                log.error(e.getMessage());
            }
        }

    }
}
