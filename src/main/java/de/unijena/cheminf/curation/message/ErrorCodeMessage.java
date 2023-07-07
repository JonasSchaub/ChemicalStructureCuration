/*
 * MIT License
 *
 * Copyright (c) 2023 Samuel Behr, Felix Baensch, Jonas Schaub, Christoph Steinbeck, and Achim Zielesny
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package de.unijena.cheminf.curation.message;

import de.unijena.cheminf.curation.enums.ErrorCodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Message class for error code descriptions.
 *
 * @author Maximilian Schaten
 * @version 1.0.0.0
 */
public class ErrorCodeMessage {

    /**
     * Reads properties file return the value of the given key ErrorCode as String.
     *
     * @param anErrorCode the errorCode to return the description of
     * @return String value of the given ErrorCode
     */
    public static String getErrorMessage(ErrorCodes anErrorCode) {
        try {
            Properties properties = new Properties();
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\de\\unijena\\cheminf\\curation\\ErrorCodes.properties");//TODO
            properties.load(fileInputStream);
            String errorMessage = properties.getProperty(String.valueOf(anErrorCode));
            fileInputStream.close();
            return errorMessage;
        } catch (IOException anIOException) {
            throw new RuntimeException(anIOException);
        }
    }

}
