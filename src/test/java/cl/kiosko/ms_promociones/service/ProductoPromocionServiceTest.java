package cl.kiosko.ms_promociones.service;

import cl.kiosko.ms_promociones.DTO.ProductoPromocionRequestDTO;
import cl.kiosko.ms_promociones.Model.ProductoPromocion;
import cl.kiosko.ms_promociones.Model.Promocion;
import cl.kiosko.ms_promociones.Repository.ProductoPromocionRepository;
import cl.kiosko.ms_promociones.Repository.PromocionRepository;
import cl.kiosko.ms_promociones.Service.ProductoPromocionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoPromocionServiceTest {

    @Mock
    private ProductoPromocionRepository productoPromocionRepository;

    @Mock
    private PromocionRepository promocionRepository;

    @InjectMocks
    private ProductoPromocionService productoPromocionService;

    private Promocion promocion;

    @BeforeEach
    void setUp() {
        promocion = new Promocion();
        promocion.setPromocionId(5L);
        promocion.setNombre("Promo Tech");
    }

    @Test
    void eliminarProductoDePromocion_CuandoExiste_DeberiaEliminar() {
        when(productoPromocionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productoPromocionRepository).deleteById(1L);

        assertDoesNotThrow(() -> productoPromocionService.eliminarProductoDePromocion(1L));
        verify(productoPromocionRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarProductoDePromocion_CuandoNoExiste_DeberiaLanzarExcepcion() {
        when(productoPromocionRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> productoPromocionService.eliminarProductoDePromocion(99L));
        verify(productoPromocionRepository, never()).deleteById(anyLong());
    }

    @Test
    void agregarProductoAPromocion_CuandoPromocionNoExiste_DeberiaLanzarExcepcion() {
        ProductoPromocionRequestDTO request = new ProductoPromocionRequestDTO();
        request.setProductoId(10L);

        when(promocionRepository.findById(99L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            productoPromocionService.agregarProductoAPromocion(99L, request);
        });

        assertTrue(exception.getMessage().contains("no existe"));
    }

    @Test
    void agregarProductoAPromocion_SinConexionInventario_DeberiaLanzarRuntimeException() {
        ProductoPromocionRequestDTO request = new ProductoPromocionRequestDTO();
        request.setProductoId(10L);

        when(promocionRepository.findById(5L)).thenReturn(Optional.of(promocion));

        // El test pasa la validación de la promoción, pero choca al hacer la petición WebClient
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoPromocionService.agregarProductoAPromocion(5L, request);
        });

        assertTrue(exception.getMessage().contains("Error de comunicación"));
    }
}