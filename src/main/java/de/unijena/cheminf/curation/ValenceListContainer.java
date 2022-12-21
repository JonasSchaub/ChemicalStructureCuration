package de.unijena.cheminf.curation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ValenceListContainer {

    public final ArrayList<int[]> VALENCE_LIST;

    public final int[][] VALENCE_LIST_POINTER_MATRIX;

    public ValenceListContainer() {     //TODO: throw IOException ?
        this.VALENCE_LIST_POINTER_MATRIX = new int[112][2];
        this.VALENCE_LIST = new ArrayList<>(981);

        try {
            File tmpValenceListFile = new File("src/main/resources/de/unijena/cheminf/curation/PubChem_Valence_list.txt");
            FileReader tmpFileReader = new FileReader(tmpValenceListFile);
            BufferedReader tmpBufferedReader = new BufferedReader(tmpFileReader);
            //
            int tmpLinesCounter = 0;
            //
            //skip first line
            tmpBufferedReader.readLine();
            tmpLinesCounter++;
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
                this.VALENCE_LIST.add(tmpListEntryArray);
                //
                if (tmpListEntryArray[0] != tmpCurrentElement) {
                    tmpCurrentElement++;
                    this.VALENCE_LIST_POINTER_MATRIX[tmpCurrentElement-1] = new int[]{tmpListEntryIndex, 0};
                }
                this.VALENCE_LIST_POINTER_MATRIX[tmpCurrentElement-1][1]++;
                //
                tmpListEntryIndex++;
                tmpLinesCounter++;
            }
            //
            /*System.out.println("------------------");
            System.out.println("Lines Count: " + tmpLinesCounter);
            System.out.println("Size of List: " + this.VALENCE_LIST.size());
            System.out.println("------------------");
            for (int[] tmpListEntry :
                    this.VALENCE_LIST) {
                for (int tmpEntry :
                        tmpListEntry) {
                    System.out.print(tmpEntry + "\t");
                }
                System.out.println();
            }
            System.out.println("------------------");
            System.out.println("Length of Array: " + this.VALENCE_LIST_POINTER_MATRIX.length);
            System.out.println("------------------");
            for (int i = 0; i < 112; i++) {
                System.out.println((i + 1) + "\t" + this.VALENCE_LIST_POINTER_MATRIX[i][0] + "\t" + this.VALENCE_LIST_POINTER_MATRIX[i][1]);
            }*/
            //
            tmpFileReader.close();
            tmpBufferedReader.close();
            //
        } catch (IOException e) {     //TODO
            //throw new RuntimeException(e);
        }
    }

}
