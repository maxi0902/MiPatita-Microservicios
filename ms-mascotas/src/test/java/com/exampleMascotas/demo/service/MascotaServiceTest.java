package com.exampleMascotas.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import com.exampleMascotas.demo.model.Mascota;
import com.exampleMascotas.demo.repository.MascotaRepository;

@ExtendWith(MockitoExtension.class)
class MascotaServiceTest {

    @Mock
    private MascotaRepository repository;

    @InjectMocks
    private MascotaService mascotaService;

    private Mascota ejemplo() {
        return new Mascota(1, "Firulais", "Labrador", 12.5, 3, "Corto", "Alto");
    }

    @Test
    void findAll_devuelveLista() {
        when(repository.findAll()).thenReturn(List.of(ejemplo()));
        assertEquals(1, mascotaService.findAll().size());
    }

    @Test
    void existsById_delega() {
        when(repository.existsById(1)).thenReturn(true);
        assertTrue(mascotaService.existsById(1));
    }

    @Test
    void findById_existente_devuelveMascota() {
        when(repository.findById(1)).thenReturn(Optional.of(ejemplo()));
        assertEquals("Firulais", mascotaService.findById(1).getNombre());
    }

    @Test
    void findById_inexistente_devuelveNull() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertNull(mascotaService.findById(99));
    }

    @Test
    void save_delega() {
        Mascota m = ejemplo();
        when(repository.save(m)).thenReturn(m);
        assertEquals("Firulais", mascotaService.save(m).getNombre());
    }

    @Test
    void deleteById_existente_devuelveTrue() {
        when(repository.existsById(1)).thenReturn(true);
        assertTrue(mascotaService.deleteById(1));
        verify(repository).deleteById(1);
    }

    @Test
    void deleteById_inexistente_devuelveFalse() {
        when(repository.existsById(99)).thenReturn(false);
        assertFalse(mascotaService.deleteById(99));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void update_existente_actualiza() {
        Mascota m = ejemplo();
        when(repository.existsById(1)).thenReturn(true);
        when(repository.save(any(Mascota.class))).thenReturn(m);
        assertEquals(1, mascotaService.update(1, m).getIdMascota());
    }

    @Test
    void update_inexistente_devuelveNull() {
        when(repository.existsById(99)).thenReturn(false);
        assertNull(mascotaService.update(99, ejemplo()));
    }

    @Test
    void findByRaza_delega() {
        when(repository.findByRaza("Labrador")).thenReturn(List.of(ejemplo()));
        assertEquals(1, mascotaService.findByRaza("Labrador").size());
    }

    @Test
    void findByEdad_delega() {
        when(repository.findByEdad(3)).thenReturn(List.of(ejemplo()));
        assertEquals(1, mascotaService.findByEdad(3).size());
    }

    @Test
    void findByNivelEnergia_delega() {
        when(repository.findByNivelEnergia("Alto")).thenReturn(List.of(ejemplo()));
        assertEquals(1, mascotaService.findByNivelEnergia("Alto").size());
    }

    @Test
    void findByEdadBetween_delega() {
        when(repository.findByEdadBetween(1, 5)).thenReturn(List.of(ejemplo()));
        assertEquals(1, mascotaService.findByEdadBetween(1, 5).size());
    }

    @Test
    void countByRaza_delega() {
        when(repository.countByRaza("Labrador")).thenReturn(2L);
        assertEquals(2L, mascotaService.countByRaza("Labrador"));
    }
}
