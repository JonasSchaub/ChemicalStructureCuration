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

import org.apache.commons.lang3.NotImplementedException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import java.util.Objects;

/**
 * Class of utils for filtering sets of atom containers based on molecular descriptors.
 */
public class FilterUtils {

    /**
     * Returns the number of atoms that regard to a given atom container. Based on the boolean parameter, implicit
     * hydrogen atoms are taken into account or not.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aConsiderImplicitHydrogens Boolean value whether implicit hydrogen atoms should be considered
     * @return Integer number of atoms in the given atom container
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int countAtoms(IAtomContainer anAtomContainer, boolean aConsiderImplicitHydrogens) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of AtomContainer) is null.");
        int tmpAtomCount = anAtomContainer.getAtomCount();
        if (aConsiderImplicitHydrogens) {
            tmpAtomCount += FilterUtils.countImplicitHydrogens(anAtomContainer);
        }
        return tmpAtomCount;
    }

    /**
     * Returns the number of implicit hydrogen atoms present in the given atom container.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @return Integer number of implicit hydrogen atoms in the given atom container
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int countImplicitHydrogens(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of AtomContainer) is null.");
        int tmpHydrogenCount = 0;
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            tmpHydrogenCount += tmpAtom.getImplicitHydrogenCount();
        }
        return tmpHydrogenCount;
    }

    /**
     * Checks whether the atom count of a given atom container exceeds or equals the given threshold. Based on the
     * boolean parameter, implicit hydrogen atoms are taken into account or not.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aThresholdValue Integer value of the atom count threshold
     * @param aConsiderImplicitHydrogens Boolean value whether to consider implicit hydrogen atoms
     * @return Boolean value whether the given atom container exceeds or equals the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is less than zero
     */
    public static boolean exceedsOrEqualsAtomCount(IAtomContainer anAtomContainer, int aThresholdValue,
                                                   boolean aConsiderImplicitHydrogens) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of AtomContainer) is null.");
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException("aThresholdValue (Integer value) is < than 0.");
        }
        final int tmpAtomCount = FilterUtils.countAtoms(anAtomContainer, aConsiderImplicitHydrogens);
        return tmpAtomCount >= aThresholdValue;
    }

    /**
     * Returns the number of bonds that are present in the given atom container. Based on the boolean parameter,
     * bonds to implicit hydrogen atoms are taken into account or not.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aConsiderImplicitHydrogens Boolean value whether to consider implicit hydrogen atoms
     * @return Integer number of bonds in the given atom container
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int countBonds(IAtomContainer anAtomContainer, boolean aConsiderImplicitHydrogens) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        int tmpBondCount = anAtomContainer.getBondCount();
        if (aConsiderImplicitHydrogens) {
            tmpBondCount += FilterUtils.countImplicitHydrogens(anAtomContainer);
        }
        return tmpBondCount;
    }

    /**
     * Checks whether the bond count of a given atom container exceeds or equals the given threshold. Based on the
     * boolean parameter, bonds to implicit hydrogen atoms are taken into account or not.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aThresholdValue Integer value of the bond count threshold
     * @param aConsiderImplicitHydrogens Boolean value whether to consider implicit hydrogen atoms
     * @return Boolean value whether the given atom container exceeds or equals the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is less than zero
     */
    public static boolean exceedsOrEqualsBondCount(IAtomContainer anAtomContainer, int aThresholdValue,
                                                  boolean aConsiderImplicitHydrogens) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException("aThresholdValue (Integer value) is < than 0.");
        }
        final int tmpBondCount = FilterUtils.countBonds(anAtomContainer, aConsiderImplicitHydrogens);
        return tmpBondCount >= aThresholdValue;
    }

    /**
     * TODO
     * @param anAtomContainer
     * @param aBondType
     * @param aConsiderImplicitHydrogens
     * @return
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int countBondType(IAtomContainer anAtomContainer, IBond.Order aBondType, boolean aConsiderImplicitHydrogens) throws NullPointerException {
        //TODO: param checks
        throw new NotImplementedException();
    }

    /*public static int countHeavyAtoms(IAtomContainer anAtomContainer) { //TODO
        throw new NotImplementedException();
    }*/

    /*public static boolean exceedsOrEqualsHeavyAtomCount(IAtomContainer anAtomContainer, int aThresholdValue)    //TODO
            throws NullPointerException, IllegalArgumentException {
        throw new NotImplementedException();
    }*/

    /*public static int countAtomType/ElementOccurrence(IAtomContainer anAtomContainer, Element) { //TODO
        throw new NotImplementedException();
    }*/

    /*public static boolean exceedsOrEqualsElementCount(IAtomContainer anAtomContainer, int aThresholdValue)    //TODO
            throws NullPointerException, IllegalArgumentException {
        throw new NotImplementedException();
    }*/

}
