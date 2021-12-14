package com.plcoding.cleanarchitecturenoteapp.feature_note.domain.util

sealed class NoteOrder(val orderTyp: OrderType) {

    class Title(orderTyp: OrderType) : NoteOrder(orderTyp)

    class Date(orderTyp: OrderType) : NoteOrder(orderTyp)

    class Color(orderTyp: OrderType) : NoteOrder(orderTyp)

}