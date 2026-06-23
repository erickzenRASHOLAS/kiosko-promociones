package cl.kiosko.ms_promociones.Controller;

import cl.kiosko.ms_promociones.Assembler.ProductoPromocionAssembler;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionResponseDTO;
import cl.kiosko.ms_promociones.Service.ProductoPromocionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producto_promocion")
@Tag(name="Producto Promocion", description = "Operaciones relacionadas con el objeto/entidad de Producto Promoción, el cual solo sirve para aplicar una promoción a un producto")
public class ProductoPromocionController {

    @Autowired
    private ProductoPromocionService productoPromocionService;

    @Autowired
    private ProductoPromocionAssembler assembler;

    @PostMapping("/promocion/{promocionId}")
    @Operation(summary = "Asignar Promoción a un Producto", description = "Toma una promoción existente (Requerido por el modelo de la relación) y le asigna un producto para que este quede en oferta (Crea un objeto de ProductoPromoción)")
    public ResponseEntity<ProductoPromocionResponseDTO> asignarProducto(
            @PathVariable Long promocionId,
            @Valid @RequestBody ProductoPromocionRequestDTO dto) {

        ProductoPromocionResponseDTO response = productoPromocionService.agregarProductoAPromocion(promocionId, dto);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Promoción a un Producto Por ID", description = "Se introducir el ID del ProductoPromoción a borrar. Toma una promoción existente (Requerido por el modelo de la relación) y le elimina la oferta (Elimina un objeto de ProductoPromoción)")
    public ResponseEntity<Void> eliminarRelacion(@PathVariable Long id) {
        productoPromocionService.eliminarProductoDePromocion(id);
        return ResponseEntity.noContent().build();
    }
}