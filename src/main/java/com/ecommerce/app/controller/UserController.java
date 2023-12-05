package com.ecommerce.app.controller;

import com.ecommerce.app.dto.ResponseMessageDto;
import com.ecommerce.app.entities.UserEntity;
import com.ecommerce.app.entities.enums.UserRole;
import com.ecommerce.app.security.JwtUtilService;
import com.ecommerce.app.service.UserService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @GetMapping
    public List<UserEntity> getAll(){
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserEntity>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.loadUserById(id));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserEntity> getProfile(HttpServletRequest request) {
        var username = jwtUtilService.extractUsername(jwtUtilService.getToken(request));

        if (!StringUtils.isBlank(username)){
            return ResponseEntity.ok(userService.loadUserByEmail(username));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

   @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessageDto> delete(@PathVariable Long id) {

        if (userService.loadUserById(id) != null) {
            userService.delete(id);
            return ResponseEntity.ok(new ResponseMessageDto("Eliminado"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

   @PutMapping()
   public ResponseEntity<UserEntity> update(@RequestBody UserEntity user) {
       ResponseEntity<UserEntity> response = null;

       Optional<UserEntity> userDetails = userService.loadUserById(user.getId());
       if (userDetails != null) {
           response = ResponseEntity.ok(userService.update(user));
       } else {
           response = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
       }

       return response;
   }

    @PostMapping("/create")
    public ResponseEntity register(HttpServletRequest request, @RequestBody UserEntity userEntity)
    {
        var username = jwtUtilService.extractUsername(jwtUtilService.getToken(request));

        if (!StringUtils.isBlank(username)){
            var userLogged = userService.loadUserByEmail(username);
            if (userLogged.getRole() == UserRole.ROOT){
                if(userService.existsByEmail(userEntity.getEmail()))
                { return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un usuario registrado con ese correo!"); }
                else
                {
                    UserEntity user = new UserEntity();
                    user.setEmail(userEntity.getEmail());
                    user.setName(userEntity.getName());
                    user.setRole(userEntity.getRole());
                    user.setPassword(passwordEncoder.encode(userEntity.getPassword()));
                    userService.create(user);
                    return ResponseEntity.status(HttpStatus.OK).body(user);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tiene permisos para crear un usuario");
    }

    @GetMapping("/pdf")
    public void generatePdf(HttpServletResponse response) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=ListadoUsuarios_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        userService.generate(response);

    }
}
