package com.juanmaalt.imageServer.service

import com.juanmaalt.imageServer.request.ImageRequestBody
import com.juanmaalt.imageServer.response.ImageResponseBody

interface ImageService {
    fun getImageWithName(name: String): ByteArray?
    fun saveImagesOnServer(imageRequestBody: ImageRequestBody): ImageResponseBody
}
