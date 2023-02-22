package com.ecommerce.site.admin.controller;

import com.ecommerce.site.admin.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;


@TestPropertySource(locations = "/application-test.properties")
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class UserControllerIntegrationTest {

    private static MockHttpServletRequest request ;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Value("${sql.script.insert.user}")
    private String sqlInsertUser;

    @Value("${sql.script.delete.user}")
    private String sqlDeleteUser;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlInsertUser);
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteUser);
    }

    @BeforeAll
    public static void setup() {
        request = new MockHttpServletRequest();
    }

    @InjectMocks
    private UserService userService;

    @Transactional
    @Test
    public void getUserThroughHttpRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();
        if (mav != null) {
            ModelAndViewAssert.assertViewName(mav, "index");
        }
    }

    @Test
    public void deleteStudentHttpRequest () throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/users/delete/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        if (modelAndView != null) {
            ModelAndViewAssert.assertViewName(modelAndView, String.format("redirect:%s", request.getHeader("Referer")));
        }
    }

    @Test
    public void testGetUserDetailHttpRequest () throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/users/detail/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        if (modelAndView != null) {
            ModelAndViewAssert.assertViewName(modelAndView, "user/user_detail_modal");
        }
    }

    @Test
    @DisplayName(value = "Test get user return HTTP Status 404")
    public void testGetUserHttpRequestDoesNotExist () throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/users/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus(),
                "HTTP Status is not set to 404");
    }

    @Test
    @DisplayName(value = "Test update user enabled status return HTTP Status 400")
    public void testUpdateUserEnabledStatusHttpRequest () throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/users/{id}/enabled/{status}", 1, "disabled"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus(),
                "HTTP Status should be 400");
    }

    @Test
    public void createScienceGradeHttpRequest () throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test.jpg",
                "image/jpeg",
                new byte[0]
        );

        MvcResult mvcResult = this.mockMvc
                .perform(MockMvcRequestBuilders.multipart("/users/save")
                        //.file(mockMultipartFile)
                        .param("image", mockMultipartFile.getContentType())
                        .param("email", "test2@test.com")
                        .param("firstName", "test2")
                        .param("lastName", "test2")
                        .param("password", "test2"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();

        if (modelAndView != null) {
            ModelAndViewAssert.assertViewName(modelAndView, "users");
        }
    }

}
