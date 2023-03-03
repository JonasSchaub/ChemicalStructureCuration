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

import org.apache.commons.lang3.NotImplementedException;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 */
public class Filter {

    public static final String MOL_ID_PROPERTY_NAME = "MolID";

    protected final LinkedList<Filter.FilterTypes> listOfSelectedFilters;

    public final LinkedList<Integer> listOfFilterParameters;

    /**
     * Constructor.
     */
    public Filter() {
        this.listOfSelectedFilters = new LinkedList<>();
        this.listOfFilterParameters = new LinkedList<>();
    }

    /**
     * Protected Constructor.
     * For generating a copy of the original Filter instance.
     *
     * @param anOriginalFilter Filter instance to copy
     */
    protected Filter(Filter anOriginalFilter) {
        this.listOfSelectedFilters = anOriginalFilter.listOfSelectedFilters;
        this.listOfFilterParameters = anOriginalFilter.listOfFilterParameters;
    }

    public IAtomContainerSet filter(IAtomContainerSet anAtomContainerSet) {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of AtomContainerSet) is null");
        this.assignIdToAtomContainers(anAtomContainerSet);
        final IAtomContainerSet tmpFilteredACSet = new AtomContainerSet();
        for (IAtomContainer tmpAtomContainer :
                anAtomContainerSet.atomContainers()) {
            //apply filters
            //TODO: setProperty FILTERED_BY_FILTER to the index of the filter in the listOfSelectedFilters list
            tmpFilteredACSet.addAtomContainer(tmpAtomContainer);
        }
        //
        return tmpFilteredACSet;
    }

    /**
     * Assigns an identifier to every atom container of the given atom container set.
     *
     * @param anAtomContainerSet IAtomContainerSet
     * @throws NullPointerException
     */
    protected void assignIdToAtomContainers(IAtomContainerSet anAtomContainerSet) {     //TODO: could be static; could be placed in FilterUtils (as protected method)
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of AtomContainerSet) is null");
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            //TODO: check whether the property is already set? they might not be unique
            //TODO: assign them to the original AtomContainer instance or to a copy?
            anAtomContainerSet.getAtomContainer(i).setProperty(Filter.MOL_ID_PROPERTY_NAME, i);
        }
    }

    //TODO: could / should be placed in a Utils class
    /**
     * TODO
     * @param aAtomContainerSet
     * @return
     */
    public int[] getArrayOfAssignedMolIDs(IAtomContainerSet aAtomContainerSet) {
        final int[] tmpMolIDArray = new int[aAtomContainerSet.getAtomContainerCount()];
        for (int i = 0; i < tmpMolIDArray.length; i++) {
            tmpMolIDArray[i] = aAtomContainerSet.getAtomContainer(i).getProperty(Filter.MOL_ID_PROPERTY_NAME);
        }
        return tmpMolIDArray;
    }

    protected Filter withFilter(FilterTypes aFilterType, int anIntegerParameter) {
        Objects.requireNonNull(aFilterType, "aFilterType (constant of Filter.FilterTypes) is null");
        final Filter tmpFilterCopy = new Filter(this);
        tmpFilterCopy.listOfSelectedFilters.add(aFilterType);
        tmpFilterCopy.listOfFilterParameters.add(anIntegerParameter);
        return tmpFilterCopy;
    }

    public LinkedList<Filter.FilterTypes> getListOfSelectedFilters() {
        return this.listOfSelectedFilters;
    }

    public LinkedList<Integer> getListOfFilterParameters() {
        return this.listOfFilterParameters;
    }

    public Object withMaxAtomCountFilter(int i, boolean b) {
        throw new NotImplementedException();
    }

    /**
     * Enum of filter types.
     */
    public enum FilterTypes {
        NONE
    }
}
