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

import org.openscience.cdk.interfaces.IAtom;

/**
 * Interface of valence models (mainly) for checking the validity of valences and configurations of atoms.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public interface IValenceModel {

    /*
    TODO: extend the models with additional functionalities
     */

    /**
     * Calls {@link #hasValidValence(IAtom, boolean)} with whether to generally consider atoms with wildcard atomic
     * number as valid set to false.
     *
     * @param anAtom the atom to check
     * @return true, if the valence is considered as valid
     * @throws NullPointerException if the atom or any value accessed by the valence model is null
     */
    public boolean hasValidValence(IAtom anAtom) throws NullPointerException;

    /**
     * Checks whether the valence of the given atom is considered as valid according to the valence model. Atoms with
     * wildcard atomic number zero may generally be considered to have a valid valence; otherwise atoms with wildcard
     * atomic number need to be covered by the valence model or are generally considered as invalid.
     *
     * @param anAtom                       the atom to check
     * @param aWildcardAtomicNumberIsValid boolean value whether to generally consider atoms with wildcard atomic number
     *                                     (zero) as having a valid valence
     * @return true, if the valence is considered as valid
     * @throws NullPointerException if the atom or any value accessed by the valence model is null
     */
    public boolean hasValidValence(IAtom anAtom, boolean aWildcardAtomicNumberIsValid) throws NullPointerException;

}
