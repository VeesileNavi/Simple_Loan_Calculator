package com.blvckrelig.kotlinsandbox

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * Declare SeekBars
         **/
        val sumSeekBar = findViewById<SeekBar>(R.id.sumSeekBar)
        val termsSeekBar = findViewById<SeekBar>(R.id.termsSeekBar)
        val percentSeekBar = findViewById<SeekBar>(R.id.percentSeekBar)

        /**
         Declare TextViews (titles)
         **/
        val resultTitle = findViewById<TextView>(R.id.resultTitle)
        val overpaymentTitle = findViewById<TextView>(R.id.overpaymentTitle)
        val sumTitle = findViewById<TextView>(R.id.sumTitle)
        val termsTitle = findViewById<TextView>(R.id.termsTitle)
        val percentTitle = findViewById<TextView>(R.id.percentTitle)

        resultTitle.text = "0₽"
        overpaymentTitle.text = "0₽"
        sumTitle.text = "0₽"
        termsTitle.text = "0м."
        percentTitle.text = "0%"

        /**
         Declare variables
         **/
        var result: Int
        result = 0
        var sum: Int
        sum = 0
        var terms: Int
        terms = 0
        var percent: Float
        percent = 0.0f

        /**
         * Declare listeners for SeekBars
         */

        sumSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{

            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sum = progress*10000
                sumTitle.text = "$sum₽"
                result = calculateMonthPayment(sum, terms, percent)
                resultTitle.text = formatMoney(result)
                overpaymentTitle.text = formatMoney(calculateOverpayment(terms, result, sum))
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //Need for correct working
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //Need for correct working
            }

        })

        termsSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                terms = progress
                termsTitle.text = formatMonth(terms)
                result = calculateMonthPayment(sum, terms, percent)
                resultTitle.text = formatMoney(result)
                overpaymentTitle.text = formatMoney(calculateOverpayment(terms, result, sum))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //Need for correct working
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //Need for correct working
            }

        })

        percentSeekBar.setOnSeekBarChangeListener(object  : SeekBar.OnSeekBarChangeListener{
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                percent = progress*0.1f
                percentTitle.text = String.format("%.1f", percent) + "%"
                result = calculateMonthPayment(sum, terms, percent)
                resultTitle.text = formatMoney(result)
                overpaymentTitle.text = formatMoney(calculateOverpayment(terms, result, sum))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //Need for correct working
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //Need for correct working
            }

        })


    }
//Платеж=Кредит*(Проц+(Проц/(1+Проц)*Мес-1)),
    fun calculateMonthPayment(sum : Int, terms: Int, percent: Float): Int{

    var result: Float = sum * (percent / (100 * 12) / (1 - (1 + percent / (100 * 12)).pow(-terms + 1)))
    if (result < 0 || result.isNaN()) {
        result = 0.0f
    }
    return result.toInt()
}

    fun formatMoney(money: Int): String{
        return "$money₽"
    }

    fun calculateOverpayment(terms: Int, result: Int, sum: Int) : Int{
        var overpayment: Int = terms*result-sum
        if(overpayment < 0){
            overpayment = 0
        }
        return overpayment
    }

    fun formatMonth(terms: Int):String{
        return if(terms>=12) {
            if (terms % 12 == 0) {
                "${terms / 12}г."
            } else{
                "${terms/12}г. ${terms%12}м."
            }
        } else{
            "${terms}м."
        }
    }

}