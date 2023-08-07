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
     * Integer value of the highest atomic number present in the test valence list.
     */
    public static final int HIGHEST_ATOMIC_NUMBER_IN_LIST = 8;

    /**
     * Integer value of the lowest atomic number present in the test valence list.
     */
    public static final int LOWEST_ATOMIC_NUMBER_IN_LIST = 0;

    /**
     * Integer matrix containing the atomic numbers present in the test valence list with their respective absolute
     * frequency.
     */
    public static final int[][] ALL_ATOMIC_NUMBERS_IN_LIST = {{0, 1}, {1, 4}, {6, 11}, {8, 8}};

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

    /**
     * Tests whether the valenceListMatrix has the expected dimensions (24 x 5).
     */
    @Test
    public void dimensionsOfValenceListMatrixTest_25x5() {
        Assertions.assertEquals(ValenceListMatrixWrapperTest.NUMBER_OF_LINES_IN_TEST_FILE - 1,
                this.valenceListMatrix.length);
        Assertions.assertEquals(ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE,
                this.valenceListMatrix[0].length);
        Assertions.assertEquals(ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE,
                this.valenceListMatrix[10].length);
    }

    /**
     * Tests whether the valenceListPointerMatrix has the dimensions: (highest known atomic number + 1) x 2.
     */
    @Test
    public void dimensionsOfValenceListPointerMatrixTest_119x2() {
        Assertions.assertEquals(ValenceListMatrixWrapper.HIGHEST_KNOWN_ATOMIC_NUMBER + 1,
                this.valenceListPointerMatrix.length);
        Assertions.assertEquals(2, this.valenceListPointerMatrix[0].length);
        Assertions.assertEquals(2, this.valenceListPointerMatrix[50].length);
    }

    /**
     * Tests exemplary whether the .getValenceListEntry(int, int) method of class ValenceListMatrixWrapper returns
     * the values imported from the test valence list text file correctly.
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_returnsValuesOfValenceList() {
        // line 2; index 0; first line of the valence list
        Assertions.assertEquals(0, this.getValenceListEntry(0, 0));
        Assertions.assertEquals(1, this.getValenceListEntry(0, 3));
        //
        // line 11; index 9; test of all five values;
        Assertions.assertEquals(6, this.getValenceListEntry(9, 0));
        Assertions.assertEquals(-1, this.getValenceListEntry(9, 1));
        Assertions.assertEquals(0, this.getValenceListEntry(9, 2));
        Assertions.assertEquals(3, this.getValenceListEntry(9, 3));
        Assertions.assertEquals(3, this.getValenceListEntry(9, 4));
        //
        // line 25; index 23; last value of the valence list
        Assertions.assertEquals(0, this.getValenceListEntry(23, 4));
    }

    /**
     * Tests whether the .getValenceListEntry(int, int) method of class ValenceListMatrixWrapper throws an
     * IndexOutOfBoundsException if one of the given indices has a negative value.
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_indicesBelowZero_throwsIndexOutOfBoundsException() {
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(-1, 0)
        );
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(0, -1)
        );
    }

    /**
     * Tests whether the .getValenceListEntry(int, int) method of class ValenceListMatrixWrapper throws an
     * IndexOutOfBoundsException if the first parameter equals or exceeds the length of the first dimension of the
     * valence list matrix; two tests.
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_paramOneAboveUpperBound_throwsIndexOutOfBoundsException() {
        //test 1: value equals the length
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(this.getLengthOfValenceList(), 0)
        );
        //test 2: value exceeds the length
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(this.getLengthOfValenceList() + 1, 0)
        );
    }

    /**
     * Tests whether the .getValenceListEntry(int, int) method of class ValenceListMatrixWrapper throws an
     * IndexOutOfBoundsException if the second parameter is of a value greater than 4; two tests.
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_paramTwoAboveUpperBound_throwsIndexOutOfBoundsException() {
        //test 1
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(0,
                        ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE)
        );
        //test 2
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(0,
                        ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE)
        );
    }

    /**
     * Tests exemplary whether the .getValenceListEntry(int) method of class ValenceListMatrixWrapper returns
     * the values imported from the test valence list text file correctly.
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_returnsValuesOfValenceList() {
        //values of line 2; index 0; first line of the valence list
        Assertions.assertArrayEquals(new int[] {0, 0, 0, 1, 0}, this.getValenceListEntry(0));
        //values of line 6; index 4
        Assertions.assertArrayEquals(new int[] {1, 0, 0, 1, 1}, this.getValenceListEntry(4));
        //values of line 11; index 9
        Assertions.assertArrayEquals(new int[] {6, -1, 0, 3, 3}, this.getValenceListEntry(9));
        //values of line 15; index 13
        Assertions.assertArrayEquals(new int[] {6, 0, 1, 3, 2}, this.getValenceListEntry(13));
        //values of line 25; index 23; last line of the valence list
        Assertions.assertArrayEquals(new int[] {8, 1, 2, 1, 0}, this.getValenceListEntry(23));
    }

    /**
     * Tests whether the arrays returned by the .getValenceListEntry(int) method of class ValenceListMatrixWrapper
     * are clones of the original arrays contained by the valence list matrix (-> different pointer, same values);
     * two exemplary tests.
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_returnsCloneWithSameValues_twoTests() {
        //test 1
        int tmpIndex = 10;
        Assertions.assertNotSame(this.valenceListMatrix[tmpIndex],
                this.getValenceListEntry(tmpIndex));
        Assertions.assertArrayEquals(this.valenceListMatrix[tmpIndex],
                this.getValenceListEntry(tmpIndex));
        //test 2
        tmpIndex = 15;
        Assertions.assertNotSame(this.valenceListMatrix[tmpIndex],
                this.getValenceListEntry(tmpIndex));
        Assertions.assertArrayEquals(this.valenceListMatrix[tmpIndex],
                this.getValenceListEntry(tmpIndex));
    }

    /**
     * Tests whether the .getValenceListEntry(int) method of class ValenceListMatrixWrapper throws an
     * IndexOutOfBoundsException if the given index has a negative value.
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_negativeIndex_throwsIndexOutOfBoundsException() {
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(-1)
        );
    }

    /**
     * Tests whether the .getValenceListEntry(int) method of class ValenceListMatrixWrapper throws an
     * IndexOutOfBoundsException if the index is of a value greater than or equal to the length of the first
     * dimension of the valence list matrix; two tests.
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_indexAboveUpperBound_throwsIndexOutOfBoundsException() {
        //test 1: value equals the length
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(this.getLengthOfValenceList())
        );
        //test 2: value exceeds the length
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> this.getValenceListEntry(this.getLengthOfValenceList() + 1)
        );
    }

    /**
     * Tests whether the .getLengthOfValenceList() method of class ValenceListMatrixWrapper returns the length of the
     * first dimension of the valence list matrix which equals the number of lines in the PubChem valence list text
     * file minus 1.
     */
    @Test
    public void getLengthOfValenceListMethodTest_returnsLengthOfValenceListMatrix_24() {
        int tmpReturnedValue = this.getLengthOfValenceList();
        Assertions.assertEquals(this.valenceListMatrix.length, tmpReturnedValue);
        Assertions.assertEquals(ValenceListMatrixWrapperTest.NUMBER_OF_LINES_IN_TEST_FILE - 1, tmpReturnedValue);
    }

    /**
     * Tests whether the values returned by the .getValenceListElementPointer() method for atomic numbers that are
     * present in the imported valence list each point to the first entry with the respective atomic number in the
     * valence list matrix if used as index for the first matrix dimension (via the .getValenceListEntry() method).
     */
    @Test
    public void getValenceListElementPointerMethodTest_returnedPointersPointAtFirstListEntryWithTheSpecificAtomicNumber() {
        int tmpPointedIndex;
        int tmpPointedAtAtomicNumber;
        int tmpAtomicNumber;
        for (int i = 0; i < ValenceListMatrixWrapperTest.ALL_ATOMIC_NUMBERS_IN_LIST.length; i++) {
            tmpAtomicNumber = ValenceListMatrixWrapperTest.ALL_ATOMIC_NUMBERS_IN_LIST[i][0];
            tmpPointedIndex = this.getValenceListElementPointer(tmpAtomicNumber);
            tmpPointedAtAtomicNumber = this.getValenceListEntry(tmpPointedIndex, 0);
            Assertions.assertEquals(tmpAtomicNumber, tmpPointedAtAtomicNumber);
            if (tmpPointedIndex != 0) {
                Assertions.assertTrue(this.getValenceListEntry(tmpPointedIndex - 1,
                        0) < tmpPointedAtAtomicNumber);
            }
        }
    }

    /**
     * Tests whether the .getValenceListElementPointer() method of class ValenceListMatrixWrapper returns the
     * DEFAULT_POINTER_VALUE for atomic numbers not present in the valence list.
     */
    @Test
    public void getValenceListElementPointerMethodTest_returnsDefaultPointerValueIfAtomicNumberIsNotInList() {
        int tmpAtomicNumbersInListIndex = 0;
        for (int i = 0; i <= ValenceListMatrixWrapper.HIGHEST_KNOWN_ATOMIC_NUMBER + 1; i++) {
            if (i <= this.getHighestAtomicNumberInList()) {
                if (i == ValenceListMatrixWrapperTest.ALL_ATOMIC_NUMBERS_IN_LIST[tmpAtomicNumbersInListIndex][0]) {
                    tmpAtomicNumbersInListIndex++;
                    continue;
                }
            }
            Assertions.assertEquals(ValenceListMatrixWrapper.DEFAULT_POINTER_VALUE,
                    this.getValenceListElementPointer(i));
        }
    }

    /**
     * Tests whether the .getAtomConfigurationsCountOfElement() method of class ValenceListMatrixWrapper returns the
     * correct count of entries in the valence list that regard to the specific chemical element for all atomic numbers;
     * testing for all elements (i.e. elements present and not present in the test valence list).
     */
    @Test
    public void getAtomConfigurationsCountOfElementMethodTest_allAtomicNumbersInList_returnsCorrectCount() {
        int tmpAtomicNumbersInListIndex = 0;
        for (int i = 0; i <= ValenceListMatrixWrapper.HIGHEST_KNOWN_ATOMIC_NUMBER + 1; i++) {
            if (this.getValenceListElementPointer(i) == ValenceListMatrixWrapper.DEFAULT_POINTER_VALUE) {
                Assertions.assertEquals(0, this.getAtomConfigurationsCountOfElement(i));
            } else {
                Assertions.assertEquals(
                        ValenceListMatrixWrapperTest.ALL_ATOMIC_NUMBERS_IN_LIST[tmpAtomicNumbersInListIndex][1],
                        this.getAtomConfigurationsCountOfElement(i)
                );
                tmpAtomicNumbersInListIndex++;
            }
        }
    }

    /**
     * Tests whether the .getLowestAtomicNumberInList() method of class ValenceListMatrixWrapper returns the lowest
     * atomic number present in the imported valence list text file.
     */
    @Test
    public void getLowestAtomicNumberInListMethodTest_returnsHighestAtomicNumberInList() {
        Assertions.assertEquals(ValenceListMatrixWrapperTest.LOWEST_ATOMIC_NUMBER_IN_LIST,
                this.getLowestAtomicNumberInList());
    }

    /**
     * Tests whether the .getHighestAtomicNumberInList() method of class ValenceListMatrixWrapper returns the highest
     * atomic number present in the imported valence list text file.
     */
    @Test
    public void getHighestAtomicNumberInListMethodTest_returnsHighestAtomicNumberInList() {
        Assertions.assertEquals(ValenceListMatrixWrapperTest.HIGHEST_ATOMIC_NUMBER_IN_LIST,
                this.getHighestAtomicNumberInList());
    }

}
