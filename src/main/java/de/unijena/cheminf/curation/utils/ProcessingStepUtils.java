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

package de.unijena.cheminf.curation.utils;

import de.unijena.cheminf.curation.processingSteps.IProcessingStep;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;

import java.util.Objects;

/**
 * Util class of the processing steps; contains methods for assigning, retrieving and removing MolIDs from / to atom
 * containers.
 *
 * @author Samuel Behr
 * @version 1.0.0.0
 */
public class ProcessingStepUtils {

    /*
    TODO: check methods getArrayOfAssignedMolIDs and getAssignedMolID and check their doc comments 
     */

    /**
     * Assigns a unique identifier in form of a MolID to every atom container of the given atom container set. For this
     * purpose, each atom container is assigned a String type property of name "Processing_MolID". The assigned MolID
     * equals the index of the atom container in the given atom container set.
     *
     * @param anAtomContainerSet set of atom containers to assign MolIDs to
     * @throws NullPointerException if the given IAtomContainerSet instance is null
     * @see IProcessingStep#MOL_ID_PROPERTY_NAME
     * @see #getAssignedMolID(IAtomContainer)
     * @see #getArrayOfAssignedMolIDs(IAtomContainerSet)
     */
    public static void assignMolIdToAtomContainers(IAtomContainerSet anAtomContainerSet) throws NullPointerException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        IAtomContainer tmpAtomContainer;
        for (int i = 0; i < anAtomContainerSet.getAtomContainerCount(); i++) {
            tmpAtomContainer = anAtomContainerSet.getAtomContainer(i);
            if (tmpAtomContainer != null) {
                tmpAtomContainer.setProperty(IProcessingStep.MOL_ID_PROPERTY_NAME, String.valueOf(i));
            }
        }
    }

    /**
     * Removes the atom container property with the name {@link IProcessingStep#MOL_ID_PROPERTY_NAME} from every atom
     * container of the given atom container set.
     *
     * @param anAtomContainerSet set of atom containers to remove property from
     * @see IProcessingStep#MOL_ID_PROPERTY_NAME
     */
    public static void removeMolIdPropertyFromAtomContainers(IAtomContainerSet anAtomContainerSet) {
        //TODO: check for null? (or just do nothing instead?)
        if (anAtomContainerSet == null) {
            return;
        }
        for (IAtomContainer tmpAtomContainer : anAtomContainerSet.atomContainers()) {
            if (tmpAtomContainer != null) {
                tmpAtomContainer.removeProperty(IProcessingStep.MOL_ID_PROPERTY_NAME);
            }
        }
    }

    /**
     * Returns the MolIDs assigned to the atom containers in the given atom container set as a String array. If atom
     * containers in the given set do not have a property with the name {@link IProcessingStep#MOL_ID_PROPERTY_NAME},
     * an IllegalArgumentException gets thrown. Every atom container set returned by {@link IProcessingStep#process}
     * should meet this criterion.
     * <br>
     * If a MolID is not of data type String, a string representation of the respective object is used.
     *
     * @param anAtomContainerSet IAtomContainerSet instance to return the MolIDs of
     * @return String array with the MolIDs of the atom containers of the given atom container set
     * @throws NullPointerException if the given instance of IAtomContainerSet or an AtomContainer contained by it
     * is null
     * @throws IllegalArgumentException if an AtomContainer of the given IAtomContainerSet instance has no MolID
     * assigned
     * @see #getAssignedMolID(IAtomContainer)
     * @see IProcessingStep#MOL_ID_PROPERTY_NAME
     */
    public static String[] getArrayOfAssignedMolIDs(IAtomContainerSet anAtomContainerSet) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainerSet, "anAtomContainerSet (instance of IAtomContainerSet) is null.");
        final String[] tmpMolIDArray = new String[anAtomContainerSet.getAtomContainerCount()];
        for (int i = 0; i < tmpMolIDArray.length; i++) {
            try {
                tmpMolIDArray[i] = ProcessingStepUtils.getAssignedMolID(anAtomContainerSet.getAtomContainer(i));
            } catch (NullPointerException aNullPointerException) {
                throw new NullPointerException("AtomContainer " + i + " of the given IAtomContainerSet instance is null.");
            } catch (IllegalArgumentException anIllegalArgumentException) {
                throw new IllegalArgumentException("AtomContainer " + i + " of the given AtomContainerSet has no " +
                        "MolID assigned.");
            }
        }
        return tmpMolIDArray;
    }

    /**
     * Returns the MolID assigned to the given atom container. This method may only be used for atom containers that
     * have a property with the name {@link IProcessingStep#MOL_ID_PROPERTY_NAME}. Every atom container returned by
     * {@link IProcessingStep#process} should meet this criterion.
     * <br>
     * If the property is no string property, a string representation of the object is returned.
     *
     * @param anAtomContainer IAtomContainer instance the MolID should be returned of
     * @return MolID of the given AtomContainer
     * @throws NullPointerException if the given instance of IAtomContainer is null
     * @throws IllegalArgumentException if the given atom container has no MolID assigned
     * @see #getArrayOfAssignedMolIDs(IAtomContainerSet)
     * @see IProcessingStep#MOL_ID_PROPERTY_NAME
     */
    public static String getAssignedMolID(IAtomContainer anAtomContainer) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(anAtomContainer, "anAtomContainer (instance of IAtomContainer) is null.");
        final Object tmpMolID = anAtomContainer.getProperty(IProcessingStep.MOL_ID_PROPERTY_NAME);
        if (tmpMolID == null) {
            throw new IllegalArgumentException("The given IAtomContainer instance has no MolID assigned.");
        }
        return tmpMolID.toString();
    }

}
