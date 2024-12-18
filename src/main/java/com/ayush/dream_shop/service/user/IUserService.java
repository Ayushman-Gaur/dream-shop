package com.ayush.dream_shop.service.user;

import com.ayush.dream_shop.dto.UserDto;
import com.ayush.dream_shop.model.User;
import com.ayush.dream_shop.request.CreateUserRequest;
import com.ayush.dream_shop.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto converUsertoDto(User user);

    User getAuthenticatedUser();
}
