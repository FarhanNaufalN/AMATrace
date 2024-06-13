package com.example.amatrace.pages.adapter

import com.example.core.data.source.remote.response.ForecastNextMonth
import com.example.core.data.source.remote.response.rawProductUsageMonthly
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ForecastNextMonthAdapter : JsonDeserializer<ForecastNextMonth> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ForecastNextMonth {
        return if (json.isJsonArray) {


            val listType = object : TypeToken<List<rawProductUsageMonthly>>() {}.type
            val list: List<rawProductUsageMonthly> = context.deserialize(json, listType)
            ForecastNextMonth.ForecastList(list)
        } else {
            ForecastNextMonth.InsufficientData(json.asString)
        }
    }
}
