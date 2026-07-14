package com.mipatita.comidas.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mipatita.comidas.model.Comida;
import com.mipatita.comidas.service.ComidaService;

@WebMvcTest(ComidaController.class)
class ComidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComidaService comidaService;

    private final String json = """
            {"idMascota":1,"tipoComida":"Croquetas","cantidadGramos":150,"fecha":"2026-07-12"}
            """;

    private Comida ejemplo() {
        return new Comida(1, 1, "Croquetas", 150, "2026-07-12", "ok");
    }

    @Test
    void listar_conDatos_retorna200() throws Exception {
        when(comidaService.getComidas()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/comidas")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoComida").value("Croquetas"));
    }

    @Test
    void listar_sinDatos_retorna204() throws Exception {
        when(comidaService.getComidas()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/comidas")).andExpect(status().isNoContent());
    }

    @Test
    void obtener_existente_retorna200() throws Exception {
        when(comidaService.getComida(1)).thenReturn(ejemplo());
        mockMvc.perform(get("/api/v1/comidas/1")).andExpect(status().isOk());
    }

    @Test
    void obtener_inexistente_retorna404() throws Exception {
        when(comidaService.getComida(99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/comidas/99")).andExpect(status().isNotFound());
    }

    @Test
    void listarPorMascota_sinDatos_retorna204() throws Exception {
        when(comidaService.getComidasPorMascota(1)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/comidas/mascota/1")).andExpect(status().isNoContent());
    }

    @Test
    void crear_valido_retorna201() throws Exception {
        when(comidaService.saveComida(any(Comida.class))).thenReturn(ejemplo());
        mockMvc.perform(post("/api/v1/comidas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void crear_invalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/comidas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idMascota\":1,\"tipoComida\":\"\",\"cantidadGramos\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_existente_retorna200() throws Exception {
        when(comidaService.updateComida(eq(1), any(Comida.class))).thenReturn(ejemplo());
        mockMvc.perform(put("/api/v1/comidas/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_inexistente_retorna404() throws Exception {
        when(comidaService.updateComida(eq(99), any(Comida.class))).thenReturn(null);
        mockMvc.perform(put("/api/v1/comidas/99").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_existente_retorna204() throws Exception {
        when(comidaService.deleteComida(1)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/comidas/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminar_inexistente_retorna404() throws Exception {
        when(comidaService.deleteComida(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/comidas/99")).andExpect(status().isNotFound());
    }
}
