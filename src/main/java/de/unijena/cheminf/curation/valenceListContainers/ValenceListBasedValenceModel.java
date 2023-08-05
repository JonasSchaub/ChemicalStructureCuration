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

package de.unijena.cheminf.curation.valenceListContainers;

import de.unijena.cheminf.curation.enums.ErrorCodes;
import de.unijena.cheminf.curation.utils.ChemUtils;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IElement;

import java.io.IOException;
import java.util.Objects;

/**
 * Valence model based on a list of valid valences and atom configurations wrapped by an instance of class {@link
 * ValenceListMatrixWrapper}. It may be used to create a valence model based on lists of valid valences and
 * configurations of atoms with respect to atomic number, charge, number of π bonds, number of σ bonds and the maximum
 * number of implicit hydrogens.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see ValenceListMatrixWrapper
 * @see PubChemValenceModel
 */
public class ValenceListBasedValenceModel implements IValenceModel {

    /**
     * The valence list matrix wrapper instance containing the valence list based on which the validity of the valences
     * is checked on.
     */
    private final ValenceListMatrixWrapper valenceListMatrixWrapper;

    /**
     * Main constructor; initializes the valence model and sets the valence list matrix wrapper instance based on which
     * the validity of the valences is checked on.
     *
     * @param aValenceListMatrixWrapper the valence list matrix wrapper class instance based on which the validity of
     *                                  valences shall be checked on
     * @throws NullPointerException if the passed instance is null
     */
    public ValenceListBasedValenceModel(ValenceListMatrixWrapper aValenceListMatrixWrapper)
            throws NullPointerException {
        Objects.requireNonNull(aValenceListMatrixWrapper, "The given valence list matrix wrapper instance" +
                " is null.");
        this.valenceListMatrixWrapper = aValenceListMatrixWrapper;
    }

    /**
     * Constructor; calls the main constructor with an instance of {@link ValenceListMatrixWrapper} initialized with
     * the given file path and number of lines in file value. Passing the lines count fastens the data import.
     * <br>
     * The valence list, in which each line is expected to stand for one valid atom configuration, must be structured
     * according to the following format and contain the following information:
     * <ul>
     * <li>one headline (content is ignored);
     * <li>no empty or blank lines;
     * <li>five columns per line, separated by a tabulator ("\t");
     * <li>stored data: atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ
     * bonds (column 4), maximum number of implicit hydrogens (column 5).
     * </ul>
     * The list entries must be sorted according to their atomic number, starting with the entries with the lowest
     * atomic number.
     *
     * @param aValenceListFilePath String of the file path of the valence list text file
     * @param aNumberOfLinesInFile Integer value of the number of lines in the file (including the headline)
     * @throws NullPointerException if the given file path string is null
     * @throws IllegalArgumentException if the file contains less than two lines
     * @throws IOException if a problem occurs reading the file, e.g. the file does not fit to the expected format
     */
    public ValenceListBasedValenceModel(String aValenceListFilePath, int aNumberOfLinesInFile)
            throws NullPointerException, IllegalArgumentException, IOException {
        this(new ValenceListMatrixWrapper(aValenceListFilePath, aNumberOfLinesInFile));
    }

    /**
     * Constructor; calls the main constructor with an instance of {@link ValenceListMatrixWrapper} wrapping the valence
     * list text file located at the given file path. Passing the lines count would fasten the data import.
     * <br>
     * The valence list, in which each line is expected to stand for one valid atom configuration, must be structured
     * according to the following format and contain the following information:
     * <ul>
     * <li>one headline (content is ignored);
     * <li>no empty or blank lines;
     * <li>five columns per line, separated by a tabulator ("\t");
     * <li>stored data: atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ
     * bonds (column 4), maximum number of implicit hydrogens (column 5).
     * </ul>
     * The list entries must be sorted according to their atomic number, starting with the entries with the lowest
     * atomic number.
     *
     * @param aValenceListFilePath String of the file path of the valence list text file
     * @throws NullPointerException if the given file path string is null
     * @throws IllegalArgumentException if the file contains less than two lines
     * @throws IOException if a problem occurs reading the file, e.g. the file does not fit to the expected format
     */
    public ValenceListBasedValenceModel(String aValenceListFilePath)
            throws NullPointerException, IllegalArgumentException, IOException {
        this(new ValenceListMatrixWrapper(aValenceListFilePath));
    }

    /**
     * @throws NullPointerException if the atom is null; if atomic number, formal charge or the implicit hydrogen count
     *                              of an atom is null; if the bond order of a bond is null
     * @throws IllegalArgumentException if the bond order of a bond is IBond.Order.UNSET
     */
    @Override
    public boolean hasValidValence(IAtom anAtom) throws NullPointerException, IllegalArgumentException {
        return this.hasValidValence(anAtom, false);
    }

    /**
     * @throws NullPointerException if the atom is null; if atomic number, formal charge or the implicit hydrogen count
     *                              of an atom is null; if the bond order of a bond is null
     * @throws IllegalArgumentException if the bond order of a bond is IBond.Order.UNSET
     */
    @Override
    public boolean hasValidValence(IAtom anAtom, boolean aWildcardAtomicNumberIsValid) throws NullPointerException,
            IllegalArgumentException {
        Objects.requireNonNull(anAtom, ErrorCodes.ATOM_NULL_ERROR.name());
        Integer tmpNotNullInteger;
        //<editor-fold desc="atomic number" defaultstate="collapsed">
        if ((tmpNotNullInteger = anAtom.getAtomicNumber()) == null) {
            throw new NullPointerException(ErrorCodes.ATOMIC_NUMBER_NULL_ERROR.name());
        }
        final int tmpAtomicNumber = tmpNotNullInteger;
        //</editor-fold>
        //<editor-fold desc="formal charge" defaultstate="collapsed">
        if ((tmpNotNullInteger = anAtom.getFormalCharge()) == null) {
            throw new NullPointerException(ErrorCodes.FORMAL_CHARGE_NULL_ERROR.name());
        }
        final int tmpFormalCharge = tmpNotNullInteger;
        //</editor-fold>
        //<editor-fold desc="implicit hydrogen count" defaultstate="collapsed">
        if ((tmpNotNullInteger = anAtom.getImplicitHydrogenCount()) == null) {
            throw new NullPointerException(ErrorCodes.IMPLICIT_HYDROGEN_COUNT_NULL_ERROR.name());
        }
        final int tmpImplicitHydrogenCount = tmpNotNullInteger;
        //</editor-fold>
        //<editor-fold desc="sigma and pi bond counts" defaultstate="collapsed">
        boolean tmpConsiderImplicitHydrogens = true;
        int[] tmpSigmaAndPiBondCounts = ChemUtils.getSigmaAndPiBondCounts(anAtom, tmpConsiderImplicitHydrogens);
        final int tmpSigmaBondCount = tmpSigmaAndPiBondCounts[0];
        final int tmpPiBondCount = tmpSigmaAndPiBondCounts[1];
        //</editor-fold>
        //
        if (aWildcardAtomicNumberIsValid && tmpAtomicNumber == IElement.Wildcard) {
            return true;
        }
        //
        int tmpIndexOfFirstEntry = this.valenceListMatrixWrapper.getValenceListElementPointer(tmpAtomicNumber);
        if (tmpIndexOfFirstEntry == ValenceListMatrixWrapper.DEFAULT_POINTER_VALUE) {
            // case: no valid valence / atom configuration defined for the specific atomic number
            return false;
        }
        //
        int tmpNumberOfEntries = this.valenceListMatrixWrapper.getAtomConfigurationsCountOfElement(tmpAtomicNumber);
        for (int i = 0; i < tmpNumberOfEntries; i++) {
            if (tmpFormalCharge == this.valenceListMatrixWrapper.getValenceListEntry(tmpIndexOfFirstEntry + i, 1)
                    && tmpPiBondCount == this.valenceListMatrixWrapper.getValenceListEntry(tmpIndexOfFirstEntry + i, 2)
                    && tmpSigmaBondCount == this.valenceListMatrixWrapper.getValenceListEntry(tmpIndexOfFirstEntry + i, 3)
                    && tmpImplicitHydrogenCount <= this.valenceListMatrixWrapper.getValenceListEntry(tmpIndexOfFirstEntry + i, 4)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the valence list matrix wrapper instance based on which the validity of the valences is checked on.
     *
     * @return ValenceListMatrixWrapper instance
     */
    public ValenceListMatrixWrapper getValenceListMatrixWrapper() {
        return this.valenceListMatrixWrapper;
    }

}
