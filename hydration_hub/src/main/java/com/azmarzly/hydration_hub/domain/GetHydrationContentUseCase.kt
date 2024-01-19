package com.azmarzly.hydration_hub.domain

import core.domain.FirebaseStorageApi
import java.util.Locale
import javax.inject.Inject

class GetHydrationContentUseCase @Inject constructor(
    private val firebaseStorageApi: FirebaseStorageApi,
) {

    suspend operator fun invoke() = firebaseStorageApi.getHydrationHubContent(Locale.getDefault().language)
}