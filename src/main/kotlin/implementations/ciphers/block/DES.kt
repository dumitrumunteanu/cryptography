package implementations.ciphers.block

class DES(key: String) {
    var keyBits = IntArray(64)

    init {
        for (i in 0..15) {
            val s = StringBuilder(Integer.toBinaryString((key[i].toString() + "").toInt(16)))
            while (s.length < 4) {
                s.insert(0, "0")
            }
            for (j in 0..3) {
                keyBits[4 * i + j] = (s[j].toString() + "").toInt()
            }
        }
    }

    fun encrypt(text: String): IntArray {
        val inputBits = convertToIntArray(text)
        return permute(inputBits, keyBits, false)
    }

    fun decrypt(text: String): IntArray {
        val inputBits = convertToIntArray(text)
        return permute(inputBits, keyBits, true)
    }

    private fun convertToIntArray(text: String): IntArray {
        val inputBits = IntArray(64)
        for (i in 0..15) {
            val s = StringBuilder(Integer.toBinaryString((text[i].toString() + "").toInt(16)))
            while (s.length < 4) {
                s.insert(0, "0")
            }
            for (j in 0..3) {
                inputBits[4 * i + j] = (s[j].toString() + "").toInt()
            }
        }
        return inputBits
    }

    companion object {
        // Initial Permutation table
        private val IP = byteArrayOf(
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
        )

        // Permuted Choice 1 table
        private val PC1 = byteArrayOf(
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
        )

        // Permuted Choice 2 table
        private val PC2 = byteArrayOf(
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
        )

        // Array to store the number of rotations that are to be done on each round
        private val rotations = byteArrayOf(
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
        )

        // Expansion (aka P-box) table
        private val E = byteArrayOf(
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
        )

        // S-boxes (i.e. Substitution boxes)
        private val S = arrayOf(
            byteArrayOf(
                14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
                0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
                4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
                15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
            ), byteArrayOf(
                15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
                3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
                0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
                13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
            ), byteArrayOf(
                10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
                13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
                13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
                1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
            ), byteArrayOf(
                7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
                13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
                10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
                3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
            ), byteArrayOf(
                2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
                14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
                4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
                11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
            ), byteArrayOf(
                12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
                10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
                9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
                4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
            ), byteArrayOf(
                4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
                13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
                1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
                6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
            ), byteArrayOf(
                13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
                1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
                7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
                2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
            )
        )

        // Permutation table
        private val P = byteArrayOf(
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
        )

        // Inverse permutation
        private val FP = byteArrayOf(
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
        )
        private var C = IntArray(28)
        private var D = IntArray(28)
        private val subkey = Array(16) { IntArray(48) }
        private fun permute(inputBits: IntArray, keyBits: IntArray, isDecrypt: Boolean): IntArray {
            val newBits = IntArray(inputBits.size)
            for (i in inputBits.indices) {
                newBits[i] = inputBits[IP[i] - 1]
            }
            var L = IntArray(32)
            var R = IntArray(32)
            var i: Int
            i = 0
            while (i < 28) {
                C[i] = keyBits[PC1[i] - 1]
                i++
            }
            while (i < 56) {
                D[i - 28] = keyBits[PC1[i] - 1]
                i++
            }
            System.arraycopy(newBits, 0, L, 0, 32)
            System.arraycopy(newBits, 32, R, 0, 32)
            for (n in 0..15) {
                var newR: IntArray
                newR = if (isDecrypt) {
                    fiestel(R, subkey[15 - n])
                } else {
                    fiestel(R, keySchedule(n, keyBits))
                }
                val newL = xor(L, newR)
                L = R
                R = newL
            }
            val output = IntArray(64)
            System.arraycopy(R, 0, output, 0, 32)
            System.arraycopy(L, 0, output, 32, 32)
            val finalOutput = IntArray(64)
            i = 0
            while (i < 64) {
                finalOutput[i] = output[FP[i] - 1]
                i++
            }
            val hex = StringBuilder()
            i = 0
            while (i < 16) {
                val bin = StringBuilder()
                for (j in 0..3) {
                    bin.append(finalOutput[4 * i + j])
                }
                val decimal = bin.toString().toInt(2)
                hex.append(Integer.toHexString(decimal))
                i++
            }
            return finalOutput
        }

        private fun keySchedule(round: Int, key: IntArray): IntArray {
            val C1: IntArray
            val D1: IntArray
            val rotationTimes = rotations[round].toInt()
            C1 = leftShift(C, rotationTimes)
            D1 = leftShift(D, rotationTimes)
            val CnDn = IntArray(56)
            System.arraycopy(C1, 0, CnDn, 0, 28)
            System.arraycopy(D1, 0, CnDn, 28, 28)
            val Kn = IntArray(48)
            for (i in Kn.indices) {
                Kn[i] = CnDn[PC2[i] - 1]
            }
            subkey[round] = Kn
            C = C1
            D = D1
            return Kn
        }

        private fun fiestel(R: IntArray, roundKey: IntArray): IntArray {
            val expandedR = IntArray(48)
            for (i in 0..47) {
                expandedR[i] = R[E[i] - 1]
            }
            val temp = xor(expandedR, roundKey)
            return sBlock(temp)
        }

        private fun xor(a: IntArray, b: IntArray): IntArray {
            val answer = IntArray(a.size)
            for (i in a.indices) {
                answer[i] = a[i] xor b[i]
            }
            return answer
        }

        private fun sBlock(bits: IntArray): IntArray {
            val output = IntArray(32)
            for (i in 0..7) {
                val row = IntArray(2)
                row[0] = bits[6 * i]
                row[1] = bits[6 * i + 5]
                val sRow = row[0].toString() + "" + row[1]
                val column = IntArray(4)
                column[0] = bits[6 * i + 1]
                column[1] = bits[6 * i + 2]
                column[2] = bits[6 * i + 3]
                column[3] = bits[6 * i + 4]
                val sColumn = column[0].toString() + "" + column[1] + "" + column[2] + "" + column[3]
                val iRow = sRow.toInt(2)
                val iColumn = sColumn.toInt(2)
                val x = S[i][iRow * 16 + iColumn].toInt()
                val s = StringBuilder(Integer.toBinaryString(x))
                while (s.length < 4) {
                    s.insert(0, "0")
                }
                for (j in 0..3) {
                    output[i * 4 + j] = (s[j].toString() + "").toInt()
                }
            }
            val finalOutput = IntArray(32)
            for (i in 0..31) {
                finalOutput[i] = output[P[i] - 1]
            }
            return finalOutput
        }

        private fun leftShift(bits: IntArray, n: Int): IntArray {
            val answer = IntArray(bits.size)
            System.arraycopy(bits, 0, answer, 0, bits.size)
            for (i in 0 until n) {
                val temp = answer[0]
                System.arraycopy(answer, 1, answer, 0, bits.size - 1)
                answer[bits.size - 1] = temp
            }
            return answer
        }
    }
}