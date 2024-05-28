
INSERT INTO producto (nombre, descripcion, precio, habilitado)
VALUES
('Producto 1', 'Este producto es de prueba, 1111', 5000, true),
('Producto 2', 'Este producto es de prueba, 2222', 7000, true);

INSERT INTO stock (fk_producto,telefono_comprador,estado)
VALUES (1,null,'A_LA_VENTA'), (1,null,'A_LA_VENTA'), (2,null,'A_LA_VENTA');


INSERT INTO dia_disponible (dia,horario_inicio,horario_fin)
VALUES ('Lunes','08:00','12:00'), ('Lunes','14:00','18:00'),
('Martes','08:00','12:00'), ('Martes','14:00','18:00'),
('Miercoles','08:00','12:00'), ('Miercoles','14:00','18:00'),
('Jueves','08:00','12:00'), ('Jueves','14:00','18:00'),
('Viernes','08:00','12:00'), ('Viernes','14:00','18:00');

