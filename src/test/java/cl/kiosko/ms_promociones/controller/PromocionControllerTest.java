package cl.kiosko.ms_promociones.controller;

import cl.kiosko.ms_promociones.Assembler.PromocionAssembler;
import cl.kiosko.ms_promociones.DTO.PromocionRequestDTO;
import cl.kiosko.ms_promociones.DTO.PromocionResponseDTO;
import cl.kiosko.ms_promociones.Service.PromocionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Apagamos JWT para las pruebas
public class PromocionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PromocionService promocionService;

    @MockitoBean
    private PromocionAssembler assembler;

    private ObjectMapper objectMapper;
    private PromocionRequestDTO requestDTO;
    private PromocionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Necesario para parsear LocalDate

        // Configurar Mock del Assembler para evitar JSON vacío
        when(assembler.toModel(any(PromocionResponseDTO.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Preparar datos de prueba
        requestDTO = new PromocionRequestDTO();
        requestDTO.setNombre("Promo Verano");
        requestDTO.setPorcentajeDescuento(20.0);
        requestDTO.setFechaInicio(LocalDate.now());
        requestDTO.setFechaFin(LocalDate.now().plusDays(10));
        requestDTO.setProductosId(Arrays.asList(1L, 2L));

        responseDTO = new PromocionResponseDTO(
                1L,
                "Promo Verano",
                20.0,
                LocalDate.now(),
                LocalDate.now().plusDays(10),
                Arrays.asList(1L, 2L)
        );
    }

    @Test
    void crearPromocion_DeberiaRetornar201() throws Exception {
        when(promocionService.savePromocion(any(PromocionRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Promo Verano"));
    }

    @Test
    void crearPromocion_ConDatosInvalidos_DeberiaRetornar400() throws Exception {
        PromocionRequestDTO badRequest = new PromocionRequestDTO();
        badRequest.setNombre(""); // Falla @NotBlank
        badRequest.setPorcentajeDescuento(150.0); // Falla @Max(100)

        mockMvc.perform(post("/promociones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarPromociones_DeberiaRetornar200() throws Exception {
        List<PromocionResponseDTO> lista = Collections.singletonList(responseDTO);
        when(promocionService.listPromociones()).thenReturn(lista);

        mockMvc.perform(get("/promociones"))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPromocionID_DeberiaRetornar200() throws Exception {
        when(promocionService.findPromocionById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/promociones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Promo Verano"));
    }

    @Test
    void eliminarPromocion_DeberiaRetornar204() throws Exception {
        doNothing().when(promocionService).deletePromocion(1L);

        mockMvc.perform(delete("/promociones/1"))
                .andExpect(status().isNoContent());
    }
}