package com.juanmaalt.imageServer.service.impl

import com.juanmaalt.imageServer.exceptions.ResourceNotFoundException
import com.juanmaalt.imageServer.exceptions.InternalServerErrorException
import com.juanmaalt.imageServer.request.ImageRequestBody
import com.juanmaalt.imageServer.response.ImageResponseBody
import com.juanmaalt.imageServer.service.ImageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.hateoas.Link
import org.springframework.stereotype.Service
import java.io.File
import kotlin.jvm.Throws

@Service
@Profile("local")
class ImageServiceImpl : ImageService {

    private val logger = KotlinLogging.logger {}

    @Value("\${application.image.path}")
    lateinit var imagePath: String

    @Throws(ResourceNotFoundException::class)
    override fun getImageWithName(name: String): ByteArray? {
        logger.info { """getImageWithName received a request for $name""" }
        val file = File("""$imagePath$name""")

        if (!file.exists()) {
            logger.error { """There is no match for $name""" }
            throw ResourceNotFoundException("The requested file does not exists")
        }

        logger.info { """The '$name' file exists, reading bytes""" }
        return file.readBytes()
    }

    @Throws(ResourceNotFoundException::class)
    override fun deleteImageWithName(name: String): String {
        logger.info { """deteleImageWithName received a request for $name""" }
        val file = File("""$imagePath$name""")

        if (!file.exists()) {
            logger.error { """There is no match for $name""" }
            throw ResourceNotFoundException("The requested file does not exists")
        }

        logger.info { """The '$name' file exists, deleting...""" }
        file.delete()

        return "The image was successfully deleted"
    }

    @Throws(InternalServerErrorException::class)
    override fun saveImagesOnServer(imageRequestBody: ImageRequestBody): ImageResponseBody {
        logger.info { """saveImagesOnServer received ${imageRequestBody.images.size} files to persist""" }
        val listOfPaths: MutableList<Link> = mutableListOf()
        var path = imagePath

        if (imageRequestBody.path != "") {
            path = """$path${imageRequestBody.path}/"""
        }

        val directory = File(path)

        if (!directory.exists()) {
            logger.warn { """The $path directory does not exists, it was created""" }
            directory.mkdir()
        }

        for (imageFile in imageRequestBody.images) {
            val imageURL = Link("""/images/${imageFile.originalFilename}?folder=${imageRequestBody.path}""")
            val filePath = """$path${imageFile.originalFilename}"""
            val file = File(filePath)

            try {
                imageFile.transferTo(file)
            } catch (ex: Exception) {
                logger.error { """Error trying to save the ${imageFile.originalFilename} image into the file system""" }
                throw InternalServerErrorException(ex.message.toString())
            }

            logger.info { """The ${imageFile.originalFilename} was correclty saved on the file system""" }
            listOfPaths.add(imageURL)
        }

        logger.info { """Returning the list of URLs""" }
        return ImageResponseBody(listOfPaths.toTypedArray())
    }
}
