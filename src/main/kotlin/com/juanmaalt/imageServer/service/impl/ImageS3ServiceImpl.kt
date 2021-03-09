package com.juanmaalt.imageServer.service.impl

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.juanmaalt.imageServer.request.ImageRequestBody
import com.juanmaalt.imageServer.response.ImageResponseBody
import com.juanmaalt.imageServer.service.ImageService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.hateoas.Link
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.net.InetAddress
import javax.annotation.PostConstruct

@Service
@Profile("aws")
class ImageS3ServiceImpl : ImageService {

    private val logger = KotlinLogging.logger {}

    private val host = InetAddress.getLoopbackAddress().hostAddress

    @Value("\${server.port}")
    lateinit var port: String

    @Value("\${app.awsServices.bucketName}")
    private val bucketName: String = ""

    @Autowired
    private lateinit var amazonS3Client: AmazonS3

    override fun getImageWithName(name: String): ByteArray {
        logger.info { """getImageWithName received a request for $name""" }
        val file = amazonS3Client.getObject(bucketName, name)
        val s3Object = file.objectContent

        logger.info { """The '$name' file exists, reading bytes""" }
        return s3Object.readAllBytes()
    }

    override fun saveImagesOnServer(imageRequestBody: ImageRequestBody): ImageResponseBody {
        logger.info { """saveImagesOnServer received ${imageRequestBody.images.size} files to persist""" }
        val listOfPaths: MutableList<Link> = mutableListOf()
        val hostAddress = """$host:$port"""

        for (imageFile in imageRequestBody.images) {
            val imageURL = Link("""$hostAddress/images/${imageFile.originalFilename}?folder=${imageRequestBody.path}""")
            val fileToPost = convertMultipartFileToFile(imageFile)

            amazonS3Client?.putObject(bucketName, """${imageRequestBody.path}/${imageFile.originalFilename}""", fileToPost)

            logger.info { """The ${imageFile.originalFilename} was correclty saved on AWS S3 bucket""" }
            listOfPaths.add(imageURL)
        }

        logger.info { """Returning the list of URLs""" }
        return ImageResponseBody(listOfPaths.toTypedArray())
    }

    private fun convertMultipartFileToFile (multipartFile: MultipartFile): File {
        logger.info { """Starting the conversion of the ${multipartFile.originalFilename} from MultipartFile to File""" }
        val file = File(multipartFile.originalFilename)
        val fileOutputStream = FileOutputStream(file)

        fileOutputStream.write(multipartFile.bytes)
        fileOutputStream.close()

        logger.info { """Conversion for ${multipartFile.originalFilename} correctly made""" }
        return file
    }
}