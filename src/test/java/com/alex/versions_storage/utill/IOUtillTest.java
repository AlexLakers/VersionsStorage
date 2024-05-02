package com.alex.versions_storage.utill;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;

@ExtendWith(MockitoExtension.class)
class IOutillTest {

    @Test
    void readBytes_shouldReturnBytesArrayFromInputStream_whenInputStreamIsValid() throws IOException {
        byte[] expected = new byte[]{10, 99, 13, 11, 12, 111, 2};
        ByteArrayInputStream bis = new ByteArrayInputStream(expected);

        byte[] actual = IOUtill.readBytes(bis, 1024);
        bis.close();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void writeBytes_shouldWriteBytesArrayToOutputStream_whenOutputStreamIsValid() throws IOException {
        byte[] expected = new byte[]{10, 99, 13, 11, 12, 111, 2};
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        IOUtill.writeBytes(expected, bos);
        byte[] actual = bos.toByteArray();

        Assertions.assertArrayEquals(expected, actual);
    }
}