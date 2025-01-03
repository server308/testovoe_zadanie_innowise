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
        // Создаем роли
        Role adminRole = new Role();
        adminRole.setName("ADMINISTRATOR");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName("SIMPLE_USER");
        roleRepository.save(userRole);

        // Создаем администратора
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));

        // Присваиваем роли пользователю
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        admin.setRoles(adminRoles);

        userRepository.save(admin);

        // Создаем обычного пользователя
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));

        // Присваиваем роли пользователю
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);

        userRepository.save(user);
    }
}