package com.example.calendardemo.ui.page.calendar

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.calendardemo.CalendarAccount
import com.example.calendardemo.addCalendarAccount
import com.example.calendardemo.deleteCalendarAccount
import com.example.calendardemo.getDefaultCalendarAccount
import com.example.calendardemo.listCalendarAccount

@Composable
fun CalendarAccountPage() {
    val context = LocalContext.current
    var account: CalendarAccount? by remember {
        mutableStateOf(null)
    }
    var accounts: List<CalendarAccount> by remember {
        mutableStateOf(listOf())
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        if (it.values.reduce { acc, b -> acc && b }) {
            context.run {
                account = getDefaultCalendarAccount()
                accounts = listCalendarAccount()
            }
        }
    }

    LaunchedEffect(permissionLauncher) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR,
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        account?.let {
            Text(text = "${it.id}")
            Text(text = it.name)
            Text(text = it.accountName)
            Text(text = it.accountType)
            Text(text = it.displayName)
        }

        var name by remember {
            mutableStateOf("")
        }
        var accountName by remember {
            mutableStateOf("")
        }
        var accountType by remember {
            mutableStateOf("")
        }
        var displayName by remember {
            mutableStateOf("")
        }

        TextField(value = name, onValueChange = {name = it})
        TextField(value = accountName, onValueChange = {accountName = it})
        TextField(value = accountType, onValueChange = {accountType = it})
        TextField(value = displayName, onValueChange = {displayName = it})
        Button(onClick = {
            context.run {
                addCalendarAccount(
                    name=name,
                    accountName=accountName,
                    accountType=accountType,
                    displayName=displayName,
                )
                accounts = listCalendarAccount()
            }
        }) {
            Text(text = "添加账号")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = rememberLazyListState()
        ) {
            itemsIndexed(accounts) { i, account ->
                Column {
                    Row (
                        modifier = Modifier
                            .clickable {
                                context.run {
                                    deleteCalendarAccount(account.id)
                                    accounts = listCalendarAccount()
                                }
                            }
                    ) {
                        Text(text = "${account.id} ")
                        Text(text = account.name)
                    }
                    Text(text = account.accountName)
                    Text(text = account.accountType)
                    Text(text = account.displayName)
                }
            }
        }
    }
}