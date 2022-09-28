package implementations.ciphers.classical

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class CaesarCipherTest
{
    @Test
    fun testEncryptMethod()
    {
        val encryptionKey = 6
        val message = "TEST MESSAGE"
        val sut = CaesarCipher(encryptionKey)

        val encryptedMessage = sut.encrypt(message)

        assertEquals("ZKYZ SKYYGMK", encryptedMessage)
    }

    @Test
    fun testDecryptMethod()
    {
        val encryptionKey = 10
        val encryptedMessage = "CYWO NEWWI WOCCKQO"
        val sut = CaesarCipher(encryptionKey)

        val decryptedMessage = sut.decrypt(encryptedMessage)

        assertEquals("SOME DUMMY MESSAGE", decryptedMessage)
    }

    @Test
    fun testWhenEncryptionOfMessageIsSameAsDecryption()
    {
        val key = 26
        val message = "SOMETHING WRITTEN HERE"
        val sut = CaesarCipher(key)

        val encryptedMessage = sut.encrypt(message)

        assertEquals(message, encryptedMessage)
    }
}