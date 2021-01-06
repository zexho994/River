package test;

import stream.IStream;
import stream.IntegerStreamGenerator;

public class test1 {
  public static void main(String[] args) {
    IStream stream = IntegerStreamGenerator.getIntegerStream(10,20);
    System.out.println(stream);
  }
}
