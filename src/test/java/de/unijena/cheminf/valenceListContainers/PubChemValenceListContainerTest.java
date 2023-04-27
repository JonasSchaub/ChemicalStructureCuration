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

package de.unijena.cheminf.valenceListContainers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.IOException;

/**
 * Test class of class PubChemValenceListContainer.
 *
 * @see PubChemValenceListContainer
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PubChemValenceListContainerTest {

    /*
    TODO: add references to the PubChem valence list?
    TODO: test the constructor? (all the different reasons to throw an IOException)
     */

    /**
     * Tests whether the initial call of the .getInstance() method of class PubChemValenceListContainer initializes
     * the static class variable instance with an instance of PubChemValenceListContainer and returns it.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    @Order(1)
    public void getInstanceMethodTest_initializesStaticVarInstanceOnFirstCallAndReturnsIt() throws IOException {
        Assertions.assertNull(PubChemValenceListContainer.instance);
        Object tmpReturnedInstance = PubChemValenceListContainer.getInstance();
        Assertions.assertNotNull(PubChemValenceListContainer.instance);
        Assertions.assertSame(PubChemValenceListContainer.instance, tmpReturnedInstance);
        Assertions.assertInstanceOf(PubChemValenceListContainer.class, tmpReturnedInstance);
    }

    /**
     * Tests whether the second call of the .getInstance() method of class PubChemValenceListContainer returns the same
     * instance of PubChemValenceListContainer as the first call.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getInstanceMethodTest_secondCallReturnsSameInstanceAsFirstCall() throws IOException {
        Assertions.assertSame(
                PubChemValenceListContainer.getInstance(),
                PubChemValenceListContainer.getInstance()
        );
    }

    /**
     * Tests whether the valenceListMatrix of the PubChemValenceListContainer instance returned by the .getInstance()
     * method of class PubChemValenceListContainer has the same dimensions as the imported valence list (981 x 5).
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getInstanceMethodTest_valenceListMatrixOfReturnedInstanceHasExpectedDimensions_981x5() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertEquals(PubChemValenceListContainer.NUMBER_OF_ROWS_IN_FILE - 1,
                tmpValenceListContainer.valenceListMatrix.length);
        Assertions.assertEquals(BaseValenceListContainer.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE,
                tmpValenceListContainer.valenceListMatrix[0].length);
    }

    /**
     * Tests whether the valenceListPointerMatrix of the PubChemValenceListContainer instance returned by the
     * .getInstance() method of class PubChemValenceListContainer has the dimensions: highest atomic number in the
     * valence list x 2.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getInstanceMethodTest_valenceListPointerMatrixOfReturnedInstanceHasExpectedDimensions_112x2() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertEquals(PubChemValenceListContainer.HIGHEST_ATOMIC_NUMBER_IN_LIST,
                tmpValenceListContainer.valenceListPointerMatrix.length);
        Assertions.assertEquals(2, tmpValenceListContainer.valenceListPointerMatrix[0].length);
    }

    /**
     * Tests exemplary whether the .getValenceListEntry(int, int) method of class PubChemValenceListContainer returns
     * the values imported from the PubChem valence list text file and whether these where imported correctly.
     * TODO: reference?
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_returnsValuesOfPubChemValenceList() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        //value 1 of line 2; indices 0, 0; first value of the valence list
        Assertions.assertEquals(1, tmpValenceListContainer.getValenceListEntry(0, 0));
        //value 2 of line 2; indices 0, 1;
        Assertions.assertEquals(1, tmpValenceListContainer.getValenceListEntry(0, 1));
        //value 3 of line 2; indices 0, 2;
        Assertions.assertEquals(0, tmpValenceListContainer.getValenceListEntry(0, 2));
        //value 1 of line 42; indices 40, 0;
        Assertions.assertEquals(7, tmpValenceListContainer.getValenceListEntry(40, 0));
        //value 4 of line 42; indices 40, 3;
        Assertions.assertEquals(3, tmpValenceListContainer.getValenceListEntry(40, 3));
        //value 5 of line 982; indices 980, 4; last value of the valence list
        Assertions.assertEquals(0, tmpValenceListContainer.getValenceListEntry(980, 4));
    }

    /**
     * Tests whether the .getValenceListEntry(int, int) method of class PubChemValenceListContainer throws an
     * IllegalArgumentException if one of the given parameters is of a negative value.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_paramValuesLessThanZero_throwsIllegalArgumentException() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> tmpValenceListContainer.getValenceListEntry(-1, 0)
        );
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> tmpValenceListContainer.getValenceListEntry(0, -1)
        );
    }

    /**
     * Tests whether the .getValenceListEntry(int, int) method of class PubChemValenceListContainer throws an
     * IllegalArgumentException if the first parameter is of a value greater than or equal to the length of the first
     * dimension of the valence list matrix; two tests.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_param1OutOfBounds_throwsIllegalArgumentException() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        //test 1: value equals the length
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> tmpValenceListContainer
                        .getValenceListEntry(tmpValenceListContainer.getLengthOfValenceList(), 0)
        );
        //test 2: value exceeds the length
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> tmpValenceListContainer
                        .getValenceListEntry(tmpValenceListContainer.getLengthOfValenceList() + 1, 0)
        );
    }

    /**
     * Tests whether the .getValenceListEntry(int, int) method of class PubChemValenceListContainer throws an
     * IllegalArgumentException if the second parameter is of a value greater than 4; two tests.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_param2OutOfBounds_throwsIllegalArgumentException() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        //test 1
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> tmpValenceListContainer
                        .getValenceListEntry(0, BaseValenceListContainer.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE)
        );
        //test 2
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> tmpValenceListContainer
                        .getValenceListEntry(0, BaseValenceListContainer.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE)
        );
    }

    /**
     * Tests exemplary whether the .getValenceListEntry(int) method of class PubChemValenceListContainer returns
     * the values imported from the PubChem valence list text file and whether these where imported correctly.
     * TODO: reference?
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_returnsValuesOfPubChemValenceList() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        //values of line 2; index 0; first line of the valence list
        Assertions.assertArrayEquals(new int[] {1, 1, 0, 0, 0}, tmpValenceListContainer.getValenceListEntry(0));
        //values of line 6; index 4
        Assertions.assertArrayEquals(new int[] {2, 0, 0, 0, 0}, tmpValenceListContainer.getValenceListEntry(4));
        //values of line 32; index 30
        Assertions.assertArrayEquals(new int[] {6, -1, 0, 3, 3}, tmpValenceListContainer.getValenceListEntry(30));
        //values of line 36; index 34
        Assertions.assertArrayEquals(new int[] {6, 0, 1, 3, 2}, tmpValenceListContainer.getValenceListEntry(34));
        //values of line 982; index 980; last line of the valence list
        Assertions.assertArrayEquals(new int[] {112, 0, 0, 0, 0}, tmpValenceListContainer.getValenceListEntry(980));
    }

    /**
     * Tests whether an array returned by the .getValenceListEntry(int) method of class PubChemValenceListContainer
     * is a clone of the original array contained by the valence list matrix (-> different pointer, same values);
     * two exemplary tests.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_returnsCloneWithSameValues_twoTests() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        //test 1
        int tmpIndex = 10;
        Assertions.assertNotSame(tmpValenceListContainer.valenceListMatrix[tmpIndex],
                tmpValenceListContainer.getValenceListEntry(tmpIndex));
        Assertions.assertArrayEquals(tmpValenceListContainer.valenceListMatrix[tmpIndex],
                tmpValenceListContainer.getValenceListEntry(tmpIndex));
        //test 2
        tmpIndex = 127;
        Assertions.assertNotSame(tmpValenceListContainer.valenceListMatrix[tmpIndex],
                tmpValenceListContainer.getValenceListEntry(tmpIndex));
        Assertions.assertArrayEquals(tmpValenceListContainer.valenceListMatrix[tmpIndex],
                tmpValenceListContainer.getValenceListEntry(tmpIndex));
    }

    /**
     * Tests whether the .getValenceListEntry(int) method of class PubChemValenceListContainer throws an
     * IllegalArgumentException if the given parameter is of a negative value.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_param1IsNegative_throwsIllegalArgumentException() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    tmpValenceListContainer.getValenceListEntry(-1);
                }
        );
    }

    /**
     * Tests whether the .getValenceListEntry(int) method of class PubChemValenceListContainer throws an
     * IllegalArgumentException if the parameter is of a value greater than or equal to the length of the first
     * dimension of the valence list matrix; two tests.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_param1OutOfBounds_throwsIllegalArgumentException() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        //test 1: value equals the length
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> tmpValenceListContainer.getValenceListEntry(tmpValenceListContainer.getLengthOfValenceList())
        );
        //test 2: value exceeds the length
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> tmpValenceListContainer.getValenceListEntry(tmpValenceListContainer.getLengthOfValenceList() + 1)
        );
    }

    /**
     * Tests whether the .getLengthOfValenceList() method of class PubChemValenceListContainer returns the length of
     * the first dimension of the valence list matrix which equals the number of lines in the PubChem valence list text
     * file minus 1.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getLengthOfValenceListMethodTest_returnsLengthOfValenceListMatrix_981() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        int tmpReturnedValue = tmpValenceListContainer.getLengthOfValenceList();
        Assertions.assertEquals(tmpValenceListContainer.valenceListMatrix.length, tmpReturnedValue);
        Assertions.assertEquals(PubChemValenceListContainer.NUMBER_OF_ROWS_IN_FILE - 1, tmpReturnedValue);
    }

    /**
     * Tests whether the .getValenceListElementPointer() method of class PubChemValenceListContainer returns pointers
     * that each point at an entry in the list with the specific atomic number.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListElementPointerMethodTest_returnedPointersPointAtListEntryWithTheSpecificAtomicNumber() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertEquals(112, tmpValenceListContainer.getHighestAtomicNumberInList());
        int tmpPointedIndex;
        for (int tmpAtomicNumber = 1;
             tmpAtomicNumber <= tmpValenceListContainer.getHighestAtomicNumberInList();
             tmpAtomicNumber++) {
            tmpPointedIndex = tmpValenceListContainer.getValenceListElementPointer(tmpAtomicNumber);
            Assertions.assertEquals(tmpAtomicNumber,
                    tmpValenceListContainer.getValenceListEntry(tmpPointedIndex, 0));
        }
    }

    /**
     * Tests exemplary whether the .getValenceListElementPointer() method of class PubChemValenceListContainer returns
     * pointers that each point at the first entry in the list that contains the specific atomic number.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListElementPointerMethodTest_returnedPointersPointAtFirstListEntryWithTheSpecificAtomicNumber() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        int tmpPointedIndex;
        //
        int tmpAtomicNumber = 2;
        tmpPointedIndex = tmpValenceListContainer.getValenceListElementPointer(tmpAtomicNumber);
        Assertions.assertEquals(tmpAtomicNumber,
                tmpValenceListContainer.getValenceListEntry(tmpPointedIndex, 0));
        Assertions.assertNotEquals(tmpAtomicNumber,
                tmpValenceListContainer.getValenceListEntry(tmpPointedIndex - 1, 0));
        //
        tmpAtomicNumber = 16;
        tmpPointedIndex = tmpValenceListContainer.getValenceListElementPointer(tmpAtomicNumber);
        Assertions.assertEquals(tmpAtomicNumber,
                tmpValenceListContainer.getValenceListEntry(tmpPointedIndex, 0));
        Assertions.assertNotEquals(tmpAtomicNumber,
                tmpValenceListContainer.getValenceListEntry(tmpPointedIndex - 1, 0));
    }

    /**
     * Tests whether the .getValenceListElementPointer() method of class PubChemValenceListContainer returns minus one
     * if the given atomic number is not present in the valence list.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListElementPointerMethodTest_atomicNumberNotInList_returnsMinusOne() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        //
        int tmpAtomicNumber = 0;
        Assertions.assertEquals(-1, tmpValenceListContainer.getValenceListElementPointer(tmpAtomicNumber));
        //
        tmpAtomicNumber = -1;
        Assertions.assertEquals(-1, tmpValenceListContainer.getValenceListElementPointer(tmpAtomicNumber));
        //
        tmpAtomicNumber = 113;
        Assertions.assertEquals(-1, tmpValenceListContainer.getValenceListElementPointer(tmpAtomicNumber));
    }

    /**
     * Tests whether the .getAtomConfigurationsCountOfElement() method of class PubChemValenceListContainer returns the
     * count of entries in the valence list that regard to the specific chemical element for atomic numbers that are
     * present in the valence list; this test uses the fact that the entries in the PubChem valence list are sorted
     * according to their atomic number.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getAtomConfigurationsCountOfElementMethodTest_allAtomicNumbersInList_returnsCorrectCount() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertEquals(112, tmpValenceListContainer.getHighestAtomicNumberInList());
        int tmpPointedIndex;
        int tmpElementRegardingEntriesCount;
        for (int tmpAtomicNumber = 1;
             tmpAtomicNumber <= tmpValenceListContainer.getHighestAtomicNumberInList();
             tmpAtomicNumber++) {
            tmpPointedIndex = tmpValenceListContainer.getValenceListElementPointer(tmpAtomicNumber);
            tmpElementRegardingEntriesCount = 0;
            while (tmpValenceListContainer.getValenceListEntry(tmpPointedIndex + tmpElementRegardingEntriesCount, 0)
                    == tmpAtomicNumber) {
                tmpElementRegardingEntriesCount++;
                if (tmpPointedIndex + tmpElementRegardingEntriesCount >= tmpValenceListContainer.getLengthOfValenceList()) {
                    break;
                }
            }
            Assertions.assertEquals(
                    tmpElementRegardingEntriesCount,
                    tmpValenceListContainer.getAtomConfigurationsCountOfElement(tmpAtomicNumber)
            );
        }
    }

    /**
     * Tests whether the .getAtomConfigurationsCountOfElement() method of class PubChemValenceListContainer returns
     * zero if the given atomic number is not present in the valence list.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getAtomConfigurationsCountOfElementMethodTest_atomicNumberNotInList_returnsZero() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        //
        int tmpAtomicNumber = 0;
        Assertions.assertEquals(0, tmpValenceListContainer.getAtomConfigurationsCountOfElement(tmpAtomicNumber));
        //
        tmpAtomicNumber = -1;
        Assertions.assertEquals(0, tmpValenceListContainer.getAtomConfigurationsCountOfElement(tmpAtomicNumber));
        //
        tmpAtomicNumber = 113;
        Assertions.assertEquals(0, tmpValenceListContainer.getAtomConfigurationsCountOfElement(tmpAtomicNumber));
    }

    /**
     * Tests whether the .getHighestAtomicNumberInList() method of class PubChemValenceListContainer returns the highest
     * atomic number present in the PubChem valence list text file which also equals the length of the first dimension
     * of the valence list pointer matrix.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getHighestAtomicNumberInListMethodTest_returnsHighestAtomicNumberInTheValenceList_112() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertEquals(PubChemValenceListContainer.HIGHEST_ATOMIC_NUMBER_IN_LIST,
                tmpValenceListContainer.getHighestAtomicNumberInList());
        Assertions.assertEquals(tmpValenceListContainer.valenceListPointerMatrix.length,
                tmpValenceListContainer.getHighestAtomicNumberInList());
    }

}
