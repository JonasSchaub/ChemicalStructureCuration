/*
 * MIT License
 *
 * Copyright (c) 2022 Samuel Behr, Felix Baensch, Jonas Schaub, Christoph Steinbeck, and Achim Zielesny
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

import org.apache.commons.lang3.NotImplementedException;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;

public class Filter {

    public static final String MOL_ID_PROPERTY_NAME = "MolID";

    public Filter() {
        //
    }

    public IAtomContainerSet filter(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of AtomContainerSet) is null");
        this.assignIdToAtomContainers(anAtomContainerSet);
        IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            //apply filters
            tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
        }
        //
        return tmpFilteredACSet;
    }

    /**
     * Assigns an identifier to every atom container of the given atom container set.
     *
     * @param anAtomContainerSet AtomContainerSet
     * @throws NullPointerException
     */
    protected void assignIdToAtomContainers(IAtomContainerSet anAtomContainerSet) {     //TODO: could be static
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of AtomContainerSet) is null");
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            //TODO: check whether the property is already set? they might not be unique
            //TODO: assign them to the original AtomContainer instance or to a copy?
            anAtomContainerSet.getAtomContainer(i).setProperty(Filter.MOL_ID_PROPERTY_NAME, i);
        }
    }

}
