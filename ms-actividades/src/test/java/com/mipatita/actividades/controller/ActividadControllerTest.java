package com.mipatita.actividades.controller;

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

import com.mipatita.actividades.model.Actividad;
import com.mipatita.actividades.service.ActividadService;

/**
 * Pruebas del controlador de Actividades (capa web).
 * El servicio se simula: aqui solo se valida el manejo HTTP (codigos y JSON).
 */
@WebMvcTest(ActividadController.class)
class ActividadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ActividadService actividadService;

    private final String json = """
            {"idMascota":1,"tipoActividad":"Paseo","duracionMinutos":45,"fecha":"2026-07-12"}
            """;

    private Actividad ejemplo() {
        return new Actividad(1, 1, "Paseo", 45, 3200, "2026-07-12", "ok");
    }

    @Test
    void listar_conDatos_retorna200() throws Exception {
        when(actividadService.getActividades()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/actividades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoActividad").value("Paseo"));
    }

    @Test
    void listar_sinDatos_retorna204() throws Exception {
        when(actividadService.getActividades()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/actividades")).andExpect(status().isNoContent());
    }

    @Test
    void obtener_existente_retorna200() throws Exception {
        when(actividadService.getActividad(1)).thenReturn(ejemplo());
        mockMvc.perform(get("/api/v1/actividades/1")).andExpect(status().isOk());
    }

    @Test
    void obtener_inexistente_retorna404() throws Exception {
        when(actividadService.getActividad(99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/actividades/99")).andExpect(status().isNotFound());
    }

    @Test
    void listarPorMascota_conDatos_retorna200() throws Exception {
        when(actividadService.getActividadesPorMascota(1)).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/actividades/mascota/1")).andExpect(status().isOk());
    }

    @Test
    void listarPorMascota_sinDatos_retorna204() throws Exception {
        when(actividadService.getActividadesPorMascota(1)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/actividades/mascota/1")).andExpect(status().isNoContent());
    }

    @Test
    void crear_valido_retorna201() throws Exception {
        when(actividadService.saveActividad(any(Actividad.class))).thenReturn(ejemplo());
        mockMvc.perform(post("/api/v1/actividades").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void crear_invalido_retorna400() throws Exception {
        // tipoActividad vacio y duracion 0 -> Bean Validation -> GlobalExceptionHandler 400
        mockMvc.perform(post("/api/v1/actividades").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idMascota\":1,\"tipoActividad\":\"\",\"duracionMinutos\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_existente_retorna200() throws Exception {
        when(actividadService.updateActividad(eq(1), any(Actividad.class))).thenReturn(ejemplo());
        mockMvc.perform(put("/api/v1/actividades/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_inexistente_retorna404() throws Exception {
        when(actividadService.updateActividad(eq(99), any(Actividad.class))).thenReturn(null);
        mockMvc.perform(put("/api/v1/actividades/99").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_existente_retorna204() throws Exception {
        when(actividadService.deleteActividad(1)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/actividades/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminar_inexistente_retorna404() throws Exception {
        when(actividadService.deleteActividad(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/actividades/99")).andExpect(status().isNotFound());
    }
}
