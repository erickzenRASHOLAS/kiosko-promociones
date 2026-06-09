package cl.kiosko.ms_promociones.Config; // Verifica que sea tu paquete correcto

import cl.kiosko.ms_promociones.Model.ProductoPromocion;
import cl.kiosko.ms_promociones.Model.Promocion;
import cl.kiosko.ms_promociones.Repository.PromocionRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataFakerConfig {

    @Bean
    CommandLineRunner iniciarDatosFalsos(PromocionRepository promocionRepository) {
        return args -> {
            // Verificamos si la tabla promociones está vacía
            if (promocionRepository.count() == 0) {
                System.out.println("⏳ Generando promociones falsas con Datafaker...");
                Faker faker = new Faker();
                List<Promocion> promocionesFalsas = new ArrayList<>();

                // Nombres creativos para las promociones
                List<String> nombresPromos = List.of(
                        "Promo Verano", "Ciber Kiosko", "Descuento Estudiantes",
                        "Fin de Mes", "Viernes Loco", "Promo Desayuno"
                );

                // Vamos a generar 5 promociones
                for (int i = 0; i < 5; i++) {
                    Promocion promocion = new Promocion();

                    // Asignamos un nombre aleatorio de nuestra lista
                    String nombreAleatorio = nombresPromos.get(faker.number().numberBetween(0, nombresPromos.size()));
                    promocion.setNombre(nombreAleatorio + " " + faker.number().numberBetween(2025, 2030));

                    // Descuento aleatorio entre 5.0% y 50.0%
                    promocion.setPorcentajeDescuento(faker.number().randomDouble(1, 5, 50));

                    // Lógica de fechas (LocalDate)
                    // Fecha de inicio: Entre hace 5 días y hoy
                    int diasRestar = faker.number().numberBetween(0, 6);
                    promocion.setFechaInicio(LocalDate.now().minusDays(diasRestar));

                    // Fecha de fin: Entre 5 y 30 días en el futuro
                    int diasSumar = faker.number().numberBetween(5, 31);
                    promocion.setFechaFin(LocalDate.now().plusDays(diasSumar));

                    // Generar los productos asociados a esta promoción
                    List<ProductoPromocion> productosEnPromo = new ArrayList<>();
                    int cantidadProductos = faker.number().numberBetween(1, 6); // De 1 a 5 productos en oferta

                    for (int j = 0; j < cantidadProductos; j++) {
                        ProductoPromocion prodPromo = new ProductoPromocion();

                        // Simulamos que estos IDs de producto vienen de ms_productos/ms_ventas (ej. del 1 al 50)
                        prodPromo.setProductoId((long) faker.number().numberBetween(1, 51));

                        // VINCULACIÓN BIDIRECCIONAL IMPORTANTE
                        prodPromo.setPromocion(promocion);

                        productosEnPromo.add(prodPromo);
                    }

                    // Añadimos la lista de productos a la promoción principal
                    promocion.setProductos(productosEnPromo);

                    promocionesFalsas.add(promocion);
                }

                // Guardamos todas las promociones (gracias al cascade, se guardan los ProductoPromocion también)
                promocionRepository.saveAll(promocionesFalsas);
                System.out.println("✅ ¡Base de datos poblada exitosamente con 5 promociones!");

            } else {
                System.out.println("👍 La base de datos ya tiene información en promociones, omitiendo Datafaker.");
            }
        };
    }
}