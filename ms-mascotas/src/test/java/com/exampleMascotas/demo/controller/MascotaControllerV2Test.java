package com.exampleMascotas.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.exampleMascotas.demo.assemblers.MascotaModelAssembler;
import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.service.MascotaService;

/**
 * Prueba del controlador V2 con HATEOAS. Importa el assembler real para que
 * se generen los enlaces (_links) y asi se cubre tambien esa clase.
 */
@WebMvcTest(MascotaControllerV2.class)
@Import(MascotaModelAssembler.class)
class MascotaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MascotaService mascotaService;

    private Mascota ejemplo() {
        return new Mascota(1, "Firulais", "Labrador", 12.5, 3, "Corto", "Alto");
    }

    @Test
    void listarTodas_conLinksHateoas() throws Exception {
        when(mascotaService.findAll()).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v2/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._embedded.mascotaList[0]._links.self.href").exists());
    }

    @Test
    void obtener_existente_conLinkSelf() throws Exception {
        when(mascotaService.findById(1)).thenReturn(ejemplo());
        mockMvc.perform(get("/api/v2/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void obtener_inexistente_retorna404() throws Exception {
        when(mascotaService.findById(99)).thenReturn(null);
        mockMvc.perform(get("/api/v2/mascotas/99")).andExpect(status().isNotFound());
    }

    @Test
    void porRaza_retorna200() throws Exception {
        when(mascotaService.findByRaza("Labrador")).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v2/mascotas/raza/Labrador")).andExpect(status().isOk());
    }

    @Test
    void porEdad_retorna200() throws Exception {
        when(mascotaService.findByEdad(3)).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v2/mascotas/edad/3")).andExpect(status().isOk());
    }

    @Test
    void porNivelEnergia_retorna200() throws Exception {
        when(mascotaService.findByNivelEnergia("Alto")).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v2/mascotas/nivel-energia/Alto")).andExpect(status().isOk());
    }

    @Test
    void porEdadEntre_retorna200() throws Exception {
        when(mascotaService.findByEdadBetween(1, 5)).thenReturn(List.of(ejemplo()));
        mockMvc.perform(get("/api/v2/mascotas/edad/1/5")).andExpect(status().isOk());
    }

    @Test
    void totalPorRaza_conLinks() throws Exception {
        when(mascotaService.countByRaza("Labrador")).thenReturn(2L);
        mockMvc.perform(get("/api/v2/mascotas/raza/Labrador/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalMascotas").value(2))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}
