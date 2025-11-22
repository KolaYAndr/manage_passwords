package com.kolayandr.passwordmanager

import android.util.Base64

class SimplePasswordEncryptor : PasswordEncryptor {
    override fun encrypt(plain: String): String {
        return Base64.encodeToString(plain.toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
    }

    override fun decrypt(encrypted: String): String {
        return String(Base64.decode(encrypted, Base64.NO_WRAP), Charsets.UTF_8)
    }
}
