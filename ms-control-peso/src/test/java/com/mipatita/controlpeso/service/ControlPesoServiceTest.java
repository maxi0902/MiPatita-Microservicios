package com.mipatita.controlpeso.service;

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

import com.mipatita.controlpeso.client.MascotaClient;
import com.mipatita.controlpeso.model.ControlPeso;
import com.mipatita.controlpeso.repository.ControlPesoRepository;

@ExtendWith(MockitoExtension.class)
class ControlPesoServiceTest {

    @Mock
    private ControlPesoRepository controlPesoRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private ControlPesoService controlPesoService;

    private ControlPeso controlValido() {
        return new ControlPeso(1, 1, 12.5, "2026-07-12", "Peso estable");
    }

    @Test
    void getControles_devuelveListaDelRepositorio() {
        when(controlPesoRepository.findAll()).thenReturn(List.of(controlValido()));
        assertEquals(1, controlPesoService.getControles().size());
    }

    @Test
    void getControl_existente_devuelveControl() {
        when(controlPesoRepository.findById(1)).thenReturn(Optional.of(controlValido()));
        assertEquals(12.5, controlPesoService.getControl(1).getPesoKg());
    }

    @Test
    void getControl_inexistente_devuelveNull() {
        when(controlPesoRepository.findById(99)).thenReturn(Optional.empty());
        assertNull(controlPesoService.getControl(99));
    }

    @Test
    void saveControl_conMascotaExistente_guarda() {
        ControlPeso control = controlValido();
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(controlPesoRepository.save(control)).thenReturn(control);
        assertEquals(12.5, controlPesoService.saveControl(control).getPesoKg());
        verify(controlPesoRepository).save(control);
    }

    @Test
    void saveControl_conMascotaInexistente_lanzaExcepcionYNoGuarda() {
        ControlPeso control = controlValido();
        when(mascotaClient.verificarMascota(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> controlPesoService.saveControl(control));
        verify(controlPesoRepository, never()).save(any());
    }

    @Test
    void updateControl_inexistente_devuelveNull() {
        when(controlPesoRepository.existsById(99)).thenReturn(false);
        assertNull(controlPesoService.updateControl(99, controlValido()));
    }

    @Test
    void updateControl_existenteConMascotaValida_actualiza() {
        ControlPeso control = controlValido();
        when(controlPesoRepository.existsById(1)).thenReturn(true);
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(controlPesoRepository.save(any(ControlPeso.class))).thenReturn(control);
        assertEquals(1, controlPesoService.updateControl(1, control).getIdControl());
    }

    @Test
    void deleteControl_existente_devuelveTrue() {
        when(controlPesoRepository.existsById(1)).thenReturn(true);
        assertTrue(controlPesoService.deleteControl(1));
        verify(controlPesoRepository).deleteById(1);
    }

    @Test
    void deleteControl_inexistente_devuelveFalse() {
        when(controlPesoRepository.existsById(99)).thenReturn(false);
        assertFalse(controlPesoService.deleteControl(99));
        verify(controlPesoRepository, never()).deleteById(any());
    }
}
