package com.vanclykin.bulletinboard.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.util.*

object CityHelper {
    fun getAllCountries(context: Context): ArrayList<String> {
        var tempArray = ArrayList<String>()
        try {

            val inputStream: InputStream =
                context.assets.open("countriesToCities.json") //создаем инпутстрим и указываем из какого файла берем
            val size: Int = inputStream.available() //узнаем размер списка
            val bytesArray = ByteArray(size) // создаем массив для помещения считаных байтов
            inputStream.read(bytesArray) // считываем и помещаем в массив
            val jsonFile = String(bytesArray) // после этот массив превращаем в из байт в стринг
            val jsonObject = JSONObject(jsonFile) //специальный класс для превращения в json обьект
            val countryNames = jsonObject.names() //Получить названия стран
            if (countryNames != null) {
                for (n in 0 until countryNames.length()) {
                    tempArray.add(countryNames.getString(n))
                }
            }

        } catch (e: IOException) {

        }
        return tempArray
    }

    fun getAllCities(country:String ,context: Context): ArrayList<String> {
        var tempArray = ArrayList<String>()
        try {

            val inputStream: InputStream =
                context.assets.open("countriesToCities.json") //создаем инпутстрим и указываем из какого файла берем
            val size: Int = inputStream.available() //узнаем размер списка
            val bytesArray = ByteArray(size) // создаем массив для помещения считаных байтов
            inputStream.read(bytesArray) // считываем и помещаем в массив
            val jsonFile = String(bytesArray) // после этот массив превращаем в из байт в стринг
            val jsonObject = JSONObject(jsonFile) //специальный класс для превращения в json обьект
            val cityNames = jsonObject.getJSONArray(country) //Получить города

                for (n in 0 until cityNames.length()) {
                    tempArray.add(cityNames.getString(n))
                }

        } catch (e: IOException) {

        }
        return tempArray
    }

    fun filterListData(list: ArrayList<String>, searchText: String?): ArrayList<String> {
        val tempList = ArrayList<String>()
        tempList.clear()
        if (searchText == null) {
            tempList.add("нет результата")                                                             //допилить через стринг
            return tempList
        }
        for (selection: String in list) {
            if (selection.lowercase(Locale.ROOT).startsWith(searchText.lowercase(Locale.ROOT)))
                tempList.add(selection)
        }
        if (tempList.size == 0) tempList.add("нет результата")                                          //допилить через стринг
        return tempList
    }

}