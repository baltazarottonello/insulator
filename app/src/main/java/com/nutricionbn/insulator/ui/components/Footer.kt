package com.nutricionbn.insulator.ui.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.nutricionbn.insulator.R

val BackgroundColor = Color(0xFF111827)
val TextColorPrimary = Color(0xFFFFFFFF)
val TextColorSecondary = Color(0xFF9CA3AF)
val IconTealColor = Color(0xFF0D9488)
val SocialBgColor = Color(0xFF1F2937)
val DividerColor = Color(0xFF374151)

@Composable
fun Footer(isSuccess: Boolean) {
    val context = LocalContext.current
    if (!isSuccess) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(BackgroundColor)
                .padding(top = 64.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Comenzá tu cambio hoy",
                color = TextColorPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Contactame para agendar tu consulta o hacerme cualquier pregunta.\n¡Estoy para ayudarte!",
                color = TextColorSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            ContactItem(
                iconResId = R.drawable.envelope_solid_full,
                text = "lic.barbarafavata@gmail.com",
                onClick = { sendEmail(context, "lic.barbarafavata@gmail.com") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ContactItem(
                iconResId = R.drawable.whatsapp_brands_solid_full,
                text = "+54 9 223 519-4077",
                onClick = { openWhatsApp(context, "+54 9 223 519-4077") }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialIconButton(
                    iconResId = R.drawable.instagram_brands_solid_full,
                    onClick = { openInstagram(context, "nutricion.bn") }
                )
                Spacer(modifier = Modifier.width(16.dp))
                SocialIconButton(
                    iconResId = R.drawable.facebook_f_brands_solid_full,
                    onClick = { openFacebook(context, "https://www.facebook.com/share/1RFfzB5X8v/") }
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(0.8f),
                color = DividerColor,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "© 2026 Lic. Bárbara Favata Nicolari - Nutrición BN. MP 4507. Todos los derechos reservados.",
                color = TextColorSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    } else null
}

@Composable
fun ContactItem(iconResId: Int, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = IconTealColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = TextColorSecondary,
            fontSize = 14.sp
        )
    }
}

@Composable
fun SocialIconButton(iconResId: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(SocialBgColor),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onClick) {
            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                tint = TextColorSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

fun openWhatsApp(context: Context, phoneNumber: String) {
    val cleanNumber = phoneNumber.replace("[^0-9]".toRegex(), "")
    val uri = "https://wa.me/$cleanNumber".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
}

fun sendEmail(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:$email".toUri()
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No hay app de email instalada", Toast.LENGTH_SHORT).show()
    }
}

fun openInstagram(context: Context, username: String) {
    val intent = try {
        Intent(Intent.ACTION_VIEW, "http://instagram.com/_u/$username".toUri()).apply {
            setPackage("com.instagram.android")
        }
    } catch (e: Exception) {
        null
    }
    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW, "https://instagram.com/$username".toUri())
        )
    }
}

fun openFacebook(context: Context, profileUrl: String) {
    val intent = Intent(Intent.ACTION_VIEW, profileUrl.toUri())
    context.startActivity(intent)
}