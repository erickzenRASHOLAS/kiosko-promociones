package cl.kiosko.ms_promociones.Assembler;

import cl.kiosko.ms_promociones.Controller.PromocionController;
import cl.kiosko.ms_promociones.DTO.PromocionResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PromocionAssembler implements RepresentationModelAssembler<PromocionResponseDTO, PromocionResponseDTO> {

    @Override
    public PromocionResponseDTO toModel(PromocionResponseDTO dto) {
        // Link hacia la promoción específica (self)
        dto.add(linkTo(methodOn(PromocionController.class).obtenerPromocionID(dto.getPromocionId())).withSelfRel());

        // Link hacia la lista de todas las promociones
        dto.add(linkTo(methodOn(PromocionController.class).listarPromociones()).withRel("promociones"));

        return dto;
    }
}