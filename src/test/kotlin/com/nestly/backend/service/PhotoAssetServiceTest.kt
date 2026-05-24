package com.nestly.backend.service

import kotlin.test.Test
import kotlin.test.assertEquals

class PhotoAssetServiceTest {
    private val service = PhotoAssetService()

    @Test
    fun `returns sports photo asset`() {
        val photo = service.forCategory("Sports")

        assertEquals("bike", photo.iconKey)
        assertEquals(4294956709, photo.colorValue)
    }

    @Test
    fun `falls back to chair asset for unknown categories`() {
        val photo = service.forCategory("Garden")

        assertEquals("chair", photo.iconKey)
        assertEquals(4290241735, photo.colorValue)
    }
}
