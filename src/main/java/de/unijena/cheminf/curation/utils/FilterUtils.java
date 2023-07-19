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
import org.openscience.cdk.interfaces.IPseudoAtom;

import java.util.Objects;

/**
 * Class of utils for filtering sets of atom containers based on molecular descriptors.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class FilterUtils {

    /*
    TODO: this class might later on be removed and the code be integrated in the respective filters
     */

    /**
     * Checks whether the atom count of a given atom container exceeds or equals the given threshold. Based on the
     * boolean parameters, the count of explicit atoms (including explicit hydrogen atoms) is added with the number of
     * implicit hydrogen atoms or lowered by the number of pseudo-atoms. Use the {@link #exceedsOrEqualsHeavyAtomCount}
     * method to only count heavy atoms (excluding all hydrogen atoms). Atoms are considered as pseudo-atoms if they are
     * instances of {@link IPseudoAtom}.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aThresholdValue Integer value of the atom count threshold
     * @param aConsiderImplicitHydrogens Boolean value whether to consider implicit hydrogen atoms
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms
     * @return true, if the atom count of the given atom container exceeds or equals the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is below zero
     * @see #exceedsOrEqualsHeavyAtomCount(IAtomContainer, int, boolean)
     */
    public static boolean exceedsOrEqualsAtomCount(IAtomContainer anAtomContainer,
                                                   int aThresholdValue,
                                                   boolean aConsiderImplicitHydrogens,
                                                   boolean aConsiderPseudoAtoms)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException(ErrorCodes.ILLEGAL_THRESHOLD_VALUE_ERROR.name());
        }
        final int tmpAtomCount = ChemUtils.getAtomCount(anAtomContainer, aConsiderImplicitHydrogens, aConsiderPseudoAtoms);
        return tmpAtomCount >= aThresholdValue;
    }

    /**
     * Checks whether the heavy atom count, the number of non-hydrogen atoms, of the given atom container equals or
     * exceeds the given threshold value. Instances of {@link IPseudoAtom} may or may not be taken into account.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aThresholdValue Integer value of the heavy atom count threshold
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms
     * @return true, if the heavy atom count of the given atom container exceeds or equals the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is below zero
     */
    public static boolean exceedsOrEqualsHeavyAtomCount(IAtomContainer anAtomContainer,
                                                        int aThresholdValue,
                                                        boolean aConsiderPseudoAtoms)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException(ErrorCodes.ILLEGAL_THRESHOLD_VALUE_ERROR.name());
        }
        final int tmpHeavyAtomCount = ChemUtils.getHeavyAtomsCount(anAtomContainer, aConsiderPseudoAtoms);
        return tmpHeavyAtomCount >= aThresholdValue;
    }

    /**
     * Checks whether the bond count of a given atom container exceeds or equals the given threshold value. Based on the
     * boolean parameters, bonds to implicit hydrogen atoms and bonds to pseudo-atoms are taken into account or not. If
     * either of the two boolean parameters is false, bonds to implicit hydrogen atoms of pseudo-atoms are not taken
     * into account. Pseudo-atoms are expected to be instances of {@link IPseudoAtom}.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aThresholdValue Integer value of the bond count threshold
     * @param aConsiderImplicitHydrogens Boolean value whether to consider implicit hydrogen atoms
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms
     * @return Boolean value whether the given atom container exceeds or equals the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is below zero
     */
    public static boolean exceedsOrEqualsBondCount(IAtomContainer anAtomContainer, int aThresholdValue,
                                                  boolean aConsiderImplicitHydrogens, boolean aConsiderPseudoAtoms)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException(ErrorCodes.ILLEGAL_THRESHOLD_VALUE_ERROR.name());
        }
        final int tmpBondCount = ChemUtils.getBondCount(anAtomContainer, aConsiderImplicitHydrogens, aConsiderPseudoAtoms);
        return tmpBondCount >= aThresholdValue;
    }

    /**
     * Checks whether the count of bonds of a specific bond order of a given atom container exceeds or equals the given
     * threshold value. Based on the boolean parameters, bonds to implicit hydrogen atoms and bonds to pseudo-atoms are
     * taken into account or not. If either of the two boolean parameters is false, bonds to implicit hydrogen atoms of
     * pseudo-atoms are not taken into account. Atoms are considered as pseudo-atoms if they are instances of {@link
     * IPseudoAtom}. The given bond order may be IBond.Order.UNSET or null.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aBondOrder Constant of IBond.Order to specify the bond order of the bonds to be counted; null or
     *                   IBond.Order.UNSET are allowed
     * @param aThresholdValue Integer value of the bond count threshold
     * @param aConsiderImplicitHydrogens Boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of the order one / single
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms
     * @return Boolean value whether the given atom container exceeds or equals the given threshold
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given threshold value is below zero
     */
    public static boolean exceedsOrEqualsBondsOfSpecificBondOrderCount(IAtomContainer anAtomContainer,
                                                                       IBond.Order aBondOrder,
                                                                       int aThresholdValue,
                                                                       boolean aConsiderImplicitHydrogens,
                                                                       boolean aConsiderPseudoAtoms)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        if (aThresholdValue < 0) {
            throw new IllegalArgumentException(ErrorCodes.ILLEGAL_THRESHOLD_VALUE_ERROR.name());
        }
        final int tmpBondCount = ChemUtils.getBondsOfSpecificBondOrderCount(anAtomContainer, aBondOrder,
                aConsiderImplicitHydrogens, aConsiderPseudoAtoms);
        return tmpBondCount >= aThresholdValue;
    }

    //TODO: convenience method/filter that counts the frequency of elements and accepts element symbols (Strings) as params

    //TODO: convenience method/filter that parses element symbols to atomic numbers (see parseAtomSymbol() method of class Atom) ?

    //TODO: method containsWildcardElements()

    /**
     * Checks whether the atomic numbers of all IAtom instances contained in an IAtomContainer instance are valid. An
     * atomic number is considered as valid if it is a number between one and 118; the wildcard atomic number zero is
     * considered as valid depending on the given boolean parameter. An IllegalArgumentException is thrown if an unset
     * atomic number is encountered; throws a NullPointerException if the given IAtomContainer instance or an IAtom
     * instance is null.
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
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        for (IAtom tmpAtom : anAtomContainer.atoms()) {
            if (!FilterUtils.hasValidAtomicNumber(tmpAtom, anIncludeWildcardNumber)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the atomic number of an IAtom instance is valid. An atomic number is seen as valid if it is
     * a number between one and 118; the wildcard atomic number zero is considered as valid depending on the given
     * boolean parameter. An IllegalArgumentException is thrown if the given IAtom instance has no atomic number.
     *
     * @param anAtom IAtom instance to check
     * @param anIncludeWildcardNumber whether to consider zero, the wildcard atomic number, as valid atomic number
     * @return true, if the atomic number of the given atom is considered as valid
     * @throws NullPointerException if the given instance of IAtom is null
     * @throws IllegalArgumentException if the atomic number of the given IAtom instance is null
     */
    public static boolean hasValidAtomicNumber(IAtom anAtom, boolean anIncludeWildcardNumber)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtom, ErrorCodes.ATOM_NULL_ERROR.name());
        if (anAtom.getAtomicNumber() == null) {
            throw new IllegalArgumentException(ErrorCodes.ATOMIC_NUMBER_NULL_ERROR.name());
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
