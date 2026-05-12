package cl.kiosko.ms_promociones.Repository;

import cl.kiosko.ms_promociones.Model.ProductoPromocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoPromocionRepository extends JpaRepository<ProductoPromocion, Long> {

    // metodo sugerido por la ia, se usa en el Service para
    // limpiar los productos viejos cuando actualicemos una promoción.
    void deleteByPromocion_PromocionId(Long promocionId);
}