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

package de.unijena.cheminf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test class of class ValenceListContainer.
 *
 * @see ValenceListContainer
 */
public class ValenceListContainerTest {

    //TODO

    /** TODO: this test need to be done as the first one!!
     * TODO
     */
    @Test
    public void getInstanceMethodTest_initializesStaticVarInstanceOnFirstCall() {
        Assertions.assertNull(ValenceListContainer.INSTANCE);
        Object tmpReturnedInstance = ValenceListContainer.getInstance();
        Assertions.assertNotNull(ValenceListContainer.INSTANCE);
        Assertions.assertInstanceOf(ValenceListContainer.class, tmpReturnedInstance);
    }

    /**
     * TODO
     */
    @Test
    public void getInstanceMethodTest_secondCallReturnsSameInstanceAsFirstCall() {
        Assertions.assertSame(
                ValenceListContainer.getInstance(),
                ValenceListContainer.getInstance()
        );
    }

    /**
     * TODO
     */
    @Test
    public void getValenceListMethodTest_returnsArrayListOfIntegerArraysOfLength5() {
        ValenceListContainer tmpValenceListContainer = ValenceListContainer.getInstance();
        Object tmpReturnedInstance = tmpValenceListContainer.getValenceList();
        Assertions.assertInstanceOf(ArrayList.class, tmpReturnedInstance);
        Assertions.assertInstanceOf(int[].class, ((List<?>) tmpReturnedInstance).get(0));
        Assertions.assertEquals(5, ((int[]) ((List<?>) tmpReturnedInstance).get(0)).length);
    }

    //TODO: getValenceList(): further tests of the logic

    /**
     * TODO
     */
    @Test
    public void getValenceListPointerMatrixMethodTest_returnsIntegerMatrix() {
        ValenceListContainer tmpValenceListContainer = ValenceListContainer.getInstance();
        Object tmpReturnedInstance = tmpValenceListContainer.getValenceListPointerMatrix();
        Assertions.assertInstanceOf(int[][].class, tmpReturnedInstance);
    }

    /**
     * TODO
     */
    @Test
    public void getValenceListPointerMatrixMethodTest_returnsIntegerMatrixOfDimensions112x2() {
        ValenceListContainer tmpValenceListContainer = ValenceListContainer.getInstance();
        int[][] tmpValenceListPointerMatrix = tmpValenceListContainer.getValenceListPointerMatrix();
        Assertions.assertEquals(112, tmpValenceListPointerMatrix.length);
        Assertions.assertEquals(2, tmpValenceListPointerMatrix[0].length);
    }

    //TODO: getValenceListPointerMatrix(): further tests of the logic

}
