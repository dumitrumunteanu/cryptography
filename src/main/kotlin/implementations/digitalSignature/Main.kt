package implementations.digitalSignature
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.Signature;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.Cipher

fun main(args: Array<String>) {
    // Create a map of user IDs to key pairs and hashed passwords
    val users = HashMap<Int, Pair<KeyPair, String>>()

    // Generate a key pair and hash the password for each user
    val keyGen = KeyPairGenerator.getInstance("RSA")
    keyGen.initialize(2048)
    for (i in 1..10) {
        val keyPair = keyGen.generateKeyPair()
        val password = "password$i"
        val passwordHash = hashPassword(password)
        users[i] = Pair(keyPair, passwordHash)
    }

    // Data to be signed and encrypted
    val data = "This is the data to be signed and encrypted".toByteArray()

    // Select a user to sign and encrypt the data
    val userId = 5
    val user = users[userId]
    val keyPair = user?.first
    val privateKey = keyPair?.private
    val publicKey = keyPair?.public

    // Encrypt the data using the public key
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)
    val encryptedData = cipher.doFinal(data)
    val encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData)

    // Create the digital signature
    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKey)
    signature.update(encryptedData)
    val signedData = signature.sign()
    val signedDataBase64 = Base64.getEncoder().encodeToString(signedData)

    // Send the encrypted data and the digital signature to the recipient
    val message = "$encryptedDataBase64|$signedDataBase64"
    println("Message: $message")

    // The recipient receives the message and splits it into the encrypted data and the digital signature
    val parts = message.split("|")
    val encryptedDataReceived = Base64.getDecoder().decode(parts[0])
    val signedDataReceived = Base64.getDecoder().decode(parts[1])

    // Verify the digital signature
    signature.initVerify(publicKey)
    signature.update(encryptedDataReceived)
    val isValid = signature.verify(signedDataReceived)

    // Decrypt the data using the private key
    cipher.init(Cipher.DECRYPT_MODE, privateKey)
    val decryptedData = cipher.doFinal(encryptedDataReceived)
    val decryptedDataString = String(decryptedData)

    println("Digital signature is valid: $isValid")
    println("Decrypted data: $decryptedDataString")
}

fun hashPassword(password: String): String
{
    val bytes = password.toByteArray()
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(bytes)
    return hash.toHexString()
}

fun ByteArray.toHexString(): String {
    return joinToString("") { "%02x".format(it) }
}
