package com.mipatita.recordatorios.service;

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

import com.mipatita.recordatorios.client.MascotaClient;
import com.mipatita.recordatorios.model.Recordatorio;
import com.mipatita.recordatorios.repository.RecordatorioRepository;

@ExtendWith(MockitoExtension.class)
class RecordatorioServiceTest {

    @Mock
    private RecordatorioRepository recordatorioRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private RecordatorioService recordatorioService;

    private Recordatorio recordatorioValido() {
        return new Recordatorio(1, 1, "Vacuna antirrabica", 30, "2026-08-01", false);
    }

    @Test
    void getRecordatorios_devuelveListaDelRepositorio() {
        when(recordatorioRepository.findAll()).thenReturn(List.of(recordatorioValido()));
        assertEquals(1, recordatorioService.getRecordatorios().size());
    }

    @Test
    void getRecordatorio_existente_devuelveRecordatorio() {
        when(recordatorioRepository.findById(1)).thenReturn(Optional.of(recordatorioValido()));
        assertEquals("Vacuna antirrabica", recordatorioService.getRecordatorio(1).getTipoRecordatorio());
    }

    @Test
    void getRecordatorio_inexistente_devuelveNull() {
        when(recordatorioRepository.findById(99)).thenReturn(Optional.empty());
        assertNull(recordatorioService.getRecordatorio(99));
    }

    @Test
    void getPendientes_devuelveNoCompletados() {
        when(recordatorioRepository.findByCompletado(false)).thenReturn(List.of(recordatorioValido()));
        assertEquals(1, recordatorioService.getPendientes().size());
    }

    @Test
    void saveRecordatorio_conMascotaExistente_guardaYPoneCompletadoFalsePorDefecto() {
        Recordatorio r = new Recordatorio(null, 1, "Bano", null, "2026-08-10", null);
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(recordatorioRepository.save(any(Recordatorio.class))).thenAnswer(inv -> inv.getArgument(0));
        Recordatorio guardado = recordatorioService.saveRecordatorio(r);
        assertFalse(guardado.getCompletado());
    }

    @Test
    void saveRecordatorio_conMascotaInexistente_lanzaExcepcion() {
        Recordatorio r = recordatorioValido();
        when(mascotaClient.verificarMascota(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> recordatorioService.saveRecordatorio(r));
        verify(recordatorioRepository, never()).save(any());
    }

    @Test
    void updateRecordatorio_inexistente_devuelveNull() {
        when(recordatorioRepository.existsById(99)).thenReturn(false);
        assertNull(recordatorioService.updateRecordatorio(99, recordatorioValido()));
    }

    @Test
    void updateRecordatorio_existenteConMascotaValida_actualiza() {
        Recordatorio r = recordatorioValido();
        when(recordatorioRepository.existsById(1)).thenReturn(true);
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(recordatorioRepository.save(any(Recordatorio.class))).thenReturn(r);
        assertEquals(1, recordatorioService.updateRecordatorio(1, r).getIdRecordatorio());
    }

    @Test
    void deleteRecordatorio_existente_devuelveTrue() {
        when(recordatorioRepository.existsById(1)).thenReturn(true);
        assertTrue(recordatorioService.deleteRecordatorio(1));
        verify(recordatorioRepository).deleteById(1);
    }

    @Test
    void deleteRecordatorio_inexistente_devuelveFalse() {
        when(recordatorioRepository.existsById(99)).thenReturn(false);
        assertFalse(recordatorioService.deleteRecordatorio(99));
    }
}
