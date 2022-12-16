# Lab 4

### Course: Cryptography & Security
### Author: Munteanu Dumitru

----

In this laboratory work I implemented the digital signature.
Firstly, the code generates a key pair and hashes the password for each user, and stores them in
a HashMap called users

```kotlin
val users = HashMap<Int, Pair<KeyPair, String>>()

val keyGen = KeyPairGenerator.getInstance("RSA")
keyGen.initialize(2048)
for (i in 1..10) {
    val keyPair = keyGen.generateKeyPair()
    val password = "password$i"
    val passwordHash = hashPassword(password)
    users[i] = Pair(keyPair, passwordHash)
}
```
The KeyPairGenerator class generates a key pair using the specified cryptographic algorithm (in this case, RSA). The initialize method sets the key size, which determines the strength of the key pair.

For each user, we generate a key pair using the generateKeyPair method and store it in the users map along with the hashed password.

Next, the code selects a user and retrieves their key pair and private key from the users map.
```kotlin
val userId = 5
val user = users[userId]
val keyPair = user?.first
val privateKey = keyPair?.private
val publicKey = keyPair?.public
```

The userId variable specifies the ID of the user whose key pair and private key we want to retrieve. We use the get method of the users map to retrieve the value associated with the user ID, which is a pair of key pair and hashed password. We then extract the key pair from the pair using the first property. Finally, we extract the private key from the key pair using the private property.

The privateKey variable now holds the private key of the selected user.

Next, the code encrypts the data using the public key of the selected user.
```kotlin
val cipher = Cipher.getInstance("RSA")
cipher.init(Cipher.ENCRYPT_MODE, publicKey)
val encryptedData = cipher.doFinal(data)
val encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData)
```

The Cipher class provides encryption and decryption functionality using various cryptographic algorithms. The getInstance method creates a new Cipher object using the specified algorithm (in this case, RSA).

The init method initializes the Cipher object for either encryption or decryption, depending on the mode specified as the first argument. 
In this case, we use the ENCRYPT_MODE constant to specify that we want to encrypt the data. The second argument specifies the key to be used for the operation. In this case, we use the public key of the selected user.

The doFinal method performs the actual encryption of the data and returns the encrypted data as a byte array.

To make it easier to transmit the encrypted data, we use the Base64 class to encode it as a string. The getEncoder method returns an encoder object that we can use to encode the byte array as a Base64 string using the encodeToString method.

The encryptedDataBase64 variable now holds the encrypted data as a Base64-encoded string.

Next, the code creates the digital signature using the private key and the encrypted data.

```kotlin
val signature = Signature.getInstance("SHA256withRSA")
signature.initSign(privateKey)
signature.update(encryptedData)
val signedData = signature.sign()
val signedDataBase64 = Base64.getEncoder().encodeToString(signedData)
```
The signedData variable now holds the digital signature as a byte array, and the signedDataBase64 variable holds the digital signature as a Base64-encoded string.

To send the encrypted data and the digital signature to the recipient, we concatenate the two Base64-encoded strings into a single message using the | character as a separator.
```kotlin
val message = "$encryptedDataBase64|$signedDataBase64"
println("Message: $message")
```
The message variable now holds the encrypted data and the digital signature as a single string that can be transmitted to the recipient.

When the recipient receives the message, they split it into the encrypted data and the digital signature using the split method and the | character as the separator.
```kotlin
val parts = message.split("|")
val encryptedDataReceived = Base64.getDecoder().decode(parts[0])
val signedDataReceived = Base64.getDecoder().decode(parts[1])
```
The encryptedDataReceived variable now holds the encrypted data as a byte array, and the signedDataReceived variable holds the digital signature as a byte array.

Next, the recipient verifies the digital signature using the public key of the sender.
```kotlin
signature.initVerify(publicKey)
signature.update(encryptedDataReceived)
val isValid = signature.verify(signedDataReceived)

println("Digital signature is valid: $isValid")
```

The isValid variable now holds a boolean value indicating whether the digital signature is valid or not.

Finally, the recipient decrypts the data using their private key.
```kotlin
cipher.init(Cipher.DECRYPT_MODE, privateKey)
val decryptedData = cipher.doFinal(encryptedDataReceived)
val decryptedDataString = String(decryptedData)

println("Decrypted data: $decryptedDataString")
```

The decryptedData variable now holds the decrypted data as a byte array, and the decryptedDataString variable holds the decrypted data as a string.

This is the complete code that adds data encryption using the RSA algorithm to the digital signature example in Kotlin. The RSA algorithm is used both for creating the digital signature and for encrypting and decrypting the data. The SHA-256 hash function is used for creating the digital signature.

The data is encrypted using the public key of the user who is going to sign and send the data. The recipient can then decrypt the data using their private key. The digital signature is created using the private key of the sender and verified using the public key of the sender.

This code also uses the Base64 encoding and decoding functions from the java.util.Base64 class to convert the encrypted data and the digital signature to and from string representations that can be transmitted as a single message.
