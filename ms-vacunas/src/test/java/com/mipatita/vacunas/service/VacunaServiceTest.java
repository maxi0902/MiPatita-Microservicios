package com.mipatita.vacunas.service;

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

import com.mipatita.vacunas.client.MascotaClient;
import com.mipatita.vacunas.model.Vacuna;
import com.mipatita.vacunas.repository.VacunaRepository;

@ExtendWith(MockitoExtension.class)
class VacunaServiceTest {

    @Mock
    private VacunaRepository vacunaRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private VacunaService vacunaService;

    private Vacuna vacunaValida() {
        return new Vacuna(1, 1, "Antirrabica", "2026-05-14", "Dra. Carolina Rojas");
    }

    @Test
    void getVacunas_devuelveListaDelRepositorio() {
        when(vacunaRepository.findAll()).thenReturn(List.of(vacunaValida()));
        assertEquals(1, vacunaService.getVacunas().size());
    }

    @Test
    void getVacuna_existente_devuelveVacuna() {
        when(vacunaRepository.findById(1)).thenReturn(Optional.of(vacunaValida()));
        assertEquals("Antirrabica", vacunaService.getVacuna(1).getNombreVacuna());
    }

    @Test
    void getVacuna_inexistente_devuelveNull() {
        when(vacunaRepository.findById(99)).thenReturn(Optional.empty());
        assertNull(vacunaService.getVacuna(99));
    }

    @Test
    void saveVacuna_conMascotaExistente_guarda() {
        Vacuna vacuna = vacunaValida();
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(vacunaRepository.save(vacuna)).thenReturn(vacuna);
        assertEquals("Antirrabica", vacunaService.saveVacuna(vacuna).getNombreVacuna());
        verify(vacunaRepository).save(vacuna);
    }

    @Test
    void saveVacuna_conMascotaInexistente_lanzaExcepcionYNoGuarda() {
        Vacuna vacuna = vacunaValida();
        when(mascotaClient.verificarMascota(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> vacunaService.saveVacuna(vacuna));
        verify(vacunaRepository, never()).save(any());
    }

    @Test
    void updateVacuna_inexistente_devuelveNull() {
        when(vacunaRepository.existsById(99)).thenReturn(false);
        assertNull(vacunaService.updateVacuna(99, vacunaValida()));
    }

    @Test
    void updateVacuna_existenteConMascotaValida_actualiza() {
        Vacuna vacuna = vacunaValida();
        when(vacunaRepository.existsById(1)).thenReturn(true);
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(vacunaRepository.save(any(Vacuna.class))).thenReturn(vacuna);
        assertEquals(1, vacunaService.updateVacuna(1, vacuna).getIdVacuna());
    }

    @Test
    void deleteVacuna_existente_devuelveTrue() {
        when(vacunaRepository.existsById(1)).thenReturn(true);
        assertTrue(vacunaService.deleteVacuna(1));
        verify(vacunaRepository).deleteById(1);
    }

    @Test
    void deleteVacuna_inexistente_devuelveFalse() {
        when(vacunaRepository.existsById(99)).thenReturn(false);
        assertFalse(vacunaService.deleteVacuna(99));
        verify(vacunaRepository, never()).deleteById(any());
    }
}
