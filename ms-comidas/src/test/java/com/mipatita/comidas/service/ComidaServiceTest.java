package com.mipatita.comidas.service;

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

import com.mipatita.comidas.client.MascotaClient;
import com.mipatita.comidas.model.Comida;
import com.mipatita.comidas.repository.ComidaRepository;

@ExtendWith(MockitoExtension.class)
class ComidaServiceTest {

    @Mock
    private ComidaRepository comidaRepository;

    @Mock
    private MascotaClient mascotaClient;

    @InjectMocks
    private ComidaService comidaService;

    private Comida comidaValida() {
        return new Comida(1, 1, "Croquetas", 150, "2026-07-12", "Comio todo");
    }

    @Test
    void getComidas_devuelveListaDelRepositorio() {
        when(comidaRepository.findAll()).thenReturn(List.of(comidaValida()));
        List<Comida> resultado = comidaService.getComidas();
        assertEquals(1, resultado.size());
        assertEquals("Croquetas", resultado.get(0).getTipoComida());
    }

    @Test
    void getComida_existente_devuelveComida() {
        when(comidaRepository.findById(1)).thenReturn(Optional.of(comidaValida()));
        assertEquals(1, comidaService.getComida(1).getIdComida());
    }

    @Test
    void getComida_inexistente_devuelveNull() {
        when(comidaRepository.findById(99)).thenReturn(Optional.empty());
        assertNull(comidaService.getComida(99));
    }

    @Test
    void saveComida_conMascotaExistente_guarda() {
        Comida comida = comidaValida();
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(comidaRepository.save(comida)).thenReturn(comida);
        Comida guardada = comidaService.saveComida(comida);
        assertEquals("Croquetas", guardada.getTipoComida());
        verify(comidaRepository).save(comida);
    }

    @Test
    void saveComida_conMascotaInexistente_lanzaExcepcionYNoGuarda() {
        Comida comida = comidaValida();
        when(mascotaClient.verificarMascota(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> comidaService.saveComida(comida));
        verify(comidaRepository, never()).save(any());
    }

    @Test
    void updateComida_inexistente_devuelveNull() {
        when(comidaRepository.existsById(99)).thenReturn(false);
        assertNull(comidaService.updateComida(99, comidaValida()));
    }

    @Test
    void updateComida_existenteConMascotaValida_actualiza() {
        Comida comida = comidaValida();
        when(comidaRepository.existsById(1)).thenReturn(true);
        when(mascotaClient.verificarMascota(1)).thenReturn(true);
        when(comidaRepository.save(any(Comida.class))).thenReturn(comida);
        assertEquals(1, comidaService.updateComida(1, comida).getIdComida());
    }

    @Test
    void deleteComida_existente_devuelveTrue() {
        when(comidaRepository.existsById(1)).thenReturn(true);
        assertTrue(comidaService.deleteComida(1));
        verify(comidaRepository).deleteById(1);
    }

    @Test
    void deleteComida_inexistente_devuelveFalse() {
        when(comidaRepository.existsById(99)).thenReturn(false);
        assertFalse(comidaService.deleteComida(99));
        verify(comidaRepository, never()).deleteById(any());
    }
}
