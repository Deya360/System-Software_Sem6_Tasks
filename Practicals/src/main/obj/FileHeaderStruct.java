package main.obj;

/*
 * File Header Structure:
 * 2-3		    Number of sections
 * 8-9-10-11    Pointer to Symbol data
 * 12-13-14-15	Number of Symbols
 * 16-17        Size of optional Header
 * */

public class FileHeaderStruct {
    public final int sectionCount;
    public final int pointerToSymbolTbl;
    public final int symbolCount;
    public final int sizeOfOptHeader;

    public FileHeaderStruct(int sectionCount, int pointerToSymbolTbl, int symbolCount, int sizeOfOptHeader) {
        this.sectionCount = sectionCount;
        this.pointerToSymbolTbl = pointerToSymbolTbl;
        this.symbolCount = symbolCount;
        this.sizeOfOptHeader = sizeOfOptHeader;
    }

    @Override public String toString() {
        return "FileHeaderStruct{" +
                "sectionCount=" + sectionCount +
                ", pointerToSymbolTbl=" + pointerToSymbolTbl +
                ", symbolCount=" + symbolCount +
                ", sizeOfOptHeader=" + sizeOfOptHeader +
                '}';
    }
}
