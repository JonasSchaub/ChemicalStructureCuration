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
 * Test class of class ValenceListContainer.
 *
 * @see PubChemValenceListContainer
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PubChemValenceListContainerTest {

    /*
    TODO
     */

    /**
     * Tests whether the initial call of the .getInstance() method of class ValenceListContainer initializes the static
     * class variable INSTANCE with an instance of ValenceListContainer and returns it.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    @Test
    @Order(1)
    public void getInstanceMethodTest_initializesStaticVarInstanceOnFirstCallAndReturnsIt() throws IOException {
        Assertions.assertNull(PubChemValenceListContainer.INSTANCE);
        Object tmpReturnedInstance = PubChemValenceListContainer.getInstance();
        Assertions.assertNotNull(PubChemValenceListContainer.INSTANCE);
        Assertions.assertSame(PubChemValenceListContainer.INSTANCE, tmpReturnedInstance);
        Assertions.assertInstanceOf(PubChemValenceListContainer.class, tmpReturnedInstance);
    }

    /**
     * Tests whether the second call of the .getInstance() method of class ValenceListContainer returns the same
     * instance of ValenceListContainer as the first call.
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
     * Tests whether the .getValenceList() method of class ValenceListContainer returns an array list of integer arrays
     * of length five.
     */
    /*@Test //TODO
    public void getValenceListMethodTest_returnsArrayListOfIntegerArraysOfLength5() {
        PubChemValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Object tmpReturnedInstance = tmpValenceListContainer.getValenceListMatrix();
        Assertions.assertInstanceOf(ArrayList.class, tmpReturnedInstance);
        Assertions.assertInstanceOf(int[].class, ((List<?>) tmpReturnedInstance).get(0));
        Assertions.assertEquals(5, ((int[]) ((List<?>) tmpReturnedInstance).get(0)).length);
    }*/

    /**
     * Tests whether the array list returned by the .getValenceList() method of class ValenceListContainer has a size
     * of 981.
     */
    /*@Test //TODO
    public void getValenceListMethodTest_returnedArrayListHasSizeOf981() {
        PubChemValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        ArrayList<int[]> tmpArrayList = tmpValenceListContainer.getValenceListMatrix();
        Assertions.assertEquals(981, tmpArrayList.size());
    }*/

    //TODO: getValenceList(): further tests of the logic

    /**
     * Tests whether the .getValenceListPointerMatrix() method of class ValenceListContainer returns a two-dimensional
     * matrix of integer values.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    /*@Test
    public void getValenceListPointerMatrixMethodTest_returnsIntegerMatrix() throws IOException {
        PubChemValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        Object tmpReturnedInstance = tmpValenceListContainer.getValenceListPointerMatrix();
        Assertions.assertInstanceOf(int[][].class, tmpReturnedInstance);
    }*/

    /**
     * Tests whether the two-dimensional matrix returned by the .getValenceListPointerMatrix() method of class
     * ValenceListContainer has a size of 112 x 2.
     *
     * @throws IOException if a problem occurs loading the data from the valence list file
     */
    /*@Test
    public void getValenceListPointerMatrixMethodTest_returnsIntegerMatrixOfDimensions112x2() throws IOException {
        PubChemValenceListContainer tmpValenceListContainer = PubChemValenceListContainer.getInstance();
        int[][] tmpValenceListPointerMatrix = tmpValenceListContainer.getValenceListPointerMatrix();
        Assertions.assertEquals(112, tmpValenceListPointerMatrix.length);
        Assertions.assertEquals(2, tmpValenceListPointerMatrix[0].length);
    }*/

    //TODO: getValenceListPointerMatrix(): further tests of the logic

}
