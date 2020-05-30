package main.obj;

/*
 * Symbol Table Structure:
 * 0-1-2-3-4-5-6-7	Name
 * 8-9-10-11	    Value
 * 12-13		    Section No
 * 16		        Storage Class
 * 17               Number of auxiliary entries (always 0 or 1)
 * */

public class SymbolTblStruct {
    public String name;
    public final long value;
    public final short sectionNo;
    public final int storageClass;
    public final int auxNo;


    public SymbolTblStruct(long value, short sectionNo, int storageClass, int auxNo) {
        this.value = value;
        this.sectionNo = sectionNo;
        this.storageClass = storageClass;
        this.auxNo = auxNo;
    }

    @Override public String toString() {
        return "SymbolTblStruct{" +
                "name='" + name + '\'' +
                ", value=" + value +
                ", sectionNo=" + sectionNo +
                ", storageClass=" + storageClass +
                ", auxNo=" + auxNo +
                '}';
    }
}
