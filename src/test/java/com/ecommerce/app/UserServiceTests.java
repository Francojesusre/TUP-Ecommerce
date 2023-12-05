//package com.ecommerce.app;
//
//import com.ecommerce.app.entities.UserEntity;
//import com.ecommerce.app.entities.enums.UserRole;
//import com.ecommerce.app.service.UserService;
//import org.junit.Assert;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class UserServiceTests {
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private MockMvc mockMvc;
//
//    public void loadData() {
//        UserEntity user = new UserEntity("Name", "prueba@gmail.com", "1234", UserRole.USER);
//        this.userService.create(user);
//    }
//
//    @Test
//    public void listUsers() throws Exception {
//        this.loadData();
//        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/user").accept(MediaType.APPLICATION_JSON))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
//
//        Assert.assertFalse(response.getResponse().getContentAsString().isEmpty());
//    }
////    @Test
////    public void getUserTest(){
////        Assert.assertNotNull(userService.loadUserById(1L));
////    }
////    @Test
////    public void deleteUserTest(){
////        userService.delete(1L);
////        Assert.assertFalse(userService.loadUserById(1L).isPresent());
////    }
//}
