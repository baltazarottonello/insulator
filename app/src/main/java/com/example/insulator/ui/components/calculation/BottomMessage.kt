package com.example.insulator.ui.components.calculation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.insulator.R
import com.example.insulator.ui.theme.InsulatorTheme
import com.example.insulator.ui.theme.WarningColor

@Composable
fun BottomMessage(modifier: Modifier = Modifier) {
    val backgroundPaleYellow = Color(0xFFFDFCEB)
    val textBrown = Color(0xFF8D6218)

    Surface(
        modifier = modifier.fillMaxWidth().padding(top = 16.dp,bottom = 16.dp),
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 12.dp,
            bottomEnd = 12.dp
        ),
        color = backgroundPaleYellow
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {

          
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(WarningColor)
            )

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    painter= painterResource(R.drawable.triangle_exclamation_solid_full),
                    contentDescription = "Aviso Importante",
                    tint = WarningColor,
                    modifier = Modifier.size(28.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "AVISO IMPORTANTE",
                        color = textBrown,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "Utilice el producto solo después de consultar a un profesional de la salud. Verifique su relación de insulina/carbohidratos y la escala de corrección de glucosa en sangre con un profesional de la salud. Las dosis de insulina son solo recomendaciones y es posible que requieran ajustes en función de otros factores.",
                        color = textBrown,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview(){
    InsulatorTheme() {
        BottomMessage()
    }
}