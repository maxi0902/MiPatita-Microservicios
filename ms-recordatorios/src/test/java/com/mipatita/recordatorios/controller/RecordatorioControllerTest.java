package com.mipatita.recordatorios.controller;

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

import com.mipatita.recordatorios.model.Recordatorio;
import com.mipatita.recordatorios.service.RecordatorioService;

@WebMvcTest(RecordatorioController.class)
class RecordatorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecordatorioService recordatorioService;

    private final String json = """
            {"idMascota":1,"tipoRecordatorio":"Control","frecuenciaDias":30,"fechaProxima":"2026-08-01","completado":false}
            """;

    private Recordatorio ejemplo() {
        return new Recordatorio(1, 1, "Control", 30, "2026-08-01", false);
    }

    @Test
    void listar_conDatos_retorna200() throws Exception {
        when(recordatorioService.getRecordatorios()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/recordatorios")).andExpect(status().isOk());
    }

    @Test
    void listar_sinDatos_retorna204() throws Exception {
        when(recordatorioService.getRecordatorios()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/recordatorios")).andExpect(status().isNoContent());
    }

    @Test
    void obtener_existente_retorna200() throws Exception {
        when(recordatorioService.getRecordatorio(1)).thenReturn(ejemplo());
        mockMvc.perform(get("/api/v1/recordatorios/1")).andExpect(status().isOk());
    }

    @Test
    void obtener_inexistente_retorna404() throws Exception {
        when(recordatorioService.getRecordatorio(99)).thenReturn(null);
        mockMvc.perform(get("/api/v1/recordatorios/99")).andExpect(status().isNotFound());
    }

    @Test
    void listarPorMascota_sinDatos_retorna204() throws Exception {
        when(recordatorioService.getRecordatoriosPorMascota(1)).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/recordatorios/mascota/1")).andExpect(status().isNoContent());
    }

    @Test
    void pendientes_conDatos_retorna200() throws Exception {
        when(recordatorioService.getPendientes()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v1/recordatorios/pendientes")).andExpect(status().isOk());
    }

    @Test
    void pendientes_sinDatos_retorna204() throws Exception {
        when(recordatorioService.getPendientes()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/recordatorios/pendientes")).andExpect(status().isNoContent());
    }

    @Test
    void crear_valido_retorna201() throws Exception {
        when(recordatorioService.saveRecordatorio(any(Recordatorio.class))).thenReturn(ejemplo());
        mockMvc.perform(post("/api/v1/recordatorios").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void crear_invalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/v1/recordatorios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idMascota\":1,\"tipoRecordatorio\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void actualizar_existente_retorna200() throws Exception {
        when(recordatorioService.updateRecordatorio(eq(1), any(Recordatorio.class))).thenReturn(ejemplo());
        mockMvc.perform(put("/api/v1/recordatorios/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_inexistente_retorna404() throws Exception {
        when(recordatorioService.updateRecordatorio(eq(99), any(Recordatorio.class))).thenReturn(null);
        mockMvc.perform(put("/api/v1/recordatorios/99").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminar_existente_retorna204() throws Exception {
        when(recordatorioService.deleteRecordatorio(1)).thenReturn(true);
        mockMvc.perform(delete("/api/v1/recordatorios/1")).andExpect(status().isNoContent());
    }

    @Test
    void eliminar_inexistente_retorna404() throws Exception {
        when(recordatorioService.deleteRecordatorio(99)).thenReturn(false);
        mockMvc.perform(delete("/api/v1/recordatorios/99")).andExpect(status().isNotFound());
    }
}
