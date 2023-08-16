package com.example.bootdemo.ui.page.ssh

data class SshAuthInfo(
    val host: String,
    val port: Int=22,
    val user: String,
    val password: String?=null,
    val privateKey: String?=null,
)