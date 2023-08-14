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
import de.unijena.cheminf.curation.enums.MassComputationFlavours;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IPseudoAtom;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

/**
 * Class with chemistry utilities; mainly contains methods for determining and calculating values of molecular
 * descriptors.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class ChemUtils {

    //<editor-fold desc="getAtomCount()" defaultstate="collapsed">
    /**
     * Returns the number of atoms present in a given atom container. Based on the boolean parameters, implicit hydrogen
     * atoms and pseudo-atoms are taken into account or not. Implicit hydrogen counts assigned to pseudo-atoms are not
     * taken into account if either of the two boolean parameters is false. Atoms are considered as pseudo-atoms if they
     * are instances of {@link IPseudoAtom}.
     * TODO: adjust tests; adjust FilterUtils method; adjust filter
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aConsiderImplicitHydrogens Boolean value whether to consider implicit hydrogen atoms
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms
     * @return Integer number of atoms in the given atom container
     * @throws NullPointerException if the given instance of IAtomContainer is null; if implicit hydrogens are to be
     *                              considered and the implicit hydrogen count of an atom is null
     */
    public static int getAtomCount(IAtomContainer anAtomContainer, boolean aConsiderImplicitHydrogens,
                                   boolean aConsiderPseudoAtoms) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        int tmpAtomCount = anAtomContainer.getAtomCount();
        if (aConsiderImplicitHydrogens) {
            tmpAtomCount += ChemUtils.getImplicitHydrogenCount(anAtomContainer, aConsiderPseudoAtoms);
        }
        if (!aConsiderPseudoAtoms) {
            for (IAtom tmpAtom : anAtomContainer.atoms()) {
                if (tmpAtom instanceof IPseudoAtom) {
                    tmpAtomCount--;
                }
            }
        }
        return tmpAtomCount;
    }
    //</editor-fold>

    //<editor-fold desc="getImplicitHydrogenCount()" defaultstate="collapsed">
    /**
     * Returns the total count of implicit hydrogen atoms present in the given atom container. The implicit hydrogen
     * count assigned to instances of {@link IPseudoAtom} may or may not be taken into account.
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aConsiderPseudoAtoms boolean value whether to consider implicit hydrogen counts of IPseudoAtom instances
     * @return Integer value of the count of implicit hydrogen atoms
     * @throws NullPointerException if the given instance of IAtomContainer is null; if the implicit hydrogen count of
     *                              an atom is null
     */
    public static int getImplicitHydrogenCount(IAtomContainer anAtomContainer, boolean aConsiderPseudoAtoms)
            throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        int tmpTotalImplicitHydrogenCount = 0;
        Integer tmpImplicitHydrogenCount;
        if (!aConsiderPseudoAtoms && ChemUtils.containsPseudoAtoms(anAtomContainer)) {
            for (IAtom tmpAtom : anAtomContainer.atoms()) {
                if (tmpAtom instanceof IPseudoAtom) {
                    continue;
                }
                if ((tmpImplicitHydrogenCount = tmpAtom.getImplicitHydrogenCount()) == null) {
                    throw new NullPointerException(ErrorCodes.IMPLICIT_HYDROGEN_COUNT_NULL_ERROR.name());
                }
                tmpTotalImplicitHydrogenCount += tmpImplicitHydrogenCount;
            }
        } else {
            for (IAtom tmpAtom : anAtomContainer.atoms()) {
                if ((tmpImplicitHydrogenCount = tmpAtom.getImplicitHydrogenCount()) == null) {
                    throw new NullPointerException(ErrorCodes.IMPLICIT_HYDROGEN_COUNT_NULL_ERROR.name());
                }
                tmpTotalImplicitHydrogenCount += tmpImplicitHydrogenCount;
            }
        }
        return tmpTotalImplicitHydrogenCount;
    }
    //</editor-fold>

    //<editor-fold desc="getBondCount()" defaultstate="collapsed">
    /**
     * Returns the number of bonds present in the given atom container. Based on the boolean parameters, bonds to
     * implicit hydrogen atoms and bonds to pseudo-atoms are taken into account or not. If either of the two boolean
     * parameters is false, bonds to implicit hydrogen atoms of pseudo-atoms are not taken into account. Pseudo-atoms
     * are expected to be instances of {@link IPseudoAtom}.
     * TODO: adjust tests; adjust FilterUtils method; adjust filter
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aConsiderImplicitHydrogens Boolean value whether to consider bonds to implicit hydrogen atoms
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms
     * @return Integer number of bonds in the given atom container
     * @throws NullPointerException if the given instance of IAtomContainer is null; if implicit hydrogens are to be
     *                              considered and the implicit hydrogen count of an atom is null
     */
    public static int getBondCount(IAtomContainer anAtomContainer, boolean aConsiderImplicitHydrogens,
                                   boolean aConsiderPseudoAtoms) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        int tmpBondCount = anAtomContainer.getBondCount();
        if (aConsiderImplicitHydrogens) {
            tmpBondCount += ChemUtils.getImplicitHydrogenCount(anAtomContainer, aConsiderPseudoAtoms);
        }
        if (!aConsiderPseudoAtoms && ChemUtils.containsPseudoAtoms(anAtomContainer)) {
            for (IBond tmpBond : anAtomContainer.bonds()) {
                for (IAtom tmpBondParticipant : tmpBond.atoms()) {
                    if (tmpBondParticipant instanceof IPseudoAtom) {
                        tmpBondCount--;
                        break;
                    }
                }
            }
        }
        return tmpBondCount;
    }
    //</editor-fold>

    //<editor-fold desc="getBondsOfSpecificBondOrderCount()" defaultstate="collapsed">
    /**
     * Returns the count of bonds of a specific bond order that are present in the given atom container. Based on the
     * boolean parameters, bonds to implicit hydrogen atoms and bonds to pseudo-atoms are taken into account or not. If
     * either of the two boolean parameters is false, bonds to implicit hydrogen atoms of pseudo-atoms are not taken
     * into account. Atoms are considered as pseudo-atoms if they are instances of {@link IPseudoAtom}.
     * The given bond order may also be {@code IBond.Order.UNSET} or {@code null}.
     * TODO: adjust tests; adjust FilterUtils method; adjust filter
     *
     * @param anAtomContainer IAtomContainer instance to check
     * @param aBondOrder Constant of IBond.Order to specify the bond order of the bonds to be counted; null or
     *                   IBond.Order.UNSET are allowed
     * @param aConsiderImplicitHydrogens Boolean value whether to consider bonds to implicit hydrogen atoms; this is
     *                                   only relevant when counting bonds of bond order one / single
     * @param aConsiderPseudoAtoms boolean value whether to consider bonds to pseudo-atoms
     * @return Integer number of bonds of the specific bond order in the given atom container
     * @throws NullPointerException if the given instance of IAtomContainer is null; if implicit hydrogens are to be
     *                              considered and the implicit hydrogen count of an atom is null
     */
    public static int getBondsOfSpecificBondOrderCount(IAtomContainer anAtomContainer,
                                                       IBond.Order aBondOrder,
                                                       boolean aConsiderImplicitHydrogens,
                                                       boolean aConsiderPseudoAtoms)
            throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        int tmpBondTypeCount = 0;
        if (!aConsiderPseudoAtoms && ChemUtils.containsPseudoAtoms(anAtomContainer)) {
            boolean tmpIsBondToPseudoAtom;
            for (IBond tmpBond : anAtomContainer.bonds()) {
                // check the bond order
                if (tmpBond.getOrder() == aBondOrder) {
                    // check whether a pseudo-atom participates
                    tmpIsBondToPseudoAtom = false;
                    for (IAtom tmpAtom : tmpBond.atoms()) {
                        if (tmpAtom instanceof IPseudoAtom) {
                            tmpIsBondToPseudoAtom = true;
                            break;
                        }
                    }
                    if (tmpIsBondToPseudoAtom) {
                        continue;
                    }
                    tmpBondTypeCount++;
                }
            }
        } else {
            for (IBond tmpBond : anAtomContainer.bonds()) {
                if (tmpBond.getOrder() == aBondOrder) {
                    tmpBondTypeCount++;
                }
            }
        }
        if (aBondOrder == IBond.Order.SINGLE && aConsiderImplicitHydrogens) {
            tmpBondTypeCount += ChemUtils.getImplicitHydrogenCount(anAtomContainer, aConsiderPseudoAtoms);
        }
        return tmpBondTypeCount;
    }
    //</editor-fold>

    //<editor-fold desc="getAtomsOfAtomicNumbersCount(IAtomContainer, int...)" defaultstate="collapsed">
    /**
     * Counts the number of atoms in the given IAtomContainer instance with one of the given atomic numbers considering
     * implicit hydrogen atoms as atoms of the atomic number one. To not consider implicit hydrogens, see
     * {@link #getAtomsOfAtomicNumbersCount(IAtomContainer, boolean, int...)}.
     *
     * @param anAtomContainer IAtomContainer instance to count the atoms of
     * @param anAtomicNumbers integer values of the atomic numbers to count the atoms of; use the {@link IElement} enum
     *                        of element symbols to receive the atomic numbers of specific elements
     * @return integer value of the count of atoms in the given atom container with one of the given atomic numbers
     * @throws NullPointerException if the given atom container or the atomic number of an atom contained by it is null;
     *                              if counting atoms of atomic number one and the implicit hydrogen count of an atom is
     *                              unset (null)
     * @throws IllegalArgumentException if one of the given integer values is no valid atomic number (below zero or
     *                                  above 118)
     */
    public static int getAtomsOfAtomicNumbersCount(IAtomContainer anAtomContainer, int... anAtomicNumbers)
            throws NullPointerException, IllegalArgumentException {
        return ChemUtils.getAtomsOfAtomicNumbersCount(anAtomContainer, true, anAtomicNumbers);
    }
    //</editor-fold>

    //<editor-fold desc="getAtomsOfAtomicNumbersCount(IAtomContainer, boolean, int...)" defaultstate="collapsed">
    /**
     * Counts the number of atoms in the given IAtomContainer instance with one of the given atomic numbers. When
     * counting atoms of the atomic number one, implicit hydrogen atoms may or may not be considered.
     * TODO: implement filter
     * TODO: convenience method/filter that parses element symbols to atomic numbers (see parseAtomSymbol() method of class Atom)
     * TODO: what about pseudo-atoms?
     *
     * @param anAtomContainer IAtomContainer instance to count the atoms of
     * @param aConsiderImplicitHydrogens boolean value whether to consider implicit hydrogen atoms when counting
     *                                   atoms of the atomic number one ({@link IElement#H})
     * @param anAtomicNumbers integer values of the atomic numbers to count the atoms of; use the {@link IElement} enum
     *                        of element symbols to receive the atomic numbers of specific elements
     * @return integer value of the count of atoms in the given atom container with one of the given atomic numbers
     * @throws NullPointerException if the given atom container is null; if the atomic number of an atom is unset
     *                              (null); if the implicit hydrogen count of an atom is unset (only relevant when
     *                              counting atoms of atomic number one and considering implicit hydrogen atoms)
     * @throws IllegalArgumentException if one of the given integer values is no valid atomic number (below zero or
     *                                  above 118)
     */
    public static int getAtomsOfAtomicNumbersCount(IAtomContainer anAtomContainer, boolean aConsiderImplicitHydrogens,
                                                   int... anAtomicNumbers)
            throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        //
        if (anAtomicNumbers.length == 0) {
            return 0;
        }
        //
        // sort the atomic numbers and remove duplicates
        // check whether they fall within the spectrum of: 0 <= tmpAtomicNumber <= 118
        TreeSet<Integer> tmpTreeSet = new TreeSet<>();
        for (int tmpAtomicNumber : anAtomicNumbers) {
            if (tmpAtomicNumber < 0 || tmpAtomicNumber > 118) {
                throw new IllegalArgumentException(ErrorCodes.INVALID_ATOMIC_NUMBER_ERROR.name());
            }
            tmpTreeSet.add(tmpAtomicNumber);
        }
        Iterator<Integer> tmpIterator = tmpTreeSet.iterator();
        int[] tmpAtomicNumbersSorted = new int[tmpTreeSet.size()];
        for (int i = 0; i < tmpAtomicNumbersSorted.length; i++) {
            tmpAtomicNumbersSorted[i] = tmpIterator.next();
        }
        //
        int tmpSpecificAtomsCount = 0;
        Integer tmpAtomicNumberOfAtom;
        for (IAtom tmpAtom : anAtomContainer.atoms()) {
            tmpAtomicNumberOfAtom = tmpAtom.getAtomicNumber();
            Objects.requireNonNull(tmpAtomicNumberOfAtom, ErrorCodes.ATOMIC_NUMBER_NULL_ERROR.name());
            for (int tmpAtomicNumber : tmpAtomicNumbersSorted) {
                if (tmpAtomicNumberOfAtom < tmpAtomicNumber) {
                    break;
                }
                if (tmpAtomicNumberOfAtom == tmpAtomicNumber) {
                    tmpSpecificAtomsCount++;
                    break;
                }
                // else: continue
            }
        }
        if (aConsiderImplicitHydrogens && tmpAtomicNumbersSorted[0] == IElement.Wildcard) {
            //TODO: consider implicit Hs of pseudo atoms?
            tmpSpecificAtomsCount += ChemUtils.getImplicitHydrogenCount(anAtomContainer, false);
        }
        return tmpSpecificAtomsCount;
    }
    //</editor-fold>

    //<editor-fold desc="getHeavyAtomCount" defaultstate="collapsed">
    /**
     * Counts the number of heavy (non-hydrogen) atoms in the given IAtomContainer instance. Instances of {@link
     * IPseudoAtom} may or may not be taken into account.
     * TODO: adjust tests; adjust FilterUtils method; adjust filter
     *
     * @param anAtomContainer IAtomContainer instance to count the heavy atoms of
     * @param aConsiderPseudoAtoms boolean value whether to consider pseudo-atoms
     * @return integer value of the count of heavy atoms in the given atom container
     * @throws NullPointerException if the given instance of IAtomContainer is null
     */
    public static int getHeavyAtomCount(IAtomContainer anAtomContainer, boolean aConsiderPseudoAtoms)
            throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        int tmpHeavyAtomsCount = anAtomContainer.getAtomCount();
        for (IAtom tmpAtom : anAtomContainer.atoms()) {
            if (tmpAtom.getAtomicNumber() == IElement.H || (!aConsiderPseudoAtoms && tmpAtom instanceof IPseudoAtom)) {
                tmpHeavyAtomsCount--;
            }
        }
        return tmpHeavyAtomsCount;
    }
    //</editor-fold>

    //<editor-fold desc="getMass()" defaultstate="collapsed">
    /**
     * Calculates the mass of a molecule (given IAtomContainer instance). This function takes a 'mass flavour' that
     * switches the computation type of the mass calculation. The key distinction is how specified/unspecified isotopes
     * are handled. A specified isotope is an atom that has either {@link IAtom#setMassNumber(Integer)} or
     * {@link IAtom#setExactMass(Double)} set to non-null and non-zero value.
     * The mass of the molecule is calculated using the CDK method
     * {@link AtomContainerManipulator#getMass(IAtomContainer, int)}.
     *
     * @param anAtomContainer IAtomContainer instance to calculate the mass of
     * @param aFlavour MassCalculationFlavours constant that switches the computation type of the mass calculation;
     *                 see: {@link MassComputationFlavours},
     *                      {@link AtomContainerManipulator#getMass(IAtomContainer, int)}
     * @return double value of the mass of the molecule
     * @throws NullPointerException if the given IAtomContainer instance or the given mass computation flavour is null
     * @see MassComputationFlavours
     * @see AtomContainerManipulator#getMass(IAtomContainer, int)
     */
    public static double getMass(IAtomContainer anAtomContainer, MassComputationFlavours aFlavour)
            throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        Objects.requireNonNull(aFlavour, ErrorCodes.FLAVOUR_NULL_ERROR.name());
        //TODO: use MolWeight as default if null is given?
        return AtomContainerManipulator.getMass(anAtomContainer, aFlavour.getAssociatedIntegerValue());
    }
    //</editor-fold>

    //<editor-fold desc="containsPseudoAtoms()" defaultstate="collapsed">
    /**
     * Returns whether the given atom container contains pseudo-atoms (which are expected to be instances of
     * {@link IPseudoAtom}).
     *
     * @param anAtomContainer the atom container to check
     * @return true, if the given atom container contains pseudo-atoms
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    public static boolean containsPseudoAtoms(IAtomContainer anAtomContainer) throws NullPointerException {
        Objects.requireNonNull(anAtomContainer, ErrorCodes.ATOM_CONTAINER_NULL_ERROR.name());
        boolean tmpContainsPseudoAtoms = false;
        for (IAtom tmpAtom : anAtomContainer.atoms()) {
            if (tmpAtom instanceof IPseudoAtom) {
                tmpContainsPseudoAtoms = true;
                break;
            }
        }
        return tmpContainsPseudoAtoms;
    }
    //</editor-fold>

    //<editor-fold desc="getSigmaBondCount()" defaultstate="collapsed">
    /**
     * Returns the sigma bond count of the atom.
     *
     * @param anAtom                     the atom to count the sigma bonds of
     * @param aConsiderImplicitHydrogens boolean value whether to consider the bonds to implicit hydrogen atoms
     * @return the sigma bond count
     * @throws NullPointerException if the given IAtom instance is null; if the bond order of a bond is null; if
     *                              implicit hydrogens are to be considered and the implicit hydrogen count of an atom
     *                              is null
     * @throws IllegalArgumentException if the bond order of a bond is UNSET
     * @throws UnsupportedOperationException if the given IAtom instance does not support the bonds of the atom being
     *                                       queried ({@link IAtom#bonds()})
     */
    public static int getSigmaBondCount(IAtom anAtom, boolean aConsiderImplicitHydrogens) throws NullPointerException,
            IllegalArgumentException, UnsupportedOperationException {
        Objects.requireNonNull(anAtom, ErrorCodes.ATOM_NULL_ERROR.name());
        int tmpSigmaBondCount = 0;
        IBond.Order tmpBondOrder;
        Iterable<IBond> tmpBonds;
        try {
            tmpBonds = anAtom.bonds();
        } catch (UnsupportedOperationException anUnsupportedOperationException) {
            throw new UnsupportedOperationException(ErrorCodes.BONDS_OF_ATOM_NOT_QUERYABLE.name());
        }
        for (IBond tmpBond : tmpBonds) {
            if ((tmpBondOrder = tmpBond.getOrder()) == null) {
                throw new NullPointerException(ErrorCodes.BOND_ORDER_NULL_ERROR.name());
            }
            switch (tmpBondOrder) {
                case SINGLE, DOUBLE, TRIPLE, QUADRUPLE, QUINTUPLE, SEXTUPLE -> {
                    tmpSigmaBondCount++;
                }
                case UNSET -> {
                    throw new IllegalArgumentException(ErrorCodes.BOND_ORDER_UNSET_ERROR.name());
                }
                default -> {
                    // should not happen; the IBond.Order enum needed to be modified for this
                    throw new IllegalArgumentException(ErrorCodes.BOND_ORDER_UNKNOWN_ERROR.name());
                }
            }
        }
        if (aConsiderImplicitHydrogens) {
            Integer tmpImplicitHydrogenCount = anAtom.getImplicitHydrogenCount();
            if (tmpImplicitHydrogenCount == null) {
                throw new NullPointerException(ErrorCodes.IMPLICIT_HYDROGEN_COUNT_NULL_ERROR.name());
            }
            tmpSigmaBondCount += tmpImplicitHydrogenCount;
        }
        return tmpSigmaBondCount;
    }
    //</editor-fold>

    //<editor-fold desc="getPiBondCount()" defaultstate="collapsed">
    /**
     * Returns the pi bond count of the atom.
     *
     * @param anAtom the atom to count the pi bonds of
     * @return the pi bond count
     * @throws NullPointerException if the given IAtom instance is null; if the bond order of a bond is null
     * @throws IllegalArgumentException if the bond order of a bond is UNSET
     * @throws UnsupportedOperationException if the given IAtom instance does not support the bonds of the atom being
     *                                       queried ({@link IAtom#bonds()})
     */
    public static int getPiBondCount(IAtom anAtom) throws NullPointerException, IllegalArgumentException,
            UnsupportedOperationException {
        Objects.requireNonNull(anAtom, ErrorCodes.ATOM_NULL_ERROR.name());
        int tmpPiBondCount = 0;
        IBond.Order tmpBondOrder;
        Iterable<IBond> tmpBonds;
        try {
            tmpBonds = anAtom.bonds();
        } catch (UnsupportedOperationException anUnsupportedOperationException) {
            throw new UnsupportedOperationException(ErrorCodes.BONDS_OF_ATOM_NOT_QUERYABLE.name());
        }
        for (IBond tmpBond : tmpBonds) {
            if ((tmpBondOrder = tmpBond.getOrder()) == null) {
                throw new NullPointerException(ErrorCodes.BOND_ORDER_NULL_ERROR.name());
            }
            switch (tmpBondOrder) {
                case SINGLE -> {}
                case DOUBLE -> {
                    tmpPiBondCount++;
                }
                case TRIPLE -> {
                    tmpPiBondCount += 2;
                }
                case QUADRUPLE -> {
                    tmpPiBondCount += 3;
                }
                case QUINTUPLE -> {
                    tmpPiBondCount += 4;
                }
                case SEXTUPLE -> {
                    tmpPiBondCount += 5;
                }
                case UNSET -> {
                    throw new IllegalArgumentException(ErrorCodes.BOND_ORDER_UNSET_ERROR.name());
                }
                default -> {
                    // should not happen; the IBond.Order enum needed to be modified for this
                    throw new IllegalArgumentException(ErrorCodes.BOND_ORDER_UNKNOWN_ERROR.name());
                }
            }
        }
        return tmpPiBondCount;
    }
    //</editor-fold>

    //<editor-fold desc="getSigmaAndPiBondCounts()" defaultstate="collapsed">
    /**
     * Returns an array containing in the first position the sigma bond count and in the second position the pi bond
     * count of the atom.
     *
     * @param anAtom                     the atom to count the sigma and pi bonds of
     * @param aConsiderImplicitHydrogens boolean value whether to consider the bonds to implicit hydrogen atoms
     * @return an array containing the sigma bond count (index 0) and the pi bond count (index 1) of the given atom
     * @throws NullPointerException if the given IAtom instance is null; if the bond order of a bond is null; if
     *                              implicit hydrogens are to be considered and the implicit hydrogen count of an atom
     *                              is null
     * @throws IllegalArgumentException if the bond order of a bond is UNSET
     * @throws UnsupportedOperationException if the given IAtom instance does not support the bonds of the atom being
     *                                       queried ({@link IAtom#bonds()})
     */
    public static int[] getSigmaAndPiBondCounts(IAtom anAtom, boolean aConsiderImplicitHydrogens)
            throws NullPointerException, IllegalArgumentException, UnsupportedOperationException {
        Objects.requireNonNull(anAtom, ErrorCodes.ATOM_NULL_ERROR.name());
        // not using getSigmaBondCount() and getPiBondCount() for a better performance
        int tmpSigmaBondCount = 0;
        int tmpPiBondCount = 0;
        IBond.Order tmpBondOrder;
        Iterable<IBond> tmpBonds;
        try {
            tmpBonds = anAtom.bonds();
        } catch (UnsupportedOperationException anUnsupportedOperationException) {
            throw new UnsupportedOperationException(ErrorCodes.BONDS_OF_ATOM_NOT_QUERYABLE.name());
        }
        for (IBond tmpBond : tmpBonds) {
            if ((tmpBondOrder = tmpBond.getOrder()) == null) {
                throw new NullPointerException(ErrorCodes.BOND_ORDER_NULL_ERROR.name());
            }
            switch (tmpBondOrder) {
                case SINGLE -> {
                    tmpSigmaBondCount++;
                }
                case DOUBLE -> {
                    tmpSigmaBondCount++;
                    tmpPiBondCount++;
                }
                case TRIPLE -> {
                    tmpSigmaBondCount++;
                    tmpPiBondCount += 2;
                }
                case QUADRUPLE -> {
                    tmpSigmaBondCount++;
                    tmpPiBondCount += 3;
                }
                case QUINTUPLE -> {
                    tmpSigmaBondCount++;
                    tmpPiBondCount += 4;
                }
                case SEXTUPLE -> {
                    tmpSigmaBondCount++;
                    tmpPiBondCount += 5;
                }
                case UNSET -> {
                    throw new IllegalArgumentException(ErrorCodes.BOND_ORDER_UNSET_ERROR.name());
                }
                default -> {
                    // should not happen; the IBond.Order enum needed to be modified for this
                    throw new IllegalArgumentException(ErrorCodes.BOND_ORDER_UNKNOWN_ERROR.name());
                }
            }
        }
        if (aConsiderImplicitHydrogens) {
            Integer tmpImplicitHydrogenCount = anAtom.getImplicitHydrogenCount();
            if (tmpImplicitHydrogenCount == null) {
                throw new NullPointerException(ErrorCodes.IMPLICIT_HYDROGEN_COUNT_NULL_ERROR.name());
            }
            tmpSigmaBondCount += tmpImplicitHydrogenCount;
        }
        return new int[]{tmpSigmaBondCount, tmpPiBondCount};
    }
    //</editor-fold>

}
