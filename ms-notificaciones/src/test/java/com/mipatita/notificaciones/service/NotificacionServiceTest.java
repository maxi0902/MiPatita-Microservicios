package com.mipatita.notificaciones.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mipatita.notificaciones.client.RecordatorioClient;
import com.mipatita.notificaciones.client.VacunaClient;
import com.mipatita.notificaciones.dto.RecordatorioDTO;
import com.mipatita.notificaciones.dto.VacunaDTO;
import com.mipatita.notificaciones.model.Notificacion;
import com.mipatita.notificaciones.repository.NotificacionRepository;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private RecordatorioClient recordatorioClient;

    @Mock
    private VacunaClient vacunaClient;

    @InjectMocks
    private NotificacionService notificacionService;

    private RecordatorioDTO recordatorioPendiente() {
        RecordatorioDTO r = new RecordatorioDTO();
        r.setIdRecordatorio(1);
        r.setIdMascota(1);
        r.setTipoRecordatorio("Vacuna antirrabica");
        r.setFechaProxima("2026-08-01");
        r.setCompletado(false);
        return r;
    }

    private VacunaDTO vacuna() {
        VacunaDTO v = new VacunaDTO();
        v.setIdVacuna(1);
        v.setIdMascota(1);
        v.setNombreVacuna("Antirrabica");
        v.setFechaAplicacion("2026-05-14");
        return v;
    }

    @Test
    void generarParaMascota_combinaRecordatoriosYVacunas() {
        when(recordatorioClient.obtenerPorMascota(1)).thenReturn(List.of(recordatorioPendiente()));
        when(vacunaClient.obtenerPorMascota(1)).thenReturn(List.of(vacuna()));
        when(notificacionRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<Notificacion> generadas = notificacionService.generarParaMascota(1);

        // 1 recordatorio pendiente + 1 vacuna = 2 notificaciones
        assertEquals(2, generadas.size());
        assertTrue(generadas.stream().anyMatch(n -> "RECORDATORIO".equals(n.getTipo())));
        assertTrue(generadas.stream().anyMatch(n -> "VACUNA".equals(n.getTipo())));
    }

    @Test
    void generarParaMascota_ignoraRecordatoriosCompletados() {
        RecordatorioDTO completado = recordatorioPendiente();
        completado.setCompletado(true);
        when(recordatorioClient.obtenerPorMascota(1)).thenReturn(List.of(completado));
        when(vacunaClient.obtenerPorMascota(1)).thenReturn(List.of());
        when(notificacionRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<Notificacion> generadas = notificacionService.generarParaMascota(1);

        assertTrue(generadas.isEmpty());
    }

    @Test
    void generarParaMascota_siFallaUnServicioRemoto_continuaConElOtro() {
        // ms-recordatorios cae, pero ms-vacunas responde
        when(recordatorioClient.obtenerPorMascota(1)).thenThrow(new RuntimeException("servicio caido"));
        when(vacunaClient.obtenerPorMascota(1)).thenReturn(List.of(vacuna()));
        when(notificacionRepository.saveAll(anyList())).thenAnswer(inv -> inv.getArgument(0));

        List<Notificacion> generadas = notificacionService.generarParaMascota(1);

        assertEquals(1, generadas.size());
        assertEquals("VACUNA", generadas.get(0).getTipo());
    }

    @Test
    void deleteNotificacion_existente_devuelveTrue() {
        when(notificacionRepository.existsById(1)).thenReturn(true);
        assertTrue(notificacionService.deleteNotificacion(1));
    }

    @Test
    void deleteNotificacion_inexistente_devuelveFalse() {
        when(notificacionRepository.existsById(99)).thenReturn(false);
        assertFalse(notificacionService.deleteNotificacion(99));
    }
}
