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

package de.unijena.cheminf.curation.valenceHandling.valenceModels;

import de.unijena.cheminf.curation.TestUtils;
import de.unijena.cheminf.curation.valenceHandling.valenceListHandling.PubChemValenceListMatrixWrapper;
import de.unijena.cheminf.curation.valenceHandling.valenceModels.PubChemValenceModel;
import de.unijena.cheminf.curation.valenceHandling.valenceModels.ValenceListBasedValenceModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IElement;

/**
 * Test class of {@link PubChemValenceModel} which is a valence list based valence model.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 * @see PubChemValenceModel
 * @see ValenceListBasedValenceModel
 */
public class PubChemValenceModelTest extends PubChemValenceModel {

    /**
     * Tests whether the valenceListMatrixWrapper instance contained by PubChemValenceModel instances is the
     * PubChemValenceListWrapper instance.
     */
    @Test
    public void valenceListWrapperMatrixInstance_isThePubChemValenceListMatrixWrapperInstance() {
        Assertions.assertSame(PubChemValenceListMatrixWrapper.getInstance(), this.getValenceListMatrixWrapper());
    }

    /**
     * Tests whether the .hasValidValence(IAtom) method of the PubChemValenceModel returns true if given
     * an atom with a valence considered as valid according to the PubChem valence list (see {@link
     * PubChemValenceListMatrixWrapper}); two tests.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasValidValenceMethod_atomWithValidValence_returnsTrue() throws InvalidSmilesException {
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "[H+]",
                "C1=CC=CC=C1"
        );
        for (IAtomContainer tmpAtomContainer : tmpAtomContainerSet.atomContainers()) {
            Assertions.assertTrue(this.hasValidValence(tmpAtomContainer.getAtom(0)));
        }
    }

    /**
     * Tests whether the .hasValidValence(IAtom) method of the PubChemValenceModel returns false if given
     * an atom with a valence considered as invalid according to the PubChem valence list (see {@link
     * PubChemValenceListMatrixWrapper}); two tests.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasValidValenceMethod_atomWithInvalidValence_returnsFalse() throws InvalidSmilesException {
        // two imaginary structures with all the same invalid valences
        IAtomContainerSet tmpAtomContainerSet = TestUtils.parseSmilesStrings(
                "[H++]",
                "[C-]1#[C-][C-]#[C-]1"
        );
        for (IAtomContainer tmpAtomContainer : tmpAtomContainerSet.atomContainers()) {
            Assertions.assertFalse(this.hasValidValence(tmpAtomContainer.getAtom(0)));
        }
    }

    /**
     * Tests whether the .hasValidValence(IAtom, boolean) method of the PubChemValenceModel returns true if given
     * an atom with wildcard atomic number and atoms with wildcard atomic number are generally to be considered to
     * have a valid valence.
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasValidValenceMethod_considerWildcardAtomicNumberAsValid_pseudoAtom_returnsTrue()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("[R]");
        boolean tmpWildcardAtomicNumberIsValid = true;
        IAtom tmpAtom = tmpAtomContainer.getAtom(0);
        Assertions.assertEquals(IElement.Wildcard, tmpAtom.getAtomicNumber());
        Assertions.assertTrue(this.hasValidValence(tmpAtom, tmpWildcardAtomicNumberIsValid));
    }

    /**
     * Tests whether the .hasValidValence(IAtom, boolean) method of the PubChemValenceModel returns false if given
     * an atom with wildcard atomic number and atoms with wildcard atomic number are not generally to be considered to
     * have a valid valence (the PubChem valence list does not cover the atomic number zero).
     *
     * @throws InvalidSmilesException if a SMILES string could not be parsed
     */
    @Test
    public void hasValidValenceMethod_notConsiderWildcardAtomicNumberAsValid_pseudoAtom_returnsFalse()
            throws InvalidSmilesException {
        IAtomContainer tmpAtomContainer = TestUtils.parseSmilesString("[R]");
        boolean tmpWildcardAtomicNumberIsValid = false;
        IAtom tmpAtom = tmpAtomContainer.getAtom(0);
        Assertions.assertEquals(IElement.Wildcard, tmpAtom.getAtomicNumber());
        Assertions.assertFalse(this.hasValidValence(tmpAtom, tmpWildcardAtomicNumberIsValid));
    }

}
