package com.example.testvoe_zadanie;

import com.example.testvoe_zadanie.Repositories.RoleRepository; // Обязательно добавьте репозиторий для ролей
import com.example.testvoe_zadanie.Repositories.UserRepository;
import com.example.testvoe_zadanie.models.Role;
import com.example.testvoe_zadanie.models.UserEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // Добавляем репозиторий для ролей
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository; // Инициализация репозитория для ролей
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!roleRepository.existsByName("ADMINISTRATOR")) {
            Role adminRole = new Role();
            adminRole.setName("ADMINISTRATOR");
            roleRepository.save(adminRole);
        }

        if (!roleRepository.existsByName("SIMPLE_USER")) {
            Role userRole = new Role();
            userRole.setName("SIMPLE_USER");
            roleRepository.save(userRole);
        }

        // Создаем администратора
        if (!userRepository.existsByUsername("admin")) {
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName("ADMINISTRATOR")); // Получаем роль из БД
            admin.setRoles(adminRoles);
            userRepository.save(admin);
        }

        // Создаем обычного пользователя
        if (!userRepository.existsByUsername("user")) {
            UserEntity user = new UserEntity();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleRepository.findByName("SIMPLE_USER")); // Получаем роль из БД
            user.setRoles(userRoles);
            userRepository.save(user);
        }
    }
}
