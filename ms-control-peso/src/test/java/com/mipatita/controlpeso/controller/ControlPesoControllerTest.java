package com.mipatita.controlpeso.controller;

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

import com.mipatita.controlpeso.model.ControlPeso;
import com.mipatita.controlpeso.service.ControlPesoService;

@WebMvcTest(ControlPesoController.class)
class ControlPesoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ControlPesoService controlPesoService;

    private final String json = """
            {"idMascota":1,"pesoKg":12.5,"fecha":"2026-07-12"}
            """;

    private ControlPeso ejemplo() {
        return new ControlPeso(1, 1, 12.5, "2026-07-12", "ok");
    }

    @Test
    void listar_conDatos_retorna200() throws Exception {
        when(controlPesoService.getControles()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/controles-peso")).andExpect(status().isOk());
    }

    @Test
    void listar_sinDatos_retorna204() throws Exception {
        when(controlPesoService.getControles()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/controles-peso")).andExpect(status().isNoContent());
    }

    @Test
    void obtener_existente_retorna200() throws Exception {
        when(controlPesoService.getControl(1)).thenReturn(ejemplo());
        mockMvc.perform(get("/api/v1/controles-peso/1")).andExpect(status().isOk());
    }

    @Test
    void obtener_inexistente_retorna404() throws Exception {
        when(controlPesoService.getControl(99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/controles-peso/99")).andExpect(status().isNotFound());
    }

    @Test
    void listarPorMascota_sinDatos_retorna204() throws Exception {
        when(controlPesoService.getControlesPorMascota(1)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/controles-peso/mascota/1")).andExpect(status().isNoContent());
    }

    @Test
    void crear_valido_retorna201() throws Exception {
        when(controlPesoService.saveControl(any(ControlPeso.class))).thenReturn(ejemplo());
        mockMvc.perform(post("/api/v1/controles-peso").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void crear_invalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/controles-peso").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idMascota\":1,\"pesoKg\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_existente_retorna200() throws Exception {
        when(controlPesoService.updateControl(eq(1), any(ControlPeso.class))).thenReturn(ejemplo());
        mockMvc.perform(put("/api/v1/controles-peso/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_inexistente_retorna404() throws Exception {
        when(controlPesoService.updateControl(eq(99), any(ControlPeso.class))).thenReturn(null);
        mockMvc.perform(put("/api/v1/controles-peso/99").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_existente_retorna204() throws Exception {
        when(controlPesoService.deleteControl(1)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/controles-peso/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminar_inexistente_retorna404() throws Exception {
        when(controlPesoService.deleteControl(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/controles-peso/99")).andExpect(status().isNotFound());
    }
}
