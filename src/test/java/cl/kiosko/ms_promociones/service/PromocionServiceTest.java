package cl.kiosko.ms_promociones.service;

import cl.kiosko.ms_promociones.DTO.PromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.PromocionResponseDTO;
import cl.kiosko.ms_promociones.Model.Promocion;
import cl.kiosko.ms_promociones.Repository.PromocionRepository;
import cl.kiosko.ms_promociones.Service.PromocionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Usamos Mockito puro, sin levantar el contexto de Spring
public class PromocionServiceTest {

    @Mock
    private PromocionRepository promocionRepository;

    @InjectMocks
    private PromocionService promocionService;

    private Promocion promocion;

    @BeforeEach
    void setUp() {
        promocion = new Promocion();
        promocion.setPromocionId(1L);
        promocion.setNombre("Promo Navidad");
        promocion.setPorcentajeDescuento(30.0);
        promocion.setFechaInicio(LocalDate.now());
        promocion.setFechaFin(LocalDate.now().plusDays(5));
        promocion.setProductos(null); // Sin productos para simplificar
    }

    @Test
    void listPromociones_DeberiaRetornarLista() {
        when(promocionRepository.findAll()).thenReturn(Arrays.asList(promocion));

        List<PromocionResponseDTO> resultado = promocionService.listPromociones();

        assertFalse(resultado.isEmpty());
        assertEquals("Promo Navidad", resultado.get(0).getNombre());
        verify(promocionRepository, times(1)).findAll();
    }

    @Test
    void findPromocionById_CuandoExiste_DeberiaRetornarDTO() {
        when(promocionRepository.findById(1L)).thenReturn(Optional.of(promocion));

        PromocionResponseDTO resultado = promocionService.findPromocionById(1L);

        assertNotNull(resultado);
        assertEquals(30.0, resultado.getPorcentajeDescuento());
    }

    @Test
    void findPromocionById_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(promocionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> promocionService.findPromocionById(99L));
    }

    @Test
    void deletePromocion_CuandoExiste_DeberiaEliminar() {
        when(promocionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(promocionRepository).deleteById(1L);

        assertDoesNotThrow(() -> promocionService.deletePromocion(1L));
        verify(promocionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePromocion_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(promocionRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> promocionService.deletePromocion(99L));
        verify(promocionRepository, never()).deleteById(anyLong());
    }

    @Test
    void savePromocion_SinConexionInventario_DeberiaLanzarRuntimeException() {
        PromocionRequestDTO requestDTO = new PromocionRequestDTO();
        requestDTO.setNombre("Promo Test");
        requestDTO.setProductosId(Arrays.asList(10L)); // Este ID disparará el WebClient

        // Como no podemos mockear el WebClient fácilmente, probamos que el bloque catch actúe correctamente
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            promocionService.savePromocion(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Error de comunicación"));
    }


}