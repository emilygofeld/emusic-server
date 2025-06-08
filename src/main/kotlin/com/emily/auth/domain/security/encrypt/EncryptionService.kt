package com.emily.auth.domain.security.encrypt

import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionService(secret: String, iv: String) {
    private val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    private val keySpec = SecretKeySpec(secret.toByteArray(), "AES")
    private val ivSpec = IvParameterSpec(iv.toByteArray())

    fun encrypt(input: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        val encrypted = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(input: String): String {
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decoded = Base64.getDecoder().decode(input)
        return String(cipher.doFinal(decoded))
    }
}
