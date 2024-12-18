package com.ayush.dream_shop.data;

import com.ayush.dream_shop.model.Role;
import com.ayush.dream_shop.model.User;
import com.ayush.dream_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationEvent> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Set<String> defaultRoles =Set.of("ROLE_ADMIN","ROLE_USER");
        createDefaultUserIfNotExits(defaultRoles);
    }

    private void createDefaultUserIfNotExits(Set<String> defaultRoles){
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        for(int i=0;i<=5;i++){
            String defaultEmail="user"+i+"@email.com";
            if(userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user= new User();
            user.setFirstName("The user");
            user.setLastName("User"+i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            userRepository.save(user);
            System.out.println("Default vet user"+i+"created successfully");
        }
    }



    private void createDefaultRoleIfNotExists(Set<String> roles){
        roles.stream()
                .filter(role-> roleRepository.findByName(role).isEmpty())
                .map(Role :: new).forEach(roleRepository :: save);
    }
}
