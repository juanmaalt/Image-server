package com.juanmaalt.imageServer.service.impl

import com.juanmaalt.imageServer.request.ImageRequestBody
import com.juanmaalt.imageServer.response.ImageResponseBody
import com.juanmaalt.imageServer.service.ImageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.hateoas.Link
import org.springframework.stereotype.Service
import java.io.File
import java.lang.Exception
import java.net.InetAddress

@Service
@Profile("local")
class ImageServiceImpl : ImageService {

    private val logger = KotlinLogging.logger {}

    @Value("\${application.image.path}")
    lateinit var imagePath: String

    private val host = InetAddress.getLoopbackAddress().hostAddress

    @Value("\${server.port}")
    lateinit var port: String

    override fun getImageWithName(name: String): ByteArray? {
        logger.info { """getImageWithName received a request for $name""" }
        val file = File("""$imagePath$name""")

        if (!file.exists()) {
            logger.error { """There is no match for $name""" }
            throw Exception("The requested file does not exists")
        }

        logger.info { """The '$name' file exists, reading bytes""" }
        return file?.readBytes()
    }

    override fun saveImagesOnServer(imageRequestBody: ImageRequestBody): ImageResponseBody {
        logger.info { """saveImagesOnServer received ${imageRequestBody.images.size} files to persist""" }
        val listOfPaths: MutableList<Link> = mutableListOf()
        val hostAddress = """$host:$port"""
        var path = imagePath

        if (imageRequestBody.path != "") {
            path = """$path${imageRequestBody.path}/"""
        }

        val directory = File(path)

        if (!directory.exists()) {
            logger.error { """The $path directory does not exists """ }
            directory.mkdir()
        }

        for (imageFile in imageRequestBody.images) {
            val imageURL = Link("""$hostAddress/images/${imageFile.originalFilename}?folder=${imageRequestBody.path}""")
            val filePath = """$path${imageFile.originalFilename}"""
            val file = File(filePath)

            imageFile.transferTo(file)
            logger.info { """The ${imageFile.originalFilename} was correclty saved on the file system""" }
            listOfPaths.add(imageURL)
        }

        logger.info { """Returning the list of URLs""" }
        return ImageResponseBody(listOfPaths.toTypedArray())
    }
}
