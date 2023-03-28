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

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;

/**
 * Abstract class Filter - implements IFilter.
 */
public abstract class Filter implements IFilter {

    /*
    TODO: flag filtered / not filtered ACs?
    TODO: boolean needsInfoOfOtherACs? (for duplicates filter)
    TODO: add countOfFilteredACs
    TODO: find a more telling name?
     */

    /**
     * Applies the filter on a set of atom containers and returns the set of those who passed the filter.
     *
     * @param anAtomContainerSet set of atom containers to be filtered
     * @return atom container set of all atom containers that passed the filter
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    public IAtomContainerSet filter(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            //apply filter
            if (!this.isFiltered(tmpAtomContainer)) {
                tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
            }
        }
        return tmpFilteredACSet;
    }

}
