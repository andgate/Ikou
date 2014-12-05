package com.andgate.ikou.exception;

import java.io.IOException;

/**
 * Thrown to indicate that a method has been passed an illegal or inappropriate format file.
 */
public class InvalidFileFormatException extends IOException
{
    /**
     * Constructs an inappropriate with the specified detail message.
     *
     * @param message the detail message.
     */
    public InvalidFileFormatException(String message) {
        super(message);
    }
}