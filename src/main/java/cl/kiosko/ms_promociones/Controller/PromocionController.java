package cl.kiosko.ms_promociones.Controller;

import cl.kiosko.ms_promociones.DTO.PromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.PromocionResponseDTO;
import cl.kiosko.ms_promociones.Service.PromocionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones")
@Tag(name="Promociones", description = "Operaciones relacionadas con el manejo de las promociones")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @PostMapping
    @Operation(summary = "Crear Promoción", description = "Crea una promoción, esta debe tener una fecha durante la cual se aplique y un porcentaje de descuento")
    public ResponseEntity<PromocionResponseDTO> crearPromocion(@Valid @RequestBody PromocionRequestDTO dto) {
        // Retorna 201 Created según el estándar definido en tu informe
        return new ResponseEntity<>(promocionService.savePromocion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar Promociones", description = "Busca y lista todas las promociones actualmente existentes")
    public ResponseEntity<List<PromocionResponseDTO>> listarPromociones() {
        return ResponseEntity.ok(promocionService.listPromociones());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Promoción por ID", description = "Introducimos el ID de la promoción que queremos ver y lo muestra (si es que esta existe)")
    public ResponseEntity<PromocionResponseDTO> obtenerPromocionID(@PathVariable Long id) {
        return ResponseEntity.ok(promocionService.findPromocionById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Promoción", description = "Elimina la promoción deseada según su ID")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable Long id) {
        promocionService.deletePromocion(id);
        return ResponseEntity.noContent().build();
    }
}