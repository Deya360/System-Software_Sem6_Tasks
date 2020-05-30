package main;

import main.obj.FileHeaderStruct;
import main.obj.SymbolTblStruct;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ObjParser {
    public static void main(String[] args) throws IOException {
        FileHeaderStruct fhs;
        ArrayList<SymbolTblStruct> symbolTblList;

        int pointerToStringTbl;
        byte[] stringTbl;

        try (RandomAccessFile file = new RandomAccessFile("program.obj", "r")) {
            //Gather information
            byte[] fileHeader = new byte[20];
            file.read(fileHeader);
            fhs = new FileHeaderStruct(
                getShort(fileHeader, 2), //just for info
                getInt(fileHeader, 8),
                getInt(fileHeader, 12),
                getInt(fileHeader, 16)
            );

            pointerToStringTbl = fhs.pointerToSymbolTbl + fhs.sizeOfOptHeader + 18 * fhs.symbolCount;
            file.seek(pointerToStringTbl);

            byte[] stringTblHeader = new byte[4];
            file.read(stringTblHeader);
            int stringTableSize = getInt(stringTblHeader, 0);

            /*
             * String Table Structure:
             * 0-1-2-3          Table Size
             * (4-n) * k        k strings of n length each
             * */
            //Make array size of String table and store all bytes for later
            stringTbl = new byte[stringTableSize];
            file.read(stringTbl);

            file.seek(fhs.pointerToSymbolTbl);
            int aux = 0; // 0 -- no extra record, 1 -- there is extra record after symbol

            //Iterate through symbol table, symbolCount number of times, 18 bytes at a time
            symbolTblList = new ArrayList<>();
            for (int symbol = 0; symbol<fhs.symbolCount; symbol++) {
                byte[] symbolData = new byte[18];
                file.read(symbolData);
                SymbolTblStruct sts = new SymbolTblStruct(
                    getInt(symbolData, 8),
                    getShort(symbolData, 12),
                    symbolData[16],
                    aux
                );

                if (aux <= 0) {
                    /*
                    * we check first 4 bits of symbol record (out of 8 total),
                    * if first 4 bits == 0 then, name is too long and
                    * is stored in string table (need to get),
                    * otherwise, read rest 4 bits to get short name
                    * */
                    if (getInt(symbolData, 0) != 0) { //short name (in symbol table)
                        int idx = 7;
                        while (idx>=0 && symbolData[idx] == 0) {
                            idx--;
                        }
                        sts.name = new String(symbolData, 0, idx+1, StandardCharsets.US_ASCII);

                    } else { //long name (in string table)
                        int stringTableOffset = getInt(symbolData, 4) - 4;

                        //search for name in list, names are separated by 0
                        int idx = stringTableOffset;
                        while (idx<stringTbl.length && stringTbl[idx] != 0) {
                            idx++;
                        }

                        sts.name = new String(stringTbl, stringTableOffset, idx - stringTableOffset, StandardCharsets.US_ASCII);
                    }

                    aux = symbolData[17] & 0xFF; //get aux for next record

                } else { //if aux, then skip
                    aux--;
                }

                symbolTblList.add(sts);
            }
        }

        System.out.println(fhs);
        symbolTblList.forEach(System.out::println);
    }

    private static short getShort(byte[] array, int offset) {
        return ByteBuffer.wrap(array, offset, 2).order(ByteOrder.LITTLE_ENDIAN).getShort(); //big endian if on linux
    }

    private static int getInt(byte[] array, int offset) {
        return ByteBuffer.wrap(array, offset, 4).order(ByteOrder.LITTLE_ENDIAN).getInt(); //big endian if on linux
    }
}
