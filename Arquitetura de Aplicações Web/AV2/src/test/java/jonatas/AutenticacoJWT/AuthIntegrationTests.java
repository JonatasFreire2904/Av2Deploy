package jonatas.AutenticacoJWT;

//import jonatas.AutenticacoJWT.Models.User;
import jonatas.AutenticacoJWT.Repository.UserRepository;
import jonatas.AutenticacoJWT.Service.AuthService;
import jonatas.AutenticacoJWT.Service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.findByUsername("admin").ifPresentOrElse(
                user -> { /* Já existe */ },
                () -> {
                    jonatas.AutenticacoJWT.Models.User admin = new jonatas.AutenticacoJWT.Models.User(null, "admin", passwordEncoder.encode("123456"), "ADMIN");
                    userRepository.save(admin);
                }
        );
        userRepository.findByUsername("user").ifPresentOrElse(
                user -> { /* Já existe */ },
                () -> {
                    jonatas.AutenticacoJWT.Models.User regularUser = new jonatas.AutenticacoJWT.Models.User(null, "user", passwordEncoder.encode("password"), "USER");
                    userRepository.save(regularUser);
                }
        );
    }

    @Test
    void testLoginSuccess() throws Exception {
        String token = mockMvc.perform(post("/auth/login")
                        .param("username", "admin")
                        .param("password", "123456")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));
    }


    @Test
    void testLoginFailureInvalidPassword() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .param("username", "admin")
                        .param("password", "senhaErrada")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Senha incorreta.")));
    }

    @Test
    void testProtectedEndpointAccessDeniedWithoutToken() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testProtectedEndpointAccessWithValidToken() throws Exception {
        String token = mockMvc.perform(post("/auth/login")
                        .param("username", "user")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/api/hello")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Olá! Você acessou um endpoint protegido com sucesso!"));
    }

    @Test
    void testProtectedAdminEndpointAccessWithAdminToken() throws Exception {
        String adminToken = mockMvc.perform(post("/auth/login")
                        .param("username", "admin")
                        .param("password", "123456")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/api/admin")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Bem-vindo, Administrador! Este é um recurso restrito."));
    }

    @Test
    void testProtectedAdminEndpointAccessDeniedWithUserToken() throws Exception {
        String userToken = mockMvc.perform(post("/auth/login")
                        .param("username", "user")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/api/admin")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }
}

