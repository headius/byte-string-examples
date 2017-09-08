package com.headius.byteStringExamples;

import org.jcodings.Ptr;
import org.jcodings.specific.ISO8859_1Encoding;
import org.jcodings.transcode.EConv;
import org.jcodings.transcode.EConvResult;
import org.jcodings.transcode.TranscoderDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;

public class PostgresqlJDBCRaw {
    public static void main(String[] args) throws Throwable{
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/headius");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from test_varchar");

        int column = rs.findColumn("someString");

        // get UTF-8 character length of column
        int utf8Size = rs.getMetaData().getColumnDisplaySize(column);

        // reserve enough ISO-8859-1 buffer for longest chars
        int isoSize = utf8Size * ISO8859_1Encoding.INSTANCE.maxLength();
        byte[] iso = new byte[isoSize];

while (rs.next()) {
    byte[] utf8 = rs.getBytes(column);

    // transcode into buffer
    Ptr in = new Ptr(0), out = new Ptr(0);
    EConv converter = TranscoderDB.open("UTF-8", "ISO-8859-1", 0);
    EConvResult result = converter.convert(utf8, in, utf8.length, iso, out, iso.length, 0);

    // check result of transcoding
    System.out.println("result: " + result);

    // print out result
    System.out.println("original string: " + rs.getString(column));
    System.out.println("bytes in UTF-8: " + Arrays.toString(utf8));
    System.out.println("bytes in ISO-8859-1: " + Arrays.toString(Arrays.copyOf(iso, out.p)));

    // reset
    converter.close();
    in.p = 0;
    out.p = 0;
}
    }
}
