package cl.kiosko.ms_promociones.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromocionResponseDTO {

    private Long promocionId;
    private String nombre;
    private Double porcentajeDescuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Devolvemos la lista de IDs para que el ¿frontend/cliente?
    // sepa qué productos tienen este descuento
    private List<Long> productosAsociadosIds;
}
