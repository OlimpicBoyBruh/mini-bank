package ru.sberbank.jd.user.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import ru.sberbank.api.user.service.dto.UserInfoDto;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.model.UserPassword;
import ru.sberbank.jd.user.repository.UserRepository;
import ru.sberbank.jd.user.service.UserService;
import ru.sberbank.jd.user.service.security.CustomUserDetails;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class})
@AutoConfigureMockMvc
class UserControllerImplTest {

    private static UUID testId;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserControllerImpl controller;
    @MockBean
    private UserService service;
    @MockBean
    private JwtDecoder jwtDecoder;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CustomUserDetails user;

    @BeforeAll
    public static void init() {
        testId = UUID.randomUUID();
    }

    @BeforeEach
    public void setUp() {
        UserInfoDto dto;
        dto = new UserInfoDto();
        dto.setFirstName("testFirstName");

        Mockito.when(service.createUser(Mockito.any())).thenReturn(dto);
        Mockito.when(service.deleteInfo(Mockito.any(), Mockito.any())).thenReturn(dto);
        Mockito.when(service.getInfo(Mockito.any())).thenReturn(dto);
        Mockito.when(service.updateInfo(Mockito.any(), Mockito.any())).thenReturn(dto);

        Mockito.when(user.getUsername()).thenReturn(testId.toString());

        UserPassword password = new UserPassword();
        password.setPassword("test");
        UserInfo user = new UserInfo();
        user.setId(testId);
        user.setEmail("test@test.ru");
        user.setPassword(password);

        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(user);
    }

    @Test
    public void testCreate_expectOk() throws Exception {
        mvc.perform(post("/user")
                        .content("{"
                                + "\"firstName\": \"test\","
                                + "\"lastName\": \"test\","
                                + "\"email\": \"1@1.ru\","
                                + "\"phone\": \"+7-1\","
                                + "\"birthDate\": \"2024-01-01\","
                                + "\"password\": \"test\""
                                + "}")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void testCreateWithWrongParams_expectBadRequest() throws Exception {
        Mockito.when(service.createUser(Mockito.any())).thenThrow(IllegalArgumentException.class);

        mvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("message")));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/user")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt
                                .subject(testId.toString())
                                .issuer("user-service")
                                .claim("scope", "USER")
                                .build()))
                        .queryParam("id", testId.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/user")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt
                                .subject(testId.toString())
                                .issuer("user-service")
                                .claim("scope", "USER")
                                .build()))
                        .queryParam("id", testId.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNonExisted_expectNoSuchElement() throws Exception {
        Mockito.when(service.getInfo(Mockito.any())).thenThrow(NoSuchElementException.class);

        mvc.perform(get("/user")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt
                                .subject(testId.toString())
                                .issuer("user-service")
                                .claim("scope", "USER")
                                .build()))
                        .queryParam("id", testId.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdate() throws Exception {
        mvc.perform(put("/user")
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt -> jwt
                                .subject(testId.toString())
                                .issuer("user-service")
                                .claim("scope", "USER")
                                .build()))
                        .queryParam("id", testId.toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("{"
                                + "\"lastName\": \"test\","
                                + "\"email\": \"1@1.ru\","
                                + "\"lastName\": \"+7-22\""
                                + "}"))
                .andExpect(status().isOk());
    }
}
