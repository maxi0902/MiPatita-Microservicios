package com.exampleMascotas.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.service.MascotaService;

@WebMvcTest(MascotaController.class)
class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MascotaService mascotaService;

    private final String json = """
            {"nombre":"Firulais","raza":"Labrador","peso":12.5,"edad":3,"tipoPelaje":"Corto","nivelEnergia":"Alto"}
            """;

    private Mascota ejemplo() {
        return new Mascota(1, "Firulais", "Labrador", 12.5, 3, "Corto", "Alto");
    }

    @Test
    void listar_retorna200() throws Exception {
        when(mascotaService.findAll()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/mascotas")).andExpect(status().isOk());
    }

    @Test
    void verificar_existente_retorna200() throws Exception {
        when(mascotaService.existsById(1)).thenReturn(true);
        mockMvc.perform(get("/api/v1/mascotas/verificar/1")).andExpect(status().isOk());
    }

    @Test
    void verificar_inexistente_retorna404() throws Exception {
        when(mascotaService.existsById(99)).thenReturn(false);
        mockMvc.perform(get("/api/v1/mascotas/verificar/99")).andExpect(status().isNotFound());
    }

    @Test
    void obtener_existente_retorna200() throws Exception {
        when(mascotaService.findById(1)).thenReturn(ejemplo());
        mockMvc.perform(get("/api/v1/mascotas/1")).andExpect(status().isOk());
    }

    @Test
    void obtener_inexistente_retorna404() throws Exception {
        when(mascotaService.findById(99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/mascotas/99")).andExpect(status().isNotFound());
    }

    @Test
    void guardar_valido_retorna201() throws Exception {
        when(mascotaService.save(any(Mascota.class))).thenReturn(ejemplo());
        mockMvc.perform(post("/api/v1/mascotas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void guardar_invalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/mascotas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"edad\":-1}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_existente_retorna200() throws Exception {
        when(mascotaService.update(eq(1), any(Mascota.class))).thenReturn(ejemplo());
        mockMvc.perform(put("/api/v1/mascotas/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_inexistente_retorna404() throws Exception {
        when(mascotaService.update(eq(99), any(Mascota.class))).thenReturn(null);
        mockMvc.perform(put("/api/v1/mascotas/99").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_existente_retorna204() throws Exception {
        when(mascotaService.deleteById(1)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/mascotas/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminar_inexistente_retorna404() throws Exception {
        when(mascotaService.deleteById(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/mascotas/99")).andExpect(status().isNotFound());
    }
}
