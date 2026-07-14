package com.mipatita.notificaciones.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mipatita.notificaciones.model.Notificacion;
import com.mipatita.notificaciones.service.NotificacionService;

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificacionService notificacionService;

    private Notificacion ejemplo() {
        return new Notificacion(1, 1, "VACUNA", "Vacuna registrada: Antirrabica");
    }

    @Test
    void listar_conDatos_retorna200() throws Exception {
        when(notificacionService.getNotificaciones()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/notificaciones")).andExpect(status().isOk());
    }

    @Test
    void listar_sinDatos_retorna204() throws Exception {
        when(notificacionService.getNotificaciones()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/notificaciones")).andExpect(status().isNoContent());
    }

    @Test
    void listarPorMascota_conDatos_retorna200() throws Exception {
        when(notificacionService.getNotificacionesPorMascota(1)).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/notificaciones/mascota/1")).andExpect(status().isOk());
    }

    @Test
    void listarPorMascota_sinDatos_retorna204() throws Exception {
        when(notificacionService.getNotificacionesPorMascota(1)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/notificaciones/mascota/1")).andExpect(status().isNoContent());
    }

    @Test
    void generar_conResultados_retorna201() throws Exception {
        when(notificacionService.generarParaMascota(1)).thenReturn(List.of(ejemplo()));
        mockMvc.perform(post("/api/v1/notificaciones/generar/1")).andExpect(status().isCreated());
    }

    @Test
    void generar_sinResultados_retorna204() throws Exception {
        when(notificacionService.generarParaMascota(1)).thenReturn(List.of());
        mockMvc.perform(post("/api/v1/notificaciones/generar/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminar_existente_retorna204() throws Exception {
        when(notificacionService.deleteNotificacion(1)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/notificaciones/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminar_inexistente_retorna404() throws Exception {
        when(notificacionService.deleteNotificacion(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/notificaciones/99")).andExpect(status().isNotFound());
    }
}
