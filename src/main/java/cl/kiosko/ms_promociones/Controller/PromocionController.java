package cl.kiosko.ms_promociones.Controller;

import cl.kiosko.ms_promociones.Assembler.PromocionAssembler;
import cl.kiosko.ms_promociones.DTO.PromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.PromocionResponseDTO;
import cl.kiosko.ms_promociones.Service.PromocionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/promociones")
@Tag(name="Promociones", description = "Operaciones relacionadas con el manejo de las promociones")
public class PromocionController {

    @Autowired
    private PromocionService promocionService;
    @Autowired
    private PromocionAssembler assembler;

    @PostMapping
    @Operation(summary = "Crear Promoción", description = "Crea una promoción, esta debe tener una fecha durante la cual se aplique y un porcentaje de descuento")
    public ResponseEntity<PromocionResponseDTO> crearPromocion(@Valid @RequestBody PromocionRequestDTO dto) {
        PromocionResponseDTO response = promocionService.savePromocion(dto);
        return new ResponseEntity<>(assembler.toModel(response), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar Promociones", description = "Busca y lista todas las promociones actualmente existentes")
    public ResponseEntity<CollectionModel<PromocionResponseDTO>> listarPromociones() {
        List<PromocionResponseDTO> promociones = promocionService.listPromociones();
        List<PromocionResponseDTO> dtosConLinks = promociones.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<PromocionResponseDTO> collectionModel = CollectionModel.of(dtosConLinks,
                linkTo(methodOn(PromocionController.class).listarPromociones()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Promoción por ID", description = "Introducimos el ID de la promoción que queremos ver y lo muestra (si es que esta existe)")
    public ResponseEntity<PromocionResponseDTO> obtenerPromocionID(@PathVariable Long id) {
        PromocionResponseDTO response = promocionService.findPromocionById(id);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar Promoción", description = "Elimina la promoción deseada según su ID")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable Long id) {
        promocionService.deletePromocion(id);
        return ResponseEntity.noContent().build();
    }
}