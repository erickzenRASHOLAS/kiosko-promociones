package cl.kiosko.ms_promociones.Service;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionResponseDTO;
import cl.kiosko.ms_promociones.Model.ProductoPromocion;
import cl.kiosko.ms_promociones.Model.Promocion;
import cl.kiosko.ms_promociones.Repository.ProductoPromocionRepository;
import cl.kiosko.ms_promociones.Repository.PromocionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductoPromocionService {
    @Autowired
    private ProductoPromocionRepository productoPromocionRepository;

    @Autowired
    private PromocionRepository promocionRepository;

    // Instancia de WebClient para consultar el ms_inventario
    private final WebClient webClient = WebClient.create();

    /**
     * Valida si un producto existe en el microservicio de Inventario
     */
    private void validarProductoExiste(Long productoId) {
        log.info("Validando existencia del producto ID: {} para asignarlo a una promoción", productoId);
        try {
            webClient.get()
                    .uri("http://localhost:8080/api/v1/productos/" + productoId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            log.error("El producto ID {} no existe en el inventario", productoId);
            throw new NoSuchElementException("El Producto con ID " + productoId + " no existe.");
        } catch (Exception e) {
            log.error("Error al conectar con ms_inventario: {}", e.getMessage());
            throw new RuntimeException("Error de comunicación con el servicio de inventario.");
        }
    }

    @Transactional
    public ProductoPromocionResponseDTO agregarProductoAPromocion(Long promocionId, ProductoPromocionRequestDTO dto) {
        log.info("Agregando producto ID {} a la promoción ID {}", dto.getProductoId(), promocionId);

        // 1. Validar que la promoción existe
        Promocion promocion = promocionRepository.findById(promocionId)
                .orElseThrow(() -> new NoSuchElementException("La promoción con ID " + promocionId + " no existe."));

        // 2. Validar que el producto existe en el otro microservicio
        validarProductoExiste(dto.getProductoId());

        // 3. Crear y guardar la relación
        ProductoPromocion relacion = new ProductoPromocion();
        relacion.setPromocion(promocion);
        relacion.setProductoId(dto.getProductoId());

        ProductoPromocion guardado = productoPromocionRepository.save(relacion);

        return new ProductoPromocionResponseDTO(guardado.getId(), promocionId, guardado.getProductoId());
    }

    @Transactional
    public void eliminarProductoDePromocion(Long idRelacion) {
        log.info("Eliminando relación producto-promoción ID: {}", idRelacion);
        if (!productoPromocionRepository.existsById(idRelacion)) {
            throw new NoSuchElementException("El registro con ID " + idRelacion + " no existe.");
        }
        productoPromocionRepository.deleteById(idRelacion);
    }
}
