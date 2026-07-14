package com.mipatita.usuarios.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mipatita.usuarios.exception.CredencialesInvalidasException;
import com.mipatita.usuarios.security.JwtTokenProvider;
import com.mipatita.usuarios.service.UsuarioService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // no evaluamos el filtro JWT en el test de controlador
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void registro_valido_retorna201() throws Exception {
        when(usuarioService.registrarUsuario(org.mockito.ArgumentMatchers.any())).thenReturn(null);
        mockMvc.perform(post("/api/v1/auth/registro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"maxito\",\"password\":\"1234\",\"role\":\"ADMIN\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void registro_invalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/auth/registro").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"\",\"password\":\"1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_valido_devuelveToken() throws Exception {
        when(usuarioService.autenticar("maxito", "1234")).thenReturn("token.jwt");
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"maxito\",\"password\":\"1234\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token.jwt"));
    }

    @Test
    void login_credencialesMalas_retorna401() throws Exception {
        when(usuarioService.autenticar(anyString(), anyString()))
                .thenThrow(new CredencialesInvalidasException("Credenciales incorrectas"));
        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"maxito\",\"password\":\"mala\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void validar_devuelveResultado() throws Exception {
        when(usuarioService.validarToken("abc")).thenReturn(true);
        mockMvc.perform(get("/api/v1/auth/validar").param("token", "abc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valido").value(true));
    }
}
