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

import de.unijena.cheminf.curation.valenceHandling.valenceListHandling.PubChemValenceListMatrixWrapper;
import org.openscience.cdk.interfaces.IAtom;

/**
 * Valence model based on a list of valid valences and atom configurations received from a paper describing the PubChem
 * standardization approaches, published under the terms of the <a href="http://creativecommons.org/licenses/by/4.0/">
 * Creative Commons Attribution 4.0 International License </a>. The valid valences and atom configurations are listed
 * with respect to atomic number, charge, number of π bonds, number of σ bonds and the maximum number of implicit
 * hydrogens.
 * <p>
 * Source:
 * <a href="https://doi.org/10.1186/s13321-018-0293-8"> "Hähnke, V.D., Kim, S., Bolton, E.E. PubChem chemical
 * structure standardization. J Cheminform 10, 36 (2018). https://doi.org/10.1186/s13321-018-0293-8" </a>
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class PubChemValenceModel extends ValenceListBasedValenceModel {

    /**
     * Constructor; creates a valence model based on the PubChem valence list by calling the super passing the {@link
     * PubChemValenceListMatrixWrapper} instance.
     */
    public PubChemValenceModel() {
        super(PubChemValenceListMatrixWrapper.getInstance());
    }

    /**
     * {@inheritDoc}
     * <br>
     * <b>Note:</b> The PubChem valence model does not cover the wildcard atomic number.
     */
    @Override
    public boolean hasValidValence(IAtom anAtom, boolean aWildcardAtomicNumberIsValid) throws NullPointerException,
            IllegalArgumentException, UnsupportedOperationException {
        return super.hasValidValence(anAtom, aWildcardAtomicNumberIsValid);
    }

}
