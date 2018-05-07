package com.alexiusacademia.tester;

import com.alexiusacademia.hydraulics.*;

public class Main {
  public static void main(String[] args) {
    OpenChannel oc = new OpenChannel(OpenChannel.OpenChannelUnknown.DISCHARGE);
    System.out.println(oc.getUnknown());
  }
}
