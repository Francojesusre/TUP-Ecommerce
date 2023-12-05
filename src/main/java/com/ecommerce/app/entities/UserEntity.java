package com.ecommerce.app.entities;
import com.ecommerce.app.entities.enums.UserRole;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class UserEntity {
        @Id
        @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
        private Long id;
        private String name;
        private String email;
        private String password;
        @Enumerated(EnumType.STRING)
        private UserRole role;

        public UserEntity() {
        }

        public UserEntity(String name, String email, String password, UserRole role) {
                this.name = name;
                this.email = email;
                this.password = password;
                this.role = role;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public UserRole getRole() {
                return role;
        }

        public void setRole(UserRole role) {
                this.role = role;
        }
}
