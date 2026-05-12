package cl.kiosko.ms_promociones.Service;

import cl.kiosko.ms_promociones.DTO.PromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.PromocionResponseDTO;
import cl.kiosko.ms_promociones.Model.ProductoPromocion;
import cl.kiosko.ms_promociones.Model.Promocion;
import cl.kiosko.ms_promociones.Repository.PromocionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PromocionService {

    @Autowired
    private PromocionRepository promocionRepository;

    // Declaración correcta de WebClient a nivel de clase
    private final WebClient webClient = WebClient.create();

    private PromocionResponseDTO mapToDTO(Promocion promocion) {
        List<Long> productosIds = promocion.getProductos() != null ?
                promocion.getProductos().stream()
                        .map(ProductoPromocion::getProductoId)
                        .collect(Collectors.toList()) : new ArrayList<>();

        return new PromocionResponseDTO(
                promocion.getPromocionId(),
                promocion.getNombre(),
                promocion.getPorcentajeDescuento(),
                promocion.getFechaInicio(),
                promocion.getFechaFin(),
                productosIds
        );
    }

    private void validarProductoExiste(Long productoId) {
        log.info("Validando existencia masiva del producto ID: {}", productoId);
        try {
            webClient.get()
                    .uri("http://localhost:8080/api/v1/productos/" + productoId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            throw new NoSuchElementException("El Producto con ID " + productoId + " no existe en el inventario.");
        } catch (Exception e) {
            throw new RuntimeException("Error de comunicación con ms_inventario.");
        }
    }

    @Transactional
    public PromocionResponseDTO savePromocion(PromocionRequestDTO dto) {
        log.info("Creando promoción: {}", dto.getNombre());

        for (Long productoId : dto.getProductoIds()) {
            validarProductoExiste(productoId);
        }

        Promocion promocion = new Promocion();
        promocion.setNombre(dto.getNombre());
        promocion.setPorcentajeDescuento(dto.getPorcentajeDescuento());
        promocion.setFechaInicio(dto.getFechaInicio());
        promocion.setFechaFin(dto.getFechaFin());

        List<ProductoPromocion> listaProductos = new ArrayList<>();
        for (Long productoId : dto.getProductoIds()) {
            ProductoPromocion pp = new ProductoPromocion();
            pp.setProductoId(productoId);
            pp.setPromocion(promocion);
            listaProductos.add(pp);
        }
        promocion.setProductos(listaProductos);

        Promocion guardada = promocionRepository.save(promocion);
        return mapToDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<PromocionResponseDTO> listPromociones() {
        return promocionRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PromocionResponseDTO findPromocionById(Long id) {
        Promocion promocion = promocionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Promoción no encontrada."));
        return mapToDTO(promocion);
    }

    @Transactional
    public void deletePromocion(Long id) {
        log.info("Eliminando promoción completa ID: {}", id);
        if (!promocionRepository.existsById(id)) {
            throw new NoSuchElementException("La promoción no existe.");
        }
        promocionRepository.deleteById(id);
    }
}