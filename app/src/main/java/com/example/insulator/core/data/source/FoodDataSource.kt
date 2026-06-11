package com.example.insulator.core.data.source

import com.example.insulator.core.data.ui.Food

// TODO: Las comidas deben cargarse desde la base de datos

class FoodDataSource {

    fun getAvailableFoods(): List<Food> {
        return listOf(
            Food(1, "🥩 🍗 🐖 🐟", "1 Milanesa (Empanado Simple)", 15),
            Food(2, "🥩 🍗 🐖 🐟", "1 Milanesa (Empanado Doble)", 20),
            Food(3, "🌮", "1 tortilla/fajita (tipo Rapidita)", 15),
            Food(4, "🥧", "1 porción tarta sin tapa (verdura)", 15),
            Food(5, "🥧", "1 porción tarta sin tapa (choclo/zapallo)", 30),
            Food(6, "🍣", "3 piezas de sushi (maki/nigiri)", 15),
            Food(7, "🌾", "1/3 taza de quinua cocida (70 g)", 15),
            Food(8, "🍜", "1 taza de fideos de arroz cocidos", 45),
            Food(9, "🥣", "1 taza de polenta cocida", 30),
            Food(10, "🍊", "1 mandarina mediana", 15),
            Food(11, "🍞", "1 rebanada de pan de molde", 15),
            Food(12, "🍘", "3-4 galletas de agua medianas", 15),
            Food(13, "🥣", "1/2 taza de cereales/papas puré", 15),
            Food(14, "🍚", "1 taza de arroz o pasta", 45),
            Food(15, "🫘", "1 taza legumbres secas cocidas", 30),
            Food(16, "🌽", "1 taza de arvejas o choclo", 30),
            Food(17, "🥛", "1 taza de leche, yogurt dietético", 15),
            Food(18, "🍟", "15 papas fritas (aprox. 30 g)", 15),
            Food(19, "🍕", "1 porción de pizza (1/8)", 15),
            Food(20, "🥟", "1 empanada (carne ó jamón/queso)", 15),
            Food(21, "🍇", "10 uvas", 15),
            Food(22, "🍊", "1 naranja mediana (150 g)", 15),
            Food(23, "🍎", "1 manzana/pera pequeña (100 g)", 15),
            Food(24, "🍌", "1/2 banana", 15),
            Food(25, "🍏", "1 manzana o pera grande (200 g)", 30),
            Food(26, "🍨", "1 taza de helado (3 bochitas)", 45)
        )
    }
}