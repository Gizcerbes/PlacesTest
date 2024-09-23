package com.uogames.balinasoft.test.network.map

interface MapperDTO<F, S> {

    fun F.toDTO(): S

}