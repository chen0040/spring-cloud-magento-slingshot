package com.github.chen0040.commons.utils;


/**
 * Created by xschen on 18/9/2017.
 */
public class TupleThree<T1, T2, T3> {
   private T1 item1;
   private T2 item2;
   private T3 item3;

   public TupleThree(T1 item1, T2 item2, T3 item3) {
      this.item1 = item1;
      this.item2 = item2;
      this.item3 = item3;
   }

   public T1 _1() {
      return item1;
   }

   public T2 _2() {
      return item2;
   }

   public T3 _3() {
      return item3;
   }
}
