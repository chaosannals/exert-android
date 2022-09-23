package com.example.anidemo.ui.page.employee

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EmployeeBean(
    val id: Long? = null,
    val name: String? = null,
    val jobNumber: String? = null,
) : Parcelable