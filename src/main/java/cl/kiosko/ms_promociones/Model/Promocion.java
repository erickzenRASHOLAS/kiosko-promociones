package cl.kiosko.ms_promociones.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "promocion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promocion_id")
    private Long promocionId;

    @NotBlank(message = "El nombre de la promoción no puede estar vacío")
    @Column(name = "nombre")
    private String nombre;

    @NotNull(message = "El porcentaje de descuento es obligatorio")
    @Min(value = 1, message = "El descuento mínimo es del 1%")
    @Max(value = 100, message = "El descuento máximo es del 100%")
    @Column(name = "porcentaje_descuento")
    private Double porcentajeDescuento;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    // RELACIÓN ACTUALIZADA: Una promoción tiene muchos ProductoPromocion
    @OneToMany(mappedBy = "promocion", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProductoPromocion> productos;
}