package cl.kiosko.ms_promociones.assembler;

import cl.kiosko.ms_promociones.Assembler.PromocionAssembler;
import cl.kiosko.ms_promociones.DTO.PromocionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PromocionAssemblerTest {

    private PromocionAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new PromocionAssembler();
    }

    @Test
    void toModel_DeberiaAgregarLinksHateoas() {
        // 1. Preparamos un DTO básico con un ID
        PromocionResponseDTO dto = new PromocionResponseDTO();
        dto.setPromocionId(10L);

        // 2. Ejecutamos el assembler
        PromocionResponseDTO resultado = assembler.toModel(dto);

        // 3. Validamos que no sea nulo y que contenga los links esperados
        assertNotNull(resultado);
        assertTrue(resultado.hasLinks(), "El DTO debería tener links HATEOAS");

        // Verificamos que existan las relaciones "self" y "promociones"
        assertTrue(resultado.getLink("self").isPresent(), "Debería contener el link 'self'");
        assertTrue(resultado.getLink("promociones").isPresent(), "Debería contener el link 'promociones'");
    }
}