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
    TODO
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
        Assertions.assertEquals(PubChemValenceListContainer.NUMBER_OF_ROWS_IN_FILE - 1,
                tmpValenceListContainer.valenceListPointerMatrix.length);
        Assertions.assertEquals(BaseValenceListContainer.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE,
                tmpValenceListContainer.valenceListPointerMatrix[0].length);
    }

    /**
     * TODO
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_twoParams_() throws IOException {
        //TODO
    }

    /**
     * Tests exemplary whether the .getValenceListEntry(int) method of the PubChemValenceListContainer class instance
     * returns the imported values of the PubChem valence list text file.
     * TODO: reference
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_returnsValuesOfPubChemValenceList() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertArrayEquals(new int[] {1, 1, 0, 0, 0}, tmpValenceListContainer.getValenceListEntry(0));
        Assertions.assertArrayEquals(new int[] {2, 0, 0, 0, 0}, tmpValenceListContainer.getValenceListEntry(4));
        Assertions.assertArrayEquals(new int[] {6, -1, 0, 3, 3}, tmpValenceListContainer.getValenceListEntry(30));
        Assertions.assertArrayEquals(new int[] {6, 0, 1, 3, 2}, tmpValenceListContainer.getValenceListEntry(34));
        Assertions.assertArrayEquals(new int[] {112, 0, 0, 0, 0}, tmpValenceListContainer.getValenceListEntry(980));
    }

    //TODO: changes on the returned array do not affect the original -> different pointers

    /**
     * TODO
     * Tests exemplary whether the .getValenceListEntry(int) method of the PubChemValenceListContainer class instance
     * returns the imported values of the PubChem valence list text file.
     * TODO: reference
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_throwsIllegalArgumentException_ifParam1ExceedsListSize() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    tmpValenceListContainer.getValenceListEntry(981);
                }
        );
    }

    /**
     * TODO
     * Tests exemplary whether the .getValenceListEntry(int) method of the PubChemValenceListContainer class instance
     * returns the imported values of the PubChem valence list text file.
     * TODO: reference
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    public void getValenceListEntryMethodTest_oneParam_throwsIllegalArgumentException_ifParam1IsNegative() throws IOException {
        BaseValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> {
                    tmpValenceListContainer.getValenceListEntry(-1);
                }
        );
    }

    /**
     * TODO
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
     * TODO
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
