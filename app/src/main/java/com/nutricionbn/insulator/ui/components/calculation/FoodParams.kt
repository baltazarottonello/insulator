package com.nutricionbn.insulator.ui.components.calculation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutricionbn.insulator.R
import com.nutricionbn.insulator.ui.viewModels.calculation.Food

@Composable
fun FoodParams(
    foodList: List<Food>,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onFoodClick: (Food) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.tertiary)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            painter = painterResource(R.drawable.list_check_solid_full),
                            contentDescription = "Lista",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Contenido Aproximado",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    OutlinedButton(
                        onClick = onClearClick,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFE53935)),
                        border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.3f)),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.trash_can_solid_full),
                            contentDescription = "Limpiar",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Limpiar", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onQueryChange,
                    label = { Text("Buscar alimento...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.magnifying_glass_solid_full),
                            contentDescription = "Icono de búsqueda"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "(1 taza = 240 ml). Tocá para sumar:",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp
                )


                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (foodList.isEmpty()) {
                        item {
                            Text("No se encontraron resultados", modifier = Modifier.padding(16.dp))
                        }
                    } else {
                        items(foodList) { food ->
                            FoodCard(food = food, onClick = { onFoodClick(food) })
                        }
                    }
                }
            }
        }
    }
}