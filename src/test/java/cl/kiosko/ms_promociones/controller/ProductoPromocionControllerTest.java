package cl.kiosko.ms_promociones.controller;

import cl.kiosko.ms_promociones.Assembler.ProductoPromocionAssembler;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.ProductoPromocionResponseDTO;
import cl.kiosko.ms_promociones.Service.ProductoPromocionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Apagamos JWT
public class ProductoPromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoPromocionService productoPromocionService;

    @MockitoBean
    private ProductoPromocionAssembler assembler;

    private ObjectMapper objectMapper;
    private ProductoPromocionRequestDTO requestDTO;
    private ProductoPromocionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Configurar Mock del Assembler
        when(assembler.toModel(any(ProductoPromocionResponseDTO.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        requestDTO = new ProductoPromocionRequestDTO();
        requestDTO.setProductoId(10L);

        responseDTO = new ProductoPromocionResponseDTO(1L, 5L, 10L);
    }

    @Test
    void asignarProducto_DeberiaRetornar201() throws Exception {
        when(productoPromocionService.agregarProductoAPromocion(eq(5L), any(ProductoPromocionRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/producto_promocion/promocion/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productoId").value(10L));
    }

    @Test
    void asignarProducto_SinIdProducto_DeberiaRetornar400() throws Exception {
        ProductoPromocionRequestDTO badRequest = new ProductoPromocionRequestDTO();
        // No le seteamos el productoId para forzar el fallo de @NotNull

        mockMvc.perform(post("/producto_promocion/promocion/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarRelacion_DeberiaRetornar204() throws Exception {
        doNothing().when(productoPromocionService).eliminarProductoDePromocion(1L);

        mockMvc.perform(delete("/producto_promocion/1"))
                .andExpect(status().isNoContent());
    }
}