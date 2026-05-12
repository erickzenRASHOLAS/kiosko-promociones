package cl.kiosko.ms_promociones.Controller;

import cl.kiosko.ms_promociones.DTO.ProductoPromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionResponseDTO;
import cl.kiosko.ms_promociones.Service.ProductoPromocionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producto_promocion")
public class ProductoPromocionController {

    @Autowired
    private ProductoPromocionService productoPromocionService;

    /**
     * Endpoint para asociar un nuevo producto a una promoción existente
     */
    @PostMapping("/promocion/{promocionId}")
    public ResponseEntity<ProductoPromocionResponseDTO> asignarProducto(
            @PathVariable Long promocionId,
            @Valid @RequestBody ProductoPromocionRequestDTO dto) {
        return new ResponseEntity<>(
                productoPromocionService.agregarProductoAPromocion(promocionId, dto),
                HttpStatus.CREATED
        );
    }

    /**
     * Endpoint para eliminar un producto específico de una promoción
     * @param id El ID de la relación en la tabla intermedia
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRelacion(@PathVariable Long id) {
        productoPromocionService.eliminarProductoDePromocion(id);
        return ResponseEntity.noContent().build();
    }
}