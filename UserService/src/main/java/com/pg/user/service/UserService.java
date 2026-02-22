package com.pg.user.service;

import com.pg.user.dto.CreateDto;
import com.pg.user.dto.ResponseDto;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.stereotype.Service;

public interface UserService {

    ResponseDto createUser(CreateDto dto);

    ResponseDto updateUser(Long id, CreateDto dto);

    void deleteUser(Long id);

    ResponseDto getUserById(Long id);
}