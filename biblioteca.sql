-- Eliminar la base de datos si existe para comenzar desde cero
DROP DATABASE IF EXISTS biblioteca;

-- Crear la base de datos
CREATE DATABASE biblioteca;
USE biblioteca;

-- Crear la tabla base ElementoBiblioteca
CREATE TABLE ElementoBiblioteca (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    ano_publicacion INT NOT NULL,
    tipo VARCHAR(20) NOT NULL COMMENT 'LIBRO, REVISTA o DVD'
);

-- Crear la tabla Libro
CREATE TABLE Libro (
    id INT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL,
    numero_paginas INT NOT NULL,
    genero VARCHAR(50) NOT NULL,
    editorial VARCHAR(100) NOT NULL,
    FOREIGN KEY (id) REFERENCES ElementoBiblioteca(id) ON DELETE CASCADE
);

-- Crear la tabla Revista
CREATE TABLE Revista (
    id INT PRIMARY KEY,
    numero_edicion INT NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    FOREIGN KEY (id) REFERENCES ElementoBiblioteca(id) ON DELETE CASCADE
);

-- Crear la tabla DVD
CREATE TABLE DVD (
    id INT PRIMARY KEY,
    duracion INT NOT NULL,
    genero VARCHAR(50) NOT NULL,
    FOREIGN KEY (id) REFERENCES ElementoBiblioteca(id) ON DELETE CASCADE
);

-- Insertar datos de ejemplo para libros
INSERT INTO ElementoBiblioteca (titulo, autor, ano_publicacion, tipo) VALUES
('Cien años de soledad', 'Gabriel García Márquez', 1967, 'LIBRO'),
('El código Da Vinci', 'Dan Brown', 2003, 'LIBRO'),
('La sombra del viento', 'Carlos Ruiz Zafón', 2001, 'LIBRO'),
('El nombre del viento', 'Patrick Rothfuss', 2007, 'LIBRO'),
('1984', 'George Orwell', 1949, 'LIBRO');

INSERT INTO Libro (id, isbn, numero_paginas, genero, editorial) VALUES
(1, '978-0307474728', 432, 'Realismo mágico', 'Vintage Español'),
(2, '978-0307474278', 592, 'Suspense', 'Anchor'),
(3, '978-0143126393', 512, 'Misterio', 'Penguin Books'),
(4, '978-0756404741', 662, 'Fantasía', 'DAW Books'),
(5, '978-0451524935', 328, 'Distopía', 'Signet Classics');

-- Insertar datos de ejemplo para revistas
INSERT INTO ElementoBiblioteca (titulo, autor, ano_publicacion, tipo) VALUES
('National Geographic', 'National Geographic Society', 2023, 'REVISTA'),
('Scientific American', 'Scientific American Inc.', 2023, 'REVISTA'),
('Time', 'Time USA, LLC', 2023, 'REVISTA'),
('The Economist', 'The Economist Group', 2023, 'REVISTA'),
('Nature', 'Springer Nature', 2023, 'REVISTA');

INSERT INTO Revista (id, numero_edicion, categoria) VALUES
(6, 256, 'Ciencia y Naturaleza'),
(7, 128, 'Ciencia'),
(8, 200, 'Actualidad'),
(9, 8843, 'Economía'),
(10, 7969, 'Ciencia');

-- Insertar datos de ejemplo para DVDs
INSERT INTO ElementoBiblioteca (titulo, autor, ano_publicacion, tipo) VALUES
('El Padrino', 'Francis Ford Coppola', 1972, 'DVD'),
('Matrix', 'Wachowski Brothers', 1999, 'DVD'),
('El Señor de los Anillos: La Comunidad del Anillo', 'Peter Jackson', 2001, 'DVD'),
('Interestelar', 'Christopher Nolan', 2014, 'DVD'),
('Parásitos', 'Bong Joon-ho', 2019, 'DVD');

INSERT INTO DVD (id, duracion, genero) VALUES
(11, 175, 'Drama'),
(12, 136, 'Ciencia Ficción'),
(13, 178, 'Fantasía'),
(14, 169, 'Ciencia Ficción'),
(15, 132, 'Drama');

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_elementobiblioteca_tipo ON ElementoBiblioteca(tipo);
CREATE INDEX idx_elementobiblioteca_titulo ON ElementoBiblioteca(titulo);
CREATE INDEX idx_elementobiblioteca_autor ON ElementoBiblioteca(autor);
CREATE INDEX idx_libro_genero ON Libro(genero);
CREATE INDEX idx_revista_categoria ON Revista(categoria);
CREATE INDEX idx_dvd_genero ON DVD(genero);

-- Privilegios (ajusta según tus necesidades)
-- GRANT ALL PRIVILEGES ON biblioteca.* TO 'tu_usuario'@'localhost';
-- FLUSH PRIVILEGES;

-- Procedimiento almacenado para buscar elementos por título
DELIMITER //
CREATE PROCEDURE BuscarPorTitulo(IN p_titulo VARCHAR(200))
BEGIN
    SELECT e.id, e.titulo, e.autor, e.ano_publicacion, e.tipo
    FROM ElementoBiblioteca e
    WHERE e.titulo LIKE CONCAT('%', p_titulo, '%')
    ORDER BY e.titulo;
END //
DELIMITER ;

-- Procedimiento almacenado para buscar elementos por autor
DELIMITER //
CREATE PROCEDURE BuscarPorAutor(IN p_autor VARCHAR(100))
BEGIN
    SELECT e.id, e.titulo, e.autor, e.ano_publicacion, e.tipo
    FROM ElementoBiblioteca e
    WHERE e.autor LIKE CONCAT('%', p_autor, '%')
    ORDER BY e.autor, e.titulo;
END //
DELIMITER ;

-- Procedimiento almacenado para obtener estadísticas de la biblioteca
DELIMITER //
CREATE PROCEDURE ObtenerEstadisticas()
BEGIN
    SELECT
        COUNT(*) as total_elementos,
        SUM(CASE WHEN tipo = 'LIBRO' THEN 1 ELSE 0 END) as total_libros,
        SUM(CASE WHEN tipo = 'REVISTA' THEN 1 ELSE 0 END) as total_revistas,
        SUM(CASE WHEN tipo = 'DVD' THEN 1 ELSE 0 END) as total_dvds
    FROM ElementoBiblioteca;
END //
DELIMITER ;