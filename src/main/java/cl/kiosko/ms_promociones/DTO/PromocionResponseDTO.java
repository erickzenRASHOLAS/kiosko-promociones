package cl.kiosko.ms_promociones.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PromocionResponseDTO extends RepresentationModel<PromocionResponseDTO> {

    private Long promocionId;
    private String nombre;
    private Double porcentajeDescuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Devolvemos la lista de IDs para que el ¿frontend/cliente?
    // sepa qué productos tienen este descuento
    private List<Long> productosAsociadosIds;
}
