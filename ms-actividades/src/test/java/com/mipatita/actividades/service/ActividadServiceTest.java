package com.mipatita.actividades.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mipatita.actividades.client.MascotaClient;
import com.mipatita.actividades.model.Actividad;
import com.mipatita.actividades.repository.ActividadRepository;

/**
 * Pruebas unitarias del servicio de Actividades (estructura Given-When-Then).
 * El repositorio y el cliente Feign se simulan con mocks: aqui solo se valida
 * la logica de negocio, no la base de datos ni la red.
 */
@ExtendWith(MockitoExtension.class)
class ActividadServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private ActividadService actividadService;

    private Actividad actividadValida() {
        return new Actividad(1, 1, "Paseo", 45, 3200, "2026-06-20", "Muy activo");
    }

    @Test
    void getActividades_devuelveListaDelRepositorio() {
        // Given
        when(actividadRepository.findAll()).thenReturn(List.of(actividadValida()));
        // When
        List<Actividad> resultado = actividadService.getActividades();
        // Then
        assertEquals(1, resultado.size());
        assertEquals("Paseo", resultado.get(0).getTipoActividad());
    }

    @Test
    void getActividad_existente_devuelveActividad() {
        when(actividadRepository.findById(1)).thenReturn(Optional.of(actividadValida()));
        Actividad resultado = actividadService.getActividad(1);
        assertEquals(1, resultado.getIdActividad());
    }

    @Test
    void getActividad_inexistente_devuelveNull() {
        when(actividadRepository.findById(99)).thenReturn(Optional.empty());
        assertNull(actividadService.getActividad(99));
    }

    @Test
    void saveActividad_conMascotaExistente_guarda() {
        // Given: la mascota existe segun el microservicio de Mascotas
        Actividad actividad = actividadValida();
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(actividadRepository.save(actividad)).thenReturn(actividad);
        // When
        Actividad guardada = actividadService.saveActividad(actividad);
        // Then
        assertEquals("Paseo", guardada.getTipoActividad());
        verify(actividadRepository).save(actividad);
    }

    @Test
    void saveActividad_conMascotaInexistente_lanzaExcepcionYNoGuarda() {
        // Given: la mascota NO existe
        Actividad actividad = actividadValida();
        when(mascotaClient.verificarMascota(1)).thenReturn(false);
        // When / Then
        assertThrows(IllegalArgumentException.class, () -> actividadService.saveActividad(actividad));
        verify(actividadRepository, never()).save(any());
    }

    @Test
    void updateActividad_inexistente_devuelveNull() {
        when(actividadRepository.existsById(99)).thenReturn(false);
        assertNull(actividadService.updateActividad(99, actividadValida()));
        verify(mascotaClient, never()).verificarMascota(any());
    }

    @Test
    void updateActividad_existenteConMascotaValida_actualiza() {
        Actividad actividad = actividadValida();
        when(actividadRepository.existsById(1)).thenReturn(true);
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(actividadRepository.save(any(Actividad.class))).thenReturn(actividad);

        Actividad actualizada = actividadService.updateActividad(1, actividad);

        assertEquals(1, actualizada.getIdActividad());
    }

    @Test
    void deleteActividad_existente_devuelveTrue() {
        when(actividadRepository.existsById(1)).thenReturn(true);
        assertTrue(actividadService.deleteActividad(1));
        verify(actividadRepository).deleteById(1);
    }

    @Test
    void deleteActividad_inexistente_devuelveFalse() {
        when(actividadRepository.existsById(99)).thenReturn(false);
        assertFalse(actividadService.deleteActividad(99));
        verify(actividadRepository, never()).deleteById(any());
    }
}
