package com.example.mpl_base.util

class CalcUtil
{
    companion object{
//        var randomNumber: Int = rng()

        fun rng(): Int
        {
            return (1..100).random()
        }

        fun checkIfPrime(number: Int): Boolean
        {
            if (number <= 1)
            {
                return false
            }

            for (i in 2 until number)
            {
                if (number % i == 0)
                {
                    return false
                }
            }

            return true
        }
    }
}