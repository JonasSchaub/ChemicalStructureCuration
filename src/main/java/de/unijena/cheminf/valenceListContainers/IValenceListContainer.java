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
     * maximum number of implicit hydrogens.<p>
     * For the entry with the index x in the valence list, the values specifying the respective atom configuration may
     * be accessed via:
     * <ul>
     *      (x, 0) -> atomic number<p>
     *      (x, 1) -> charge<p>
     *      (x, 2) -> number of π bonds<p>
     *      (x, 3) -> number of σ bonds<p>
     *      (x, 4) -> maximum number of implicit hydrogens.
     * </ul>
     * To get the length of the valence list matrix (which equals the number of valid valences and configurations of
     * atoms contained by the valence list matrix), use the method {@link #getLengthOfValenceList()}. To directly access
     * list entries regarding a specific chemical element, the methods {@link #getValenceListElementPointer(int)} and
     * {@link #getAtomConfigurationsCountOfElement(int)} might be used.
     *
     * @param aValenceListEntryIndex index of the valence list entry to be accessed
     * @param anAtomConfigurationArrayIndex index of the value in the atom configuration array to be returned
     * @return integer value
     * @throws IllegalArgumentException if the given valence list entry index exceeds {@link #getLengthOfValenceList()}
     * or the atom configuration array index exceeds the value four or if one of the given indices is of a negative value
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
     * Returns the number of entries in the valence list. This value may be used as upper bound when iterating over the
     * entries of the valence list via {@link #getValenceListEntry}.
     *
     * @return integer value
     * @see #getValenceListEntry(int, int)
     * @see #getValenceListEntry(int)
     */
    public int getLengthOfValenceList();

    /**
     * TODO
     * <p>
     * Returns -1 if an atomic number is not present in the valence list.
     * TODO: exception if atomic number is not present?
     *
     * @param anAtomicNumber
     * @return
     */
    public int getValenceListElementPointer(int anAtomicNumber);

    /**
     * TODO
     * <p>
     * The given atomic number is not checked for validity. If the given atomic number is not included in the valence
     * list, zero is returned.
     *
     * @param anAtomicNumber
     * @return
     */
    public int getAtomConfigurationsCountOfElement(int anAtomicNumber);

    /**
     * Returns the highest atomic number present in the valence list. This value may be used as upper bound when
     * iterating over the valence list element pointers ({@link #getValenceListElementPointer(int)} or the atom
     * configurations count of elements ({@link #getAtomConfigurationsCountOfElement(int)}.
     *
     * @return integer value
     * @see #getValenceListElementPointer(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
    public int getHighestAtomicNumberInList();

}
