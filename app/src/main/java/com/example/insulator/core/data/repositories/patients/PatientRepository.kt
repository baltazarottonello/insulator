package com.example.insulator.core.data.repositories.patients

import com.example.insulator.core.data.dto.patient.ParametersResponse
import com.example.insulator.core.data.dto.patient.PatientParameters
import com.example.insulator.core.data.dto.patient.PatientResponse
import com.example.insulator.core.services.patient.PatientService
import com.example.insulator.ui.viewModels.management.patients.PatientState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PatientRepository(private val patientService: PatientService) {

    private val _patientState = MutableStateFlow(PatientState());
    val patientState: StateFlow<PatientState> = _patientState.asStateFlow();

    //USED BY MEDICS
    suspend fun searchPatientByEmail(email: String): PatientResponse {

        val result = patientService.searchPatientByEmail(email);

        result.onSuccess { data ->
            _patientState.update {
                it.copy(
                    id = data.id,
                    ratio = data.params.ratio,
                    sensitivity = data.params.sens,
                    targetGlucose = data.params.targetGlucose
                )
            }
        }

        return result.getOrThrow();
    }

    //USED BY PATIENTS
    suspend fun getParameters(): ParametersResponse {
        val result = patientService.getParameters();
        result.onSuccess { data ->
            _patientState.update {
                it.copy(
                    ratio = data.params.ratio,
                    sensitivity = data.params.sens,
                    targetGlucose = data.params.targetGlucose
                )
            }
        }

        return result.getOrThrow();
    }

    suspend fun setPatientParameters(id: String, params: PatientParameters): Result<Any> {
        return patientService.setParameters(id, params)
    }
}