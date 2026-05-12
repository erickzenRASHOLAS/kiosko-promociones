
CREATE TABLE promocion (
                           promocion_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           porcentaje_descuento DOUBLE NOT NULL,
                           fecha_inicio DATE NOT NULL,
                           fecha_fin DATE NOT NULL
);


CREATE TABLE producto_promocion (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    promocion_id BIGINT NOT NULL,
                                    producto_id BIGINT NOT NULL,

                                    CONSTRAINT fk_producto_promocion_promocion
                                        FOREIGN KEY (promocion_id)
                                        REFERENCES promocion(promocion_id)
                                        ON DELETE CASCADE
);