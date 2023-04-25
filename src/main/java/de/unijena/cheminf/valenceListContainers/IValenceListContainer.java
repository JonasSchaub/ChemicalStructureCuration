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

package de.unijena.cheminf.valenceListContainers;

/**
 * IValenceListContainer interface.
 */
public interface IValenceListContainer {

    /*
    TODO: check and write the doc-comments
     */

    /**
     * Returns a specific value contained by the valence list matrix, a matrix containing valid valences and
     * configurations of atoms with regard to atomic number, charge, number of π bonds, number of σ bonds and the
     * maximum number of implicit hydrogens.
     * For the entry with the index x in the valence list, the values specifying the respective atom configuration may
     * be accessed via:
     *      (x, 0) -> atomic number,
     *      (x, 1) -> charge,
     *      (x, 2) -> number of π bonds,
     *      (x, 3) -> number of σ bonds,
     *      (x, 4) -> maximum number of implicit hydrogens.
     * To get the length of the valence list / the number of valid valences and configurations of atoms contained by
     * the valence list matrix, use the method {@link #getLengthOfValenceList()}. To directly access list entries
     * regarding a specific chemical element, the methods {@link #getValenceListElementPointer(int)} and
     * {@link #getAtomConfigurationsCountOfElement(int)} might be used.
     *
     * @param aValenceListEntryIndex index of the valence list entry to be accessed
     * @param anAtomConfigurationArrayIndex index of the value in the atom configuration array to be returned
     * @return integer value
     * @throws IllegalArgumentException if the given valence list entry index exceeds {@link #getLengthOfValenceList()}
     * or the atom configuration array index exceeds the value four
     * @see #getValenceListEntry(int)
     * @see #getLengthOfValenceList()
     * @see #getValenceListElementPointer(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
    public int getValenceListEntry(int aValenceListEntryIndex, int anAtomConfigurationArrayIndex) throws IllegalArgumentException;

    /**
     * Returns a clone of ... TODO!!
     * Returns an array list of integer arrays that stores the data held by the PubChem valence list, a list of valid
     *      * valences and configurations of atoms.
     * The values contained by each of the five integers long arrays are:
     *      [0] -> atomic number,
     *      [1] -> charge,
     *      [2] -> number of π bonds,
     *      [3] -> number of σ bonds,
     *      [4] -> maximum number of implicit hydrogens.
     *      * The ... might be used for direct access on list entries
     *      * regarding a specific chemical element.
     * The returned array is a clone of the original array to avoid changes to the original data.
     *
     * @param aValenceListIndex
     * @return
     * @throws IllegalArgumentException
     * @see #getValenceListEntry(int, int)
     * @see #getLengthOfValenceList()
     * @see #getValenceListElementPointer(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
    public int[] getValenceListEntry(int aValenceListIndex) throws IllegalArgumentException;

    /**
     * TODO
     * @return
     */
    public int getLengthOfValenceList();

    /**
     * TODO
     * @param anAtomicNumber
     * @return
     * @throws IllegalArgumentException
     */
    public int getValenceListElementPointer(int anAtomicNumber) throws IllegalArgumentException;

    /**
     * TODO
     * @param anAtomicNumber
     * @return
     * @throws IllegalArgumentException
     */
    public int getAtomConfigurationsCountOfElement(int anAtomicNumber) throws IllegalArgumentException;

    /**
     * TODO
     * @return
     */
    public int getHighestAtomicNumberInList();

}
