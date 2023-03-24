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

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IElement;

import java.util.Objects;

/**
 * Chemistry utilities.
 */
public class ChemUtils {

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
            tmpAtomCount += ChemUtils.countImplicitHydrogens(anAtomContainer);
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
        return ChemUtils.countAtomsOfAtomicNumbers(anAtomContainer, false, IElement.H);
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
            tmpBondCount += ChemUtils.countImplicitHydrogens(anAtomContainer);
        }
        return tmpBondCount;
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
            tmpBondTypeCount += ChemUtils.countImplicitHydrogens(anAtomContainer);
        }
        return tmpBondTypeCount;
    }

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
        return ChemUtils.countAtomsOfAtomicNumbers(anAtomContainer, true, anAtomicNumbers);
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
                return tmpAtomsOfAtomicNumbersCount + ChemUtils.countImplicitHydrogens(anAtomContainer);
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
        int tmpExplicitHydrogensCount = ChemUtils.countExplicitHydrogens(anAtomContainer);
        return tmpExplicitAtomsCount - tmpExplicitHydrogensCount;
    }

}
