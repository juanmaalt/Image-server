package com.juanmaalt.imageServer.request

import org.springframework.web.multipart.MultipartFile

class ImageRequestBody(val path: String? = "", val images: Array<MultipartFile>)
