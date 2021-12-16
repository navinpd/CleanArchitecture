package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util

sealed class NoteOrder(val orderType: OrderType) {

    class Title(orderTyp: OrderType) : NoteOrder(orderTyp)

    class Date(orderTyp: OrderType) : NoteOrder(orderTyp)

    class Color(orderTyp: OrderType) : NoteOrder(orderTyp)

    fun copy(orderTyp: OrderType): NoteOrder {
        return when (this) {
            is Title -> Title(orderTyp)
            is Date -> Date(orderTyp)
            is Color -> Color(orderTyp)
        }
    }

}