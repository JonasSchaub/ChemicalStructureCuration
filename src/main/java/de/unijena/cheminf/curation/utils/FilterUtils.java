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

package de.unijena.cheminf.curation.utils;

import de.unijena.cheminf.curation.enums.ErrorCodes;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import java.util.Objects;

/**
 * Class of utils for filtering sets of atom containers based on molecular descriptors.
 */
public class FilterUtils {

    /**
     * Checks whether the atom count of a given atom container exceeds or equals the given threshold. Based on the
     * boolean parameter, implicit hydrogen atoms are taken into account or not.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aThresholdValue Integer value of the atom count threshold
     * @param aConsiderImplicitHydrogens Boolean value whether to consider implicit hydrogen atoms
     * @return true, if the atom count of the given atom container equals or exceeds the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is less than zero
     */
    public static boolean exceedsOrEqualsAtomCount(IAtomContainer anAtomContainer,
                                                   int aThresholdValue,
                                                   boolean aConsiderImplicitHydrogens)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException("aThresholdValue (Integer value) is less than 0.");
        }
        final int tmpAtomCount = ChemUtils.countAtoms(anAtomContainer, aConsiderImplicitHydrogens);
        return tmpAtomCount >= aThresholdValue;
    }

    /**
     * Checks whether the heavy atom count, the number of non-hydrogen atoms, of the given atom container equals or
     * exceeds the given threshold value.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aThresholdValue Integer value of the heavy atom count threshold
     * @return true, if the heavy atom count of the given atom container equals or exceeds the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is less than zero
     */
    public static boolean exceedsOrEqualsHeavyAtomCount(IAtomContainer anAtomContainer, int aThresholdValue)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of AtomContainer) is null.");
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException("aThresholdValue (Integer value) is < than 0.");
        }
        final int tmpHeavyAtomCount = ChemUtils.getHeavyAtomsCount(anAtomContainer);
        return tmpHeavyAtomCount >= aThresholdValue;
    }

    /**
     * Checks whether the bond count of a given atom container exceeds or equals the given threshold value. Based on
     * the boolean parameter, bonds to implicit hydrogen atoms are taken into account or not.
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
        final int tmpBondCount = ChemUtils.countBonds(anAtomContainer, aConsiderImplicitHydrogens);
        return tmpBondCount >= aThresholdValue;
    }

    /**
     * Checks whether the count of bonds of a specific bond order of a given atom container exceeds or equals the given
     * threshold value. Based on the boolean parameter, bonds to implicit hydrogen atoms are taken into account or not
     * when counting bonds of the order one / single. The given bond order may be IBond.Order.UNSET or null.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aBondOrder Constant of IBond.Order to specify the bond order of the bonds to be counted; null or
     *                   IBond.Order.UNSET are allowed
     * @param aThresholdValue Integer value of the bond count threshold
     * @param aConsiderImplicitHydrogens Boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of the order one / single
     * @return Boolean value whether the given atom container exceeds or equals the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is less than zero
     */
    public static boolean exceedsOrEqualsBondsOfSpecificBondOrderCount(IAtomContainer anAtomContainer, IBond.Order aBondOrder, int aThresholdValue,
                                                   boolean aConsiderImplicitHydrogens) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException("aThresholdValue (Integer value) is < than 0.");
        }
        final int tmpBondCount = ChemUtils.countBondsOfSpecificBondOrder(anAtomContainer, aBondOrder, aConsiderImplicitHydrogens);
        return tmpBondCount >= aThresholdValue;
    }

    //TODO: convenience method/filter that counts the frequency of elements and accepts element symbols (Strings) as params

    //TODO: convenience method/filter that parses element symbols to atomic numbers (see parseAtomSymbol() method of class Atom) ?

    //TODO: method containsWildcardElements()

    /**
     * Checks whether the atomic numbers of all IAtom instances contained by an IAtomContainer instance are valid.
     * An atomic number is seen as valid if it is a number between one and 118; the wildcard atomic number zero is
     * considered as valid depending on the given boolean parameter. An IllegalArgumentException is thrown if an atom
     * of the given IAtomContainer instance has no atomic number set and a NullPointerException if the given
     * IAtomContainer instance or an IAtom instance is null.
     *
     * @param anAtomContainer IAtomContainer to check
     * @param anIncludeWildcardNumber whether to consider zero, the wildcard atomic number, as valid atomic number
     * @return true, if the atomic numbers of all atoms of the given atom container are considered as valid
     * @throws NullPointerException if the given instance of IAtomContainer or an IAtom instance contained by it is null
     * @throws IllegalArgumentException if the atomic number of an IAtom instance of the given IAtomContainer instance
     * is null
     */
    public static boolean hasAllValidAtomicNumbers(IAtomContainer anAtomContainer, boolean anIncludeWildcardNumber)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        try {
            for (IAtom tmpAtom :
                    anAtomContainer.atoms()) {
                if (!FilterUtils.hasValidAtomicNumber(tmpAtom, anIncludeWildcardNumber)) {
                    return false;   //TODO: run threw all atoms to check whether an atom has no atomic number?
                }
            }
            return true;
        } catch (IllegalArgumentException anIllegalArgumentException) { //TODO: is the handling of this as you would expect it?
            throw new IllegalArgumentException("The atomic number of an IAtom instance of the given atom container was " +
                    "null.");
        } catch (NullPointerException aNullPointerException) {
            throw new NullPointerException("An IAtom instance contained by the given atom container is null.");
        }
    }

    /**
     * Checks whether the atomic number of an IAtom instance is valid. An atomic number is seen as valid if it is
     * a number between one and 118; the wildcard atomic number zero is considered as valid depending on the given
     * boolean parameter. An IllegalArgumentException is thrown if the given IAtom instance has no atomic number set.
     *
     * @param anAtom IAtom instance to check
     * @param anIncludeWildcardNumber whether to consider zero, the wildcard atomic number, as valid atomic number
     * @return true, if the atomic number of the given atom is considered as valid
     * @throws NullPointerException if the given instance of IAtom is null
     * @throws IllegalArgumentException if the atomic number of the given IAtom instance is null
     */
    public static boolean hasValidAtomicNumber(IAtom anAtom, boolean anIncludeWildcardNumber) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtom, "anAtom (instance of IAtom) is null.");
        if (anAtom.getAtomicNumber() == null) {
            throw new IllegalArgumentException("The given IAtom instance has no atomic number.");
            //return null;    //TODO: throw an exception instead?
        }
        if (anAtom.getAtomicNumber() <= 0 || anAtom.getAtomicNumber() > 118) {
            if (anAtom.getAtomicNumber() == 0) {
                return anIncludeWildcardNumber;
            }
            return false;
        }
        return true;
    }

}
