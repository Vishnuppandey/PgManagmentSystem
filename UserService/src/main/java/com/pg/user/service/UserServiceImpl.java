package com.pg.user.service;

import com.pg.user.dto.CreateDto;
import com.pg.user.dto.ResponseDto;
import com.pg.user.entity.User;
import com.pg.user.enums.Role;
import com.pg.user.enums.Status;
import com.pg.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository repository;
    public UserServiceImpl(UserRepository repository){
        this.repository=repository;
    }
    @Override
    public ResponseDto createUser(CreateDto dto) {
        User user=convertToEntity(dto);
        User savedUser=repository.save(user);
        ResponseDto responseDto=converToResponse(savedUser);
        return responseDto;
    }

    @Override
    public ResponseDto updateUser(Long id, CreateDto dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhoneNo(dto.getPhoneNo());
        user.setPassword(dto.getPassword());
        user.setRole(Role.TENANT);
        user.setStatus(Status.ACTIVE);

        User savedUser = repository.save(user);

        return converToResponse(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user=repository.findById(id).orElseThrow(()->
            new RuntimeException("user not found with this id")
        );
        repository.deleteById(id);
    }

    @Override
    public ResponseDto getUserById(Long id) {
        User user=repository.findById(id).orElseThrow(()->
                new RuntimeException("user not found with this id")
        );
        return converToResponse(user);
    }
    public User convertToEntity(CreateDto dto){
        User user=new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhoneNo(dto.getPhoneNo());
        user.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
        user.setRole(Role.TENANT);
        user.setStatus(Status.ACTIVE);
        return user;
    }
    public ResponseDto converToResponse(User user){
        ResponseDto responseDto=new ResponseDto();
        responseDto.setEmail(user.getEmail());
        responseDto.setName(user.getName());
        responseDto.setPhoneNo(user.getPhoneNo());
        responseDto.setRole(user.getRole());
        return responseDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }
}
