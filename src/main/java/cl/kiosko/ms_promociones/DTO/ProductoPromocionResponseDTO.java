package cl.kiosko.ms_promociones.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPromocionResponseDTO extends RepresentationModel<ProductoPromocionResponseDTO> {
    private Long id; // El ID de esta relación en la tabla intermedia
    private Long promocionId; // Para saber a qué promoción pertenece
    private Long productoId; // El producto que tiene el descuento
}
