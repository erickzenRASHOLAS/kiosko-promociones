package cl.kiosko.ms_promociones.DTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoPromocionRequestDTO {

    // Solo pedimos el productoId.
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    // Nota: El promocionId no va aquí porque generalmente se saca de la URL
    // en el Controller ej: /promociones/{promocionId}/productos
}
