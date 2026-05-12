package cl.kiosko.ms_promociones.Repository;

import cl.kiosko.ms_promociones.Model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {

}