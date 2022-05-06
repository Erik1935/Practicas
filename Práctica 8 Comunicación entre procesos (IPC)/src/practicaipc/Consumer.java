/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practicaipc;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
public class Consumer {

    public static void main(String args[]) throws IOException {

        RandomAccessFile rd = new RandomAccessFile("C:\\Users\\Deztro28\\Desktop\\mapped.txt", "rw");

        FileChannel fc = rd.getChannel();
        MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_ONLY, 0, 1000);
     
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     
     
        int value = mem.get();
        while(value != 0 && value < 10){
            System.out.println("Process 2 : " + value);
            value = mem.get();
        }
    }
}
