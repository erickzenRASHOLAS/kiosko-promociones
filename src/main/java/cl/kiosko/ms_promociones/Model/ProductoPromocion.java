package cl.kiosko.ms_promociones.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto_promocion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoPromocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promocion_id", nullable = false)
    @JsonBackReference // Importante: evita bucles infinitos en el JSON
    private Promocion promocion;

    @NotNull(message = "El ID del producto es obligatorio")
    @Column(name = "producto_id")
    private Long productoId;
}