package cl.kiosko.ms_promociones.DTO;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PromocionRequestDTO {

    @NotBlank(message = "El nombre de la promoción no puede estar vacío")
    private String nombre;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @Min(value = 1, message = "El descuento mínimo es del 1%")
    @Max(value = 100, message = "El descuento máximo es del 100%")
    private Double porcentajeDescuento;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    // Aquí recibimos los IDs de los productos a los que se les aplicará el descuento
    @NotEmpty(message = "Debe asignar al menos un producto a la promoción")
    private List<Long> productoIds;
}
