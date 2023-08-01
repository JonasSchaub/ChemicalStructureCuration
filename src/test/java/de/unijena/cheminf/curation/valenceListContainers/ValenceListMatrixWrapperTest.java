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

package de.unijena.cheminf.curation.valenceListContainers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * Test class of the ValenceListMatrixWrapper class and of classes extending it.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see ValenceListMatrixWrapper
 * @see PubChemValenceListMatrixWrapper
 */
public class ValenceListMatrixWrapperTest extends ValenceListMatrixWrapper {

    /**
     * Integer value of the number of lines contained by the test valence list text file.
     */
    public static final int NUMBER_OF_LINES_IN_TEST_FILE = 25;

    /**
     * Integer value of the highest atomic number contained by the test valence list.
     */
    public static final int HIGHEST_ATOMIC_NUMBER_IN_LIST = 8;

    /**
     * Integer value of the lowest atomic number contained by the test valence list.
     */
    public static final int LOWEST_ATOMIC_NUMBER_IN_LIST = 0;

    /**
     * String containing the file path of the test valence list text file.
     */
    public static final String TEST_VALENCE_LIST_FILE_PATH_STRING
            = "src/test/resources/de/unijena/cheminf/curation/TestValenceList.txt";

    /**
     * Constructor; calls the super passing the file path of the test valence list text file and the number of lines
     * contained by it.
     *
     * @throws IOException if a problem occurs reading the file, e.g. the file path is incorrect
     */
    public ValenceListMatrixWrapperTest() throws IOException {
        super(ValenceListMatrixWrapperTest.TEST_VALENCE_LIST_FILE_PATH_STRING,
                ValenceListMatrixWrapperTest.NUMBER_OF_LINES_IN_TEST_FILE);
    }

    @Test
    public void mainConstructorTest_correctLowestAndHighestAtomicNumber() {
        // Note: this instance was generated using the main constructor
        Assertions.assertEquals(ValenceListMatrixWrapperTest.LOWEST_ATOMIC_NUMBER_IN_LIST,
                this.getLowestAtomicNumberInList());
        Assertions.assertEquals(ValenceListMatrixWrapperTest.HIGHEST_ATOMIC_NUMBER_IN_LIST,
                this.getHighestAtomicNumberInList());
    }

    @Test
    public void mainConstructorTest_correctMatrixDimensions() {
        // Note: this instance was generated using the main constructor
        // valence list matrix
        Assertions.assertEquals(ValenceListMatrixWrapperTest.NUMBER_OF_LINES_IN_TEST_FILE - 1,
                this.valenceListMatrix.length);
        Assertions.assertEquals(ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE,
                this.valenceListMatrix[0].length);
        //valence list pointer matrix
        Assertions.assertEquals(ValenceListMatrixWrapper.HIGHEST_KNOWN_ATOMIC_NUMBER + 1,
                this.valenceListPointerMatrix.length);
        Assertions.assertEquals(2, this.valenceListPointerMatrix[0].length);
    }

    @Test
    public void secondConstructorTest_testsLineCount_testsLowestAndHighestAtomicNumber_testsMatrixDimensions()
            throws IOException {
        ValenceListMatrixWrapper tmpTestInstance
                = new ValenceListMatrixWrapper(ValenceListMatrixWrapperTest.TEST_VALENCE_LIST_FILE_PATH_STRING);
        Assertions.assertEquals(ValenceListMatrixWrapperTest.NUMBER_OF_LINES_IN_TEST_FILE - 1,
                tmpTestInstance.getLengthOfValenceList());
        Assertions.assertEquals(ValenceListMatrixWrapperTest.LOWEST_ATOMIC_NUMBER_IN_LIST,
                tmpTestInstance.getLowestAtomicNumberInList());
        Assertions.assertEquals(ValenceListMatrixWrapperTest.HIGHEST_ATOMIC_NUMBER_IN_LIST,
                tmpTestInstance.getHighestAtomicNumberInList());
    }

    //TODO

}
