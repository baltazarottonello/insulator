package com.nutricionbn.insulator.core.data.repositories.medics

import com.nutricionbn.insulator.core.data.dto.medic.PatientMedicCard
import com.nutricionbn.insulator.core.data.dto.medic.UnlinkMedicRequest
import com.nutricionbn.insulator.core.services.medic.MedicService

class MedicRepository(private val _medicService: MedicService) {

    suspend fun getMedics(): Result<List<PatientMedicCard>> {
        return _medicService.getMedics()
    }

    suspend fun unlinkMedic(data: UnlinkMedicRequest): Result<Any> {
        return _medicService.unlinkMedic(data)
    }
}