INSERT INTO `user` (`id`, `name`, `email`, `password`, `role`) VALUES
(1, 'chini', 'chini@gmail.com','$2a$12$9kMYTkiTnHVA5uUn9fJKb.9iNKUXs6AyzfmjCQgDA6h4rNWL.7fv6', 'ADMIN');


INSERT INTO `category` (`id`, `name`, `slug`, `description`, `estado`) VALUES
(1, 'Pizzas', 'pizzas', 'Pizzas artesanales al horno', TRUE),
(2, 'Hamburguesas', 'hamburguesas', 'Hamburguesas gourmet', TRUE),
(3, 'Pastas', 'pastas', 'Pastas frescas italianas', TRUE),
(4, 'Bebidas', 'bebidas', 'Bebidas frías y calientes', TRUE),
(5, 'Postres', 'postres', 'Postres caseros', TRUE),
(6, 'Entradas', 'entradas', 'Entrantes y aperitivos', TRUE),
(7, 'Ensaladas', 'ensaladas', 'Ensaladas frescas', TRUE),
(8, 'Sandwiches', 'sandwiches', 'Sandwiches y bocadillos', TRUE);

INSERT INTO `product` (`id`, `name`, `base_price`, `discount_percentage`, `image`) VALUES
(1, 'Pizza Margarita', 10.00, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(2, 'Pizza Pepperoni', 12.00, 5, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(3, 'Pizza Cuatro Quesos', 13.00, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(4, 'Pizza Vegetariana', 11.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(5, 'Hamburguesa Clásica', 9.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(6, 'Hamburguesa Doble Queso', 11.00, 10, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(7, 'Hamburguesa BBQ', 12.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(8, 'Hamburguesa Vegana', 11.00, 0, 'https://www.annarecetasfaciles.com/files/arroz-meloso-marisco-1-scaled.jpg'),
(9, 'Spaghetti Bolognesa', 13.00, 0, 'https://embed.widencdn.net/img/mccormick/wu1v74egf2/800x800px/pasta_marinera.jpg?crop=true&q=80&color=ffffffff&u=qwwekl'),
(10, 'Fettuccine Alfredo', 12.50, 0, 'https://www.giallozafferano.es/images/183-18316/paccheri-con-mariscos_1200x800.jpg'),
(11, 'Lasagna de Carne', 14.00, 0, 'https://opercebeiro.com/wp-content/uploads/nc/catalog/bogavante.jpg'),
(12, 'Ravioles de Ricotta', 13.50, 0, 'ravioles.jpg'),
(13, 'Coca Cola', 3.00, 0, 'https://cdn.elcocinerocasero.com/imagen/receta/1000/2022-03-10-11-11-38/arroz-caldoso-de-bogavante.jpeg'),
(14, 'Agua Mineral', 2.00, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(15, 'Limonada Natural', 2.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(16, 'Cerveza Artesanal', 4.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(17, 'Tiramisú', 6.00, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(18, 'Brownie con Helado', 6.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(19, 'Cheesecake', 6.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(20, 'Helado Artesanal', 5.00, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(21, 'Papas Fritas', 4.00, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(22, 'Aros de Cebolla', 4.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(23, 'Ensalada César', 7.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(24, 'Ensalada Caprese', 7.00, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(25, 'Sandwich de Pollo', 8.50, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg'),
(26, 'Sandwich de Jamón y Queso', 7.00, 0, 'https://canalmarmenor.carm.es/wp-content/uploads/2020/10/CALLINECTES-SAPIDUS1.jpg');

INSERT INTO `ingredient` (`id`, `name`, `price`, `image`) VALUES
(1, 'Masa de Pizza', 1.50, 'masa.jpg'),
(2, 'Salsa de Tomate', 0.80, 'salsa_tomate.jpg'),
(3, 'Queso Mozzarella', 1.20, 'mozzarella.jpg'),
(4, 'Pepperoni', 1.50, 'pepperoni.jpg'),
(5, 'Queso Azul', 1.40, 'queso_azul.jpg'),
(6, 'Queso Parmesano', 1.30, 'parmesano.jpg'),
(7, 'Verduras Asadas', 1.00, 'verduras.jpg'),
(8, 'Carne de Res', 2.00, 'carne_res.jpg'),
(9, 'Pan de Hamburguesa', 1.00, 'pan_hamburguesa.jpg'),
(10, 'Lechuga', 0.40, 'lechuga.jpg'),
(11, 'Tomate', 0.50, 'tomate.jpg'),
(12, 'Queso Cheddar', 1.10, 'cheddar.jpg'),
(13, 'Salsa BBQ', 0.70, 'bbq.jpg'),
(14, 'Medallón Vegano', 2.20, 'vegano.jpg'),
(15, 'Pasta', 1.30, 'pasta.jpg'),
(16, 'Salsa Bolognesa', 1.80, 'bolognesa.jpg'),
(17, 'Crema', 1.00, 'crema.jpg'),
(18, 'Ricotta', 1.20, 'ricotta.jpg'),
(19, 'Carne Picada', 1.90, 'carne_picada.jpg'),
(20, 'Papas', 0.60, 'papas.jpg'),
(21, 'Cebolla', 0.40, 'cebolla.jpg'),
(22, 'Pollo', 1.80, 'pollo.jpg'),
(23, 'Jamón', 1.20, 'jamon.jpg'),
(24, 'Chocolate', 1.20, 'chocolate.jpg'),
(25, 'Helado de Vainilla', 1.50, 'helado_vainilla.jpg');

INSERT INTO `product_category` (`product_id`, `category_id`) VALUES
(1,1),(2,1),(3,1),(4,1),
(5,2),(6,2),(7,2),(8,2),
(9,3),(10,3),(11,3),(12,3),
(13,4),(14,4),(15,4),(16,4),
(17,5),(18,5),(19,5),(20,5),
(21,6),(22,6),
(23,7),(24,7),
(25,8),(26,8);

INSERT INTO `product_ingredient` (`product_id`, `ingredient_id`) VALUES
-- Pizzas
(1,1),(1,2),(1,3),
(2,1),(2,2),(2,3),(2,4),
(3,1),(3,3),(3,5),(3,6),
(4,1),(4,2),(4,3),(4,7),

-- Hamburguesas
(5,8),(5,9),(5,10),(5,11),
(6,8),(6,9),(6,12),
(7,8),(7,9),(7,12),(7,13),
(8,9),(8,14),(8,10),

-- Pastas
(9,15),(9,16),
(10,15),(10,17),
(11,15),(11,19),
(12,15),(12,18),

-- Entradas
(21,20),
(22,21),

-- Ensaladas
(23,10),(23,11),(23,6),
(24,11),(24,3),

-- Sandwiches
(25,22),(25,9),(25,10),
(26,23),(26,3),(26,9),

-- Postres
(18,24),(18,25);
