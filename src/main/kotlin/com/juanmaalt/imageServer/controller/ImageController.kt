package com.juanmaalt.imageServer.controller

import com.juanmaalt.imageServer.request.ImageRequestBody
import com.juanmaalt.imageServer.response.ImageResponseBody
import com.juanmaalt.imageServer.service.ImageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/images")
class ImageController {

    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var imageService: ImageService

    @GetMapping("/{imageName}", produces = [MediaType.IMAGE_JPEG_VALUE])
    fun getImage(@PathVariable(name = "imageName") name: String, @RequestParam(required = false) folder: String?): ResponseEntity<ByteArray> {
        var imagePath = folder?.let { folder.plus("/") } ?: run { "" }
        imagePath = imagePath.plus("$name")
        logger.info { """GET request received for $imagePath""" }

        return ResponseEntity(imageService.getImageWithName(imagePath), HttpStatus.OK)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun saveImages(@ModelAttribute imageRequestBody: ImageRequestBody): ResponseEntity<ImageResponseBody> {
        logger.info { """POST request received for folder: ${imageRequestBody.path}""" }
        return ResponseEntity(imageService.saveImagesOnServer(imageRequestBody), HttpStatus.CREATED)
    }
}
