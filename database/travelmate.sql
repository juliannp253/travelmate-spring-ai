-- ============================================================
-- TravelMate - Base de datos completa
-- Proyecto Base - Trabajo Práctico Integrador
-- Curso Spring AI - TodoCode Academy
-- https://www.todocodeacademy.com
-- Compatible con MySQL 8+
-- ============================================================

DROP DATABASE IF EXISTS travelmate;
CREATE DATABASE travelmate
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE travelmate;

-- ============================================================
-- TABLA: destinos
-- ============================================================

CREATE TABLE destinos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    temperatura_promedio DECIMAL(10,2) NOT NULL,
    apto_familias BOOLEAN NOT NULL,
    apto_parejas BOOLEAN NOT NULL,
    tranquilo BOOLEAN NOT NULL,
    mejor_epoca VARCHAR(80) NOT NULL
);

-- ============================================================
-- TABLA: hoteles
-- ============================================================

CREATE TABLE hoteles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(140) NOT NULL,
    estrellas INT NOT NULL,
    precio_noche DECIMAL(12,2) NOT NULL,
    desayuno_incluido BOOLEAN NOT NULL,
    pileta BOOLEAN NOT NULL,
    spa BOOLEAN NOT NULL,
    apto_ninos BOOLEAN NOT NULL,
    destino_id BIGINT NOT NULL,

    CONSTRAINT chk_hoteles_estrellas
        CHECK (estrellas BETWEEN 1 AND 5),

    CONSTRAINT chk_hoteles_precio
        CHECK (precio_noche >= 0),

    CONSTRAINT fk_hoteles_destino
        FOREIGN KEY (destino_id)
        REFERENCES destinos(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- TABLA: actividades
-- ============================================================

CREATE TABLE actividades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(140) NOT NULL,
    tipo VARCHAR(30) NOT NULL,
    descripcion VARCHAR(800) NOT NULL,
    duracion_horas INT NOT NULL,
    precio DECIMAL(12,2) NOT NULL,
    apta_ninos BOOLEAN NOT NULL,
    destino_id BIGINT NOT NULL,

    CONSTRAINT chk_actividades_duracion
        CHECK (duracion_horas > 0),

    CONSTRAINT chk_actividades_precio
        CHECK (precio >= 0),

    CONSTRAINT fk_actividades_destino
        FOREIGN KEY (destino_id)
        REFERENCES destinos(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- TABLA: paquetes_turisticos
-- ============================================================

CREATE TABLE paquetes_turisticos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    cantidad_dias INT NOT NULL,
    precio_por_persona DECIMAL(12,2) NOT NULL,
    incluye_traslado BOOLEAN NOT NULL,
    incluye_desayuno BOOLEAN NOT NULL,
    incluye_excursiones BOOLEAN NOT NULL,
    tipo_transporte VARCHAR(30) NOT NULL,
    descripcion VARCHAR(1000) NOT NULL,
    destino_id BIGINT NOT NULL,
    hotel_id BIGINT NOT NULL,

    CONSTRAINT chk_paquetes_dias
        CHECK (cantidad_dias > 0),

    CONSTRAINT chk_paquetes_precio
        CHECK (precio_por_persona >= 0),

    CONSTRAINT fk_paquetes_destino
        FOREIGN KEY (destino_id)
        REFERENCES destinos(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_paquetes_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES hoteles(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- TABLA INTERMEDIA: paquete_actividad
-- ============================================================

CREATE TABLE paquete_actividad (
    paquete_id BIGINT NOT NULL,
    actividad_id BIGINT NOT NULL,

    PRIMARY KEY (paquete_id, actividad_id),

    CONSTRAINT fk_paquete_actividad_paquete
        FOREIGN KEY (paquete_id)
        REFERENCES paquetes_turisticos(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_paquete_actividad_actividad
        FOREIGN KEY (actividad_id)
        REFERENCES actividades(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ============================================================
-- ÍNDICES
-- ============================================================

CREATE INDEX idx_destinos_pais
    ON destinos(pais);

CREATE INDEX idx_destinos_tipo
    ON destinos(tipo);

CREATE INDEX idx_destinos_tranquilo
    ON destinos(tranquilo);

CREATE INDEX idx_destinos_apto_familias
    ON destinos(apto_familias);

CREATE INDEX idx_destinos_apto_parejas
    ON destinos(apto_parejas);

CREATE INDEX idx_hoteles_destino
    ON hoteles(destino_id);

CREATE INDEX idx_hoteles_estrellas
    ON hoteles(estrellas);

CREATE INDEX idx_actividades_destino
    ON actividades(destino_id);

CREATE INDEX idx_actividades_tipo
    ON actividades(tipo);

CREATE INDEX idx_paquetes_destino
    ON paquetes_turisticos(destino_id);

CREATE INDEX idx_paquetes_precio
    ON paquetes_turisticos(precio_por_persona);

CREATE INDEX idx_paquetes_dias
    ON paquetes_turisticos(cantidad_dias);

CREATE INDEX idx_paquetes_transporte
    ON paquetes_turisticos(tipo_transporte);

-- ============================================================
-- DATOS: destinos
-- ============================================================

INSERT INTO destinos
(nombre, pais, ciudad, tipo, descripcion, temperatura_promedio, apto_familias, apto_parejas, tranquilo, mejor_epoca)
VALUES
('Punta Cana', 'República Dominicana', 'Punta Cana', 'PLAYA',
 'Playas de arena blanca, resorts all inclusive y zonas tranquilas ideales para descansar.',
 28.00, TRUE, TRUE, TRUE, 'Diciembre a abril'),

('Bariloche', 'Argentina', 'San Carlos de Bariloche', 'NIEVE',
 'Destino de montaña con nieve, lagos, esquí, chocolate y actividades familiares.',
 8.00, TRUE, TRUE, TRUE, 'Junio a septiembre'),

('Mendoza', 'Argentina', 'Mendoza', 'GASTRONOMICO',
 'Viñedos, bodegas, gastronomía de autor y paisajes de montaña.',
 20.00, TRUE, TRUE, TRUE, 'Marzo a mayo'),

('Río de Janeiro', 'Brasil', 'Río de Janeiro', 'PLAYA',
 'Playas famosas, vida urbana, música, cultura y actividades al aire libre.',
 26.00, TRUE, TRUE, FALSE, 'Abril a octubre'),

('El Calafate', 'Argentina', 'El Calafate', 'NATURALEZA',
 'Glaciares, paisajes patagónicos, navegación y caminatas en contacto con la naturaleza.',
 7.00, TRUE, TRUE, TRUE, 'Octubre a marzo'),

('Cusco', 'Perú', 'Cusco', 'CULTURAL',
 'Historia inca, arquitectura colonial, gastronomía y acceso a Machu Picchu.',
 13.00, TRUE, TRUE, FALSE, 'Mayo a septiembre'),

('Maldivas', 'Maldivas', 'Malé', 'ROMANTICO',
 'Islas paradisíacas, aguas cristalinas, bungalows sobre el mar y privacidad.',
 29.00, FALSE, TRUE, TRUE, 'Noviembre a abril'),

('Costa Rica Aventura', 'Costa Rica', 'La Fortuna', 'AVENTURA',
 'Selva, volcanes, rafting, tirolesa, termas y contacto con la naturaleza.',
 24.00, TRUE, TRUE, FALSE, 'Diciembre a abril'),

('Cancún', 'México', 'Cancún', 'PLAYA',
 'Caribe mexicano con playas, hoteles, excursiones y entretenimiento.',
 27.00, TRUE, TRUE, FALSE, 'Diciembre a abril'),

('Ushuaia', 'Argentina', 'Ushuaia', 'AVENTURA',
 'Paisajes australes, navegación, senderismo y experiencias en el fin del mundo.',
 6.00, TRUE, TRUE, TRUE, 'Noviembre a marzo'),

('Santorini', 'Grecia', 'Oia', 'ROMANTICO',
 'Atardeceres, casas blancas, mar Egeo y ambiente ideal para parejas.',
 23.00, FALSE, TRUE, TRUE, 'Mayo a septiembre'),

('Disney Orlando', 'Estados Unidos', 'Orlando', 'FAMILIAR',
 'Parques temáticos, entretenimiento y propuestas diseñadas para familias con niños.',
 25.00, TRUE, FALSE, FALSE, 'Enero a mayo'),

('Cartagena', 'Colombia', 'Cartagena de Indias', 'CULTURAL',
 'Ciudad amurallada, playas, arquitectura colonial y gastronomía caribeña.',
 29.00, TRUE, TRUE, FALSE, 'Diciembre a marzo'),

('Villa La Angostura', 'Argentina', 'Villa La Angostura', 'MONTANA',
 'Bosques, lagos, senderos y una atmósfera tranquila de montaña.',
 11.00, TRUE, TRUE, TRUE, 'Octubre a abril'),

('Madrid', 'España', 'Madrid', 'CIUDAD',
 'Museos, gastronomía, compras, vida nocturna y patrimonio cultural.',
 19.00, TRUE, TRUE, FALSE, 'Marzo a junio'),

('Tulum', 'México', 'Tulum', 'PLAYA',
 'Playas, ruinas mayas, cenotes y opciones de bienestar en un entorno relajado.',
 27.00, TRUE, TRUE, TRUE, 'Noviembre a abril'),

('San Martín de los Andes', 'Argentina', 'San Martín de los Andes', 'MONTANA',
 'Lagos, montaña, senderismo, pesca y paisajes patagónicos.',
 10.00, TRUE, TRUE, TRUE, 'Octubre a abril'),

('Roma', 'Italia', 'Roma', 'CULTURAL',
 'Historia, arte, arquitectura, gastronomía y monumentos emblemáticos.',
 20.00, TRUE, TRUE, FALSE, 'Abril a junio'),

('Jericoacoara', 'Brasil', 'Jericoacoara', 'PLAYA',
 'Dunas, lagunas, playas tranquilas y ambiente relajado.',
 28.00, TRUE, TRUE, TRUE, 'Julio a diciembre'),

('Queenstown', 'Nueva Zelanda', 'Queenstown', 'AVENTURA',
 'Montañas, lagos, senderismo, deportes extremos y paisajes naturales.',
 12.00, TRUE, TRUE, FALSE, 'Diciembre a febrero');

-- ============================================================
-- DATOS: hoteles
-- ============================================================

INSERT INTO hoteles
(nombre, estrellas, precio_noche, desayuno_incluido, pileta, spa, apto_ninos, destino_id)
VALUES
('Caribe Serena Resort', 5, 240.00, TRUE, TRUE, TRUE, TRUE, 1),
('Lago Andino Hotel', 4, 155.00, TRUE, TRUE, TRUE, TRUE, 2),
('Casa de Viñas', 4, 130.00, TRUE, TRUE, TRUE, TRUE, 3),
('Copacabana Urban Hotel', 4, 180.00, TRUE, TRUE, FALSE, TRUE, 4),
('Patagonia Glaciar Lodge', 4, 170.00, TRUE, FALSE, TRUE, TRUE, 5),
('Imperio Inca Hotel', 4, 145.00, TRUE, FALSE, TRUE, TRUE, 6),
('Ocean Villas Maldives', 5, 520.00, TRUE, TRUE, TRUE, FALSE, 7),
('Volcano Adventure Lodge', 4, 160.00, TRUE, TRUE, TRUE, TRUE, 8),
('Cancún Family Resort', 5, 250.00, TRUE, TRUE, TRUE, TRUE, 9),
('Austral Fin del Mundo', 4, 165.00, TRUE, FALSE, TRUE, TRUE, 10),
('Aegean Sunset Suites', 5, 340.00, TRUE, TRUE, TRUE, FALSE, 11),
('Orlando Family Inn', 4, 210.00, TRUE, TRUE, FALSE, TRUE, 12),
('Casa Colonial Cartagena', 4, 175.00, TRUE, TRUE, FALSE, TRUE, 13),
('Bosque Patagónico Spa', 4, 150.00, TRUE, TRUE, TRUE, TRUE, 14),
('Madrid Central Hotel', 4, 190.00, TRUE, FALSE, FALSE, TRUE, 15),
('Tulum Eco Boutique', 4, 220.00, TRUE, TRUE, TRUE, TRUE, 16),
('Lácar Mountain Hotel', 4, 145.00, TRUE, TRUE, TRUE, TRUE, 17),
('Roma Centro Storico', 4, 205.00, TRUE, FALSE, FALSE, TRUE, 18),
('Dunas de Jeri Posada', 4, 135.00, TRUE, TRUE, FALSE, TRUE, 19),
('Queenstown Alpine Lodge', 4, 230.00, TRUE, TRUE, TRUE, TRUE, 20);

-- ============================================================
-- DATOS: actividades
-- ============================================================

INSERT INTO actividades
(nombre, tipo, descripcion, duracion_horas, precio, apta_ninos, destino_id)
VALUES
('Paseo en catamarán', 'ACUATICA', 'Navegación tranquila por aguas caribeñas.', 4, 95.00, TRUE, 1),
('Día de playa privada', 'RELAX', 'Jornada de descanso con sombrillas y servicio de playa.', 6, 70.00, TRUE, 1),

('Clase de esquí', 'NIEVE', 'Clase introductoria con instructor y equipamiento.', 3, 120.00, TRUE, 2),
('Circuito Chico', 'NATURALEZA', 'Recorrido panorámico por lagos y montañas.', 5, 65.00, TRUE, 2),

('Tour de bodegas', 'GASTRONOMICA', 'Visita y degustación en bodegas mendocinas.', 6, 110.00, FALSE, 3),
('Cabalgata entre viñedos', 'AVENTURA', 'Cabalgata guiada por fincas y viñedos.', 3, 80.00, TRUE, 3),

('Cristo Redentor y Pan de Azúcar', 'CULTURAL', 'Visita a dos íconos de Río de Janeiro.', 7, 90.00, TRUE, 4),
('Paseo por Copacabana', 'RELAX', 'Recorrido libre por la costanera y playa.', 3, 20.00, TRUE, 4),

('Glaciar Perito Moreno', 'NATURALEZA', 'Excursión de día completo al glaciar.', 8, 140.00, TRUE, 5),
('Navegación por glaciares', 'NATURALEZA', 'Navegación entre témpanos y glaciares.', 6, 160.00, TRUE, 5),

('City tour Cusco', 'CULTURAL', 'Recorrido por templos y sitios históricos.', 4, 55.00, TRUE, 6),
('Machu Picchu', 'CULTURAL', 'Excursión completa a la ciudadela inca.', 12, 220.00, TRUE, 6),

('Cena sobre el agua', 'ROMANTICA', 'Cena privada en un entorno exclusivo.', 3, 180.00, FALSE, 7),
('Snorkel en arrecife', 'ACUATICA', 'Actividad guiada en arrecifes coralinos.', 3, 100.00, FALSE, 7),

('Tirolesa en la selva', 'AVENTURA', 'Circuito de tirolesas sobre el bosque tropical.', 4, 85.00, TRUE, 8),
('Termas volcánicas', 'RELAX', 'Acceso a piscinas termales naturales.', 4, 75.00, TRUE, 8),

('Isla Mujeres', 'ACUATICA', 'Excursión en barco y tiempo de playa.', 7, 105.00, TRUE, 9),
('Parque Xcaret', 'FAMILIAR', 'Parque natural y cultural para toda la familia.', 10, 165.00, TRUE, 9),

('Navegación Canal Beagle', 'NATURALEZA', 'Navegación para observar fauna y paisajes australes.', 4, 100.00, TRUE, 10),
('Trekking Laguna Esmeralda', 'AVENTURA', 'Senderismo de dificultad media.', 6, 85.00, FALSE, 10),

('Atardecer en Oia', 'ROMANTICA', 'Recorrido privado para disfrutar el atardecer.', 3, 90.00, FALSE, 11),
('Paseo en velero', 'ACUATICA', 'Navegación por las islas del mar Egeo.', 5, 150.00, FALSE, 11),

('Entrada parques Disney', 'FAMILIAR', 'Acceso por un día a un parque temático.', 12, 180.00, TRUE, 12),
('Encuentro con personajes', 'FAMILIAR', 'Experiencia fotográfica para niños.', 2, 60.00, TRUE, 12),

('City tour amurallado', 'CULTURAL', 'Recorrido guiado por el centro histórico.', 4, 50.00, TRUE, 13),
('Islas del Rosario', 'ACUATICA', 'Excursión a playas e islas cercanas.', 8, 120.00, TRUE, 13),

('Bosque de Arrayanes', 'NATURALEZA', 'Navegación y caminata por el bosque.', 6, 95.00, TRUE, 14),
('Sendero al lago', 'NATURALEZA', 'Caminata suave por senderos boscosos.', 3, 35.00, TRUE, 14),

('Museo del Prado', 'CULTURAL', 'Visita guiada a una de las principales pinacotecas.', 3, 45.00, TRUE, 15),
('Tour gastronómico', 'GASTRONOMICA', 'Degustación de tapas y platos típicos.', 4, 80.00, TRUE, 15),

('Ruta de cenotes', 'NATURALEZA', 'Recorrido y baño en cenotes.', 6, 100.00, TRUE, 16),
('Yoga frente al mar', 'RELAX', 'Clase al amanecer en la playa.', 2, 35.00, TRUE, 16),

('Ruta de los Siete Lagos', 'NATURALEZA', 'Excursión panorámica en vehículo.', 8, 90.00, TRUE, 17),
('Trekking Mirador Bandurrias', 'AVENTURA', 'Caminata con vistas al lago Lácar.', 4, 45.00, TRUE, 17),

('Coliseo y Foro Romano', 'CULTURAL', 'Visita guiada por sitios históricos.', 4, 75.00, TRUE, 18),
('Tour de pasta y gelato', 'GASTRONOMICA', 'Experiencia gastronómica italiana.', 4, 95.00, TRUE, 18),

('Paseo por dunas', 'AVENTURA', 'Recorrido en buggy por dunas y lagunas.', 5, 80.00, TRUE, 19),
('Atardecer en Pedra Furada', 'RELAX', 'Caminata tranquila para ver el atardecer.', 3, 25.00, TRUE, 19),

('Bungee jumping', 'AVENTURA', 'Salto extremo con equipo profesional.', 2, 160.00, FALSE, 20),
('Crucero por Milford Sound', 'NATURALEZA', 'Navegación entre fiordos y montañas.', 8, 190.00, TRUE, 20);

-- ============================================================
-- DATOS: paquetes turísticos
-- ============================================================

INSERT INTO paquetes_turisticos
(nombre, cantidad_dias, precio_por_persona, incluye_traslado, incluye_desayuno, incluye_excursiones, tipo_transporte, descripcion, destino_id, hotel_id)
VALUES
('Punta Cana Relax', 7, 1850.00, TRUE, TRUE, TRUE, 'AVION',
 'Paquete de playa tranquila con resort, descanso y navegación.', 1, 1),

('Bariloche Nieve Familiar', 6, 1450.00, TRUE, TRUE, TRUE, 'AVION',
 'Propuesta familiar con nieve, esquí y paisajes de montaña.', 2, 2),

('Mendoza Gourmet', 5, 980.00, TRUE, TRUE, TRUE, 'AVION',
 'Experiencia de vino, gastronomía y actividades entre viñedos.', 3, 3),

('Río Completo', 6, 1320.00, TRUE, TRUE, TRUE, 'AVION',
 'Playas, ciudad y visitas a los principales atractivos.', 4, 4),

('Glaciares Patagónicos', 5, 1550.00, TRUE, TRUE, TRUE, 'AVION',
 'Naturaleza, glaciares y navegación en la Patagonia.', 5, 5),

('Cusco y Machu Picchu', 7, 1750.00, TRUE, TRUE, TRUE, 'AVION',
 'Historia, cultura y una visita completa a Machu Picchu.', 6, 6),

('Maldivas Luna de Miel', 8, 4200.00, TRUE, TRUE, TRUE, 'AVION',
 'Paquete romántico de lujo con privacidad y experiencias sobre el mar.', 7, 7),

('Costa Rica Aventura', 7, 2100.00, TRUE, TRUE, TRUE, 'AVION',
 'Aventura, selva, termas y actividades al aire libre.', 8, 8),

('Cancún en Familia', 7, 2300.00, TRUE, TRUE, TRUE, 'AVION',
 'Resort familiar, playa, parque y excursiones.', 9, 9),

('Ushuaia Extrema', 6, 1680.00, TRUE, TRUE, TRUE, 'AVION',
 'Naturaleza austral, trekking y navegación.', 10, 10),

('Santorini Romántica', 6, 2900.00, TRUE, TRUE, TRUE, 'AVION',
 'Atardeceres, mar Egeo y experiencias pensadas para parejas.', 11, 11),

('Orlando Mágico', 8, 3100.00, TRUE, TRUE, TRUE, 'AVION',
 'Paquete familiar con parques temáticos y actividades para niños.', 12, 12),

('Cartagena Caribe Colonial', 6, 1500.00, TRUE, TRUE, TRUE, 'AVION',
 'Historia, cultura y playas del Caribe colombiano.', 13, 13),

('Villa La Angostura Tranquila', 5, 1100.00, TRUE, TRUE, TRUE, 'AUTO',
 'Bosques, lagos y actividades suaves en un entorno tranquilo.', 14, 14),

('Madrid Cultural', 6, 1900.00, TRUE, TRUE, TRUE, 'AVION',
 'Museos, gastronomía y recorridos por la ciudad.', 15, 15),

('Tulum Bienestar', 6, 2050.00, TRUE, TRUE, TRUE, 'AVION',
 'Playa, cenotes, yoga y descanso en un entorno relajado.', 16, 16),

('San Martín Naturaleza', 5, 1250.00, TRUE, TRUE, TRUE, 'AUTO',
 'Lagos, trekking y paisajes patagónicos.', 17, 17),

('Roma Histórica', 6, 2100.00, TRUE, TRUE, TRUE, 'AVION',
 'Historia, monumentos y gastronomía italiana.', 18, 18),

('Jericoacoara Relax', 7, 1600.00, TRUE, TRUE, TRUE, 'AVION',
 'Dunas, lagunas y playas tranquilas en Brasil.', 19, 19),

('Queenstown Aventura', 8, 3500.00, TRUE, TRUE, TRUE, 'AVION',
 'Deportes extremos y naturaleza en Nueva Zelanda.', 20, 20);

-- ============================================================
-- DATOS: relación paquete - actividad
-- ============================================================

INSERT INTO paquete_actividad (paquete_id, actividad_id) VALUES
(1,1),(1,2),
(2,3),(2,4),
(3,5),(3,6),
(4,7),(4,8),
(5,9),(5,10),
(6,11),(6,12),
(7,13),(7,14),
(8,15),(8,16),
(9,17),(9,18),
(10,19),(10,20),
(11,21),(11,22),
(12,23),(12,24),
(13,25),(13,26),
(14,27),(14,28),
(15,29),(15,30),
(16,31),(16,32),
(17,33),(17,34),
(18,35),(18,36),
(19,37),(19,38),
(20,39),(20,40);

-- ============================================================
-- VERIFICACIÓN
-- ============================================================

SELECT COUNT(*) AS total_destinos FROM destinos;
SELECT COUNT(*) AS total_hoteles FROM hoteles;
SELECT COUNT(*) AS total_actividades FROM actividades;
SELECT COUNT(*) AS total_paquetes FROM paquetes_turisticos;
SELECT COUNT(*) AS total_relaciones FROM paquete_actividad;
