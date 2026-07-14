package com.mipatita.vacunas.controller;

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

import com.mipatita.vacunas.model.Vacuna;
import com.mipatita.vacunas.service.VacunaService;

@WebMvcTest(VacunaController.class)
class VacunaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VacunaService vacunaService;

    private final String json = """
            {"idMascota":1,"nombreVacuna":"Antirrabica","fechaAplicacion":"2026-05-14"}
            """;

    private Vacuna ejemplo() {
        return new Vacuna(1, 1, "Antirrabica", "2026-05-14", "Dra. Rojas");
    }

    @Test
    void listar_conDatos_retorna200() throws Exception {
        when(vacunaService.getVacunas()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/vacunas")).andExpect(status().isOk());
    }

    @Test
    void listar_sinDatos_retorna204() throws Exception {
        when(vacunaService.getVacunas()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/vacunas")).andExpect(status().isNoContent());
    }

    @Test
    void obtener_existente_retorna200() throws Exception {
        when(vacunaService.getVacuna(1)).thenReturn(ejemplo());
        mockMvc.perform(get("/api/v1/vacunas/1")).andExpect(status().isOk());
    }

    @Test
    void obtener_inexistente_retorna404() throws Exception {
        when(vacunaService.getVacuna(99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/vacunas/99")).andExpect(status().isNotFound());
    }

    @Test
    void listarPorMascota_sinDatos_retorna204() throws Exception {
        when(vacunaService.getVacunasPorMascota(1)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/vacunas/mascota/1")).andExpect(status().isNoContent());
    }

    @Test
    void crear_valido_retorna201() throws Exception {
        when(vacunaService.saveVacuna(any(Vacuna.class))).thenReturn(ejemplo());
        mockMvc.perform(post("/api/v1/vacunas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void crear_invalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/vacunas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idMascota\":1,\"nombreVacuna\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_existente_retorna200() throws Exception {
        when(vacunaService.updateVacuna(eq(1), any(Vacuna.class))).thenReturn(ejemplo());
        mockMvc.perform(put("/api/v1/vacunas/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_inexistente_retorna404() throws Exception {
        when(vacunaService.updateVacuna(eq(99), any(Vacuna.class))).thenReturn(null);
        mockMvc.perform(put("/api/v1/vacunas/99").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_existente_retorna204() throws Exception {
        when(vacunaService.deleteVacuna(1)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/vacunas/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminar_inexistente_retorna404() throws Exception {
        when(vacunaService.deleteVacuna(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/vacunas/99")).andExpect(status().isNotFound());
    }
}
