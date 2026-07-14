package com.nutricionbn.insulator.core.data.repositories.patients

import com.nutricionbn.insulator.core.data.dto.patient.ParametersResponse
import com.nutricionbn.insulator.core.data.dto.patient.PatientCard
import com.nutricionbn.insulator.core.data.dto.patient.PatientParameters
import com.nutricionbn.insulator.core.data.dto.patient.PatientParamsHistoryResponse
import com.nutricionbn.insulator.core.data.dto.patient.PatientResponse
import com.nutricionbn.insulator.core.services.patient.PatientService

class PatientRepository(private val _patientService: PatientService) {

    //USED BY MEDICS
    suspend fun searchPatientByEmail(email: String): Result<PatientResponse> {
        return _patientService.searchPatientByEmail(email)
    }

    suspend fun getPatients(id: String): Result<List<PatientCard>> {
        return _patientService.getPatients(id)
    }

    //USED BY PATIENTS
    suspend fun getParameters(): Result<ParametersResponse> {
        return _patientService.getParameters();
    }

    suspend fun setPatientParameters(id: String, params: PatientParameters): Result<Any> {
        return _patientService.setParameters(id, params)
    }

    suspend fun getPatientHistory(id: String): Result<PatientParamsHistoryResponse> {
        return _patientService.getHistory(id)
    }
}