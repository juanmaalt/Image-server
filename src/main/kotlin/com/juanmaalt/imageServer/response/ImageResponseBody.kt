package com.juanmaalt.imageServer.response

import org.springframework.hateoas.Link

data class ImageResponseBody(val imageUrls: Array<Link>)
