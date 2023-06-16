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
 * IFilter interface. Classes implementing this interface are meant not to cause any changes to given data other
 * than simply filtering it.    TODO
 */
public interface IFilter extends IProcessingStep {

    /**
     * {@inheritDoc}
     * <ul>
     * <b>Note:</b> Classes implementing {@link IFilter} are not meant to cause any modifications to the provided
     * atom containers themself.
     * </ul>
     */
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet, boolean aCloneBeforeProcessing, boolean anAssignIdentifiers) throws NullPointerException;

    /**
     * Checks whether the filter applies on a given IAtomContainer instance.
     * <br>
     * True is returned, if the given atom container gets filtered; returns false if the atom container passes the
     * filter; throws an exception if there is an issue with the structure.
     * TODO: still not that happy with the naming
     *  suggestions: getsFiltered / passesFilter / ... ?!
     *
     * @param anAtomContainer IAtomContainer instance to be checked
     * @return true if the structure does not pass the filter
     * @throws NullPointerException if the given IAtomContainer instance is null
     */
    public boolean isFiltered(IAtomContainer anAtomContainer) throws NullPointerException;

}
