package com.example.bootdemo.ui.page.ssh

data class SshAuthInfo(
    val host: String="localhost",
    val port: Int=22,
    val user: String="root",
    val password: String?=null,
    val privateKey: String?=null,
)