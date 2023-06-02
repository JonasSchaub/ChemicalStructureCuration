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

import org.openscience.cdk.interfaces.IAtomContainerSet;

/**
 * TODO
 */
public interface IStandAloneProcessingStep extends IProcessingStep {

    /*
    TODO
     */

    /**
     * Clones and processes the given atom container set.
     * TODO
     *
     * @param anAtomContainerSet atom container set to clone and process
     * @return the processed atom container set
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     */
    public IAtomContainerSet cloneAndProcess(IAtomContainerSet anAtomContainerSet) throws NullPointerException;

    /**
     * Processes the given atom container set.
     * <ul>
     * <b>WARNING:</b> The given atom container set is not being cloned before processing. The given data might be
     * modified during the processing.
     * </ul>
     * To avoid any changes to your original data, use the method {@link #cloneAndProcess(IAtomContainerSet)}.
     *
     * @param anAtomContainerSet atom container set to process
     * @return the processed atom container set
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     * @see #cloneAndProcess(IAtomContainerSet)
     */
    public IAtomContainerSet process(IAtomContainerSet anAtomContainerSet) throws NullPointerException;

}
