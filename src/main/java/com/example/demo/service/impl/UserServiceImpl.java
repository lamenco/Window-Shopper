package com.example.demo.service.impl;

import com.example.demo.models.dto.UserRegisterDto;
import com.example.demo.models.entity.User;
import com.example.demo.models.enums.UserRoleEnum;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private final UserRoleRepository userRoleRepository;
    private final UserDetailsService userDetailsService;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, ModelMapper modelMapper1, UserRoleRepository userRoleRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper1;
        this.userRoleRepository = userRoleRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void register(UserRegisterDto userRegisterDto) {
        User newUser = modelMapper.map(userRegisterDto, User.class);
        newUser.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        if(userRegisterDto.getUsername().equals("dekrower")){
            newUser.setUserRole(List.of(userRoleRepository.findUserRoleByUserRole(UserRoleEnum.ADMIN).orElseThrow()));
        }else{
            newUser.setUserRole(List.of(userRoleRepository.findUserRoleByUserRole(UserRoleEnum.USER).orElseThrow()));
        }

        this.userRepository.save(newUser);
    }

    @Override
    public void login(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
