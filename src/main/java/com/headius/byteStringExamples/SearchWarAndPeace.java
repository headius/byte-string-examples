package com.headius.byteStringExamples;

import org.joni.Matcher;
import org.joni.Regex;

import java.io.File;
import java.io.FileInputStream;

public class SearchWarAndPeace {
    public static void main(String[] args) throws Throwable {
File book1 = new File("book1.txt");
FileInputStream fis = new FileInputStream(book1);

byte[] bytes = new byte[(int)book1.length()];
fis.read(bytes);

byte[] nameBytes = "Пьер".getBytes("Windows-1251");

Regex regex = new Regex(nameBytes);

Matcher matcher = regex.matcher(bytes);

int index = 0;
int count = 0;
while ((index = matcher.search(index, bytes.length, 0)) >= 0) {
    index += nameBytes.length;
    count++;
}

System.out.println("Found the string \"Пьер\" " + count + " times");
    }
}
