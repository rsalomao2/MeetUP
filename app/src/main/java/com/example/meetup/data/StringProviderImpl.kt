package com.example.meetup.data

import android.content.Context
import com.example.meetup.domain.StringProvider

class StringProviderImpl(private val context: Context): StringProvider {
    override fun getString(stringId: Int): String {
        return context.getString(stringId)
    }
}
