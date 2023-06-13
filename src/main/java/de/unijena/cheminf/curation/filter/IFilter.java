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

package de.unijena.cheminf.curation.filter;

import de.unijena.cheminf.curation.IProcessingStep;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * IFilter interface.
 */
public interface IFilter extends IProcessingStep {

    /**
     * TODO: do I need to use the tag "@Override"?
     * Applies the filter on a set of atom containers and returns the set of those who passed the filter.
     * TODO
     *
     * @param anAtomContainerSet set of atom containers to be filtered
     * @param aCloneBeforeProcessing boolean value, whether to clone the atom containers of the given set before
     *                               processing them
     * @return atom container set of all atom containers that passed the filter
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing) throws NullPointerException;

    /**
     * Checks whether the filter applies on a given IAtomContainer instance.
     * Returns true, if the given atom container gets filtered.
     * TODO: mention that the method might throw other exceptions?
     * TODO: still not that happy with the naming
     *  suggestions: getsFiltered / passesFilter / ... ?!
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @return true if the structure does not pass the filter
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException;

}
