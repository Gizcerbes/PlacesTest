package com.uogames.balinasoft.test.network.map

interface MapperModel<F, S> {


    fun F.toModel(): S

}