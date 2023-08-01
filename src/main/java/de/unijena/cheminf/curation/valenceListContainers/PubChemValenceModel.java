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

import java.util.Objects;

/**
 * Valence model based on a list of valid valences and atom configurations received from a paper describing the PubChem
 * standardization approaches, published under the terms of the <a href="http://creativecommons.org/licenses/by/4.0/">
 * Creative Commons Attribution 4.0 International License </a>. The valid valences and atom configurations are listed
 * with respect to atomic number, charge, number of π bonds, number of σ bonds and the maximum number of implicit
 * hydrogens.
 * <p>
 * Source:
 * <a href="https://doi.org/10.1186/s13321-018-0293-8"> "Hähnke, V.D., Kim, S. & Bolton, E.E. PubChem chemical
 * structure standardization. J Cheminform 10, 36 (2018). https://doi.org/10.1186/s13321-018-0293-8" </a>
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class PubChemValenceModel implements IValenceModel {

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
     * Checks whether the valence of the given atom is considered as valid according to the valence model. Gives the
     * option to consider atoms with wildcard atomic number (zero) as having a valid valence; otherwise these atoms are
     * generally considered as having an invalid valence since the wildcard atomic number is not covered by the PubChem
     * valence model.
     * TODO: implement tests
     *
     * @param anAtom                       the atom to check
     * @param aWildcardAtomicNumberIsValid boolean value whether to consider atoms with wildcard atomic number (zero) as
     *                                     having a valid valence
     * @return true, if the valence is considered as valid
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
        if (tmpAtomicNumber == IElement.Wildcard) {
            return aWildcardAtomicNumberIsValid;
        }
        //
        PubChemValenceListMatrixWrapper tmpMatrixWrapper = PubChemValenceListMatrixWrapper.getInstance();
        int tmpIndexOfFirstEntry = tmpMatrixWrapper.getValenceListElementPointer(tmpAtomicNumber);
        int tmpNumberOfEntries = tmpMatrixWrapper.getAtomConfigurationsCountOfElement(tmpAtomicNumber);
        for (int i = 0; i < tmpNumberOfEntries; i++) {
            if (tmpFormalCharge == tmpMatrixWrapper.getValenceListEntry(tmpIndexOfFirstEntry + i, 1)
                    && tmpPiBondCount == tmpMatrixWrapper.getValenceListEntry(tmpIndexOfFirstEntry + i, 2)
                    && tmpSigmaBondCount == tmpMatrixWrapper.getValenceListEntry(tmpIndexOfFirstEntry + i, 3)
                    && tmpImplicitHydrogenCount <= tmpMatrixWrapper.getValenceListEntry(tmpIndexOfFirstEntry + i, 4)) {
                return true;
            }
        }
        return false;
    }

}
