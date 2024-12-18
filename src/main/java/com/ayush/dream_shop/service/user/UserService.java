package com.ayush.dream_shop.service.user;

import com.ayush.dream_shop.dto.UserDto;
import com.ayush.dream_shop.exceptions.AlreadyExistsException;
import com.ayush.dream_shop.exceptions.ResourceNotfoundException;
import com.ayush.dream_shop.model.User;
import com.ayush.dream_shop.repository.UserRepository;
import com.ayush.dream_shop.request.CreateUserRequest;
import com.ayush.dream_shop.request.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotfoundException("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user-> !userRepository.existsByEmail(request.getEmail()))
                .map(req->{
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    return userRepository.save(user);
                }).orElseThrow(()-> new AlreadyExistsException("Opps"+request.getEmail()+"already exists"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        return userRepository.findById(userId).map(exisitingUser->{
            exisitingUser.setFirstName(request.getFirstName());
            exisitingUser.setLastName(request.getLastName());
            return userRepository.save(exisitingUser);
        }).orElseThrow(()-> new ResourceNotfoundException("User not found exception"));

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository ::delete,()-> {
            throw new ResourceNotfoundException("User not found");
        });
    }


    @Override
    public UserDto converUsertoDto(User user){
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email= authentication.getName();
        return userRepository.findByEmail(email);
    }
}
