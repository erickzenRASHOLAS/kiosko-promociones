package cl.kiosko.ms_promociones.assembler;

import cl.kiosko.ms_promociones.Assembler.ProductoPromocionAssembler;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductoPromocionAssemblerTest {

    private ProductoPromocionAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = new ProductoPromocionAssembler();
    }

    @Test
    void toModel_DeberiaAgregarLinksHateoas() {
        // 1. Preparamos un DTO básico con el ID de la promoción padre
        ProductoPromocionResponseDTO dto = new ProductoPromocionResponseDTO();
        dto.setPromocionId(1L);
        dto.setPromocionId(5L); // El ID que usará el link "promocion_padre"

        // 2. Ejecutamos el assembler
        ProductoPromocionResponseDTO resultado = assembler.toModel(dto);

        // 3. Validamos que contenga los links esperados
        assertNotNull(resultado);
        assertTrue(resultado.hasLinks(), "El DTO debería tener links HATEOAS");

        // Verificamos que exista la relación "promocion_padre"
        assertTrue(resultado.getLink("promocion_padre").isPresent(), "Debería contener el link 'promocion_padre'");
    }
}