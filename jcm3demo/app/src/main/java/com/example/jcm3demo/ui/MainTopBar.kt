package com.example.jcm3demo.ui

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.jcm3demo.R

@ExperimentalMaterial3Api
@Composable
fun MainTopBar() {

    MediumTopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "star"
                )
            }
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "star"
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = colorResource(id = R.color.deep_sky_blue)
        ),
    )
}

@ExperimentalMaterial3Api
@Preview(showBackground = true)
@Composable
fun MainTopBarPreview() {
    MainTopBar()
}