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

package de.unijena.cheminf.curation.valenceListContainers;

/**
 * IValenceListContainer interface.
 */
public interface IValenceListContainer {

    /**
     * Returns a specific value contained by the valence list matrix, a matrix containing valid valences and
     * configurations of atoms with regard to atomic number, charge, number of π bonds, number of σ bonds and the
     * maximum number of implicit hydrogens. The index of an entry equals its line in the imported valence list file
     * minus two. The entries in the list are sorted by their atomic number.
     * <br>
     * For the entry with the index x in the valence list, the values specifying the respective atom configuration may
     * be accessed via:
     * <ul>
     *      <li>(x, 0) -> atomic number</li>
     *      <li>(x, 1) -> charge</li>
     *      <li>(x, 2) -> number of π bonds</li>
     *      <li>(x, 3) -> number of σ bonds</li>
     *      <li>(x, 4) -> maximum number of implicit hydrogens.</li>
     * </ul>
     * For iterating over the entries of the valence list, use the method {@link #getLengthOfValenceList()} to receive
     * the total count of entries in the list. To directly access list entries regarding a specific chemical element,
     * see the methods {@link #getValenceListElementPointer(int)} and {@link #getAtomConfigurationsCountOfElement(int)}.
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
     * Returns an array which is the equivalent to a line in the imported valence list file and contains info on a valid
     * configuration of an atom with regard to atomic number, charge, number of π bonds, number of σ bonds and the
     * maximum number of implicit hydrogens. The index of an entry equals its line in the file minus two. The entries
     * in the list are sorted by their atomic number.
     * <br>
     * The values contained by each of the five integer long arrays are:
     * <ul>
     *      <li>[0] -> atomic number,</li>
     *      <li>[1] -> charge,</li>
     *      <li>[2] -> number of π bonds,</li>
     *      <li>[3] -> number of σ bonds,</li>
     *      <li>[4] -> maximum number of implicit hydrogens.</li>
     * </ul>
     * To get the length of the valence list matrix (which equals the number of valid valences and configurations of
     * atoms contained by the valence list), use the method {@link #getLengthOfValenceList()}. To directly access
     * list entries regarding a specific chemical element, the methods {@link #getValenceListElementPointer(int)} and
     * {@link #getAtomConfigurationsCountOfElement(int)} might be used.
     * <br>
     * To avoid changes to the original data, the returned array is a clone of the original array.
     *
     * @param aValenceListEntryIndex index of the valence list entry to be returned
     * @return integer array of length five
     * @throws IllegalArgumentException if the given index is of a negative value or exceeds the length of the valence
     * list ({@link #getLengthOfValenceList()})
     * @see #getValenceListEntry(int, int)
     * @see #getLengthOfValenceList()
     * @see #getValenceListElementPointer(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
    public int[] getValenceListEntry(int aValenceListEntryIndex) throws IllegalArgumentException;

    /**
     * Returns the number of entries in the valence list. This value may be used as upper bound when iterating over the
     * entries of the valence list using one of the {@link #getValenceListEntry} methods.
     *
     * @return integer value
     * @see #getValenceListEntry(int, int)
     * @see #getValenceListEntry(int)
     */
    public int getLengthOfValenceList();

    /**
     * Returns the index of the first entry in the valence list regarding a specific chemical element. The list entries
     * may be accessed via the methods {@link #getValenceListEntry}. To receive the count of how many entries in the
     * list regard the specific element, use the method {@link #getAtomConfigurationsCountOfElement(int)}. The entries
     * in the list are sorted by their atomic number.
     * <br>
     * The given atomic number is not checked for validity. If the given atomic number is not present in the valence
     * list, -1 is returned.
     *
     * @param anAtomicNumber atomic number of the element
     * @return index of first entry regarding the element or -1 if the atomic number is not present in the list
     * @see #getValenceListEntry(int, int)
     * @see #getValenceListEntry(int)
     * @see #getAtomConfigurationsCountOfElement(int)
     */
    public int getValenceListElementPointer(int anAtomicNumber);

    /**
     * Returns the count of how many entries in the valence list regard to a specific chemical element. The list entries
     * may be accessed via the methods {@link #getValenceListEntry}. To get a pointer pointing at the first entry
     * regarding the specific element, use the method {@link #getValenceListElementPointer(int)}. The entries in the
     * list are sorted by their atomic number.
     * <br>
     * The given atomic number is not checked for validity. If the given atomic number is not included in the valence
     * list, zero is returned.
     *
     * @param anAtomicNumber atomic number of the element
     * @return count of entries regarding the element
     * @see #getValenceListEntry(int, int)
     * @see #getValenceListEntry(int)
     * @see #getValenceListElementPointer(int)
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
