/*
     This file is part of Ikou.
     Ikou is free software: you can redistribute it and/or modify
     it under the terms of the GNU General Public License as published by
     the Free Software Foundation, either version 2 of the License.
     Ikou is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU General Public License for more details.
     You should have received a copy of the GNU General Public License
     along with Ikou.  If not, see <http://www.gnu.org/licenses/>.
 */

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