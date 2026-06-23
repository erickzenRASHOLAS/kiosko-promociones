package cl.kiosko.ms_promociones.Assembler;

import cl.kiosko.ms_promociones.Controller.ProductoPromocionController;
import cl.kiosko.ms_promociones.Controller.PromocionController;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProductoPromocionAssembler implements RepresentationModelAssembler<ProductoPromocionResponseDTO, ProductoPromocionResponseDTO> {

    @Override
    public ProductoPromocionResponseDTO toModel(ProductoPromocionResponseDTO dto) {
        // Link para ver la promoción principal a la que pertenece este producto
        dto.add(linkTo(methodOn(PromocionController.class).obtenerPromocionID(dto.getPromocionId())).withRel("promocion_padre"));

        return dto;
    }
}