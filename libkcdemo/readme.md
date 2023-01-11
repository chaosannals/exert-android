# 

```bash
# 密码 123456
keytool -keystore demo.jks -alias sampleAlias -genkeypair -keyalg RSA -keysize 4096 -validity 3 -dname 'CN=localhost, OU=ktor, O=ktor, L=SHANTOU, ST=GUANGDONG, C=CN'
```

```bash
# 
openssl pkcs12 -export -in cert.pem -inkey key.pem -out keystore.p12 -name "sampleAlias"
```