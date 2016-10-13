package com.raviv.coupons.tests;

public class PrintUtils {
	
    public static final void printHeader(final String str) {
        final int    nRowSize = 89;
        final String sTmp     = StringUtil.concat("#--| ", str, " |");
        final int    nTmp     = sTmp.length() - 1;
        final String sLine    = StringUtil.makeLength(StringUtil.makeLength("#--+-", '-', nTmp) + '+', '-', nRowSize);
        System.out.println(sLine);
        System.out.println(StringUtil.makeLength(sTmp, '-', nRowSize));
        System.out.println(sLine);
    }

}
