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

package de.unijena.cheminf.curation;

import de.unijena.cheminf.curation.reporter.IReporter;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO
 */
public class ChemicalStructureCurationUtils {

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(ChemicalStructureCurationUtils.class.getName());

    /**
     * Clones the given atom container set. Atom containers that cause a CloneNotSupportedException to be thrown are
     * appended to the given reporter and excluded from the returned atom container set. The total count of atom
     * containers failing the cloning-process is being logged.
     *
     * @param anAtomContainerSet atom container set to be cloned
     * @param aReporter          reporter to append the reports to
     * @return a clone of the given atom container set
     * @throws NullPointerException if the given IAtomContainerSet or IReporter instance is null
     */
    public static IAtomContainerSet cloneAtomContainerSet(IAtomContainerSet anAtomContainerSet, IReporter aReporter) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        //Objects.requireNonNull(aReporter, "aReporter (instance of IReporter) is null.");  //TODO: add this line after a default reporter was integrated
        IAtomContainerSet tmpCloneOfGivenACSet = new AtomContainerSet();
        int tmpCloneNotSupportedExceptionsCount = 0;
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            try {
                tmpCloneOfGivenACSet.addAtomContainer(tmpAtomContainer.clone());
            } catch (CloneNotSupportedException aCloneNotSupportedException) {
                tmpCloneNotSupportedExceptionsCount++;
                //TODO: report
            }
        }
        if (tmpCloneNotSupportedExceptionsCount > 0) {
            ChemicalStructureCurationUtils.LOGGER.log(Level.WARNING, tmpCloneNotSupportedExceptionsCount + " of " +
                    anAtomContainerSet.getAtomContainerCount() + " given atom containers could not be cloned and" +
                    " thereby were excluded from the returned atom container set.");
        }
        return tmpCloneOfGivenACSet;
    }

}
