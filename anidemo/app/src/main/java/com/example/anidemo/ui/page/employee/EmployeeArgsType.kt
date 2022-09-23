package com.example.anidemo.ui.page.employee

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

class EmployeeArgsType : NavType<EmployeeBean>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): EmployeeBean? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): EmployeeBean {
        return Gson().fromJson(value, EmployeeBean::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: EmployeeBean) {
        bundle.putParcelable(key, value)
    }
}