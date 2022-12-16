package implementations.authentication

import implementations.Cipher
import implementations.ciphers.classical.CaesarCipher
import implementations.ciphers.classical.CaesarPermutationCipher
import implementations.ciphers.substitution.VigenereCipher
import java.security.MessageDigest
import java.util.HashMap

data class User(val username: String, val passwordHash: String, val ciphers: String)

fun main(args: Array<String>) {
    // Create a hashmap to store the users
    val users = HashMap<String, User>()

    // Add some users to the hashmap
    users["Alice"] = User("Alice", hashPassword("password1"), "1")
    users["Bob"] = User("Bob", hashPassword("password2"), "12")
    users["Charlie"] = User("Charlie", hashPassword("password3"), "3")

    // Prompt the user to enter their username and password
    print("Enter your username: ")
    val username = readLine()!!
    print("Enter your password: ")
    val password = readLine()!!

    // Check if the username and password match a user in the hashmap
    if (username in users && users[username]!!.passwordHash == hashPassword(password)) {
        // Prompt the user to enter their 2FA code
        print("Enter your 2FA code: ")
        val twoFactorAuthCode = readLine()!!

        // Validate the 2FA code
        if (validateTwoFactorAuthCode(username, twoFactorAuthCode)) {
            println("2FA successful. Welcome, $username!")
            while (true) {
                println("What would you like to encrypt?")
                val messageToEncrypt = readLine()!!
                encryptUserMessage(users[username]!!, messageToEncrypt)
            }
        } else {
            println("Invalid 2FA code. Access denied.")
        }
    } else {
        println("Invalid username or password. Access denied.")
    }
}

// This function hashes a password using the SHA-256 algorithm
fun hashPassword(password: String): String {
    val bytes = password.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("", { str, it -> str + "%02x".format(it) })
}

// This function would typically call an external service or use a local algorithm to validate the 2FA code
fun validateTwoFactorAuthCode(username: String, code: String): Boolean {
    return code == getTwoFactorAuthKey(username)
}

fun getTwoFactorAuthKey(username: String): String {
    // For the sake of this example, we'll just return a hardcoded key for each user
    return when (username) {
        "Alice" -> "1234567890"
        "Bob" -> "0987654321"
        "Charlie" -> "abcdefghij"
        else -> ""
    }
}

fun encryptUserMessage(user: User, message: String) {
    val caesarCipher = CaesarCipher(15)
    val caesarPermutationCipher = CaesarPermutationCipher("secret", 4)
    val vigenere = VigenereCipher("verySECRETkey")

    println("Which cipher would you like to use (1 - Caesar; 2 - Caesar with permutation; 3 - Vigenere)")
    val choice = readLine()!!

    // check if the user has access to the cipher
    if (user.ciphers.contains(choice[0])) {
        when (choice) {
            "1" -> {
                encryptMessageWithCipher(message, caesarCipher)
            }
            "2" -> {
                encryptMessageWithCipher(message, caesarPermutationCipher)
            }
            "3" -> {
                encryptMessageWithCipher(message, vigenere)
            }
        }
    } else {
        println("You are not allowed to use this cipher. Please use another cipher.")
    }
}

fun encryptMessageWithCipher(message: String, cipher: Cipher) {
    println(cipher.encrypt(message))
}