package implementations.ciphers.classical

import implementations.ciphers.substitution.PlayfairCipher
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlayfairCipherTest
{
    @Test
    fun testEncryptMethod()
    {
        val message = "LOREM IPSUM"
        val key = "DOLOREM"
        val sut = PlayfairCipher(key)

        val encryptedMessage = sut.encrypt(message)

        assertEquals("RL ED BG QT PF", encryptedMessage)
    }

    @Test
    fun testDecryptMethod()
    {
        val encryptedMessage = "RI TV ON LP GP QC DS SM QZ OU BN"
        val key = "ACHTUNG"
        val sut = PlayfairCipher(key);

        val decryptedMessage = sut.decrypt(encryptedMessage)

        assertEquals("PL AY FA IR CI PH ER ME SX SA GE", decryptedMessage)
    }
}