package com.nutricionbn.insulator.ui.viewModels.management.patients

import com.nutricionbn.insulator.core.data.dto.medic.PatientMedicCard

data class PatientMedicAssignmentUiState(
    // General status flow
    val medics: List<PatientMedicCard> = listOf<PatientMedicCard>(),
    val error: String = "",
    val modalLoading: Boolean = false,

    // General loading flow
    val loading: Boolean = false,

    // Unlink flow
    val showUnlinkMedicConfirmationModal: Boolean = false,
    val showUnlinkMedicSuccessModal: Boolean = false,

    // Navigation flow
    val shouldNavigateAway: Boolean = false,
)