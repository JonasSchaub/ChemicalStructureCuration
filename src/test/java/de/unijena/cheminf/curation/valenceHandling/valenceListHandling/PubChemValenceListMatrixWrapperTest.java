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

package de.unijena.cheminf.curation.valenceHandling.valenceListHandling;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Test class of the PubChemValenceListMatrixWrapper class.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see PubChemValenceListMatrixWrapper
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PubChemValenceListMatrixWrapperTest {

    /**
     * Integer value of the highest atomic number present in the PubChem valence list.
     */
    public static final int HIGHEST_ATOMIC_NUMBER_IN_LIST = 112;

    /**
     * Integer value of the lowest atomic number present in the PubChem valence list.
     */
    public static final int LOWEST_ATOMIC_NUMBER_IN_LIST = 1;

    /**
     * Tests whether the static class variable {@link PubChemValenceListMatrixWrapper#instance} gets initialized via
     * a static block and therefore never is null.
     */
    @Test
    @Order(1)
    public void staticBlockTest_classVariableInstanceIsNeverNull() {
        Assertions.assertNotNull(PubChemValenceListMatrixWrapper.instance);
    }

    /**
     * Tests whether the .getInstance() method of class PubChemValenceListContainer returns the static class variable
     * "instance".
     */
    @Test
    public void getInstanceMethodTest_returnsStaticClassVarInstance() {
        Assertions.assertSame(
                PubChemValenceListMatrixWrapper.instance,
                PubChemValenceListMatrixWrapper.getInstance()
        );
    }

    /**
     * Tests whether the valenceListMatrix has the expected dimensions (number of lines in file - 1 x 5).
     */
    @Test
    public void dimensionsOfValenceListMatrixTest_981x5() {
        ValenceListMatrixWrapper tmpValenceListContainer = PubChemValenceListMatrixWrapper.getInstance();
        Assertions.assertEquals(PubChemValenceListMatrixWrapper.NUMBER_OF_LINES_IN_FILE - 1,
                tmpValenceListContainer.valenceListMatrix.length);
        Assertions.assertEquals(ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE,
                tmpValenceListContainer.valenceListMatrix[0].length);
        Assertions.assertEquals(ValenceListMatrixWrapper.NUMBER_OF_COLUMNS_PER_LINE_OF_FILE,
                tmpValenceListContainer.valenceListMatrix[50].length);
    }

    /**
     * Tests exemplary whether the data import worked correctly using the .getValenceListEntry(int, int) method.
     */
    @Test
    public void checkWhetherTheImportWorkedCorrectly_getValenceListEntry_returnsCorrectValues() {
        ValenceListMatrixWrapper tmpValenceListContainer = PubChemValenceListMatrixWrapper.getInstance();
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
     * Tests whether the .getLowestAtomicNumberInList() method returns the lowest atomic number present in the PubChem
     * valence list text file.
     */
    @Test
    public void getLowestAtomicNumberInListMethodTest_returnsLowestAtomicNumberInList() {
        Assertions.assertEquals(PubChemValenceListMatrixWrapperTest.LOWEST_ATOMIC_NUMBER_IN_LIST,
                PubChemValenceListMatrixWrapper.getInstance().getLowestAtomicNumberInList());
    }

    /**
     * Tests whether the .getHighestAtomicNumberInList() method returns the highest atomic number present in the PubChem
     * valence list text file.
     */
    @Test
    public void getHighestAtomicNumberInListMethodTest_returnsHighestAtomicNumberInList() {
        Assertions.assertEquals(PubChemValenceListMatrixWrapperTest.HIGHEST_ATOMIC_NUMBER_IN_LIST,
                PubChemValenceListMatrixWrapper.getInstance().getHighestAtomicNumberInList());
    }

}
