package com.example.insulator.core.data.repositories.patients

import com.example.insulator.core.data.dto.patient.ParametersResponse
import com.example.insulator.core.data.dto.patient.PatientCard
import com.example.insulator.core.data.dto.patient.PatientParameters
import com.example.insulator.core.data.dto.patient.PatientResponse
import com.example.insulator.core.services.patient.PatientService

class PatientRepository(private val patientService: PatientService) {

    //USED BY MEDICS
    suspend fun searchPatientByEmail(email: String): Result<PatientResponse> {
        return patientService.searchPatientByEmail(email)
    }

    suspend fun getPatients(id: String): Result<List<PatientCard>>{
        return patientService.getPatients(id)
    }

    //USED BY PATIENTS
    suspend fun getParameters(): Result<ParametersResponse> {
        return patientService.getParameters();
    }

    suspend fun setPatientParameters(id: String, params: PatientParameters): Result<Any> {
        return patientService.setParameters(id, params)
    }
}