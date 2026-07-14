package com.nutricionbn.insulator.ui.components.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.nutricionbn.insulator.ui.theme.InsulatorTheme

@Composable
fun UserProfileSettings(isMedic: Boolean) {
    Column() {
        Column() {
            Text("")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfilePreview() {
    InsulatorTheme() {
        UserProfileSettings(isMedic = false)
    }
}