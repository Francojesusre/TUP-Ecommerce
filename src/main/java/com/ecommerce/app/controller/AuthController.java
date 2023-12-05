package com.ecommerce.app.controller;

import com.ecommerce.app.dto.TokenDto;
import com.ecommerce.app.entities.UserEntity;
import com.ecommerce.app.dto.LoginDto;
import com.ecommerce.app.dto.RegisterDto;
import com.ecommerce.app.security.JwtUtilService;
import com.ecommerce.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<UserDetails> register (@RequestBody RegisterDto registerDto)
    {
        if(userService.existsByEmail(registerDto.getEmail()))
        { return new ResponseEntity("Ya existe un usuario registrado con ese correo!", HttpStatus.SEE_OTHER); }
        else
        {
            UserEntity user = new UserEntity();
            user.setEmail(registerDto.getEmail());
            user.setName(registerDto.getName());
            user.setRole(registerDto.getRole());
            user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
            var userCreated = userService.create(user);
            return ResponseEntity.ok(userCreated);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> geyToken(@RequestBody LoginDto loginDto)
    {
        try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails user = userService.loadUserByUsername(authentication.getName());
        String token = jwtUtilService.generateToken(user);
        return ResponseEntity.ok(new TokenDto(token));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
