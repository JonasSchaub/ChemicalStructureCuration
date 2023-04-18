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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * TODO
 * TODO: link!
 */
public class ValenceListContainer {

    /*
    TODO: implement tests
    TODO: check the block of code from the old branch
    TODO: doc comments
     */

    /**
     * String of the path of the PubChem valence list text file in this project.
     * TODO: link!
     */
    private static final String VALENCE_LIST_FILE_PATH = "src/main/resources/de/unijena/cheminf/curation/PubChem_Valence_list.txt";

    /**
     * Integer value of the highest atomic number contained in the "PubChem_Valence_list.txt".
     * @see #VALENCE_LIST_FILE_PATH
     */
    private static final int HIGHEST_ATOMIC_NUMBER_IN_LIST = 112;

    /**
     * Integer value of the number of
     */
    private static final int NUMBER_OF_LIST_ENTRIES = 981;

    /**
     * TODO
     */
    private static final int NUMBER_OF_VALUES_PER_ENTRY = 5;

    /**
     * TODO
     */
    private static ValenceListContainer INSTANCE;

    /**
     * TODO
     */
    private final ArrayList<int[]> valenceList; //TODO: int[][] instead?
    private final int[][] valenceListMatrix;    //TODO

    /**
     * TODO
     * Contains a pointer for every element present in the list. The pointer points to the first entry in the valence
     * list matrix that regards to a specific element. ...
     * For a faster access on the valence list matrix.
     */
    private final int[][] valenceListPointerMatrix;
    private final int[] valenceListPointerArray;    //TODO

    /**
     * Private constructor. Loads the text file "PubChem_Valence_list.txt", a list of valid valences and configurations
     * of atoms with respect to atomic number (column 1), charge (column 2), number of π bonds (column 3), number of σ
     * bonds (column 4) and the maximum number of implicit hydrogens (column 5).
     * TODO: link / reference!
     */
    private ValenceListContainer() {
        this.valenceListPointerMatrix = new int[ValenceListContainer.HIGHEST_ATOMIC_NUMBER_IN_LIST][2];
        this.valenceListPointerArray = new int[ValenceListContainer.HIGHEST_ATOMIC_NUMBER_IN_LIST]; //TODO
        this.valenceList = new ArrayList<>(ValenceListContainer.NUMBER_OF_LIST_ENTRIES);
        this.valenceListMatrix = new int[ValenceListContainer.NUMBER_OF_LIST_ENTRIES][ValenceListContainer.NUMBER_OF_VALUES_PER_ENTRY]; //TODO
        //
        try {   //TODO: check the following block of code
            File tmpValenceListFile = new File(ValenceListContainer.VALENCE_LIST_FILE_PATH);
            FileReader tmpFileReader = new FileReader(tmpValenceListFile);
            BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
            //
            //skip first line
            tmpBufferedReader.readLine();
            //
            String tmpLine;
            String[] tmpLineElements;
            int[] tmpListEntryArray;
            int tmpCurrentElement = 0;
            int tmpListEntryIndex = 0;
            while ((tmpLine = tmpBufferedReader.readLine()) != null) {
                tmpLineElements = tmpLine.split("\t", 5);
                tmpListEntryArray = new int[5];
                for (int i = 0; i < 5; i++) {
                    tmpListEntryArray[i] = Integer.parseInt(tmpLineElements[i]);
                }
                this.valenceList.add(tmpListEntryArray);
                //
                if (tmpListEntryArray[0] != tmpCurrentElement) {
                    tmpCurrentElement++;
                    this.valenceListPointerMatrix[tmpCurrentElement-1] = new int[]{tmpListEntryIndex, 0};
                }
                this.valenceListPointerMatrix[tmpCurrentElement-1][1]++;
                //
                tmpListEntryIndex++;
            }
            //
            tmpFileReader.close();
            tmpBufferedReader.close();
            //
        } catch (IOException e) {     //TODO
            //throw new RuntimeException(e);
        }
    }

    /**
     * TODO
     * @return
     */
    public static ValenceListContainer getInstance() {
        if (ValenceListContainer.INSTANCE == null) {
            ValenceListContainer.INSTANCE = new ValenceListContainer();
        }
        return ValenceListContainer.INSTANCE;
    }

    /**
     * TODO
     * @return
     */
    public ArrayList<int[]> getValenceList() {
        return valenceList;
    }

    /**
     * TODO
     * @return
     */
    public int[][] getValenceListPointerMatrix() {
        return valenceListPointerMatrix;
    }

}
