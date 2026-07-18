package com.netbank.service;

import com.netbank.dto.RegisterRequest;
import com.netbank.entity.User;

public interface UserService {
    User register(RegisterRequest request);
    User findByEmail(String email);
    User findById(Long id);
}
