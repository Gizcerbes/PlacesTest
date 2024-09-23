package com.uogames.balinasoft.test.network

import com.uogames.balinasoft.core.network.Network
import com.uogames.balinasoft.core.network.NetworkProvider
import com.uogames.balinasoft.test.network.provider.AuthProviderImpl
import com.uogames.balinasoft.test.network.provider.CommentProviderImpl
import com.uogames.balinasoft.test.network.provider.ImageProviderImpl
import com.uogames.balinasoft.test.network.service.AuthService
import com.uogames.balinasoft.test.network.service.CommentService
import com.uogames.balinasoft.test.network.service.ImageService
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json

fun Network.create(
    apiKey: () -> String? = { null }
): NetworkProvider = NetworkClient(apiKey)

class NetworkClient(
    apiKey: () -> String? = { null }
) : NetworkProvider {

    private val client: HttpClient = HttpClient(CIO) {

        install(ContentNegotiation) { json() }

        defaultRequest {
            url("https://junior.balinasoft.com/")
            apiKey()?.let {
                header("Access-Token", it)
            }

        }

    }

    override val authProvider = AuthProviderImpl(AuthService(client))

    override val commentProvider = CommentProviderImpl(CommentService(client))

    override val imageProvider = ImageProviderImpl(ImageService(client))


}