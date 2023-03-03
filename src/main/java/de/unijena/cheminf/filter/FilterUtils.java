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

package de.unijena.cheminf.filter;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import java.util.Objects;

/**
 *
 */
public class FilterUtils {

    /**
     * Returns the number of atoms that regard to a given atom container. Based on the boolean parameter, implicit
     * hydrogen are considered or not.
     *
     * @param anAtomContainer IAtomContainer instance to investigate
     * @param aConsiderImplicitHydrogen Boolean value whether implicit hydrogen should be considered
     * @return Integer number of the number of atoms
     */
    public static int countAtoms(IAtomContainer anAtomContainer, boolean aConsiderImplicitHydrogen) {
        int tmpAtomCount = anAtomContainer.getAtomCount();
        if (aConsiderImplicitHydrogen) {
            tmpAtomCount += FilterUtils.countImplicitHydrogen(anAtomContainer);
        }
        return tmpAtomCount;
    }

    /**
     * Returns the number of implicit hydrogen present in the given atom container.
     *
     * @param anAtomContainer IAtomContainer instance to investigate
     * @return Integer number of implicit hydrogen
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int countImplicitHydrogen(IAtomContainer anAtomContainer) throws NullPointerException {   //TODO: Hydrogen-s ?
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of AtomContainer) is null");
        int tmpHydrogenCount = 0;
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            tmpHydrogenCount += tmpAtom.getImplicitHydrogenCount();
        }
        return tmpHydrogenCount;
    }

    /**
     * Checks whether the atom count regarding to a given atom container exceeds or equals the given threshold. Based
     * on the boolean parameter, implicit hydrogen are considered or not.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aThresholdValue Integer value of the atom count threshold
     * @param aConsiderImplicitHydrogen Boolean value whether to consider implicit hydrogen
     * @return Boolean value whether the given atom container exceeds or equals the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold is less than zero
     */
    public static boolean exceedsOrEqualsAtomCount(IAtomContainer anAtomContainer, int aThresholdValue,
                                                   boolean aConsiderImplicitHydrogen) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of AtomContainer) is null");
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException("aThresholdValue (Integer value) is < than 0");
        }
        int tmpAtomCount = FilterUtils.countAtoms(anAtomContainer, aConsiderImplicitHydrogen);
        return tmpAtomCount >= aThresholdValue;
    }

}
