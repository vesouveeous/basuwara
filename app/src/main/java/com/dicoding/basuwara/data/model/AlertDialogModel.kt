package com.dicoding.basuwara.data.model

data class AlertDialogModel(
    val title: String,
    val message: String,
    val confirmButtonText: String,
    val onConfirmClick: () -> Unit,
    val onDismissClick: () -> Unit,
)