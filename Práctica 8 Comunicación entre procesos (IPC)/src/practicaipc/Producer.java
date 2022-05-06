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



public class Producer {

    public static void main(String args[]) throws IOException, InterruptedException
{
RandomAccessFile rd = new RandomAccessFile("C:\\Users\\Deztro28\\Desktop\\mapped.txt", "rw");
FileChannel fc = rd.getChannel();
MappedByteBuffer mem = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1000);
try
{
Thread.sleep(10000);
}
catch (InterruptedException e)
{
e.printStackTrace();
}

for(int i=1; i < 10; i++)
{
mem.put( (byte) i);
System.out.println("Process 1 : " + (byte)i );
Thread.sleep(1); // time to allow CPU cache refreshed
}
    }
}
