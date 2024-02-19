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
import org.springframework.test.web.servlet.MockMvc;
import ru.sberbank.jd.UserInfoDto;
import ru.sberbank.jd.user.service.UserService;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class})
@AutoConfigureMockMvc
class UserControllerImplTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private UserControllerImpl controller;

    @MockBean
    private UserService service;

    @BeforeEach
    public void setUp() {
        UserInfoDto dto;
        dto = new UserInfoDto();
        dto.setFirstName("testFirstName");

        Mockito.when(service.createUser(Mockito.any())).thenReturn(dto);
        Mockito.when(service.deleteInfo(Mockito.any())).thenReturn(dto);
        Mockito.when(service.getInfo(Mockito.any())).thenReturn(dto);
        Mockito.when(service.updateInfo(Mockito.any(), Mockito.any())).thenReturn(dto);
    }

    @Test
    public void testCreate() throws Exception {
        mvc.perform(post("/user")
                        .content("{}")
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
                        .queryParam("id", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testGet() throws Exception {
        mvc.perform(get("/user")
                        .queryParam("id", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetNonExisted_expectNoSuchElement() throws Exception {
        Mockito.when(service.getInfo(Mockito.any())).thenThrow(NoSuchElementException.class);

        mvc.perform(get("/user")
                        .queryParam("id", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdate() throws Exception {
        mvc.perform(put("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .queryParam("id", UUID.randomUUID().toString())
                        .content("{}"))
                .andExpect(status().isOk());
    }
}
