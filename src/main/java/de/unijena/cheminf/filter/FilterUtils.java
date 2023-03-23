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
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IElement;

import java.util.Objects;

/**
 * Class of utils for filtering sets of atom containers based on molecular descriptors.
 */
public class FilterUtils {

    /**
     * Returns the number of atoms present in a given atom container. Based on the boolean parameter, implicit hydrogen
     * atoms are taken into account or not.
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
     * @return Integer value of the count of implicit hydrogen atoms
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int countImplicitHydrogens(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of AtomContainer) is null.");
        int tmpImplicitHydrogensCount = 0;
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            tmpImplicitHydrogensCount += tmpAtom.getImplicitHydrogenCount();
        }
        return tmpImplicitHydrogensCount;
    }

    /** TODO: not used in any filter so far
     * Returns the number of explicit hydrogen atoms present in the given atom container.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @return Integer value of the count of explicit hydrogen atoms
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int countExplicitHydrogens(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of AtomContainer) is null.");
        return FilterUtils.countAtomsOfAtomicNumbers(anAtomContainer, false, IElement.H);
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
     * Returns the number of bonds present in the given atom container. Based on the boolean parameter, bonds to
     * implicit hydrogen atoms are taken into account or not.
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
        final int tmpBondCount = FilterUtils.countBonds(anAtomContainer, aConsiderImplicitHydrogens);
        return tmpBondCount >= aThresholdValue;
    }

    /**
     * Counts the number of bonds of a specific bond order present in the given atom container. Based on the boolean
     * parameter, bonds to implicit hydrogen atoms are taken into account or not. The method also counts bonds with a
     * bond order of IBond.Order.UNSET or null.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aBondOrder Constant of IBond.Order to specify the bond order of the bonds to be counted; null or
     *                   IBond.Order.UNSET are allowed
     * @param aConsiderImplicitHydrogens Boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                  only relevant when counting bonds of the order one / single
     * @return Integer number of bonds of the specific bond order in the given atom container
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int countBondsOfSpecificBondOrder(IAtomContainer anAtomContainer, IBond.Order aBondOrder, boolean aConsiderImplicitHydrogens) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        int tmpBondTypeCount = 0;
        for (IBond tmpBond :
                anAtomContainer.bonds()) {
            if (tmpBond.getOrder() == aBondOrder) {
                tmpBondTypeCount++;
            }
        }
        if (aBondOrder == IBond.Order.SINGLE && aConsiderImplicitHydrogens) {
            tmpBondTypeCount += FilterUtils.countImplicitHydrogens(anAtomContainer);
        }
        return tmpBondTypeCount;
    }

    /**
     * Checks whether the count of bonds of a specific bond order of a given atom container exceeds or equals the given
     * threshold value. Based on the boolean parameter, bonds to implicit hydrogen atoms are taken into account or not
     * when counting bonds of the order one / single. The given bond order may be IBond.Order.UNSET or null.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aBondOrder Constant of IBond.Order to specify the bond order of the bonds to be counted; null or
     *                  IBond.Order.UNSET are allowed
     * @param aThresholdValue Integer value of the bond count threshold
     * @param aConsiderImplicitHydrogens Boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                  only relevant when counting bonds of the order one / single
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
        final int tmpBondCount = FilterUtils.countBondsOfSpecificBondOrder(anAtomContainer, aBondOrder, aConsiderImplicitHydrogens);
        return tmpBondCount >= aThresholdValue;
    }

    //TODO: convenience method/filter that counts the frequency of elements and accepts element symbols (Strings) as params

    /**
     * Counts the number of atoms in the given IAtomContainer instance with one of the given atomic numbers. When
     * counting atoms with an atomic number of one, implicit hydrogen atoms are considered.
     *
     * @param anAtomContainer IAtomContainer instance to count atoms of
     * @param anAtomicNumbers integer values of the atomic numbers of atoms to count
     * @return integer value of the count of atoms in the given atom container with one of the given atomic numbers
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if one of the given integer values is no valid atomic number (less than 0 or
     * greater than 118)
     */
    public static int countAtomsOfAtomicNumbers(IAtomContainer anAtomContainer, int... anAtomicNumbers)
            throws NullPointerException, IllegalArgumentException { //TODO: accept the wildcard? (atomic number = 0)
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");   //TODO: do the tests at both stages?
        for (int tmpAtomicNumber : anAtomicNumbers) {
            if (tmpAtomicNumber < 0)
                throw new IllegalArgumentException("A given atomic number is of negative value.");
            if (tmpAtomicNumber > 118)
                throw new IllegalArgumentException("A given atomic number exceeds a value of 118.");
        }
        return FilterUtils.countAtomsOfAtomicNumbers(anAtomContainer, true, anAtomicNumbers);
    }

    /**
     * Counts the number of atoms in the given IAtomContainer instance with one of the given atomic numbers. When
     * counting atoms with an atomic number of one, implicit hydrogen atoms may or may not be considered.
     * TODO: implement filter
     * TODO: convenience method/filter that parses element symbols to atomic numbers (see parseAtomSymbol() method of class Atom)
     * TODO: what to do if one and the same atomic number is given twice?
     *
     * @param anAtomContainer IAtomContainer instance to count atoms of
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms when counting
     *                                   atoms with an atomic number of one
     * @param anAtomicNumbers integer values of the atomic numbers of atoms to count
     * @return integer value of the count of atoms in the given atom container with one of the given atomic numbers
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if one of the given integer values is no valid atomic number (less than 0 or
     * greater than 118)
     */
    private static int countAtomsOfAtomicNumbers(IAtomContainer anAtomContainer, boolean aConsiderImplicitHydrogens, int... anAtomicNumbers)
            throws NullPointerException, IllegalArgumentException {    //TODO: accept the wildcard? (atomic number = 0)
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        for (int tmpAtomicNumber : anAtomicNumbers) {
            if (tmpAtomicNumber < 0)
                throw new IllegalArgumentException("A given atomic number is of negative value.");
            if (tmpAtomicNumber > 118)
                throw new IllegalArgumentException("A given atomic number exceeds a value of 118.");
        }
        int tmpAtomsOfAtomicNumbersCount = 0;
        for (IAtom tmpAtom :
                anAtomContainer.atoms()) {
            //TODO: check, whether the IAtom instance has an / a valid atomic number?
            for (int tmpAtomicNumber : anAtomicNumbers) {
                if (tmpAtom.getAtomicNumber() == tmpAtomicNumber) {
                    tmpAtomsOfAtomicNumbersCount++;
                    break;
                }
            }
        }
        for (int tmpAtomicNumber : anAtomicNumbers) {
            if (tmpAtomicNumber == IElement.H && aConsiderImplicitHydrogens) {
                return tmpAtomsOfAtomicNumbersCount + FilterUtils.countImplicitHydrogens(anAtomContainer);
            }
        }
        return tmpAtomsOfAtomicNumbersCount;
    }

    /**
     * TODO
     * TODO: implement filter
     *
     * @param anAtomContainer
     * @return
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int getHeavyAtomsCount(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        int tmpExplicitAtomsCount = anAtomContainer.getAtomCount();
        int tmpExplicitHydrogensCount = FilterUtils.countExplicitHydrogens(anAtomContainer);
        return tmpExplicitAtomsCount - tmpExplicitHydrogensCount;
    }

    //TODO: convenience method/filter that parses element symbols to atomic numbers (see parseAtomSymbol() method of class Atom) ?

    //TODO: method containsWildcardElements()

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

    //TODO: convenience method to check atom containers for invalid atomic numbers

    /**
     * Checks whether the atomic number of an IAtom instance is valid. An atomic number is seen as valid if it is a
     * number between one and 118; zero is considered as valid based on the given boolean parameter. If the atomic
     * number is null, null is returned.
     *
     * @param anAtom IAtom instance to check
     * @param anIncludeWildcardNumber whether to consider the zero, the wildcard atomic number, as valid
     * @return boolean value, whether the atomic number of the given atom is seen as valid; null if the atomic number
     * is null
     * @throws NullPointerException if the given instance of IAtom is null
     */
    public static Boolean hasValidAtomicNumber(IAtom anAtom, boolean anIncludeWildcardNumber) throws NullPointerException {
        Objects.requireNonNull(anAtom, "anAtom (instance of IAtom) is null.");
        if (anAtom.getAtomicNumber() == null) {
            return null;    //TODO: throw an exception instead?
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
