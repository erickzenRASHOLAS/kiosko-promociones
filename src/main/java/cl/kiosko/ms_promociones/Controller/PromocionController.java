package cl.kiosko.ms_promociones.Controller;

import cl.kiosko.ms_promociones.DTO.PromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.PromocionResponseDTO;
import cl.kiosko.ms_promociones.Service.PromocionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promociones")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;

    @PostMapping
    public ResponseEntity<PromocionResponseDTO> crearPromocion(@Valid @RequestBody PromocionRequestDTO dto) {
        // Retorna 201 Created según el estándar definido en tu informe
        return new ResponseEntity<>(promocionService.savePromocion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PromocionResponseDTO>> listarPromociones() {
        return ResponseEntity.ok(promocionService.listPromociones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromocionResponseDTO> obtenerPromocion(@PathVariable Long id) {
        return ResponseEntity.ok(promocionService.findPromocionById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable Long id) {
        promocionService.deletePromocion(id);
        return ResponseEntity.noContent().build();
    }
}